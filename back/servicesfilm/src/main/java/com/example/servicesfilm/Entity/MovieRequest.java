package com.example.servicesfilm.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class MovieRequest {
    private String title;
    private String description;
    private int releaseYear;
    private String srcImage;
    private String srcTrailer;
    private Category category;
    private double voteAverage;
    private List<String> productionCompanyNames;
    private List<String> productionCompanyLogos;

}
