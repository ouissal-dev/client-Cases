package com.foundever.exchangeMsg.controller;

import com.foundever.exchangeMsg.dto.ClientCaseDTO;
import com.foundever.exchangeMsg.dto.MessageDTO;
import com.foundever.exchangeMsg.model.ClientCase;
import com.foundever.exchangeMsg.service.ClientCaseService;
import com.foundever.exchangeMsg.service.ClientCaseServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;
@RestController
public class ClientCaseController {
    @Autowired
    public ClientCaseService service;

    @PostMapping(value = "/open-issue")
    @Operation(summary = "Create a new client message and generated a unique id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "client message created successfully"),
            @ApiResponse(responseCode = "406", description = "the number of api calls exceeds a single call"),
            @ApiResponse(responseCode = "400", description = "name/content not correct or not filled in")
    })
    public ResponseEntity<?> createClient(@RequestBody MessageDTO request){
        String name = request.getName();
        String messageContent = request.getMessage();
        String messageId = service.createMsgClient(name, messageContent);
        return ResponseEntity.ok("the client id :" + messageId);
    }


    @PostMapping("/create-case")
    @Operation(summary = "Create a new client case by using the ID generated in the first Api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "client case created successfully"),
            @ApiResponse(responseCode = "406", description = "the number of api calls exceeds a single call"),
            @ApiResponse(responseCode = "404", description = "Case not found")
    })
    public ResponseEntity<Integer> createClientCase(@RequestBody ClientCaseDTO request) {
       var caseId = service.createClientCase(request.getMessageId());
        return ResponseEntity.ok().body(caseId);
    }

    @PostMapping("/answer-agent/{caseId}")
    @Operation(summary = "Create a new agent message for a given case")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Agent message created successfully"),
            @ApiResponse(responseCode = "406", description = "the number of api calls exceeds two calls"),
            @ApiResponse(responseCode = "400", description = "name/content not correct or not filled in or/and case id incorrect")
    })
    public ResponseEntity<?> createAgent(@RequestBody MessageDTO request, @PathVariable Integer caseId)
    {
        var name = request.getName();
        var messageContent = request.getMessage();
        var agentId =service.createMsgAgent(name, messageContent,caseId);
        return ResponseEntity.ok().body("the agent id :" + agentId);
    }

    @PutMapping("/update-case")
    @Operation(summary = "Update a client case with the last generated agent message ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client case updated successfully"),
            @ApiResponse(responseCode = "406", description = "the number of api calls exceeds a single call"),
            @ApiResponse(responseCode = "404", description = "Case not found")
    })
    public ResponseEntity<?> updateClientCase(@RequestBody ClientCaseDTO request) {
        var result = service.updateClientCase(request.getMessageId());
        return ResponseEntity.ok().body(result);
    }


   @Operation(summary = "Retrieve all client cases")
   @ApiResponses(value = {
           @ApiResponse(responseCode = "200", description = "List of client cases retrieved successfully"),
           @ApiResponse(responseCode = "406", description = "the number of api calls exceeds a single call")
   })
   @GetMapping("/client-cases")
    public ResponseEntity<List<ClientCase>> getCases()
    {
        List<ClientCase> clientCases = service.getAllCases();

        return ResponseEntity.ok(clientCases);
    }

}
