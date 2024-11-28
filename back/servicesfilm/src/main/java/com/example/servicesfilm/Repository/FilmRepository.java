package com.example.servicesfilm.Repository;

import com.example.servicesfilm.Entity.Category;
import com.example.servicesfilm.Entity.film;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FilmRepository extends MongoRepository<film, Integer> {
    List<film> findByCategory(Category category);// Recherche de films par catégorie
    List<film> findAllById(List<Integer> ids);


}
