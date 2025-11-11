package com.ecom.paiementservice.repository;

import com.ecom.paiementservice.entite.Paiement;
import com.ecom.paiementservice.enums.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    Optional<Paiement> findByReferencePaiement(String referencePaiement);

    Optional<Paiement> findByTransactionId(String transactionId);

    Optional<Paiement> findByPaymentIntentId(String paymentIntentId);

    List<Paiement> findByCommandeId(Long commandeId);

    List<Paiement> findByClientId(Long clientId);

    List<Paiement> findByStatut(StatutPaiement statut);

    List<Paiement> findByClientIdAndStatut(Long clientId, StatutPaiement statut);

}
