package com.example.Service_Produit.repository;


import com.example.Service_Produit.entity.Produit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduitRepository extends MongoRepository<Produit, String> {
}

