package com.ecom.commandesservice.service;

import com.ecom.commandesservice.entite.Commande;
import com.ecom.commandesservice.entite.LigneCommande;
import com.ecom.commandesservice.repository.CommandeRepository;
import com.ecom.commandesservice.repository.LigneCommandeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeService {
    private  final CommandeRepository commandeRepository;
    private  final LigneCommandeRepository ligneCommandeRepository;
    public CommandeService(CommandeRepository commandeRepository, LigneCommandeRepository ligneCommandeRepository) {
        this.commandeRepository = commandeRepository;
        this.ligneCommandeRepository = ligneCommandeRepository;
    }
    /// good
    public void ajouterCommande(Commande commande){
        Commande savedCommande = commandeRepository.save(commande);
        // Si les lignes ne sont pas sauvegardées automatiquement, les sauvegarder manuellement
        if (commande.getListligneCommande() != null) {
            commande.getListligneCommande().forEach(ligne -> {
                ligne.setCommande(savedCommande);
                ligneCommandeRepository.save(ligne);
            });
        }
    }

    public void ajouterligneCommande(Commande commande, LigneCommande ligneCommande){
        commande.getListligneCommande().add(ligneCommande);
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
