package com.example.moviefront.Domian

data class CartItem(
    val productName: String,
    val productPrice: Double,
    val productSize: String,
    val productImageUrl: String,
    var quantity: Int
)
