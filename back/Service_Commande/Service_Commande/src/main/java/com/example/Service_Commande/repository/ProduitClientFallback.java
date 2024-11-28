package com.example.Service_Commande.repository;

import com.example.Service_Commande.entity.Produit;
import org.springframework.stereotype.Component;

@Component
public abstract class ProduitClientFallback implements ProduitClient {

    @Override
    public Produit getProduitById(String id) {
        // Retourner un produit fictif si le service produit est indisponible
        Produit produitFictif = new Produit();
        produitFictif.setId(id);
        produitFictif.setNom("Produit indisponible");
        produitFictif.setQuantiteEnStock(0);
        return produitFictif;
    }
}
