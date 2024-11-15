package com.example.moviefront.models

data class User(
    val id: String? = null,          // L'identifiant est optionnel, par défaut null
    val name: String,
    val email: String,
    val password: String,
    val watchlist: List<String>? = null // La liste est optionnelle, par défaut null
)

