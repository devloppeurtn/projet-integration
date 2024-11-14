package com.example.moviefront.Activities

import Movie
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviefront.Adapters.CompaniesImageAdapter
import com.example.moviefront.Adapters.WordAdapter
import com.example.moviefront.R





import coil.load

class Details : AppCompatActivity() {
    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var imageView: ImageView
    private lateinit var vote_average: TextView
    private lateinit var namecompanies: TextView
    private lateinit var playvd: ImageView
    private lateinit var goback: ImageView

    private lateinit var webView: WebView
    private lateinit var videoUrl: String

    @SuppressLint("MissingInflatedId")
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
        goback.setOnClickListener {
            // Retourne à l'activité précédente
            onBackPressed() // ou finish()
        }
        // Configurer les paramètres de la WebView
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true // Activer le JavaScript
        webSettings.domStorageEnabled = true // Activer le stockage DOM
        webSettings.loadWithOverviewMode = true // Optimiser le rendu
        webSettings.useWideViewPort = true // Permet d'ajuster la vidéo

        // Initialiser la WebView comme cachée
        webView.visibility = View.GONE

        // Récupérer les données de l'intent
        val intent = intent
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val srcImage = intent.getStringExtra("srcImage")
        val trailer = intent.getStringExtra("srcTrailler") // L'URL de la vidéo YouTube
        val vote = intent.getDoubleExtra("vote_average", 0.0)
        val formattedVote = String.format("%.1f", vote)

        // Affichage des informations
        namecompanies.text = intent.getStringArrayListExtra("productionCompanyNames")?.joinToString(", ")
        vote_average.text = formattedVote
        titleView.text = title
        descriptionView.text = description
        srcImage?.let {
            imageView.load(it) {
                placeholder(R.drawable.image1)
                error(R.drawable.image2)
            }
        }

        // Si l'URL du trailer est disponible, on la garde dans une variable
        trailer?.let {
            videoUrl = it
        }

        // Clic sur l'image pour démarrer la vidéo
        playvd.setOnClickListener {
            // Log l'URL de la vidéo avant de la charger dans la nouvelle Activity
            Log.d("VideoURL", "URL de la vidéo: $videoUrl")

            // Vérifie que l'URL est bien initialisée
            if (::videoUrl.isInitialized) {
                // Créer un Intent pour lancer la VideoActivity
                val intent = Intent(this, VideoActivity::class.java)
                intent.putExtra("VIDEO_URL", videoUrl)  // Passer l'URL de la vidéo
                startActivity(intent)
            }
        }
    }

    // Fonction pour extraire l'ID de la vidéo à partir de l'URL YouTube
    private fun getVideoIdFromUrl(url: String): String {
        val regex = "v=([^&]+)".toRegex()  // Expression régulière pour capturer l'ID de la vidéo
        val matchResult = regex.find(url)
        val videoId = matchResult?.groups?.get(1)?.value ?: ""
        Log.d("VideoID", "Video ID extracted: $videoId")  // Log l'ID extrait
        return videoId
    }
}
