package com.example.moviefront.models

data class LoginRequest(
    val email: String,  // remplacez `email` par `username` si nécessaire
    val password: String
)
