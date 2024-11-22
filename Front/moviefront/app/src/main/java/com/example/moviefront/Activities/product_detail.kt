package com.example.moviefront.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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

        // Initialize views
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

        // Get data from intent
        val productName = intent.getStringExtra("product_name")
        val productImageResId = intent.getIntExtra("product_image", 0)
        unitPrice = intent.getDoubleExtra("product_price", 10.0)
        val productDescription = intent.getStringExtra("product_description")

        // Set data to views
        titleText.text = productName
        productImage.setImageResource(productImageResId)
        descriptionText.text = productDescription

        // Update price and total price
        updatePrice()
        updateTotalPrice()

        // Set up button actions (back, add to cart, etc.)
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
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show()
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
