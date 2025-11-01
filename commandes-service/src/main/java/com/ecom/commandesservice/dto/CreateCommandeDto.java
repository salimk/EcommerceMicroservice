package com.ecom.commandesservice.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.ecom.commandesservice.entite.Commande}
 */
@Value
public class CreateCommandeDto implements Serializable {
    Long ClientId;
    List<LigneCommandeDto1> lignesCommande;

    /**
     * DTO for {@link com.ecom.commandesservice.entite.LigneCommande}
     */
    @Value
    public static class LigneCommandeDto1 implements Serializable {
        Long produitId;
        int quantite;
    }
}