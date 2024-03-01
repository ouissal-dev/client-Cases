package com.foundever.exchangeMsg.service;

import com.foundever.exchangeMsg.exception.*;
import com.foundever.exchangeMsg.model.ClientCase;
import com.foundever.exchangeMsg.model.Message;
import com.foundever.exchangeMsg.model.Role;
import com.foundever.exchangeMsg.service.ClientCaseServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientCaseServiceImplTest {
    private ClientCaseServiceImpl service;

    @Mock
    private Map<String, Message> messages;

    @Mock
    private Map<Integer, ClientCase> clientCases;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new ClientCaseServiceImpl(messages, clientCases);
    }
    @Test
    void createMsgClient_validMessage_returnsMessageId() {
        // Setup
        Map<String, Message> messages = new HashMap<>();
        ClientCaseServiceImpl service = new ClientCaseServiceImpl(messages, null);


        String name = "John Doe";
        String content = "Help me!";

        // Verify
        String messageId = service.createMsgClient(name, content);
        Assertions.assertNotNull(messageId);
        Assertions.assertEquals(1, messages.size()); // Assuming this is the first message added
        Assertions.assertTrue(messages.containsKey(messageId));
    }


    @Test
    void createMsgClient_emptyName_throwsMessageClientException() {
        String name = "";
        String content = "Help me!";

        assertThrows(MessageClientException.class, () -> service.createMsgClient(name, content));
        verify(messages, never()).put(any(), any());
    }

    @Test
    void createMsgClient_emptyContent_throwsMessageClientException() {
        String name = "John Doe";
        String content = "";

        assertThrows(MessageClientException.class, () -> service.createMsgClient(name, content));
        verify(messages, never()).put(any(), any());
    }

    @Test
    void createMsgClient_duplicateMessage_throwsApiCalledOnceException() {
        String name = "John Doe";
        String content = "Help me!";

        when(messages.values()).thenReturn(Collections.singletonList(new Message("1", name, Role.CLIENT, content)));

        assertThrows(ApiCalledOnceException.class, () -> service.createMsgClient(name, content));
        verify(messages, never()).put(any(), any());
    }

    @Test
    void createClientCase_validMessageId_returnsCaseId() {
        String messageId = "1";
        Integer expectedCaseId = 1;

        when(messages.keySet()).thenReturn(Collections.singleton(messageId));
        when(clientCases.values()).thenReturn(Collections.emptyList());

        Integer caseId = service.createClientCase(messageId);

        assertEquals(expectedCaseId, caseId);
        verify(clientCases, times(1)).put(anyInt(), any(ClientCase.class));
    }

    @Test
    void createClientCase_invalidMessageId_throwsCreateCaseException() {
        String invalidMessageId = "invalid";

        when(messages.keySet()).thenReturn(Collections.emptySet());

        assertThrows(CreateCaseException.class, () -> service.createClientCase(invalidMessageId));
        verify(clientCases, never()).put(anyInt(), any(ClientCase.class));
    }

    @Test
    void createClientCase_duplicateClientCase_throwsApiCalledOnceException() {
        // Setup
        Map<String, Message> messages = new HashMap<>();
        Map<Integer, ClientCase> clientCases = new HashMap<>();

        ClientCaseServiceImpl service = new ClientCaseServiceImpl(messages, clientCases);

        String messageId = "message1";
        Message message = new Message(messageId, "John Doe", Role.CLIENT, "Help me!");
        messages.put(messageId, message);

        ClientCase clientCase = new ClientCase();
        clientCase.setId(1);
        clientCase.setStatus("Open");
        clientCase.getMessagesClient().add(message);
        clientCases.put(1, clientCase);

        // Verify
        Assertions.assertThrows(ApiCalledOnceException.class, () -> {
            service.createClientCase(messageId);
        });
    }

    @Test
    void getAllCases_returnsAllClientCases() {
        // Setup
        Map<String, Message> messages = new HashMap<>();
        Map<Integer, ClientCase> clientCases = new HashMap<>();
        ClientCaseServiceImpl service = new ClientCaseServiceImpl(messages, clientCases);


        // Create some client cases
        Message clientMessage1 = new Message("1", "Alice", Role.CLIENT, "Help me!");
        Message agentMessage1 = new Message("2", "Agent", Role.AGENT, "I'll assist you.");
        ClientCase clientCase1 = new ClientCase();
        clientCase1.setId(1);
        clientCase1.setStatus("Open");
        clientCase1.getMessagesClient().add(clientMessage1);
        clientCase1.getMessagesAgent().add(agentMessage1);
        clientCases.put(1, clientCase1);

        Message clientMessage2 = new Message("3", "Bob", Role.CLIENT, "Need help!");
        ClientCase clientCase2 = new ClientCase();
        clientCase2.setId(2);
        clientCase2.setStatus("Closed");
        clientCase2.getMessagesClient().add(clientMessage2);
        clientCases.put(2, clientCase2);

        // Verify
        List<ClientCase> allCases = service.getAllCases();
        Assertions.assertEquals(2, allCases.size());
        Assertions.assertTrue(allCases.contains(clientCase1));
        Assertions.assertTrue(allCases.contains(clientCase2));
    }




}
