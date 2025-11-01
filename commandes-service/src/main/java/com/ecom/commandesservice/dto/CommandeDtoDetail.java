package com.ecom.commandesservice.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link com.ecom.commandesservice.entite.Commande}
 */
@Value
public class CommandeDtoDetail implements Serializable {
    Long id;
    Long ClientId;
    Date dateCommande;
    List<LigneCommandeDto> lignesCommande;
    double prixTotal;
    String statut;
}