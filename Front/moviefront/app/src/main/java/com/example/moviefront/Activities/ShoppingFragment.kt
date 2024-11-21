package com.example.moviefront.Activities


import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefront.Adapters.ProductAdapter
import com.example.moviefront.Domian.MockDataSource
import com.example.moviefront.R


class ShoppingFragment : AppCompatActivity() {
    private lateinit var adapter: ProductAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_fragment)

        progressBar = findViewById(R.id.progressBarPr)
        recyclerView = findViewById(R.id.productview)

        setupLocationSpinner()
        setupRecyclerView()

        loadProducts() // Appelle la fonction ici
    }

    private fun setupLocationSpinner() {
        val spinner: Spinner = findViewById(R.id.locatioSp)
        val items = listOf("Option 1", "Option 2", "Option 3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = adapter
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(MockDataSource.getProducts())
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }

    private fun loadProducts() {
        recyclerView.postDelayed({
            progressBar.visibility = View.GONE // Cache le ProgressBar
            recyclerView.visibility = View.VISIBLE // Affiche le RecyclerView
        }, 5000) // Délai de 1 seconde
    }
}

