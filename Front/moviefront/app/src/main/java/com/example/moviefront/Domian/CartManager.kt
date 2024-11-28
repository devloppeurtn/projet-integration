package com.example.moviefront.Domian

object CartManager {

    private val cartItems = mutableListOf<Product>()

    // Ajouter un produit au panier
    fun addToCart(product: Product) {
        val existingProduct = cartItems.find { it.id == product.id }
        if (existingProduct != null) {
            // Si le produit existe déjà, mettre à jour la quantité
            existingProduct.quantity += product.quantity
        } else {
            // Ajouter le produit au panier
            cartItems.add(product)
        }
    }

    // Supprimer un produit du panier
    fun removeFromCart(productId: String) {
        cartItems.removeAll { it.id == productId }
    }

    // Obtenir tous les articles du panier
    fun getCartItems(): List<Product> {
        return cartItems
    }

    // Calculer le total du panier
    fun getCartTotalPrice(): Double {
        return cartItems.sumOf { it.price }
    }

    // Obtenir le nombre total d'articles dans le panier
    fun getItemCount(): Int {
        return cartItems.sumOf { it.quantity }
    }//cart movie
}