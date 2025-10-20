package com.ecom.catalogueservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import com.ecom.catalogueservice.entite.Produit;
import com.ecom.catalogueservice.service.ProduitService;

@SpringBootApplication
@EnableDiscoveryClient
public class CatalogueServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogueServiceApplication.class, args);
    }

    @Bean
    ApplicationRunner init(ProduitService produitService) {
        return args -> {
            produitService.save(new Produit(null, "Laptop", "High performance laptop", 999.99));
            produitService.save(new Produit(null, "Smartphone", "Latest model smartphone", 699.99));
            produitService.save(new Produit(null, "Tablet", "10-inch tablet", 299.99));
            produitService.save(new Produit(null, "Headphones", "Wireless noise-canceling headphones", 199.99));
        };
    }

}
