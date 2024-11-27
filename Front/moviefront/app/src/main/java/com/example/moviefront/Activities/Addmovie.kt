package com.example.moviefront.Activities

import Movie
import addMovie
import android.os.Bundle
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.moviefront.R
import com.example.moviefront.models.Category
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

import java.util.UUID
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.launch

class Addmovie : AppCompatActivity() {
    private lateinit var chipGroup: ChipGroup

    private lateinit var editTitle: EditText
    private lateinit var editDescription: EditText
    private lateinit var editReleaseYear: EditText
    private lateinit var editVoteAverage: EditText
    private lateinit var editSrcImage: EditText
    private lateinit var editSrcTrailer: EditText
    private lateinit var editSrcGeo: EditText
    private lateinit var editIsPremium: Switch
    private lateinit var editProductionCompanyNames: EditText  // Pour les noms des sociétés de production
    private lateinit var editProductionCompanyLogos: EditText  // Pour les logos des sociétés de production
    private lateinit var fabSave: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_addmovie)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialisation des vues
        editTitle = findViewById(R.id.edit_title)
        editDescription = findViewById(R.id.edit_description)
        editReleaseYear = findViewById(R.id.edit_release_year)
        editVoteAverage = findViewById(R.id.edit_vote_average)
        editSrcImage = findViewById(R.id.edit_src_image)
        editSrcTrailer = findViewById(R.id.edit_src_trailer)
        editSrcGeo = findViewById(R.id.edit_src_geo)
        editIsPremium = findViewById(R.id.edit_is_premium)
        editProductionCompanyNames = findViewById(R.id.edit_production_company_names)  // Initialiser
        editProductionCompanyLogos = findViewById(R.id.edit_production_company_logos)  // Initialiser
        fabSave = findViewById(R.id.fab_save)
        chipGroup = findViewById(R.id.chipGroupCategories)

        fabSave.setOnClickListener {
            saveMovie()
        }


    }  private fun saveMovie() {
        // Récupération des valeurs des champs
        val title = editTitle.text.toString().trim()
        val description = editDescription.text.toString().trim()
        val releaseYear = editReleaseYear.text.toString().trim()
        val voteAverage = editVoteAverage.text.toString().trim()
        val srcImage = editSrcImage.text.toString().trim()
        val srcTrailer = editSrcTrailer.text.toString().trim()
        val srcGeo = editSrcGeo.text.toString().trim()
        val isPremium = editIsPremium.isChecked
        val productionCompanyNames = editProductionCompanyNames.text.toString().trim().split(",")  // Split par virgule pour plusieurs sociétés
        val productionCompanyLogos = editProductionCompanyLogos.text.toString().trim().split(",")  // Split par virgule pour plusieurs logos
        // Récupérer la catégorie sélectionnée à partir de ChipGroup
        val selectedCategory = chipGroup.findViewById<Chip>(chipGroup.checkedChipId)?.text.toString()
        val category = Category.values().find { it.displayName == selectedCategory }

        if (category == null) {
            Toast.makeText(this, "Veuillez sélectionner une catégorie valide", Toast.LENGTH_SHORT).show()
            return
        }

        // Vérification de la validité des champs
        if (title.isEmpty() || description.isEmpty() || releaseYear.isEmpty() || voteAverage.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs requis", Toast.LENGTH_SHORT).show()
            return
        }
        val movie = Movie(
            id = generateUniqueId(),
            title = title,
            description = description,
            releaseYear = releaseYear.toInt(),
            vote_average = voteAverage.toDouble(),
            srcImage = srcImage,
            srcTrailler = srcTrailer,
            srcGeo = srcGeo,
            category = category,
            productionCompanyNames = productionCompanyNames,
            productionCompanyLogos = productionCompanyLogos,
            isPremium = isPremium
        )
        // Sauvegarder le film dans la base de données
        // Lancer la fonction addMovie dans une coroutine
        lifecycleScope.launch {
            try {
                // Appel suspend pour ajouter un film
                val response = addMovie(movie)
                if (response.status.isSuccess()) {
                    Toast.makeText(this@Addmovie, "Film ajouté avec succès!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Addmovie, "Erreur lors de l'ajout du film", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@Addmovie, "Erreur: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateUniqueId(): Int {
        // Exemple de génération d'ID unique
        return UUID.randomUUID().hashCode()
    }



    }
