package com.example.Service_Produit.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "produits")
public class Produit {
    @Id
    private String id; // Identifiant du produit
    private String name; // Nom du produit
    private String description; // Description du produit
    private double price; // Prix du produit
    private String imageResId; // Image associée au produit
    private int quantity; // Quantité en stock
}


