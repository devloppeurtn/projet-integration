package com.example.Service_Produit.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "produits")
public class Produit {
    // Getters et Setters
    @Id
    private String id;
    private String nom;
    private String description;
    private double prix;
    private int quantiteEnStock;

}


