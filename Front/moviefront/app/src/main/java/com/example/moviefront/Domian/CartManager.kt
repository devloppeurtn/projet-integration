package com.example.moviefront.Domian

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    fun addItemToCart(item: CartItem) {
        cartItems.add(item)
    }

    fun getCartItems(): List<CartItem> {
        return cartItems
    }

    fun clearCart() {
        cartItems.clear()
    }}