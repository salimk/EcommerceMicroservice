package com.ecom.paiementservice.entite;

import com.ecom.paiementservice.enums.StatutPaiement;
import com.ecom.paiementservice.enums.TypePaiement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // identifiant métier externe, lisible et partageable, généré par votre application (UUID, séquence alphanumérique, horodatage + code, etc.).
    //     – vous l’incluez dans vos mails de confirmation, vos URLs de suivi ou vos journaux d’audit
    //     – vous l’échangez avec vos prestataires de paiement (Stripe, PayPal…)
    //     – vous le remontez en front pour que l’utilisateur ou le support client puisse
    @Column(nullable = false, unique = true)
    private String referencePaiement;

    @Column(nullable = false)
    private Long commandeId;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Column(length = 3)
    private String devise; // EUR, USD, MAD, etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePaiement typePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPaiement statut;

    // Identifiant de transaction du provider (Stripe, PayPal, etc.)
    private String transactionId;

    // ID du paiement intent côté provider
    private String paymentIntentId;

    // Détails supplémentaires du paiement
    @Column(columnDefinition = "TEXT")
    private String detailsPaiement;

    // Message d'erreur en cas d'échec
    @Column(columnDefinition = "TEXT")
    private String messageErreur;

    // Email du client pour l'envoi de reçu
    private String emailClient;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateTraitement;

    private LocalDateTime dateModification;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        dateModification = LocalDateTime.now();
        if (statut == null) {
            statut = StatutPaiement.EN_ATTENTE;
        }
        if (devise == null) {
            devise = "EUR";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }
}