package com.example.User_Service.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
    private double vote_average ;
    private List<String> productionCompanyNames;
    private List<String> productionCompanyLogos;


}
