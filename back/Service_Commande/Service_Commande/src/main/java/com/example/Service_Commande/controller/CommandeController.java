package com.example.Service_Commande.controller;

import com.example.Service_Commande.dto.CommandeDto;
import com.example.Service_Commande.dto.ProduitQuantiteDto;
import com.example.Service_Commande.entity.Commande;
import com.example.Service_Commande.entity.ProduitQuantite;
import com.example.Service_Commande.service.CommandeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    private final CommandeService commandeService;

    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    @PostMapping
    public ResponseEntity<Commande> creerCommande(@RequestBody CommandeDto commandeDto) {
        try {
            Commande commande = new Commande();
            commande.setUserId(commandeDto.getUserId());
            commande.setProduits(commandeDto.getProduits().stream()
                    .map(dto -> {
                        ProduitQuantite produitQuantite = new ProduitQuantite();
                        produitQuantite.setProduitId(dto.getProduitId());
                        produitQuantite.setQuantite(dto.getQuantite());
                        return produitQuantite;
                    }).toList());
            Commande nouvelleCommande = commandeService.creerCommande(commande);
            return new ResponseEntity<>(nouvelleCommande, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Commande> obtenirCommandeParId(@PathVariable String id) {
        Commande commande = commandeService.obtenirCommandeParId(id);
        return ResponseEntity.ok(commande);
    }

    @GetMapping
    public ResponseEntity<List<Commande>> obtenirToutesLesCommandes() {
        List<Commande> commandes = commandeService.obtenirToutesLesCommandes();
        return ResponseEntity.ok(commandes);
    }

    @PutMapping("/{id}/annuler")
    public ResponseEntity<Void> annulerCommande(@PathVariable String id) {
        commandeService.annulerCommande(id);
        return ResponseEntity.noContent().build();
    }

    // Passage de commande avec gestion du stock
    @PostMapping("/passer")
    public ResponseEntity<String> passerCommande(@RequestBody CommandeDto commandeDto) {
        // Récupérer l'ID de l'utilisateur
        String userId = commandeDto.getUserId();

        // Vérifier que l'ID de l'utilisateur est présent
        if (userId == null || userId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("L'ID utilisateur est requis.");
        }

        // Traiter chaque produit et sa quantité
        for (ProduitQuantiteDto produitDto : commandeDto.getProduits()) {
            String produitId = produitDto.getProduitId();
            int quantite = produitDto.getQuantite();

            // Appeler la logique pour passer la commande
            String message = commandeService.passerCommande(produitId, quantite);

            if (message.contains("Stock insuffisant")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        }

        return ResponseEntity.ok("Commande passée avec succès");
    }

}
