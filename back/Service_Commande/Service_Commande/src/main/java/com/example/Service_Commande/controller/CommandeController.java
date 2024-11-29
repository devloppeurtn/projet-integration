package com.example.Service_Commande.controller;

import com.example.Service_Commande.dto.CommandeDto;
import com.example.Service_Commande.dto.ProduitQuantiteDto;
import com.example.Service_Commande.entity.Commande;
import com.example.Service_Commande.entity.Order;
import com.example.Service_Commande.entity.ProduitQuantite;
import com.example.Service_Commande.repository.OrderRepository;
import com.example.Service_Commande.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class CommandeController {


    @Autowired
    private OrderRepository orderRepository;
    @PostMapping("neworder")
    @ResponseStatus(HttpStatus.CREATED)
    public Order placeOrder(@RequestBody Order order) {
        // Vous pouvez ajouter une logique de validation ici si nécessaire.
        // Par exemple, vous pouvez vérifier que l'utilisateur existe ou valider la disponibilité des articles.

        // Enregistrer la commande dans MongoDB.
        return orderRepository.save(order); // Enregistrement de la commande dans la base de données.
    }
}
