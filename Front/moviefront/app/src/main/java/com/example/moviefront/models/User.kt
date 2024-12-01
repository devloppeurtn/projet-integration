package com.example.moviefront.models

data class User(
    val id: String? = null,
    val name: String,
    val email: String,
    val password: String,
    val favoriteMovies: List<String>? = null,
    val isPremiumMember: Boolean = false,
    val role: UserRole = UserRole.USER // Définir un rôle par défaut
)


