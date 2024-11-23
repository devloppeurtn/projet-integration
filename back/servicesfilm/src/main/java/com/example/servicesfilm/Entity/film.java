package com.example.servicesfilm.Entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.List;

@Document(collection = "movies")  // Indique que cette classe représente un document MongoDB
@Getter
@Setter
public class film {
    @Id
    private int id;
    private String title;
    private String description;
    private int releaseYear;
    private String srcImage;
    private String srcTrailler;
    private String srcGeo;
    private Category category;
    private double vote_average;
    private List<String> productionCompanyNames;
    private List<String> productionCompanyLogos;
    private boolean isPremium;


}
