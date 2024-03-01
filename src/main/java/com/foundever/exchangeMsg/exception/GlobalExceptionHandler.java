package com.foundever.exchangeMsg.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Value(value = "${data.exception.message1}")
    private String message1;
    @Value(value = "${data.exception.message2}")
    private String message2;
    @Value(value = "${data.exception.message3}")
    private String message3;
    @Value(value = "${data.exception.message4}")
    private String message4;
    @Value(value = "${data.exception.message5}")
    private String message5;
    @Value(value = "${data.exception.message6}")
    private String message6;

    @ExceptionHandler(ApiCalledOnceException.class)
    public ResponseEntity<?> apiCalledOnceException(ApiCalledOnceException ex) {
        return new ResponseEntity<>(message1, HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(ApiCalledTwiceException.class)
    public ResponseEntity<?> apiCalledTwiceException(ApiCalledTwiceException ex) {
        return new ResponseEntity<>(message2, HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(CaseNotFoundException.class)
    public ResponseEntity<?> caseNotFoundException(CaseNotFoundException ex) {
        return new ResponseEntity<>(message3, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MessageAgentException.class)
    public ResponseEntity<?> messageAgentException(MessageAgentException ex) {
        return new ResponseEntity<>(message4, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MessageClientException.class)
    public ResponseEntity<?> messageClientExceptions(MessageClientException ex) {
        return new ResponseEntity<>(message5, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreateCaseException.class)
    public ResponseEntity<?> createCaseExceptions(CreateCaseException ex) {
        return new ResponseEntity<>(message6, HttpStatus.BAD_REQUEST);
    }
}


