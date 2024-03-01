package com.foundever.exchangeMsg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientCase {

    private Integer id;
    private String reference;
    private String status;
    private List<Message> messagesClient = new ArrayList<>();
    private List<Message> messagesAgent = new ArrayList<>();


}
