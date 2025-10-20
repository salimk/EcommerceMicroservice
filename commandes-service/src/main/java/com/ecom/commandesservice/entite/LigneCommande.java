package com.ecom.commandesservice.entite;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "Lignecommande")
public class LigneCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcommande", nullable = false)
    private Commande commande;
    private String codebarre;
    private String nomproduit;
    private int quantite;
    private double prixUnitaire;
}

