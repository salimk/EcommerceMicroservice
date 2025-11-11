package com.ecom.paiementservice.dto;

import com.ecom.paiementservice.enums.StatutPaiement;
import com.ecom.paiementservice.enums.TypePaiement;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.ecom.paiementservice.entite.Paiement}
 */
@Value
@Builder
public class PaiementResponse implements Serializable {
    Long id;
    String referencePaiement;
    Long commandeId;
    Long clientId;
    BigDecimal montant;
    String devise;
    TypePaiement typePaiement;
    StatutPaiement statut;
    String transactionId;
    String paymentIntentId;
    String detailsPaiement;
    String messageErreur;
    String emailClient;
    LocalDateTime dateCreation;
    LocalDateTime dateTraitement;


    // URL de paiement (pour Stripe Checkout ou PayPal)
    private String urlPaiement;
}