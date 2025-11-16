package com.ecom.commandesservice.service;


import com.ecom.commandesservice.dto.CommandeDtoDetail;
import com.ecom.commandesservice.dto.CreateCommandeDto;
import com.ecom.commandesservice.entite.Commande;
import com.ecom.commandesservice.entite.LigneCommande;

import com.ecom.commandesservice.feignrequests.*;
import com.ecom.commandesservice.feignrequests.ClientFeignRequest;
import com.ecom.commandesservice.mapper.CommandeMapper;
import com.ecom.commandesservice.mapper.CommandeMapperDetail;
import com.ecom.commandesservice.repository.CommandeRepository;
import com.ecom.commandesservice.repository.LigneCommandeRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.UriBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Service
public class CommandeService {

    private  final CommandeRepository commandeRepository;
    private final CommandeMapperDetail commandeMapperDetail;
    private final ResilientFeignRequests resilientFeignRequests;

    public CommandeService(CommandeRepository commandeRepository, CommandeMapperDetail commandeMapperDetail, ResilientFeignRequests resilientFeignRequests) {
        this.commandeRepository = commandeRepository;
        this.commandeMapperDetail = commandeMapperDetail;
        this.resilientFeignRequests = resilientFeignRequests;
    }


    public void ajouterligneCommande(Commande commande, LigneCommande ligneCommande){
        commande.getLignesCommande().add(ligneCommande);
        commandeRepository.save(commande);
    }

    public List<Commande> listallCommande(){
        return commandeRepository.findAll();
    }

    public Commande getCommandeById(Long id){
        return commandeRepository.findCommandeById(id);
    }

    public Commande ajouterCommande(Commande commandeA) {
        return commandeRepository.save(commandeA);
    }


    /// A faire :Capture FeignException et mappe en 502 Bad Gateway (service tiers HS) plutôt qu’en 400.
    /// Monnaie : remplacer double par BigDecimal
    /// (Optionnel) Expose côté produit un endpoint batch pour vérifier plusieurs IDs d’un coup.
    /// Ajouter Enum
    ///Ajoute @Valid sur le DTO entrant, et des contraintes (@NotNull, @Min(1)) sur quantités, IDs, etc.
    ///
    @Transactional
    public ResponseEntity<?> createCommande(Commande commande, String idempotencyKey) {
        Commande cmd=commandeRepository.findCommandeByIdempotencyKey(idempotencyKey);

        if(cmd!=null){
            return ResponseEntity.status(HttpStatus.OK).body(cmd);
        }

        if((commande==null) || (commande.getLignesCommande().size()==0)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Commande invalide");
        }

        // 1- valider le client

        try {
            ResponseEntity<ClientDto> clientResponse= resilientFeignRequests.getClientById(commande.getClientId());
            if(!clientResponse.getStatusCode().is2xxSuccessful()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Client invalide"+commande.getClientId() );
            }

            if(clientResponse.getBody().getEtat()==0||clientResponse.getBody().getEtat()==2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Client non autorise"+commande.getClientId() );
            }

            // 2- valider les prodtuis et calculer le sous total de chaque ligne de commande
            double total=0;
            // on declare hashmap pour enregister les quantité commandé de chaque produit pour mettre a jour le stock plus tard
            Map<Long,Integer> DonneeMaJProduits = new HashMap<>();

            for(LigneCommande ligneCommande:commande.getLignesCommande()){

                ResponseEntity<ProduitDto> produitRespnse= resilientFeignRequests.getProduitById(ligneCommande.getProduitId());

                if(!produitRespnse.getStatusCode().is2xxSuccessful()){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Produit invalide "+ligneCommande.getProduitId() );
                }
                DonneeMaJProduits.put(produitRespnse.getBody().getId(),ligneCommande.getQuantite());

                if(produitRespnse.getBody().getQteStock()<ligneCommande.getQuantite()){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stock insuffisant pour le produit:"+ligneCommande.getProduitId() );

                }

                ligneCommande.setSousTotal(produitRespnse.getBody().getPrix() * ligneCommande.getQuantite());
                ligneCommande.setCommande(commande);
                ligneCommande.setPrixUnitaire(produitRespnse.getBody().getPrix());

                total+=ligneCommande.getSousTotal();
            }
            //creer et enregistrer la commande avec les lignes de commande

            commande.setDateCommande(new Date());
            commande.setStatut("Attente");
            commande.setPrixTotal(total);
            commande.setIdempotencyKey(idempotencyKey);
            commandeRepository.save(commande);
            return ResponseEntity.ok(commandeMapperDetail.toDto(commande));


        } catch (RuntimeException e) {
            throw e;
        }
    }


/// Mise à jour du stock : ne la fais pas dans le service Commande en synchrone si tu vises microservices.
/// Préfère une SAGA (orchestration ou chorégraphie) :
/// OrderCreated → le service Produit tente ReserveStock.
/// StockReserved → la commande passe à CONFIRMEE.
/// StockRejected → la commande passe à ANNULEE.
/// Avantage : résilience, cohérence et pas de couplage fort
/// Notifications (email/SMS/…): asynchrones via RabbitMQ (work-queue).






}
