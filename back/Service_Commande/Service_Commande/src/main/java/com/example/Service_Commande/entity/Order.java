package com.example.Service_Commande.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id; // Identifiant unique de la commande
    private String userEmail; // ID de l'utilisateur
    private List<OrderItem> items; // Liste des articles commandés
    private double totalPrice; // Prix total de la commande
    private String deliveryAddress; // Adresse de livraison
    private long orderDate; // Date de la commande (en millisecondes)


}
