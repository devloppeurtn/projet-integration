package com.example.moviefront.Activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.coding.imagesliderwithdotindicatorviewpager2.models.ImageItem
import com.example.moviefront.Adapters.ImageAdapter
import com.example.moviefront.Adapters.MovieAdapter
import com.example.moviefront.Domian.SpacingItemDecoration
import com.example.moviefront.R
import com.example.moviefront.models.Movie
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var viewpager2: ViewPager2
    private lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback
    private lateinit var imageList: ArrayList<ImageItem>
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBar2: ProgressBar  // Déclaration de la ProgressBar
    private lateinit var progressBar3: ProgressBar  // Déclaration de la ProgressBar
    // Déclaration de la ProgressBar
    private val handler = Handler(Looper.getMainLooper())
    private val slideInterval = 3000L // Intervalle de 3 secondes pour auto-défilement

    // Runnable pour changer d'image automatiquement
    private val autoSlideRunnable = object : Runnable {
        override fun run() {
            if (viewpager2.scrollState == ViewPager2.SCROLL_STATE_IDLE) {  // Vérifie que ViewPager2 est au repos
                val nextItem = (viewpager2.currentItem + 1) % imageList.size
                viewpager2.currentItem = nextItem
            }
            handler.postDelayed(this, slideInterval)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation de la ProgressBar
        progressBar = findViewById(R.id.progressBar1)
        progressBar2 = findViewById(R.id.progressBar2)
        progressBar3 = findViewById(R.id.progressBar3)

        // Afficher la ProgressBar avant de charger les données
        progressBar.visibility = View.VISIBLE
        progressBar2.visibility = View.VISIBLE
        progressBar3.visibility = View.VISIBLE



                // Configuration du ViewPager2
        viewpager2 = findViewById(R.id.viewpager2)
        imageList = arrayListOf(
            ImageItem(UUID.randomUUID().toString(), R.drawable.image1),
            ImageItem(UUID.randomUUID().toString(), R.drawable.image2),
            ImageItem(UUID.randomUUID().toString(), R.drawable.image3),
            ImageItem(UUID.randomUUID().toString(), R.drawable.image4),
            ImageItem(UUID.randomUUID().toString(), R.drawable.image5),
            ImageItem(UUID.randomUUID().toString(), R.drawable.image6),
        )

        // Configurer l'adaptateur ViewPager2
        val imageAdapter = ImageAdapter()
        viewpager2.adapter = imageAdapter
        imageAdapter.submitList(imageList)

        // Initialiser les indicateurs de points (dots)
        val slideDotLL = findViewById<LinearLayout>(R.id.slideDotLL)
        val dotsImage = Array(imageList.size) { ImageView(this) }

        // Paramètres des points
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(8, 0, 8, 0) }

        // Ajouter les points à l'affichage
        dotsImage.forEach {
            it.setImageResource(R.drawable.non_active_dot)
            slideDotLL.addView(it, params)
        }
        dotsImage[0].setImageResource(R.drawable.active_dot) // Le premier point est actif par défaut

        // Écouteur de changement de page pour les points
        pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                dotsImage.forEachIndexed { index, imageView ->
                    imageView.setImageResource(
                        if (position == index) R.drawable.active_dot else R.drawable.non_active_dot
                    )
                }
                super.onPageSelected(position)
            }
        }
        viewpager2.registerOnPageChangeCallback(pageChangeListener)

        // Lancement de l'auto-défilement
        handler.postDelayed(autoSlideRunnable, slideInterval)

        // Configuration du RecyclerView pour les films
        loadMovies()
    }

    private fun loadMovies() {
        // Simule un délai de chargement (ici, 2 secondes)
        handler.postDelayed({
            // Configuration de la liste des films
            val movieList = listOf(
                Movie("1", "Film 1", "Description du Film 1", R.drawable.image1),
                Movie("2", "Film 2", "Description du Film 2", R.drawable.image2),
                Movie("3", "Film 3", "Description du Film 3", R.drawable.image3),
                Movie("4", "Film 4", "Description du Film 4", R.drawable.image4)
                // Ajoutez d'autres films si nécessaire
            )

            val recyclerView = findViewById<RecyclerView>(R.id.view1)
            recyclerView.adapter = MovieAdapter(movieList)
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


            val recyclerView3 = findViewById<RecyclerView>(R.id.view3)
            recyclerView3.adapter = MovieAdapter(movieList)
            recyclerView3.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            val recyclerView4 = findViewById<RecyclerView>(R.id.view4)
            recyclerView4.adapter = MovieAdapter(movieList)
            recyclerView4.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            // Masquer la ProgressBar une fois le chargement terminé
            progressBar.visibility = View.GONE
            progressBar2.visibility = View.GONE
            progressBar3.visibility = View.GONE

        }, 2000) // Délai de simulation de chargement
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(autoSlideRunnable, slideInterval) // Recommence le défilement automatique
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(autoSlideRunnable) // Arrête le défilement pour libérer des ressources
    }

    override fun onDestroy() {
        super.onDestroy()
        viewpager2.unregisterOnPageChangeCallback(pageChangeListener)
        handler.removeCallbacks(autoSlideRunnable) // Nettoyage
    }
}
