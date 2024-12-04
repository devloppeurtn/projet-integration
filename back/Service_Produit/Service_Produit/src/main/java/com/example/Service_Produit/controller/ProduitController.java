package com.example.Service_Produit.controller;

import com.example.Service_Produit.entity.Produit;
import com.example.Service_Produit.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    // Endpoint pour obtenir tous les produits
    @GetMapping
    public List<Produit> getAllProduits() {
        return produitService.getAllProduits();
    }

    // Endpoint pour obtenir un produit par ID
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable String id) {
        Produit produit = produitService.getProduitById(id);
        if (produit != null) {
            return ResponseEntity.ok(produit);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint pour créer un produit
    @PostMapping("/creer")
    public Produit createProduit(@RequestBody Produit produit) {
        return produitService.createProduit(produit);
    }

    // Endpoint pour mettre à jour un produit
    @PutMapping("/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable String id, @RequestBody Produit produitDetails) {
        Produit updatedProduit = produitService.updateProduit(id, produitDetails);
        if (updatedProduit != null) {
            return ResponseEntity.ok(updatedProduit);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint pour supprimer un produit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable String id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }
}
