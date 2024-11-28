package com.example.moviefront.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefront.Adapters.CartAdapter
import com.example.moviefront.Domian.CartItem
import com.example.moviefront.Domian.CartManager
import com.example.moviefront.R

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var totalPriceTextView: TextView
    private lateinit var subTotalTextView: TextView
    private lateinit var fraislivraison: TextView

    private lateinit var checkoutButton: Button
    private val cartItems = mutableListOf<CartItem>() // Liste des articles du panier

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialisation des vues
        recyclerView = findViewById(R.id.rvCartItems)
        totalPriceTextView = findViewById(R.id.tvLastTotalPrice)
        subTotalTextView = findViewById(R.id.tvLastSubTotalprice)
        checkoutButton = findViewById(R.id.btnCartCheckout)
        fraislivraison = findViewById(R.id.dileveryprice)
        // Configuration du RecyclerView
        setupRecyclerView()

        // Configuration du bouton "Checkout"
        setupCheckoutButton()

        // Mise à jour du résumé du panier
        updateCartSummary()
    }

    /**
     * Configure le RecyclerView avec une liste d'articles.
     */
    private fun setupRecyclerView() {
        // Récupérer les articles du panier depuis CartManager
        cartItems.clear() // Réinitialise la liste
        cartItems.addAll(CartManager.getCartItems())

        // Configuration du LayoutManager et de l'Adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CartAdapter(cartItems) { updatedItem ->
            updateCartSummary() // Mettre à jour le résumé si la quantité change
        }
    }

    /**
     * Configure le bouton de validation du panier.
     */
    private fun setupCheckoutButton() {
        checkoutButton.setOnClickListener {
            Toast.makeText(this, "Proceed to checkout", Toast.LENGTH_SHORT).show()
        }
    }
    /**
     * Met à jour le résumé du panier, y compris le sous-total, la livraison et le total.
     */

    private fun updateCartSummary() {
        val subTotal = cartItems.sumOf { it.productPrice * it.quantity }
        var deliveryFee = 20.0  // Valeur par défaut

        if (subTotal > 100) {
            deliveryFee = 0.0
        }

        val total = subTotal + deliveryFee

        // Mise à jour des vues
        subTotalTextView.text = "dt $subTotal"
        totalPriceTextView.text = "dt $total"
        fraislivraison.text = "dt $deliveryFee"

    }
}
