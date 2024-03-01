package com.foundever.exchangeMsg.service;

import com.foundever.exchangeMsg.model.ClientCase;

import java.util.List;

public interface ClientCaseService {
    public String createMsgClient(String name, String content);
    public Integer createClientCase(String messageId);
    public String createMsgAgent(String name, String content, Integer caseId);
    public String updateClientCase(String messageId);
    public List<ClientCase> getAllCases();
}
