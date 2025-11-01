package com.ecom.catalogueservice.controller;

import com.ecom.catalogueservice.dto.ProduitDto;
import com.ecom.catalogueservice.mapper.ProduitMapper;
import com.ecom.catalogueservice.service.ProduitService;
import com.ecom.catalogueservice.entite.Produit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produits")
public class ProduitController {
    private final ProduitService produitService;
    private final ProduitMapper produitMapper;
    public ProduitController(ProduitService produitService, ProduitMapper produitMapper){
        this.produitService = produitService;
        this.produitMapper = produitMapper;
    }

        @GetMapping
        public ResponseEntity<List<ProduitDto>> getAllProduits() {
            return ResponseEntity.ok(produitService.getAllProduits().stream()
                    .map(produitMapper::toDto)
                    .toList());
        }

        @GetMapping("/{id}")
        public ResponseEntity<ProduitDto> getProduitById(@PathVariable Long id) {
            return ResponseEntity.ok(produitMapper.toDto(produitService.getProduitById(id)));
        }

        @PostMapping("/new")
        public ResponseEntity<ProduitDto> ajouterProduit(@RequestBody ProduitDto produitDto){
            Produit produit = produitMapper.toEntity(produitDto);
            produitService.AjouterProduit(produit);
            return ResponseEntity.ok(produitMapper.toDto(produit));
        }
}
