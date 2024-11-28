package com.example.moviefront.Activities

import android.os.Bundle
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.moviefront.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch

class Modify : AppCompatActivity() {
    private lateinit var fabSave: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_modify)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        fabSave = findViewById(R.id.fab_save)

        fabSave.setOnClickListener {
            updateMovieById()
        }
    }
        fun updateMovieById() {
            // Récupérer les champs du formulaire
            val id = findViewById<TextInputEditText>(R.id.edit_id).text.toString()
            val title = findViewById<TextInputEditText>(R.id.edit_title).text.toString()
            val description = findViewById<TextInputEditText>(R.id.edit_description).text.toString()
            val releaseYear = findViewById<TextInputEditText>(R.id.edit_release_year).text.toString()
            val voteAverage = findViewById<TextInputEditText>(R.id.edit_vote_average).text.toString()
            val srcImage = findViewById<TextInputEditText>(R.id.edit_src_image).text.toString()
            val srcTrailer = findViewById<TextInputEditText>(R.id.edit_src_trailer).text.toString()
            val srcGeo = findViewById<TextInputEditText>(R.id.edit_src_geo).text.toString()
            val productionCompanyNames = findViewById<TextInputEditText>(R.id.edit_production_company_names).text.toString()
            val productionCompanyLogos = findViewById<TextInputEditText>(R.id.edit_production_company_logos).text.toString()
            val isPremium = findViewById<Switch>(R.id.edit_is_premium).isChecked

            // Vérifier si l'ID est présent
            if (id.isEmpty()) {
                Toast.makeText(this, "ID du film est requis pour la modification", Toast.LENGTH_SHORT).show()
                return
            }

            // Construire un objet JSON partiel
            val updatedFields = mutableMapOf<String, Any>()
            if (title.isNotEmpty()) updatedFields["title"] = title
            if (description.isNotEmpty()) updatedFields["description"] = description
            if (releaseYear.isNotEmpty()) updatedFields["releaseYear"] = releaseYear.toInt()
            if (voteAverage.isNotEmpty()) updatedFields["vote_average"] = voteAverage.toDouble()
            if (srcImage.isNotEmpty()) updatedFields["srcImage"] = srcImage
            if (srcTrailer.isNotEmpty()) updatedFields["srcTrailler"] = srcTrailer
            if (srcGeo.isNotEmpty()) updatedFields["srcGeo"] = srcGeo
            if (productionCompanyNames.isNotEmpty())
                updatedFields["productionCompanyNames"] = productionCompanyNames.split(",").map { it.trim() }
            if (productionCompanyLogos.isNotEmpty())
                updatedFields["productionCompanyLogos"] = productionCompanyLogos.split(",").map { it.trim() }
            updatedFields["isPremium"] = isPremium

            // Envoyer la requête au backend
            lifecycleScope.launch {
                try {
                    val apiUrl = "http://10.0.2.2:8081/api/films/modifier/$id"  // Remplacez avec votre URL d'API
                    val client = HttpClient()
                    val gson = Gson()
                    val jsonBody = gson.toJson(updatedFields)

                    val response = client.patch(apiUrl) {
                        contentType(ContentType.Application.Json)
                        setBody(jsonBody)
                    }

                    if (response.status.isSuccess()) {
                        Toast.makeText(this@Modify, "Film mis à jour avec succès!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@Modify, "Erreur lors de la mise à jour : ${response.status}", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@Modify, "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

}