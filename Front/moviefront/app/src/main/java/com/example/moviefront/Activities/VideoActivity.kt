package com.example.moviefront.Activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moviefront.R

class VideoActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_video)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        webView = findViewById(R.id.webView)
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()  // Fermer l'Activity actuelle et revenir à l'Activity précédente
        }
        // Récupérer l'URL de la vidéo passée par l'intent
        val videoUrl = intent.getStringExtra("VIDEO_URL")

        if (videoUrl != null) {
            // Configurer la WebView
            val webSettings: WebSettings = webView.settings
            webSettings.javaScriptEnabled = true
            webSettings.domStorageEnabled = true

            // Charger l'URL YouTube dans la WebView (en mode intégré)
            val videoId = getVideoIdFromUrl(videoUrl)
            val embedUrl = "https://www.youtube.com/embed/$videoId"
            webView.loadUrl(embedUrl)
            webView.visibility = WebView.VISIBLE
        } else {
            Toast.makeText(this, "Aucune vidéo à afficher", Toast.LENGTH_SHORT).show()
        }

    }
    private fun getVideoIdFromUrl(url: String): String {
        val regex = "v=([^&]+)".toRegex()  // Expression régulière pour capturer l'ID de la vidéo
        val matchResult = regex.find(url)
        return matchResult?.groups?.get(1)?.value ?: ""
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Si l'orientation est paysage
            webView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            webView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Si l'orientation est portrait
            webView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            webView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
        webView.requestLayout()
    }


}