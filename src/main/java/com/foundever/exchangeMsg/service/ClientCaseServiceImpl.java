package com.foundever.exchangeMsg.service;

import com.foundever.exchangeMsg.exception.*;
import com.foundever.exchangeMsg.model.ClientCase;
import com.foundever.exchangeMsg.model.Message;
import com.foundever.exchangeMsg.model.Role;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClientCaseServiceImpl implements ClientCaseService {

    private Map<String, Message> messages = new HashMap<>();
    private Map<Integer, ClientCase> clientCases = new HashMap<>();
    private Integer nextCaseId = 1;
    private final Set<String> processedMessageIds = new HashSet<>();
    private int totalCalls = 0;
    private boolean casesFetched = false;

    public ClientCaseServiceImpl(Map<String, Message> messages, Map<Integer, ClientCase> clientCases) {
        this.messages = messages;
        this.clientCases = clientCases;
    }
    public String createMsgClient(String name, String content)
    {
        if (name.isEmpty() || content.isEmpty()) {
            throw new MessageClientException();
        }
        for (Message message : messages.values()) {
            if (message.getRole() == Role.CLIENT && message.getName().equals(name) && message.getMessage().equals(content)) {
                throw new ApiCalledOnceException();
            }
        }
        Message message = new Message(UUID.randomUUID().toString(),name, Role.CLIENT, content);
        messages.put(message.getId(), message);
        return message.getId();
    }
    public Integer createClientCase(String messageId) {
        for(String id:messages.keySet()) {
            if(messageId.equals(id)) {
                // Check if a client case with the same message already exists
                for (ClientCase clientCase : clientCases.values()) {
                    if (clientCase.getMessagesClient().contains(messages.get(messageId)))
                        throw new ApiCalledOnceException();
                }
                ClientCase clientCase = new ClientCase();
                clientCase.setId(nextCaseId);
                clientCase.setReference(null);
                clientCase.setStatus("Open");
                clientCase.getMessagesClient().add(messages.get(messageId));
                clientCases.put(clientCase.getId(),clientCase);
                nextCaseId++;
                return clientCase.getId();
            }
        }
        throw new CreateCaseException();
    }
    public String createMsgAgent(String name, String content, Integer caseId)
    {
        if (name.isEmpty() || content.isEmpty()) {
            throw new MessageAgentException();
        }

        if (!clientCases.containsKey(caseId)) {
            throw new MessageAgentException();
        }
        if (totalCalls >= 2) {
            throw new ApiCalledTwiceException();
        }
        ClientCase clientCase = clientCases.get(caseId);
        Message agent = new Message(UUID.randomUUID().toString(), name, Role.AGENT, content);
        messages.put(agent.getId(), agent);
        clientCase.getMessagesAgent().add(agent);
        totalCalls++;
        return agent.getId();
    }
    public String updateClientCase(String messageId) {

        if (processedMessageIds.contains(messageId)) {
            throw new ApiCalledOnceException();
        }
        Optional<ClientCase> optionalClientCase = clientCases.values().stream()
                .filter(clientCase -> clientCase.getMessagesAgent().stream()
                        .anyMatch(msg -> msg.getId().equals(messageId))).findFirst();
        if (optionalClientCase.isPresent()) {
            ClientCase clientCase = optionalClientCase.get();
            Message mostRecentAgentMessage = getMostRecentAgentMessage(clientCase);
                clientCase.setReference("KA-18B6");
                clientCase.setStatus("valid");
                processedMessageIds.add(messageId);
            return "Client case updated successfully";
        } else {
            throw new CaseNotFoundException();
        }
    }
    private Message getMostRecentAgentMessage(ClientCase casee){
        return casee.getMessagesAgent().stream()
                .max(Comparator.comparing(Message::getId))
                .orElse(null);
    }

    public List<ClientCase> getAllCases() {
        if (casesFetched) {
            throw new ApiCalledOnceException();
        }
        casesFetched = true;
        return new ArrayList<>(clientCases.values());
    }


}
