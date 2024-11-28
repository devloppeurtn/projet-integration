package com.example.Service_Panier.service;

import com.example.Service_Panier.dto.UserDTO;
import com.example.Service_Panier.entity.Panier;
import com.example.Service_Panier.entity.ProduitQuantite;
import com.example.Service_Panier.exception.UserNotFoundException;
import com.example.Service_Panier.repository.PanierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PanierService {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private PanierRepository panierRepository;

    public Panier getPanierByUserId(String userId) {
        UserDTO user = userServiceClient.getUserById(userId);

        if (user == null) {
            throw new UserNotFoundException("Utilisateur introuvable : " + userId);
        }

        return panierRepository.findByUserId(userId).orElseGet(() -> {
            Panier newPanier = new Panier(userId);
            panierRepository.save(newPanier);
            return newPanier;
        });
    }

    public Panier ajouterProduit(String userId, ProduitQuantite produitQuantite) {
        Panier panier = getPanierByUserId(userId);
        panier.ajouterProduit(produitQuantite.getProduit(), produitQuantite.getQuantite());
        return panierRepository.save(panier);
    }

    public Panier modifierQuantite(String userId, ProduitQuantite produitQuantite) {
        Panier panier = getPanierByUserId(userId);
        panier.modifierQuantite(produitQuantite.getProduit(), produitQuantite.getQuantite());
        return panierRepository.save(panier);
    }

    public Panier supprimerProduit(String userId, String produitId) {
        Panier panier = getPanierByUserId(userId);
        panier.supprimerProduit(produitId);
        return panierRepository.save(panier);
    }

    public Panier viderPanier(String userId) {
        Panier panier = getPanierByUserId(userId);
        panier.viderPanier();
        return panierRepository.save(panier);
    }
}
