package com.example.moviefront.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefront.Adapters.ProductAdapter
import com.example.moviefront.Domian.Product
import com.example.moviefront.R

class ShoppingFragment : AppCompatActivity() {
    private lateinit var adapter: ProductAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnpanier: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_fragment)
        btnpanier = findViewById(R.id.panier)

        progressBar = findViewById(R.id.progressBarPr)
        recyclerView = findViewById(R.id.productview)

        setupRecyclerView()

        loadProducts() // Appelle la fonction ici
        btnpanier.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupRecyclerView() {
        val products = MockDataSource.getProducts()
        adapter = ProductAdapter(products, ::onProductClick) // Using a function reference
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }

    private fun onProductClick(product: Product) {
        val intent = Intent(this, product_detail::class.java).apply {
            putExtra("product_name", product.name)
            putExtra("product_image", product.imageResId)
            putExtra("product_price", product.price)
            putExtra("product_description", product.description)
        }
        startActivity(intent)
    }

    private fun loadProducts() {
        recyclerView.postDelayed({
            progressBar.visibility = View.GONE // Hide progress bar
            recyclerView.visibility = View.VISIBLE // Show recycler view
        }, 2000) // Delay to simulate loading
    }
}
