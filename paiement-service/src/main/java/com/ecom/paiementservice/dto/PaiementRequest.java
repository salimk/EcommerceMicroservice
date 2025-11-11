package com.ecom.paiementservice.dto;

import com.ecom.paiementservice.enums.TypePaiement;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.ecom.paiementservice.entite.Paiement}
 */
@Value
@Builder
public class PaiementRequest implements Serializable {
    Long commandeId;
    Long clientId;
    BigDecimal montant;
    String devise;
    TypePaiement typePaiement;
    String emailClient;

    // URL de retour après paiement (pour redirection)
    private String urlRetourSucces;
    private String urlRetourEchec;

}