package com.example.servicesfilm.Controller;

import com.example.servicesfilm.Entity.film;
import com.example.servicesfilm.Repository.FilmRepository;
import com.example.servicesfilm.service.tmdbservices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class FilmController {
    private final tmdbservices tmdbServices;

    public FilmController(tmdbservices tmdbServices) {
        this.tmdbServices = tmdbServices;
    }

    @GetMapping("/import-tmdb")
    public String importMoviesFromTMDB() {
        tmdbServices.importMovies();
        return "Les films ont été importés depuis TMDB et enregistrés dans MongoDB.";
    }

    @GetMapping("/{id}")
    public ResponseEntity<film> getMovieDetails(@PathVariable("id") int movieId) {
        try {
            film movie = tmdbservices.getMovieDetails(movieId);
            return ResponseEntity.ok(movie);
        } catch (RuntimeException e) {
            // Gérer l'erreur ici (par exemple, journaliser l'erreur ou retourner un message d'erreur)
            return ResponseEntity.status(500).body(null); // Retourner un code d'erreur 500 si une erreur se produit
        }
    }
    @GetMapping("/message") // Définit l'endpoint à "/message"
    public String getMessage() {
        return "Hello, this is a simple message!"; // Message à retourner
    }
}
