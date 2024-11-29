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
    private String id; // Correspond à 'id' en Kotlin
    private String name; // Correspond à 'name' en Kotlin
    private String description; // Correspond à 'description' en Kotlin
    private double price; // Correspond à 'price' en Kotlin
    private String imageResId; // Correspond à 'imageResId' en Kotlin
    private int quantity; // Correspond à 'quantity' en Kotlin

}


