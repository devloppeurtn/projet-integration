package com.example.servicesfilm.Repository;

import com.example.servicesfilm.Entity.film;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FilmRepository extends MongoRepository<film, String> {
}
