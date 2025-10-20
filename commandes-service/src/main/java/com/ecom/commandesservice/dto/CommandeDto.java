package com.ecom.commandesservice.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.ecom.commandesservice.entite.Commande}
 */
@Value
public class CommandeDto implements Serializable {
    Long id;
    Date date;
    double prixTotal;
    String statut;
}