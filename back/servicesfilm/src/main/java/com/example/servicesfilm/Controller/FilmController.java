package com.example.servicesfilm.Controller;

import com.example.servicesfilm.Entity.Category;
import com.example.servicesfilm.Entity.MovieRequest;
import com.example.servicesfilm.Entity.film;
import com.example.servicesfilm.Repository.FilmRepository;
import com.example.servicesfilm.service.filmService;
import com.example.servicesfilm.service.tmdbservices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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

    @PostMapping("/add")
    public ResponseEntity<film> addMovie(@RequestBody film movie) {
        // Log des données reçues
        System.out.println("Données reçues: " + movie);

        // Traitement de l'ajout du film
     filmService.addMovie(movie);
        return ResponseEntity.ok(movie);
    }


    @PatchMapping("/modifier/{id}")
    public ResponseEntity<?> updateFilmById(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> updates) {
        try {
            film updatedFilm = filmService.updateFilmById(id, updates);
            return ResponseEntity.ok(updatedFilm);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Film not found with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFilmById(@PathVariable("id") int id) {
        try {
            boolean isDeleted = filmService.deleteFilmById(id);
            if (isDeleted) {
                return ResponseEntity.ok("Film supprimé avec succès.");
            } else {
                return ResponseEntity.status(404).body("Film non trouvé.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la suppression du film : " + e.getMessage());
        }
    }

}
