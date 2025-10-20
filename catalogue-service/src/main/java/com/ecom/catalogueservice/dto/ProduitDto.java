package com.ecom.catalogueservice.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link com.ecom.catalogueservice.entite.Produit}
 */
public class ProduitDto implements Serializable {
    private final Long id;
    private final String nom;
    private final String codebarre;
    private final double prix;

    public ProduitDto(Long id, String nom, String codebarre, double prix) {
        this.id = id;
        this.nom = nom;
        this.codebarre = codebarre;
        this.prix = prix;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getCodebarre() {
        return codebarre;
    }

    public double getPrix() {
        return prix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProduitDto entity = (ProduitDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.nom, entity.nom) &&
                Objects.equals(this.codebarre, entity.codebarre) &&
                Objects.equals(this.prix, entity.prix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, codebarre, prix);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "nom = " + nom + ", " +
                "codebarre = " + codebarre + ", " +
                "prix = " + prix + ")";
    }
}