package com.ecom.commandesservice.service;


import com.ecom.commandesservice.dto.CommandeDtoDetail;
import com.ecom.commandesservice.dto.CreateCommandeDto;
import com.ecom.commandesservice.entite.Commande;
import com.ecom.commandesservice.entite.LigneCommande;

import com.ecom.commandesservice.feignrequests.ClientDto;
import com.ecom.commandesservice.feignrequests.ProduitDto;
import com.ecom.commandesservice.feignrequests.ProduitFeignRequest;
import com.ecom.commandesservice.feignrequests.UsersFeignRequest;
import com.ecom.commandesservice.mapper.CommandeMapper;
import com.ecom.commandesservice.mapper.CommandeMapperDetail;
import com.ecom.commandesservice.repository.CommandeRepository;
import com.ecom.commandesservice.repository.LigneCommandeRepository;
import feign.FeignException;
import jakarta.ws.rs.core.UriBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommandeService {
    private  final CommandeRepository commandeRepository;
    private  final LigneCommandeRepository ligneCommandeRepository;
    private final CommandeMapperDetail commandeMapperDetail;
    private final UsersFeignRequest usersFeignRequest;
    private final ProduitFeignRequest produitFeignRequest;


    public CommandeService(CommandeRepository commandeRepository, LigneCommandeRepository ligneCommandeRepository, CommandeMapperDetail commandeMapperDetail, UsersFeignRequest usersFeignRequest, ProduitFeignRequest produitFeignRequest) {
        this.commandeRepository = commandeRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.commandeMapperDetail = commandeMapperDetail;
        this.usersFeignRequest = usersFeignRequest;
        this.produitFeignRequest = produitFeignRequest;
    }




    public void ajouterligneCommande(Commande commande, LigneCommande ligneCommande){
        commande.getLignesCommande().add(ligneCommande);
        ligneCommandeRepository.save(ligneCommande);
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

    public ResponseEntity<?> createCommande(Commande commande, String idempotency) {
        Commande cmd=commandeRepository.findCommandeByIdempotencyKey(idempotency);

        if(cmd!=null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(cmd);
        }

        if((commande==null) || (commande.getLignesCommande().size()==0)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Commande invalide");
        }

        // 1- valider le client
        ResponseEntity<ClientDto> clientResponse= usersFeignRequest.getClientById(commande.getClientId());

        if(!clientResponse.getStatusCode().is2xxSuccessful()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Client invalide"+commande.getClientId() );
        }

        if(clientResponse.getBody().getEtat()==0||clientResponse.getBody().getEtat()==2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Client non autorise"+commande.getClientId() );
        }

        //2- valider les produits + calculer le sous total de chaque ligne de commande
        List<LigneCommande> lignesCommande=commande.getLignesCommande();


        double total=0;

        List<ProduitDto> listproduitDto=new ArrayList<>();

        for(LigneCommande ligneCommande:lignesCommande){
           ResponseEntity<ProduitDto> produitRespnse= produitFeignRequest.getProduitById(ligneCommande.getProduitId());
            listproduitDto.add(produitRespnse.getBody());
           if(!produitRespnse.getStatusCode().is2xxSuccessful()){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Produit invalide"+ligneCommande.getProduitId() );
           }

           System.out.println(produitRespnse.getBody().getQteStock());
            System.out.println(ligneCommande.getQuantite());

            if(produitRespnse.getBody().getQteStock()<ligneCommande.getQuantite()){
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Out of stock"+ligneCommande.getProduitId() );

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
        commande.setIdempotencyKey(idempotency);
        commandeRepository.save(commande);
        return ResponseEntity.ok(commandeMapperDetail.toDto(commande));
    }














}
