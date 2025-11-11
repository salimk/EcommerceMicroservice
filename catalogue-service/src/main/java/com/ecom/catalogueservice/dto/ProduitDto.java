package com.ecom.catalogueservice.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link com.ecom.catalogueservice.entite.Produit}
 */
public class ProduitDto implements Serializable {
    private final Long id;
    private final String nom;
    private final String description;
    private final String sku;
    private final double prix;
    private final int qteStock;

    public ProduitDto(Long id, String nom, String description, String sku, double prix, int qteStock) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.sku = sku;
        this.prix = prix;
        this.qteStock = qteStock;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public String getSku() {
        return sku;
    }

    public double getPrix() {
        return prix;
    }

    public int getQteStock() {
        return qteStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProduitDto entity = (ProduitDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.nom, entity.nom) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.sku, entity.sku) &&
                Objects.equals(this.prix, entity.prix) &&
                Objects.equals(this.qteStock, entity.qteStock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, description, sku, prix, qteStock);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "nom = " + nom + ", " +
                "description = " + description + ", " +
                "sku = " + sku + ", " +
                "prix = " + prix + ", " +
                "qteStock = " + qteStock + ")";
    }
}