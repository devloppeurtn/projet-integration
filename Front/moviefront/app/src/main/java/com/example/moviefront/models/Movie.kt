package com.example.moviefront.models

data class Movie(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int  // Utilisé pour stocker la référence d'image depuis le dossier drawable
)

