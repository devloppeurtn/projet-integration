package com.example.ServiceUser.Entity;

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
    private String id; // Utilisé comme identifiant de l'utilisateur
    private String name;
    private String email;
    private String password;
    private List<String> watchlist = new ArrayList<>();
}
