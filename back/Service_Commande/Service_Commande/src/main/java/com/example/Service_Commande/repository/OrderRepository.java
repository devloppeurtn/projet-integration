package com.example.Service_Commande.repository;

import com.example.Service_Commande.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
    // Vous pouvez ajouter des méthodes de requêtes spécifiques si nécessaire
}