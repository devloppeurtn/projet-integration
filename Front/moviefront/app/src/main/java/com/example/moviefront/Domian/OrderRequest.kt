package com.example.moviefront.Domian

data class OrderRequest( val userEmail: String,
                         val items: List<CartItem>,
                         val totalPrice: Double,
                         val deliveryAddress: String,
                         val orderDate: Long)
