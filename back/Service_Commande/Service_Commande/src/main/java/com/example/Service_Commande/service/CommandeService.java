package com.example.Service_Commande.service;

import com.example.Service_Commande.entity.Commande;
import com.example.Service_Commande.entity.Produit;
import com.example.Service_Commande.entity.ProduitQuantite;
import com.example.Service_Commande.exception.CommandeNotFoundException;
import com.example.Service_Commande.repository.CommandeRepository;
import com.example.Service_Commande.repository.ProduitClient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private ProduitClient produitClient;

    // Créer une commande
    public Commande creerCommande(Commande commande) {
        for (ProduitQuantite produitQuantite : commande.getProduits()) {
            try {
                // Vérifier la disponibilité du produit
                Produit produit = verifierDisponibiliteProduit(produitQuantite.getProduitId());
                if (produit.getQuantiteEnStock() < produitQuantite.getQuantite()) {
                    log.warn("Le produit {} est en quantité insuffisante. Commande ignorée.", produit.getNom());
                    continue;
                }
            } catch (Exception e) {
                log.error("Erreur lors de la vérification du produit {}: {}", produitQuantite.getProduitId(), e.getMessage());
                throw new IllegalArgumentException("Le produit avec ID " + produitQuantite.getProduitId() + " n'est pas disponible.");
            }
        }

        // Calcul du total de la commande
        double totalCommande = calculerTotalCommande(commande);

        // Vous pouvez maintenant ajouter ce total à l'objet Commande avant de le sauvegarder
        commande.setTotal(totalCommande);
        commande.setDate(new Date());
        commande.setStatut("En cours");
        return commandeRepository.save(commande);
    }


    // Vérifier la disponibilité d'un produit
    public Produit verifierDisponibiliteProduit(String produitId) {
        log.info("Vérification de la disponibilité du produit avec ID : {}", produitId);
        try {
            // Utilisation de Feign pour obtenir les informations du produit
            Produit produit = produitClient.getProduitById(produitId);
            log.info("Produit trouvé : {}", produit);
            return produit;
        } catch (FeignException.NotFound e) {
            log.error("Produit avec ID {} non trouvé.", produitId);
            throw new IllegalArgumentException("Le produit avec ID " + produitId + " est introuvable.");
        } catch (FeignException.ServiceUnavailable e) {
            log.error("Service produit indisponible.");
            throw new RuntimeException("Service produit temporairement indisponible.");
        } catch (Exception e) {
            log.error("Erreur inconnue lors de la vérification du produit : {}", e.getMessage());
            throw new RuntimeException("Erreur inconnue lors de la vérification du produit.");
        }
    }

    // Obtenir une commande par ID
    public Commande obtenirCommandeParId(String id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new CommandeNotFoundException("Commande avec ID " + id + " introuvable"));
    }

    // Obtenir toutes les commandes
    public List<Commande> obtenirToutesLesCommandes() {
        return commandeRepository.findAll();
    }

    // Annuler une commande
    public void annulerCommande(String id) {
        Commande commande = obtenirCommandeParId(id);
        commande.setStatut("Annulée");
        commandeRepository.save(commande);
    }

    // Passer une commande avec gestion du stock
    public String passerCommande(String produitId, int quantite) {
        try {
            // Vérification du produit et de son stock
            Produit produit = produitClient.getProduitById(produitId);
            int stockDisponible = produit.getQuantiteEnStock();

            // Vérification si la quantité demandée est supérieure à la quantité en stock
            if (quantite > stockDisponible) {
                log.warn("Stock insuffisant pour le produit {}. Quantité demandée : {}, Quantité en stock : {}",
                        produit.getNom(), quantite, stockDisponible);
                return "Stock insuffisant pour le produit " + produit.getNom() + ". Quantité en stock : " + stockDisponible;
            }

            // Mettre à jour le stock du produit
            int nouvelleQuantite = stockDisponible - quantite;
            log.info("Mise à jour du stock du produit {}: {} -> {}", produit.getNom(), stockDisponible, nouvelleQuantite);
            produitClient.mettreAJourStock(produitId, nouvelleQuantite);
            log.info("Commande passée avec succès pour le produit {}. Nouveau stock : {} unités",
                    produit.getNom(), nouvelleQuantite);

            return "Commande passée avec succès pour le produit " + produit.getNom() + ". Nouveau stock : " + nouvelleQuantite + " unités";
        } catch (FeignException.NotFound e) {
            log.error("Produit non trouvé pour l'ID {}", produitId);
            return "Produit avec l'ID " + produitId + " non trouvé.";
        } catch (FeignException.ServiceUnavailable e) {
            log.error("Service produit temporairement indisponible. ID du produit : {}", produitId);
            return "Le service produit est temporairement indisponible. Veuillez réessayer plus tard.";
        } catch (Exception e) {
            log.error("Erreur inattendue lors du passage de la commande pour le produit {} : {}", produitId, e.getMessage());
            return "Une erreur est survenue lors du passage de la commande : " + e.getMessage();
        }
    }
    public double calculerTotalCommande(Commande commande) {
        double total = 2.0;

        // Parcourir tous les produits dans la commande
        for (ProduitQuantite produitQuantite : commande.getProduits()) {
            try {
                // Vérifier si le produit existe
                Produit produit = produitClient.getProduitById(produitQuantite.getProduitId());
                double prixUnitaire = produit.getPrixUnitaire();

                // Ajouter le total pour ce produit
                total += prixUnitaire * produitQuantite.getQuantite();
            } catch (FeignException.NotFound e) {
                log.error("Produit non trouvé pour l'ID {}", produitQuantite.getProduitId());
            }
        }

        log.info("Le total de la commande est : {}", total);
        return total;
    }



}
