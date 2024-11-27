package com.example.servicesfilm.service;

import com.example.servicesfilm.Entity.MovieRequest;
import com.example.servicesfilm.Entity.film;
import com.example.servicesfilm.Repository.FilmCustomRepository;
import com.example.servicesfilm.Repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class filmService {

    private final FilmRepository filmRepository;
    private final FilmCustomRepository customRepository;

    @Autowired
    public filmService(FilmRepository filmRepository, FilmRepository movieRepository, FilmCustomRepository customRepository) {
        this.filmRepository = filmRepository;
        this.customRepository = customRepository;
    }


    public List<film> getAllFilms() {
        return filmRepository.findAll();
    }
    public List<film> getMoviesByIds(List<Integer> movieIds) {
        List<film> films = new ArrayList<>();

        // Boucle sur chaque ID dans la liste movieIds
        for (Integer movieId : movieIds) {
            // Utiliser findById pour chercher chaque film par ID
            Optional<film> movie = filmRepository.findById(movieId);

            // Si le film est trouvé, on l'ajoute à la liste
            movie.ifPresent(films::add);
        }

        return films;
    }
    public List<film> searchByAll(String keyword) {
        return customRepository.searchByAllCriteria(keyword);
    }
    public film addMovie(film movie) {
        // Créer une nouvelle entité film
        film newMovie = new film();

        // Mapper les propriétés de l'objet Movie à l'entité film
        newMovie.setTitle(movie.getTitle());
        newMovie.setDescription(movie.getDescription());
        newMovie.setReleaseYear(movie.getReleaseYear());
        newMovie.setSrcImage(movie.getSrcImage());
        newMovie.setSrcTrailler(movie.getSrcTrailler());
        newMovie.setCategory(movie.getCategory()); // Assurez-vous que le type de 'Category' est compatible avec le champ dans 'film'
        newMovie.setVote_average(movie.getVote_average());
        newMovie.setProductionCompanyNames(movie.getProductionCompanyNames());
        newMovie.setProductionCompanyLogos(movie.getProductionCompanyLogos());
        newMovie.setId(movie.getId());
        // Sauvegarder l'entité film dans la base de données
        return filmRepository.save(newMovie);
    }

}