package com.example.servicesfilm.Controller;

import com.example.servicesfilm.Entity.Category;
import com.example.servicesfilm.Entity.film;
import com.example.servicesfilm.Repository.FilmRepository;
import com.example.servicesfilm.service.filmService;
import com.example.servicesfilm.service.tmdbservices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/films")
public class FilmController {
    private final tmdbservices tmdbServices;
    private final filmService filmService;

    public FilmController(tmdbservices tmdbServices, FilmRepository movieRepository, com.example.servicesfilm.service.filmService filmService) {
        this.tmdbServices = tmdbServices;
        this.filmService = filmService;
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





    @GetMapping
    public ResponseEntity<List<film>> getAllFilms() {
        List<film> films = filmService.getAllFilms();
        return ResponseEntity.ok(films);
    }


    @PostMapping("/details")
    public ResponseEntity<List<film>> getMoviesDetails(@RequestBody List<Integer> movieIds) {
        List<film> movies = filmService.getMoviesByIds(movieIds); // Appel correct au service
        return ResponseEntity.ok(movies); // Retourne les films dans la réponse
    }
    @GetMapping("/search")
    public List<film> searchFilms(@RequestParam String keyword) {
        return filmService.searchByAll(keyword);
    }




}
