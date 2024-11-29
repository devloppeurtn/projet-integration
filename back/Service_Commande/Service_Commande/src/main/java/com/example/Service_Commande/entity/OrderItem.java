package com.example.Service_Commande.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItem {

    private String productId; // L'ID du produit
    private String productName; // Nom du produit
    private double productPrice; // Prix du produit
    private int quantity; // Quantité de produit
    private String productImageUrl; // URL de l'image du produit
}