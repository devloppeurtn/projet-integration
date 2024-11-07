package com.example.servicesfilm.service;

import com.example.servicesfilm.Entity.Category;
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

                // Extraire les genres à partir de "genre_ids" et les mapper à Category
                List<Integer> genreIds = (List<Integer>) movieData.get("genre_ids");
                if (genreIds != null && !genreIds.isEmpty()) {
                    // Prendre le premier genre (ou un autre traitement si vous voulez gérer plusieurs genres)
                    Category genre = mapGenreIdToCategory(genreIds.get(0));
                    movie.setCategory(genre);
                }

                // Sauvegarde dans MongoDB
                movieRepository.save(movie);
            }
        }
    }
    private Category mapGenreIdToCategory(int genreId) {
        switch (genreId) {
            case 28: return Category.ACTION;
            case 12: return Category.ADVENTURE;
            case 16: return Category.ANIMATION;
            case 35: return Category.COMEDY;
            case 80: return Category.CRIME;
            case 99: return Category.DOCUMENTARY;
            case 18: return Category.DRAMA;
            case 10751: return Category.FAMILY;
            case 14: return Category.FANTASY;
            case 36: return Category.HISTORY;
            case 27: return Category.HORROR;
            case 10402: return Category.MUSIC;
            case 9648: return Category.MYSTERY;
            case 10749: return Category.ROMANCE;
            case 878: return Category.SCIENCE_FICTION;
            case 10770: return Category.TV_MOVIE;
            case 53: return Category.THRILLER;
            case 10752: return Category.WAR;
            case 37: return Category.WESTERN;
            default: return null; // Ou gérer l'absence de correspondance
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
