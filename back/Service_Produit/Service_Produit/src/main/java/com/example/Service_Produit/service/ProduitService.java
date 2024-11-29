package com.example.Service_Produit.service;





import com.example.Service_Produit.entity.Produit;
import com.example.Service_Produit.exception.ProduitNotFoundException;
import com.example.Service_Produit.repository.ProduitRepository;
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

    public Produit obtenirProduitParId(String id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new ProduitNotFoundException("Produit avec ID " + id + " introuvable"));
    }


    public List<Produit> obtenirTousProduits() {
        return produitRepository.findAll();
    }

    public Produit modifierProduit(String id, Produit produitMisAJour) {
        // Récupérer le produit existant
        Produit produitExistant = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit avec ID " + id + " introuvable"));

        // Mettre à jour les informations du produit
        produitExistant.setName(produitMisAJour.getName());
        produitExistant.setDescription(produitMisAJour.getDescription());
        produitExistant.setPrice(produitMisAJour.getPrice());
        produitExistant.setQuantity(produitMisAJour.getQuantity());  // N'oubliez pas de renommer "quantiteEnStock" en "quantity" si vous avez changé le nom du champ

        // Sauvegarder et retourner le produit mis à jour
        return produitRepository.save(produitExistant);
    }


    public void supprimerProduit(String id) {
        Produit produit = obtenirProduitParId(id);
        produitRepository.delete(produit);
    }
    public Produit mettreAJourStock(String produitId, int nouvelleQuantite) {
        // Récupérer le produit par ID
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé"));

        // Mettre à jour la quantité en stock
        produit.setQuantity(nouvelleQuantite);

        // Sauvegarder les modifications dans la base de données
        produitRepository.save(produit);
        return produit;
    }
}



