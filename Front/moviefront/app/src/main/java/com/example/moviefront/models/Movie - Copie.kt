package com.example.moviefront.models

data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val releaseYear: Int,
    val srcImage: String,
    val srcTrailler: String,
    val srcGeo: String,
    val category: Category
)

