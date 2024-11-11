package com.example.moviefront.Activities
import Movie
import androidx.lifecycle.lifecycleScope // Si dans un fragment, remplacer par viewLifecycleOwner.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.coding.imagesliderwithdotindicatorviewpager2.models.ImageItem
import com.example.moviefront.Adapters.ImageAdapter
import com.example.moviefront.Domian.SpacingItemDecoration
import com.example.moviefront.R
import com.example.moviefront.Adapters.MovieAdapter


import getMovies
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
        // Affiche la ProgressBar au début du chargement
        progressBar.visibility = View.VISIBLE
        progressBar2.visibility = View.VISIBLE
        progressBar3.visibility = View.VISIBLE

        // Lance une coroutine sur le Dispatcher IO pour effectuer la récupération des films
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel de la fonction suspendue pour récupérer les films
                Log.d("LoadMovies", "Avant l'appel à getMovies()")
                val movies = getMovies()
                Log.d("LoadMovies", "Après l'appel à getMovies()")
                withContext(Dispatchers.Main) {
                    val movieAdapter = MovieAdapter(movies)
                    // Configuration des RecyclerViews
                    val recyclerView = findViewById<RecyclerView>(R.id.view1)
                    recyclerView.adapter = movieAdapter
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

                    val recyclerView3 = findViewById<RecyclerView>(R.id.view3)
                    recyclerView3.adapter = movieAdapter
                    recyclerView3.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

                    val recyclerView4 = findViewById<RecyclerView>(R.id.view4)
                    recyclerView4.adapter = movieAdapter
                    recyclerView4.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

                    // Masquer la ProgressBar après le chargement des films
                    progressBar.visibility = View.GONE
                    progressBar2.visibility = View.GONE
                    progressBar3.visibility = View.GONE
                }
            } catch (e: Exception) {
                // Gérer les erreurs éventuelles lors de la récupération des films
                withContext(Dispatchers.Main) {
                    // Masquer les ProgressBars et afficher un message d'erreur
                    progressBar.visibility = View.GONE
                    progressBar2.visibility = View.GONE
                    progressBar3.visibility = View.GONE
                    Toast.makeText(this@MainActivity, "Erreur de chargement des films", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
