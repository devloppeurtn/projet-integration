package com.example.moviefront.Domian

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    fun addItemToCart(item: CartItem) {
        cartItems.add(item)
    }
    fun removeItemFromCart(item: CartItem) {
        cartItems.remove(item)  // Retirer l'article spécifié du panier
    }

    fun getCartItems(): List<CartItem> {
        return cartItems
    }

    fun clearCart() {
        cartItems.clear()
    }}