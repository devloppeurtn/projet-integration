package com.example.moviefront.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: String? = null,
    val name: String,
    val email: String,
    val password: String,
    val profileImageUrl: String? = null,
    val favoriteMovies: List<String>? = null,
    @SerializedName("isPremiumMember") val isPremiumMember: Boolean = false,
    val role: UserRole = UserRole.USER // Définir un rôle par défaut
)


