package com.example.Service_Commande.repository;

import com.example.Service_Commande.entity.Commande;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeRepository extends MongoRepository<Commande, String> {
}
