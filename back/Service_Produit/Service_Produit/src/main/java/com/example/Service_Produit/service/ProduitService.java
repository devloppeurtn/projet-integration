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
        Produit produitExistant = obtenirProduitParId(id);
        produitExistant.setNom(produitMisAJour.getNom());
        produitExistant.setDescription(produitMisAJour.getDescription());
        produitExistant.setPrix(produitMisAJour.getPrix());
        produitExistant.setQuantiteEnStock(produitMisAJour.getQuantiteEnStock());
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
        produit.setQuantiteEnStock(nouvelleQuantite);

        // Sauvegarder les modifications dans la base de données
        produitRepository.save(produit);
        return produit;
    }
}



