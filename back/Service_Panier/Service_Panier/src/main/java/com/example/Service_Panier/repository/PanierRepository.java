package com.example.Service_Panier.repository;

import com.example.Service_Panier.entity.Panier;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PanierRepository extends MongoRepository<Panier, String> {
    Optional<Panier> findByUserId(String userId);
}
