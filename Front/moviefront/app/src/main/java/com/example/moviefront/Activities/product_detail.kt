package com.example.moviefront.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.moviefront.Domian.CartItem
import com.example.moviefront.Domian.CartManager
import com.example.moviefront.R

class product_detail : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var favoriteButton: ImageView
    private lateinit var productImage: ImageView
    private lateinit var priceText: TextView
    private lateinit var titleText: TextView
    private lateinit var weightText: TextView
    private lateinit var plusButton: TextView
    private lateinit var minusButton: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var ratingText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var addToCartButton: Button
    private lateinit var totalPriceText: TextView
    private var quantity = 1
    private var unitPrice = 10.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Initialisation des vues
        backButton = findViewById(R.id.backbtn)
        favoriteButton = findViewById(R.id.favBtn)
        productImage = findViewById(R.id.img)
        priceText = findViewById(R.id.pricetxt)
        titleText = findViewById(R.id.titletxt)
        weightText = findViewById(R.id.weightTxt)
        plusButton = findViewById(R.id.plusBtn)
        minusButton = findViewById(R.id.minusbtn)
        ratingBar = findViewById(R.id.ratingBar)
        ratingText = findViewById(R.id.ratingtxt)
        descriptionText = findViewById(R.id.description)
        addToCartButton = findViewById(R.id.Addbtn)
        totalPriceText = findViewById(R.id.totalTxt)

        // Récupérer les données de l'intent
        val productId = intent.getStringExtra("product_id") ?: "default_id"
        val productName = intent.getStringExtra("product_name")
        val productImageResId = intent.getStringExtra("product_image")
        unitPrice = intent.getDoubleExtra("product_price", 10.0)
        val productDescription = intent.getStringExtra("product_description")

        // Mettre les données dans les vues
        titleText.text = productName

// Utiliser Glide pour charger l'image à partir de l'URL
        Glide.with(this)
            .load(productImageResId)  // Ici vous passez l'URL de l'image
            .into(productImage)  // Le ImageView où vous voulez afficher l'image        descriptionText.text = productDescription

        // Mettre à jour le prix et le total
        updatePrice()
        updateTotalPrice()

        // Actions des boutons
        backButton.setOnClickListener { finish() }
        plusButton.setOnClickListener {
            quantity++
            updateQuantity()
            updateTotalPrice()
        }
        minusButton.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantity()
                updateTotalPrice()
            }
        }

        addToCartButton.setOnClickListener {
            // Vérifiez si le produit est déjà dans le panier
            val existingProduct = CartManager.getCartItems().find { it.productName == productName }

            if (existingProduct != null) {
                // Si le produit est déjà dans le panier, affichez un message
                Toast.makeText(this, "$productName est déjà dans le panier", Toast.LENGTH_SHORT).show()
            } else {
                // Si le produit n'est pas dans le panier, ajoutez-le
                val cartItem = CartItem(
                    productName = productName ?: "Unknown Product", // Nom du produit
                    productPrice = unitPrice, // Prix du produit
                    productSize = "Default Size", // Vous pouvez adapter cela si vous avez des tailles de produit
                    productImageUrl = productImageResId.toString(), // Vous pouvez stocker l'URL ou l'ID de l'image
                    quantity = quantity // Quantité sélectionnée
                )

                // Ajouter l'article au panier
                CartManager.addItemToCart(cartItem)

                // Afficher un message de confirmation
                Toast.makeText(this, "$productName ajouté au panier", Toast.LENGTH_SHORT).show()

                // Facultatif : Vous pouvez rediriger l'utilisateur vers la page du panier si vous le souhaitez
                // val intent = Intent(this, CartActivity::class.java)
                // startActivity(intent)
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateQuantity() {
        weightText.text = "$quantity pcs"
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun updatePrice() {
        priceText.text = "$${String.format("%.2f", unitPrice)}"
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun updateTotalPrice() {
        val totalPrice = quantity * unitPrice
        totalPriceText.text = "$${String.format("%.2f", totalPrice)}"
    }
}
