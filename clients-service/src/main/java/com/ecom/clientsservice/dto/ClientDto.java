package com.ecom.clientsservice.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.ecom.clientsservice.entite.Client}
 */
@Value
public class ClientDto implements Serializable {
    Long id;
    String nom;
    String email;
}