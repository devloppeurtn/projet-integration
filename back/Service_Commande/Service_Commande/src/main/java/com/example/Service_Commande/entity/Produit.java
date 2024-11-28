package com.example.Service_Commande.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Produit {
    // Getters et Setters
    private String id;
    private String nom;
    private String description;
    private double prixUnitaire;
    private int quantiteEnStock;

}
