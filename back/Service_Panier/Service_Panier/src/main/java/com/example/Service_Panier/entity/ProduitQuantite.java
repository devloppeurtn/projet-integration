package com.example.Service_Panier.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProduitQuantite {
    @NotBlank(message = "L'ID du produit ne peut pas être vide")
    private String produit;

    @Min(value = 1, message = "La quantité doit être au moins égale à 1")
    private int quantite;

    public ProduitQuantite(String produit, int quantite) {
        this.produit = produit;
        this.quantite = quantite;
    }
}
