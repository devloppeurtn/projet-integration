package com.example.servicesfilm.Repository;

import com.example.servicesfilm.Entity.Category;
import com.example.servicesfilm.Entity.film;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FilmRepository extends MongoRepository<film, String> {
    List<film> findByCategory(Category category);  // Recherche de films par catégorie
}
