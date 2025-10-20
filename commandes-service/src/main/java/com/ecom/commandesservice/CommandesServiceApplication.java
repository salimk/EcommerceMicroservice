package com.ecom.commandesservice;

import com.ecom.commandesservice.entite.Commande;
import com.ecom.commandesservice.entite.LigneCommande;
import com.ecom.commandesservice.service.CommandeService;
import jakarta.persistence.OneToMany;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class CommandesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommandesServiceApplication.class, args);
    }

    @Bean
    ApplicationRunner init(CommandeService commandeService) {
        return args -> {

            Commande commandeA = new Commande(null,1L,new Date(),null,0,"en-cours");

            LigneCommande ligne1 = new LigneCommande(null, commandeA, 1L, "laptop", 1, 2000.00);
            LigneCommande ligne2 = new LigneCommande(null, commandeA, 2L, "laptop", 1, 2000.00);
            LigneCommande ligne3 = new LigneCommande( null, commandeA,3L, "laptop", 1, 2000.00);

            List<LigneCommande> lignes = Arrays.asList(ligne1, ligne2, ligne3);
            commandeA.setListligneCommande(lignes);


            commandeService.ajouterCommande(commandeA);
        };
    }
}
