package com.example.servicesfilm.service;

import com.example.servicesfilm.Entity.film;
import com.example.servicesfilm.Repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class filmService {

    private final FilmRepository filmRepository;

    @Autowired
    public filmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<film> getAllFilms() {
        return filmRepository.findAll();
    }
}