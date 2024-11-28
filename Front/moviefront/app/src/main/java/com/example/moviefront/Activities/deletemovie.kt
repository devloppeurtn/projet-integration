package com.example.moviefront.Activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.moviefront.R
import deleteMovieById
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch

class deletemovie : AppCompatActivity() {
    private lateinit var filmIdInput: EditText // Déclaration de la variable EditText
    private lateinit var deleteButton: Button
    private lateinit var resultMessage: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_deletemovie)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialiser les vues
        filmIdInput = findViewById(R.id.filmIdInput)  // Liaison avec l'EditText dans le XML
        deleteButton = findViewById(R.id.deleteButton)
        resultMessage = findViewById(R.id.resultMessage)
        deleteButton.setOnClickListener {
            val filmId = filmIdInput.text.toString().toIntOrNull() // Récupérer l'ID du film
            if (filmId != null) {
                // Si l'ID est valide, appeler la méthode pour supprimer le film
                deleteMovie(filmId)
            } else {
                // Si l'ID n'est pas valide, afficher un message d'erreur
                Toast.makeText(this, "Veuillez entrer un ID valide", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun deleteMovie(movieId: Int) {
        lifecycleScope.launch {
            try {
                // Lancer la suppression du film dans un contexte d'arrière-plan
                val response = deleteMovieById(movieId)

                // Mettre à jour l'UI après la réponse
                if (response.status.isSuccess()) {
                    resultMessage.text = "Film supprimé avec succès"
                    resultMessage.setVisibility(View.VISIBLE)
                } else {
                    resultMessage.text = "Erreur lors de la suppression"
                    resultMessage.setVisibility(View.VISIBLE)
                }
            } catch (e: Exception) {
                // Gérer les erreurs
                resultMessage.text = "Une erreur est survenue"
                resultMessage.setVisibility(View.VISIBLE)
                Log.e("DeleteMovie", "Erreur : ${e.message}")
            }
        }    }
}