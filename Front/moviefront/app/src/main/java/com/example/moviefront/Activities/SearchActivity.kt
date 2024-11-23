package com.example.moviefront.Activities
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moviefront.R

import Movie
import SearchAdapter
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import searchMovies

class SearchActivity : AppCompatActivity() {

    private lateinit var searchInput: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        // Ajustement des marges pour les barres système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialiser les vues
        searchInput = findViewById(R.id.etSearch)
        recyclerView = findViewById(R.id.recyclerViewResults)
        val userEmail = intent.getStringExtra("USER_EMAIL")

        // Configurer RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        searchAdapter = SearchAdapter(emptyList(),this,userEmail)
        recyclerView.adapter = searchAdapter

        // Récupérer la requête passée via l'Intent
        val searchQuery = intent.getStringExtra("search_query")
        if (!searchQuery.isNullOrEmpty()) {
            searchInput.setText(searchQuery) // Afficher dans l'input
            performSearch(searchQuery) // Lancer la recherche directement
        }

        // Ajouter un listener pour surveiller les modifications du champ de recherche
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    performSearch(query)
                } else {
                    searchAdapter.updateMovies(emptyList()) // Réinitialiser les résultats
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
    private fun performSearch(query: String) {
        val cleanedQuery = query.trim() // Supprimer les espaces inutiles avant et après
        if (cleanedQuery.isNotEmpty()) {
            // Encoder correctement le texte de la requête pour gérer les espaces et caractères spéciaux
            val encodedQuery = URLEncoder.encode(cleanedQuery, StandardCharsets.UTF_8.toString())

            // Construire l'URL avec la requête encodée
            val url = "http://10.0.2.2:8081/api/films/search?keyword=$encodedQuery"

            // Effectuer la recherche
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val searchResults = searchMovies(url) // Recherche avec l'URL encodée
                    searchAdapter.updateMovies(searchResults)
                } catch (e: Exception) {
                    Log.e("SearchActivity", "Erreur lors de la recherche: ${e.message}")
                }
            }
        } else {
            searchAdapter.updateMovies(emptyList())
        }
    }



}