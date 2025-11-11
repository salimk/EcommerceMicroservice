package com.ecom.paiementservice.service;

import com.ecom.paiementservice.dto.PaiementRequest;
import com.ecom.paiementservice.dto.PaiementResponse;
import com.ecom.paiementservice.entite.Paiement;
import com.ecom.paiementservice.enums.StatutPaiement;
import com.ecom.paiementservice.enums.TypePaiement;
import com.ecom.paiementservice.repository.PaiementRepository;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j

public class PaimentService {
    private final PaiementRepository paiementRepository;
    private final StripePaymentService stripePaymentService;
    private final PayPalPaymentService payPalPaymentService;

    @Transactional
    public PaiementResponse initierPaiement(PaiementRequest request) {
        log.info("Initiation du paiement pour la commande: {}", request.getCommandeId());

        // Créer l'entité paiement
        Paiement paiement = new Paiement();
        paiement.setReferencePaiement(genererReference());
        paiement.setCommandeId(request.getCommandeId());
        paiement.setClientId(request.getClientId());
        paiement.setMontant(request.getMontant());
        paiement.setDevise(request.getDevise() != null ? request.getDevise() : "EUR");
        paiement.setTypePaiement(request.getTypePaiement());
        paiement.setStatut(StatutPaiement.EN_ATTENTE);
        paiement.setEmailClient(request.getEmailClient());
        paiement = paiementRepository.save(paiement);


        try {
            // Déléguer au service approprié selon le type de paiement
            PaiementResponse response;

            if (request.getTypePaiement() == TypePaiement.STRIPE) {
                response = stripePaymentService.creerPaiement(paiement, request);
            } else if (request.getTypePaiement() == TypePaiement.PAYPAL) {
                response = payPalPaymentService.creerPaiement(paiement, request);
            } else {
                throw new IllegalArgumentException("Type de paiement non supporté: " + request.getTypePaiement());
            }

            // Mettre à jour le paiement avec les informations du provider
            paiement.setStatut(StatutPaiement.EN_COURS);
            paiement.setPaymentIntentId(response.getTransactionId());
            paiementRepository.save(paiement);

            return response;

        } catch (Exception e) {
            log.error("Erreur lors de l'initiation du paiement", e);
            paiement.setStatut(StatutPaiement.ECHOUE);
            paiement.setMessageErreur(e.getMessage());
            paiementRepository.save(paiement);

            throw new RuntimeException("Erreur lors de l'initiation du paiement: " + e.getMessage(), e);
        }
    }

    @Transactional
    public PaiementResponse confirmerPaiement(String referencePaiement) {
        log.info("Confirmation du paiement: {}", referencePaiement);

        Paiement paiement = paiementRepository.findByReferencePaiement(referencePaiement)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé: " + referencePaiement));

        if (paiement.getStatut() == StatutPaiement.REUSSIE) {
            log.warn("Le paiement {} est déjà confirmé", referencePaiement);
            return mapToResponse(paiement);
        }

        try {
            // Vérifier le statut auprès du provider
            boolean estConfirme;

            if (paiement.getTypePaiement() == TypePaiement.STRIPE) {
                estConfirme = stripePaymentService.verifierPaiement(paiement.getPaymentIntentId());
            } else if (paiement.getTypePaiement() == TypePaiement.PAYPAL) {
                estConfirme = payPalPaymentService.verifierPaiement(paiement.getPaymentIntentId());
            } else {
                throw new IllegalArgumentException("Type de paiement non supporté");
            }

            if (estConfirme) {
                paiement.setStatut(StatutPaiement.REUSSIE);
                paiement.setDateTraitement(LocalDateTime.now());
                log.info("Paiement {} confirmé avec succès", referencePaiement);
            } else {
                paiement.setStatut(StatutPaiement.ECHOUE);
                paiement.setMessageErreur("Paiement non confirmé par le provider");
                log.warn("Paiement {} non confirmé", referencePaiement);
            }

            paiementRepository.save(paiement);
            return mapToResponse(paiement);

        } catch (Exception e) {
            log.error("Erreur lors de la confirmation du paiement", e);
            paiement.setStatut(StatutPaiement.ECHOUE);
            paiement.setMessageErreur(e.getMessage());
            paiementRepository.save(paiement);

            throw new RuntimeException("Erreur lors de la confirmation: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void annulerPaiement(String referencePaiement) {
        log.info("Annulation du paiement: {}", referencePaiement);

        Paiement paiement = paiementRepository.findByReferencePaiement(referencePaiement)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé: " + referencePaiement));

        if (paiement.getStatut() == StatutPaiement.REUSSIE) {
            throw new RuntimeException("Impossible d'annuler un paiement réussi. Utilisez le remboursement.");
        }

        paiement.setStatut(StatutPaiement.ANNULE);
        paiementRepository.save(paiement);
    }

    @Transactional
    public PaiementResponse rembourserPaiement(String referencePaiement) {
        log.info("Remboursement du paiement: {}", referencePaiement);

        Paiement paiement = paiementRepository.findByReferencePaiement(referencePaiement)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé: " + referencePaiement));

        if (paiement.getStatut() != StatutPaiement.REUSSIE) {
            throw new RuntimeException("Seuls les paiements réussis peuvent être remboursés");
        }

        try {
            // Effectuer le remboursement auprès du provider
            boolean estRembourse;

            if (paiement.getTypePaiement() == TypePaiement.STRIPE) {
                estRembourse = stripePaymentService.rembourser(paiement.getPaymentIntentId());
            } else if (paiement.getTypePaiement() == TypePaiement.PAYPAL) {
                estRembourse = payPalPaymentService.rembourser(paiement.getPaymentIntentId());
            } else {
                throw new IllegalArgumentException("Type de paiement non supporté");
            }

            if (estRembourse) {
                paiement.setStatut(StatutPaiement.REMBOURSE);
                paiementRepository.save(paiement);
                log.info("Paiement {} remboursé avec succès", referencePaiement);
            }

            return mapToResponse(paiement);

        } catch (Exception e) {
            log.error("Erreur lors du remboursement", e);
            throw new RuntimeException("Erreur lors du remboursement: " + e.getMessage(), e);
        }
    }

    public PaiementResponse getPaiementByReference(String referencePaiement) {
        Paiement paiement = paiementRepository.findByReferencePaiement(referencePaiement)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé: " + referencePaiement));
        return mapToResponse(paiement);
    }

    public List<PaiementResponse> getPaiementsByCommande(Long commandeId) {
        return paiementRepository.findByCommandeId(commandeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<PaiementResponse> getPaiementsByClient(Long clientId) {
        return paiementRepository.findByClientId(clientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<PaiementResponse> getPaiementsByStatut(StatutPaiement statut) {
        return paiementRepository.findByStatut(statut)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private String genererReference() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private PaiementResponse mapToResponse(Paiement paiement) {
        return PaiementResponse.builder()
                .id(paiement.getId())
                .referencePaiement(paiement.getReferencePaiement())
                .commandeId(paiement.getCommandeId())
                .clientId(paiement.getClientId())
                .montant(paiement.getMontant())
                .devise(paiement.getDevise())
                .typePaiement(paiement.getTypePaiement())
                .statut(paiement.getStatut())
                .transactionId(paiement.getTransactionId())
                .dateCreation(paiement.getDateCreation())
                .dateTraitement(paiement.getDateTraitement())
                .messageErreur(paiement.getMessageErreur())
                .build();
    }
}

