package com.example.moviefront.Activities

import Movie
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefront.Adapters.FavoriteApadter
import com.example.moviefront.R
import com.example.moviefront.objecte.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class whatchList : AppCompatActivity() {
    private lateinit var progressBar7: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var moviesAdapter: FavoriteApadter
    private val moviesList = mutableListOf<Movie>()
    private lateinit var userEmail: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_whatch_list)

        // Gestion des barres système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        progressBar7 = findViewById(R.id.favprog)


        recyclerView = findViewById(R.id.recfav)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Récupérer l'email de l'utilisateur connecté
       userEmail = intent.getStringExtra("USER_EMAIL") ?: ""

        // Configurer l'adaptateur
        moviesAdapter = FavoriteApadter(moviesList) { movie ->
            removeFavoriteMovie(userEmail, movie)
        }
        recyclerView.adapter = moviesAdapter
        progressBar7.visibility = View.VISIBLE

        // Appeler l'API pour obtenir les films favoris
        getFavoriteMovies(userEmail)








        val backButton: ImageView = findViewById(R.id.acbtn)

        // Ajouter un listener de clic
        backButton.setOnClickListener {
            // Fermer l'activité actuelle et revenir à l'activité précédente
            finish()
        }
    }

    private fun getFavoriteMovies(userEmail: String) {
        progressBar7.visibility = View.VISIBLE

        val call = RetrofitInstance.api.getFavoriteMovies(userEmail)

        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if (response.isSuccessful) {
                    progressBar7.visibility = View.GONE

                    // Si la réponse est réussie, ajouter les films à la liste et mettre à jour l'adaptateur
                    moviesList.clear()
                    response.body()?.let {
                        moviesList.addAll(it)
                    }
                    moviesAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@whatchList, "Erreur : ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Toast.makeText(this@whatchList, "Erreur de connexion", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun removeFavoriteMovie(userEmail: String, movie: Movie) {
        val call = RetrofitInstance.api.removeFavoriteMovie(userEmail, movie.id.toString())

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@whatchList, "${movie.title} supprimé des favoris", Toast.LENGTH_SHORT).show()

                    // Supprimer le film de la liste et mettre à jour l'adaptateur
                    if (moviesList.contains(movie)) {
                        moviesList.remove(movie)
                        moviesAdapter.notifyDataSetChanged()
                    }
                } else {
                    // Log détaillé pour examiner la réponse en cas d'erreur
                    Log.e("RemoveFavorite", "Erreur ${response.code()}: ${response.message()}")
                    Toast.makeText(this@whatchList, "Erreur 1 : ${response.code()} - ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@whatchList, "Erreur de connexion", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
