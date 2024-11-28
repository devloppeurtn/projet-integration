package com.example.Service_Panier.controller;

import com.example.Service_Panier.entity.Panier;
import com.example.Service_Panier.entity.ProduitQuantite;
import com.example.Service_Panier.service.PanierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/panier")
public class PanierController {

    @Autowired
    private PanierService panierService;

    @GetMapping("/{userId}")
    public Panier getPanier(@PathVariable String userId) {
        return panierService.getPanierByUserId(userId);
    }

    @PostMapping("/{userId}/ajouter")
    public Panier ajouterProduit(@PathVariable String userId, @RequestBody @Valid ProduitQuantite produitQuantite) {
        return panierService.ajouterProduit(userId, produitQuantite);
    }

    @PutMapping("/{userId}/modifier")
    public Panier modifierQuantite(@PathVariable String userId, @RequestBody @Valid ProduitQuantite produitQuantite) {
        return panierService.modifierQuantite(userId, produitQuantite);
    }

    @DeleteMapping("/{userId}/supprimer")
    public Panier supprimerProduit(@PathVariable String userId, @RequestParam String produitId) {
        return panierService.supprimerProduit(userId, produitId);
    }

    @DeleteMapping("/{userId}/vider")
    public Panier viderPanier(@PathVariable String userId) {
        return panierService.viderPanier(userId);
    }
}
