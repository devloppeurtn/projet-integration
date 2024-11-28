package com.example.moviefront.Activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefront.Adapters.CartAdapter
import com.example.moviefront.Domian.CartManager
import com.example.moviefront.R

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var totalPriceText: TextView
    private lateinit var subtotalText: TextView
    private lateinit var checkoutButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.rvCartItems)
        totalPriceText = findViewById(R.id.tvLastSubTotal)
        subtotalText = findViewById(R.id.tvLastSubTotalItems)
        checkoutButton = findViewById(R.id.btnCartCheckout)

        setupRecyclerView()
        updateTotalPrice()

        checkoutButton.setOnClickListener {
            Toast.makeText(this, "Proceed to checkout", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        val cartItems = CartManager.getCartItems()
        recyclerView.adapter = CartAdapter(cartItems)
    }//cart movi

    private fun updateTotalPrice() {
        val totalPrice = CartManager.getCartTotalPrice()
        totalPriceText.text = "₹${String.format("%.2f", totalPrice)}"

        val itemCount = CartManager.getItemCount()
        subtotalText.text = "Subtotal Items ($itemCount)"
    }
}