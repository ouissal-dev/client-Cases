package com.foundever.exchangeMsg.controller;

import com.foundever.exchangeMsg.dto.ClientCaseDTO;
import com.foundever.exchangeMsg.dto.MessageDTO;
import com.foundever.exchangeMsg.model.ClientCase;
import com.foundever.exchangeMsg.service.ClientCaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientCaseControllerTest {

    @Mock
    private ClientCaseService service;

    @InjectMocks
    private ClientCaseController controller;

    private MessageDTO messageDTO;
    private ClientCaseDTO clientCaseDTO;
    private List<ClientCase> clientCases;

    @BeforeEach
    void setUp() {
        messageDTO = new MessageDTO("test", "test");
        clientCaseDTO = new ClientCaseDTO("test");
        clientCases = new ArrayList<>();
        clientCases.add(new ClientCase());
    }

    @Test
    void createClient() {
        when(service.createMsgClient(anyString(), anyString())).thenReturn("1");

        ResponseEntity<?> response = controller.createClient(messageDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("the client id :1", response.getBody());
        verify(service, times(1)).createMsgClient(anyString(), anyString());
    }

    @Test
    void createClientCase() {
        when(service.createClientCase(anyString())).thenReturn(1);

        ResponseEntity<Integer> response = controller.createClientCase(clientCaseDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(service, times(1)).createClientCase(anyString());
    }

    @Test
    void createAgent() {
        when(service.createMsgAgent(anyString(), anyString(), anyInt())).thenReturn("1");

        ResponseEntity<?> response = controller.createAgent(messageDTO, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("the agent id :1", response.getBody());
        verify(service, times(1)).createMsgAgent(anyString(), anyString(), anyInt());
    }

    @Test
    void updateClientCase() {
        when(service.updateClientCase(anyString())).thenReturn("Client case updated successfully");

        ResponseEntity<?> response = controller.updateClientCase(clientCaseDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Client case updated successfully", response.getBody());
        verify(service, times(1)).updateClientCase(anyString());
    }

    @Test
    void getCases() {
        when(service.getAllCases()).thenReturn(clientCases);

        ResponseEntity<List<ClientCase>> response = controller.getCases();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clientCases, response.getBody());
        verify(service, times(1)).getAllCases();
    }


}
