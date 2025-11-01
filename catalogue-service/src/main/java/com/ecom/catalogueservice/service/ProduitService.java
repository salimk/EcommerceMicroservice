package com.ecom.catalogueservice.service;

import com.ecom.catalogueservice.entite.Produit;
import com.ecom.catalogueservice.repository.ProduitRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> getAllProduits(){
        return produitRepository.findAll();
    }

    public Produit getProduitById(Long id){
        return produitRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());

    }
    public void AjouterProduit(Produit produit) {
        produitRepository.save(produit);
    }
}
