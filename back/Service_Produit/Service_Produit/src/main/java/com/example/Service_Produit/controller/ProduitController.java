package com.example.Service_Produit.controller;

import com.example.Service_Produit.entity.Produit;
import com.example.Service_Produit.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    // Récupérer un produit par ID
    @GetMapping("/{id}")
    public ResponseEntity<Produit> obtenirProduitParId(@PathVariable String id) {
        if (id == null || id.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        String cleanedId = id.trim();
        Produit produit = produitService.obtenirProduitParId(cleanedId);
        return ResponseEntity.ok(produit);
    }



    // Créer un produit
    @PostMapping("/creer")
    public ResponseEntity<Produit> creerProduit(@RequestBody Produit produit) {
        Produit nouveauProduit = produitService.creerProduit(produit);
        return new ResponseEntity<>(nouveauProduit, HttpStatus.CREATED);
    }

    // Obtenir tous les produits
    @GetMapping
    public ResponseEntity<List<Produit>> obtenirTousProduits() {
        List<Produit> produits = produitService.obtenirTousProduits();
        return ResponseEntity.ok(produits);
    }

    // Modifier un produit
    @PutMapping("/{id}")
    public ResponseEntity<Produit> modifierProduit(@PathVariable String id, @RequestBody Produit produit) {
        Produit produitMisAJour = produitService.modifierProduit(id, produit);
        return ResponseEntity.ok(produitMisAJour);
    }

    // Supprimer un produit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerProduit(@PathVariable String id) {
        produitService.supprimerProduit(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/stock")
    public ResponseEntity<Produit> mettreAJourStock(@PathVariable String id, @RequestParam int nouvelleQuantite) {
        try {
            Produit produitMisAJour = produitService.mettreAJourStock(id, nouvelleQuantite);
            return ResponseEntity.ok(produitMisAJour);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Si le produit n'est pas trouvé
        }
    }
}
