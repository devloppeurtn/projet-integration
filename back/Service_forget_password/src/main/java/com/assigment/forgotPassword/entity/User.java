package com.assigment.forgotPassword.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "users") // Spécifie la collection MongoDB
public class User {
    @Id
    private String id; // MongoDB utilise des identifiants de type String par défaut

    private String name;

    private String email;

    private String password;

    private String token;

    private LocalDateTime tokenCreationDate;

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getTokenCreationDate() {
        return tokenCreationDate;
    }

    public void setTokenCreationDate(LocalDateTime tokenCreationDate) {
        this.tokenCreationDate = tokenCreationDate;
    }
}
