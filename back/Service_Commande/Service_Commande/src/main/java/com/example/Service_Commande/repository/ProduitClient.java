package com.example.Service_Commande.repository;

import com.example.Service_Commande.entity.Produit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "service-produit", url = "http://localhost:8088")
public interface ProduitClient {

    @RequestMapping(method = RequestMethod.GET, value = "/produits/{id}")
    Produit verifierProduit(@PathVariable("id") String id);

    // Ajouter une annotation HTTP pour la méthode
    @GetMapping("/produits/{id}")
    Produit getProduitById(@PathVariable("id") String id);

    @PutMapping("/produits/{id}/stock")
    void mettreAJourStock(@PathVariable("id") String id, @RequestParam int nouvelleQuantite);
}




