package com.example.moviefront.Activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefront.Adapters.CartAdapter
import com.example.moviefront.Domian.CartItem
import com.example.moviefront.Domian.CartManager
import com.example.moviefront.Domian.OrderRequest
import com.example.moviefront.R
import com.example.moviefront.objecte.RetrofitInstance
import com.example.moviefront.repository.OrderApiService

class CartActivity : AppCompatActivity() {
    private lateinit var goback: ImageView

    private lateinit var recyclerView: RecyclerView
    private lateinit var totalPriceTextView: TextView
    private lateinit var subTotalTextView: TextView
    private lateinit var fraislivraison: TextView
    private lateinit var btnPasserCommande: Button

    private lateinit var checkoutButton: Button
    private val cartItems = mutableListOf<CartItem>() // Liste mutable des articles du panier

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        // Initialisation des vues
        goback = findViewById(R.id.goback)
        goback.setOnClickListener { onBackPressed() }

        // Initialisation des vues
        recyclerView = findViewById(R.id.rvCartItems)
        totalPriceTextView = findViewById(R.id.tvLastTotalPrice)
        subTotalTextView = findViewById(R.id.tvLastSubTotalprice)
        checkoutButton = findViewById(R.id.btnCartCheckout)
        fraislivraison = findViewById(R.id.dileveryprice)
        // Configuration du
                btnPasserCommande = findViewById(R.id.btnCartCheckout)
        btnPasserCommande.setOnClickListener {
            // Afficher la boîte de dialogue
            showDeliveryAddressDialog()
        }
        setupRecyclerView()

        // Configuration du bouton "Checkout"

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
        recyclerView.adapter = CartAdapter(cartItems, { updatedItem ->
            updateCartSummary() // Mettre à jour le résumé si la quantité change
        }, { itemToRemove ->
            CartManager.removeItemFromCart(itemToRemove) // Supprimer du CartManager
            updateCartSummary() // Mettre à jour le résumé après suppression
        })
    }

    /**
     * Configure le bouton de validation du panier.
     */

    /**
     * Met à jour le résumé du panier, y compris le sous-total, la livraison et le total.
     */

    private fun updateCartSummary() {
        val subTotal = cartItems.sumOf { it.productPrice * it.quantity }
        var deliveryFee = 200.0  // Valeur par défaut

        if (subTotal > 2000) {
            deliveryFee = 0.0
        }

        val total = subTotal + deliveryFee

        // Mise à jour des vues
        subTotalTextView.text = "dt $subTotal"
        totalPriceTextView.text = "dt $total"
        fraislivraison.text = "dt $deliveryFee"

    }  private fun showDeliveryAddressDialog() {
        // Créer une boîte de dialogue personnalisée
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_delivery_address)

        // Récupérer les vues de la boîte de dialogue
        val editTextAddress = dialog.findViewById<EditText>(R.id.editTextDialogAddress)
        val btnConfirm = dialog.findViewById<Button>(R.id.btnConfirmAddress)

        // Action au clic sur le bouton "Confirmer"
        btnConfirm.setOnClickListener {
            val deliveryAddress = editTextAddress.text.toString().trim()

            if (deliveryAddress.isNotEmpty()) {
                // Envoyer la commande avec l'adresse de livraison
                placeOrder(deliveryAddress)
                dialog.dismiss() // Fermer la boîte de dialogue
            } else {
                // Afficher un message si l'adresse est vide
                Toast.makeText(this, "Veuillez entrer une adresse de livraison", Toast.LENGTH_SHORT).show()
            }
        }

        // Afficher la boîte de dialogue
        dialog.show()
    }
    private fun placeOrder(deliveryAddress: String) {
        // Créer une instance de Retrofit
        val retrofit = RetrofitInstance.instance
        val orderApiService = retrofit.create(OrderApiService::class.java)

        // Récupérer les données du panier
        val subTotal = cartItems.sumOf { it.productPrice * it.quantity }
        var deliveryFee = 200.0  // Exemple de frais de livraison

        if (subTotal > 2000) {
            deliveryFee = 0.0
        }

        val total = subTotal + deliveryFee

        // Créer une requête d'ordre
        val orderRequest = OrderRequest(
            userEmail = "houssem.hfaissi@outlook.fr", // Remplacez par l'email de l'utilisateur connecté
            items = cartItems,
            totalPrice = total,
            deliveryAddress = deliveryAddress,
            orderDate = System.currentTimeMillis()
        )

        // Envoyer la commande au backend
        orderApiService.createOrder(orderRequest).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CartActivity, "Commande passée avec succès !", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CartActivity, "Erreur lors de l'envoi de la commande", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Erreur réseau : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
