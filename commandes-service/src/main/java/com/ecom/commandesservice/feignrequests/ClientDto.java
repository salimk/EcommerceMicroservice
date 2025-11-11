package com.ecom.commandesservice.feignrequests;

import lombok.Value;

@Value
public class ClientDto {
    private final Long id;
    private final String name;
    private final String email;
    private final int etat;

}
