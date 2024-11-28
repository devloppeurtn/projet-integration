package com.example.Service_Panier.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "paniers")
public class Panier {
    @Id
    private String id;
    private String userId;
    private List<ProduitQuantite> produits = new ArrayList<>();

    public Panier(String userId) {
        this.userId = userId;
    }

    public void ajouterProduit(String produitId, int quantite) {
        for (ProduitQuantite pq : produits) {
            if (pq.getProduit().equals(produitId)) {
                pq.setQuantite(pq.getQuantite() + quantite);
                return;
            }
        }
        produits.add(new ProduitQuantite(produitId, quantite));
    }

    public void modifierQuantite(String produitId, int quantite) {
        for (ProduitQuantite pq : produits) {
            if (pq.getProduit().equals(produitId)) {
                pq.setQuantite(quantite);
                return;
            }
        }
    }

    public void supprimerProduit(String produitId) {
        produits.removeIf(pq -> pq.getProduit().equals(produitId));
    }

    public void viderPanier() {
        produits.clear();
    }
}
