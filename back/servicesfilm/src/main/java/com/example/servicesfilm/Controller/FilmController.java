package com.example.servicesfilm.Controller;

import com.example.servicesfilm.Entity.Category;
import com.example.servicesfilm.Entity.film;
import com.example.servicesfilm.Repository.FilmRepository;
import com.example.servicesfilm.service.tmdbservices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/films")
public class FilmController {
    private final tmdbservices tmdbServices;
    private final FilmRepository movieRepository;
    public FilmController(tmdbservices tmdbServices, FilmRepository movieRepository) {
        this.tmdbServices = tmdbServices;
        this.movieRepository = movieRepository;
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



    @PostMapping("/films")
    public ResponseEntity<film> createFilm(@RequestBody film movie) {
        // Sauvegarder le film avec sa catégorie
        return ResponseEntity.ok(movieRepository.save(movie));
    }

    @GetMapping("/category/{category}")
    public List<film> getFilmsByCategory(@PathVariable("category") Category category) {
        return movieRepository.findByCategory(category);
    }
    @DeleteMapping("/films/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable("id") String id) {
        movieRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
   
    @PutMapping("/films/{id}")
    public ResponseEntity<film> editFilm(@PathVariable("id") String id, @RequestBody film updatedFilm) {
        film movie = movieRepository.findById(id).orElseThrow();
        movie.setTitle(updatedFilm.getTitle());
        movie.setDescription(updatedFilm.getDescription());
        movie.setReleaseYear(updatedFilm.getReleaseYear());
        // Mettre à jour d'autres champs si nécessaire
        return ResponseEntity.ok(movieRepository.save(movie));
    }

}
