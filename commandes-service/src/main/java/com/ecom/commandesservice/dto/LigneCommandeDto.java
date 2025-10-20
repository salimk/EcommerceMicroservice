package com.ecom.commandesservice.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.ecom.commandesservice.entite.LigneCommande}
 */
@Value
public class LigneCommandeDto implements Serializable {
    Long id;
    String idproduit;
    String nomproduit;
    int quantite;
    double prixUnitaire;
}