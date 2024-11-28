package com.example.Service_Commande.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Document(collection = "commandes")
public class Commande {

    // Getters et Setters
    @Id
    private String id;
    private String userId; // Si l'utilisateur est identifié par un ID
    private List<ProduitQuantite> produits;
    private Date date;
    private String statut;
    private double total;

    // Constructeurs
    public Commande() {
    }

    public Commande(String userId, List<ProduitQuantite> produits, Date date, String statut) {
        this.userId = userId;
        this.produits = produits;
        this.date = date;
        this.statut = statut;
    }

}

