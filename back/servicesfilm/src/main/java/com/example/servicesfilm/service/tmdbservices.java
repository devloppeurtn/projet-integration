package com.example.servicesfilm.service;

import com.example.servicesfilm.Entity.Category;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import com.example.servicesfilm.Entity.film;
import com.example.servicesfilm.Repository.FilmRepository;

import java.util.*;

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

    public Map<String, Object> getProductionCompanies(int movieId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> movieDetails = response.getBody();

                // Récupérer les companies de production
                List<Map<String, Object>> productionCompanies = (List<Map<String, Object>>) movieDetails.get("production_companies");
                List<String> productionCompanyNames = new ArrayList<>();
                List<String> productionCompanyLogos = new ArrayList<>();

                // Parcours des entreprises de production et extraction des informations
                for (Map<String, Object> company : productionCompanies) {
                    productionCompanyNames.add((String) company.get("name"));
                    String logoPath = (String) company.get("logo_path");
                    // Ajouter l'URL complète pour le logo si le chemin existe
                    productionCompanyLogos.add(logoPath != null ? "https://image.tmdb.org/t/p/w500" + logoPath : null);
                }

                // Créer un map avec les données récupérées
                Map<String, Object> productionData = new HashMap<>();
                productionData.put("names", productionCompanyNames);
                productionData.put("logos", productionCompanyLogos);

                return productionData; // Retourne un map avec les noms et logos
            }
        } catch (HttpClientErrorException e) {
            // Gérer l'exception ici, par exemple en journalisant l'erreur
            throw new RuntimeException("Erreur lors de la récupération des companies de production", e);
        }
        return null; // Retourner null si aucune donnée n'est trouvée
    }

    public void importMovies() {
        int totalPages = 60; // Exemple pour récupérer 60 pages de films
        List<film> allMovies = new ArrayList<>(); // Liste pour stocker tous les films importés temporairement

        for (int page = 1; page <= totalPages; page++) {
            String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + apiKey + "&language=en-US&sort_by=popularity.desc&page=" + page;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                List<Map<String, Object>> movies = (List<Map<String, Object>>) response.getBody().get("results");

                for (Map<String, Object> movieData : movies) {
                    film movie = new film();
                    movie.setId((Integer) movieData.get("id"));
                    movie.setTitle((String) movieData.get("title"));
                    movie.setDescription((String) movieData.get("overview"));
                    int movieId = (Integer) movieData.get("id");
                    String trailerUrl = getTrailerUrl(movieId);
                    movie.setSrcTrailler(trailerUrl);

                    String releaseDate = (String) movieData.get("release_date");
                    if (releaseDate != null && releaseDate.length() >= 4) {
                        movie.setReleaseYear(Integer.parseInt(releaseDate.substring(0, 4)));
                    }

                    movie.setSrcImage("https://image.tmdb.org/t/p/w500" + movieData.get("backdrop_path"));

                    List<Integer> genreIds = (List<Integer>) movieData.get("genre_ids");
                    if (genreIds != null && !genreIds.isEmpty()) {
                        Category genre = mapGenreIdToCategory(genreIds.get(0));
                        movie.setCategory(genre);
                    }
                    movie.setVote_average((Double) movieData.get("vote_average"));

                    Map<String, Object> productionData = getProductionCompanies(movieId);
                    if (productionData != null) {
                        List<String> productionCompanyNames = (List<String>) productionData.get("names");
                        List<String> productionCompanyLogos = (List<String>) productionData.get("logos");

                        movie.setProductionCompanyNames(productionCompanyNames);
                        movie.setProductionCompanyLogos(productionCompanyLogos);
                    }

                    // Ajouter le film à la liste temporaire
                    allMovies.add(movie);
                }
            }
        }

        // Mélanger la liste pour rendre la sélection premium aléatoire
        Collections.shuffle(allMovies);

        // Définir 40% des films comme premium
        int premiumCount = (int) (allMovies.size() * 0.4); // Calcul du nombre de films premium
        for (int i = 0; i < allMovies.size(); i++) {
            if (i < premiumCount) {
                allMovies.get(i).setPremium(true); // Définir le film comme premium
            } else {
                allMovies.get(i).setPremium(false); // Définir le film comme non premium
            }
            // Sauvegarder chaque film dans le dépôt
            movieRepository.save(allMovies.get(i));
        }
    }



        private Category mapGenreIdToCategory ( int genreId){
            switch (genreId) {
                case 28:
                    return Category.ACTION;
                case 12:
                    return Category.ADVENTURE;
                case 16:
                    return Category.ANIMATION;
                case 35:
                    return Category.COMEDY;
                case 80:
                    return Category.CRIME;
                case 99:
                    return Category.DOCUMENTARY;
                case 18:
                    return Category.DRAMA;
                case 10751:
                    return Category.FAMILY;
                case 14:
                    return Category.FANTASY;
                case 36:
                    return Category.HISTORY;
                case 27:
                    return Category.HORROR;
                case 10402:
                    return Category.MUSIC;
                case 9648:
                    return Category.MYSTERY;
                case 10749:
                    return Category.ROMANCE;
                case 878:
                    return Category.SCIENCE_FICTION;
                case 10770:
                    return Category.TV_MOVIE;
                case 53:
                    return Category.THRILLER;
                case 10752:
                    return Category.WAR;
                case 37:
                    return Category.WESTERN;
                default:
                    return null; // Ou gérer l'absence de correspondance
            }
        }


        public static film getMovieDetails ( int movieId){
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
        // public List<film> getMoviesByIds(List<String> movieIds) {
        // Utilise le repository pour trouver les films par leurs IDs
        //  return movieRepository.findAllById(movieIds);
        //}
    }
