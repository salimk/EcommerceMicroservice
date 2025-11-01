package com.ecom.commandesservice.service;

import com.ecom.commandesservice.apicall.ClientServiceFeign;
import com.ecom.commandesservice.apicall.ProduitServiceFeign;
import com.ecom.commandesservice.apicall.dtoResponse.ClientResponse;
import com.ecom.commandesservice.apicall.dtoResponse.ProduitResponse;
import com.ecom.commandesservice.entite.Commande;
import com.ecom.commandesservice.entite.LigneCommande;
import com.ecom.commandesservice.mapper.CommandeMapper;
import com.ecom.commandesservice.mapper.CommandeMapperDetail;
import com.ecom.commandesservice.repository.CommandeRepository;
import com.ecom.commandesservice.repository.LigneCommandeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.geom.RectangularShape;
import java.util.List;

@Service
public class CommandeService {
    private  final CommandeRepository commandeRepository;
    private  final LigneCommandeRepository ligneCommandeRepository;
    private final ClientServiceFeign clientServiceFeign;
    private final ProduitServiceFeign produitServiceFeign;
    private final CommandeMapperDetail commandeMapperDetail;

    public CommandeService(CommandeRepository commandeRepository, LigneCommandeRepository ligneCommandeRepository, ClientServiceFeign clientServiceFeign, ProduitServiceFeign produitServiceFeign, CommandeMapperDetail commandeMapperDetail) {
        this.commandeRepository = commandeRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
        this.clientServiceFeign = clientServiceFeign;
        this.produitServiceFeign = produitServiceFeign;
        this.commandeMapperDetail = commandeMapperDetail;
    }

    /// good
    public ResponseEntity<?> ajouterCommande(Commande commande){
        //call api client service
       ResponseEntity<ClientResponse> responseClient =clientServiceFeign.getProduitbyId(commande.getClientId());
        if (responseClient.getStatusCode().is2xxSuccessful()) {
            ClientResponse clientResponse =responseClient.getBody();
        }else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Client not found in client-service ");

        double total = 0;
        List<LigneCommande> lignes = commande.getLignesCommande();
        for (LigneCommande ligne : lignes) {
            ResponseEntity<ProduitResponse> responseProduit = produitServiceFeign.getProduitbyId(ligne.getProduitId());

            if( responseProduit.getStatusCode().is2xxSuccessful()){
                ProduitResponse produitResponse = responseProduit.getBody();
                ligne.setPrixUnitaire(produitResponse.getPrix());
                System.out.println("name"+produitResponse.getNom());
                ligne.setSousTotal( ligne.getQuantite() * ligne.getPrixUnitaire());
                ligne.setCommande(commande);
                total += ligne.getSousTotal();
            } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Produit not found in catalogue-service ");
        }
        commande.setPrixTotal(total);
        commande.setStatut("En cours");
        commande.setDateCommande(new java.util.Date());

        commandeRepository.save(commande);

        return ResponseEntity.ok(commandeMapperDetail.toDto(commande));
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
}
