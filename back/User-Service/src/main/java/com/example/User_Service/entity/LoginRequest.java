package com.example.User_Service.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    // Getters et Setters
    private String email;
    private String password;
}
