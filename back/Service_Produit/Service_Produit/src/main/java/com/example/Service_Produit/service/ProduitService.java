package com.example.Service_Produit.service;

import com.example.Service_Produit.entity.Produit;
import com.example.Service_Produit.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public Produit creerProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    // Obtenir tous les produits
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    // Obtenir un produit par ID
    public Produit getProduitById(String id) {
        return produitRepository.findById(id).orElse(null);
    }

    // Ajouter un nouveau produit
    public Produit createProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    // Mettre à jour un produit existant
    public Produit updateProduit(String id, Produit produitDetails) {
        Produit produit = produitRepository.findById(id).orElse(null);
        if (produit != null) {
            produit.setName(produitDetails.getName());
            produit.setDescription(produitDetails.getDescription());
            produit.setPrice(produitDetails.getPrice());
            produit.setImageResId(produitDetails.getImageResId());
            produit.setQuantity(produitDetails.getQuantity());
            return produitRepository.save(produit);
        }
        return null;
    }

    // Supprimer un produit
    public void deleteProduit(String id) {
        produitRepository.deleteById(id);
    }
}
