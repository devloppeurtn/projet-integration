package com.example.servicesfilm.service;

import com.example.servicesfilm.Entity.film;
import com.example.servicesfilm.Repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class filmService {

    private final FilmRepository filmRepository;

    @Autowired
    public filmService(FilmRepository filmRepository, FilmRepository movieRepository) {
        this.filmRepository = filmRepository;
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


}