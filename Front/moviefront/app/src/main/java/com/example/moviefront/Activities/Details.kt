package com.example.moviefront.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.moviefront.R
import com.example.moviefront.objecte.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Details : AppCompatActivity() {
    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var imageView: ImageView
    private lateinit var vote_average: TextView
    private lateinit var namecompanies: TextView
    private lateinit var playvd: ImageView
    private lateinit var goback: ImageView
    private lateinit var favoriteIcon: ImageView
    private lateinit var webView: WebView
    private lateinit var videoUrl: String

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Initialisation des vues
        titleView = findViewById(R.id.titledet)
        descriptionView = findViewById(R.id.descdet)
        imageView = findViewById(R.id.imagedet)
        vote_average = findViewById(R.id.vote_average)
        namecompanies = findViewById(R.id.namecompanies)
        webView = findViewById(R.id.webView)
        playvd = findViewById(R.id.playvd)
        goback = findViewById(R.id.goback)
        favoriteIcon = findViewById(R.id.favoris)

        // Retourne à l'activité précédente
        goback.setOnClickListener { onBackPressed() }

        // Configurer la WebView
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true

        webView.visibility = View.GONE // Masquer la WebView par défaut

        // Récupérer les données de l'intent
        val movieId = intent.getIntExtra("id",0)
        val movieIdString = movieId.toString()
        Log.d("idfilm", "id de film  $movieId")

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val srcImage = intent.getStringExtra("srcImage")
        val trailer = intent.getStringExtra("srcTrailler")
        val vote = intent.getDoubleExtra("vote_average", 0.0)
        val formattedVote = String.format("%.1f", vote)
        val productionCompanies = intent.getStringArrayListExtra("productionCompanyNames")
Log.e("urlimg","$srcImage")
        // Afficher les informations
        titleView.text = title
        descriptionView.text = description
        vote_average.text = formattedVote
        namecompanies.text = productionCompanies?.joinToString(", ")
        srcImage?.let {
            imageView.load(it) {
                placeholder(R.drawable.image1)
                error(R.drawable.image2)
            }
        }
        videoUrl = trailer ?: ""

        val userEmail = intent.getStringExtra("USER_EMAIL")
        // Gérer le clic sur le bouton Play
        playvd.setOnClickListener {
            if (::videoUrl.isInitialized) {
                Log.d("VideoURL", "URL de la vidéo: $videoUrl")
                val intent = Intent(this, VideoActivity::class.java)
                intent.putExtra("VIDEO_URL", videoUrl)
                startActivity(intent)
            } else {
                Toast.makeText(this, "URL de la vidéo introuvable", Toast.LENGTH_SHORT).show()
            }
        }

        // Gestion de l'ajout aux favoris
        favoriteIcon.setOnClickListener {
            if (!userEmail.isNullOrEmpty()) {  // Vérifiez que l'email n'est ni null, ni vide
                addToFavorites(movieIdString, userEmail)  // Passer à addToFavorites
            } else {
                Toast.makeText(this, "Email utilisateur introuvable", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Ajouter le film aux favoris
     */
    private fun addToFavorites(movieId: String , userEmail:String) {
         // Remplacez par l'email de l'utilisateur connecté
        val call = RetrofitInstance.apiUser.addToFavorites(userEmail, movieId)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    favoriteIcon.setImageResource(R.drawable.added)
                    Toast.makeText(this@Details, "Ajouté aux favoris", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this@Details,
                        "Erreur : ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@Details, "Erreur réseau : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Extraire l'ID de la vidéo à partir d'une URL YouTube
     */
    private fun getVideoIdFromUrl(url: String): String {
        val regex = "v=([^&]+)".toRegex()
        val matchResult = regex.find(url)
        val videoId = matchResult?.groups?.get(1)?.value ?: ""
        Log.d("VideoID", "Video ID extracted: $videoId")
        return videoId
    }
}
