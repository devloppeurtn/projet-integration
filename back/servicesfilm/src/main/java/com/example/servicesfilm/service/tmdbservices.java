package com.example.servicesfilm.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import com.example.servicesfilm.Entity.film;
import com.example.servicesfilm.Repository.FilmRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class tmdbservices {

    private static final String apiKey = "a904c913c4304a63bb36592fd334b0e0"; // Remplacez par votre clé API
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    @Value("${tmdb.bearer.token}") // Votre Bearer Token pour l'autorisation
    private String bearerToken;
    private final RestTemplate restTemplate;
    private final FilmRepository movieRepository;

    public tmdbservices(RestTemplate restTemplate, FilmRepository movieRepository) {
        this.restTemplate = restTemplate;
        this.movieRepository = movieRepository;
    }
    public String getTrailerUrl(int movieId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + apiKey;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            List<Map<String, Object>> videos = (List<Map<String, Object>>) response.getBody().get("results");

            // Vérifiez si des vidéos sont disponibles
            if (videos != null && !videos.isEmpty()) {
                for (Map<String, Object> video : videos) {
                    // Cherchez le trailer (type "Trailer")
                    if ("Trailer".equals(video.get("type"))) {
                        return "https://www.youtube.com/watch?v=" + video.get("key");
                    }
                }
            }
        } catch (HttpClientErrorException e) {
            // Gérer l'exception ici, par exemple en journalisant l'erreur
            throw new RuntimeException("Erreur lors de la récupération des vidéos du film", e);
        }
        return null; // Retourner null si aucun trailer n'est trouvé
    }

    public void importMovies() {
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<Map<String, Object>> movies = (List<Map<String, Object>>) response.getBody().get("results");

            for (Map<String, Object> movieData : movies) {
                film movie = new film();
                movie.setId((Integer) movieData.get("id")); // Utiliser Integer pour récupérer l'ID
                movie.setTitle((String) movieData.get("title"));
                movie.setDescription((String) movieData.get("overview"));
                int movieId = (Integer) movieData.get("id");
                String trailerUrl = getTrailerUrl(movieId);
                movie.setSrcTrailler(trailerUrl);
                // Conversion de la date de sortie au format entier (année)
                String releaseDate = (String) movieData.get("release_date");
                if (releaseDate != null && releaseDate.length() >= 4) {
                    movie.setReleaseYear(Integer.parseInt(releaseDate.substring(0, 4))); // Extraire l'année
                }

                movie.setSrcImage("https://image.tmdb.org/t/p/w500" + movieData.get("backdrop_path")); // Assurez-vous que le chemin est complet
                // Traiter ici pour l'URL du trailer si nécessaire
                // movie.setSrcTrailer(getTrailerUrl(movie.getId())); // Décommentez si vous souhaitez définir l'URL du trailer

                // Sauvegarde dans MongoDB
                movieRepository.save(movie);
            }
        }
    }


    public static film getMovieDetails(int movieId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + movieId + "?api_key=" + apiKey;

        try {
            ResponseEntity<film> response = restTemplate.getForEntity(url, film.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            // Gérer l'exception ici, par exemple en journalisant l'erreur
            throw new RuntimeException("Erreur lors de la récupération des détails du film", e);
        }
    }

}
