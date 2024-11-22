package com.example.moviefront.Domian


data class Product(
    val name: String,
    val imageResId: Int,
    val price: Double,
    val description: String// You can use drawable resources for product images
)
