package com.example.User_Service.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private List<String> favoriteMovies = new ArrayList<>();
    @JsonProperty("isPremiumMember")
    private boolean isPremiumMember;
    @Enumerated(EnumType.STRING) // Stocke la valeur sous forme de chaîne dans la base de données
    private UserRole role;

}

