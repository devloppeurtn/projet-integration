package com.example.moviefront.Activities
import Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.coding.imagesliderwithdotindicatorviewpager2.models.ImageItem
import com.example.moviefront.Adapters.ImageAdapter
import com.example.moviefront.R
import com.example.moviefront.Adapters.MovieAdapter
import com.example.moviefront.models.Category


import getMovies
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var searchInput: EditText

    private lateinit var viewpager2: ViewPager2
    private lateinit var pageChangeListener: ViewPager2.OnPageChangeCallback
    private lateinit var imageList: ArrayList<ImageItem>
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBar2: ProgressBar  // Déclaration de la ProgressBar
    private lateinit var progressBar3: ProgressBar
    private lateinit var progressBar4: ProgressBar
    private lateinit var progressBar5: ProgressBar
    private lateinit var progressBar6: ProgressBar
    private lateinit var progressBar7: ProgressBar
    private var isPremium: Boolean = false

    // Déclaration de la ProgressBar
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

        isPremium = intent.getBooleanExtra("IS_PREMIUM", false)
        Log.e("ispremium", "  lemail est : $isPremium")

        // Initialisation de la ProgressBar
        progressBar = findViewById(R.id.pg1)
        progressBar2 = findViewById(R.id.pg2)
        progressBar3 = findViewById(R.id.pg3)
        progressBar4 = findViewById(R.id.pg4)
        progressBar5 = findViewById(R.id.pg5)
        progressBar6 = findViewById(R.id.pg6)
        progressBar7 = findViewById(R.id.pg7)
        searchInput = findViewById(R.id.searchInput)

        // Ajouter un TextWatcher pour détecter les changements dans le champ de texte
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                val userEmail : String? = intent.getStringExtra("USER_EMAIL")

                if (query.isNotEmpty()) {
                    // Si l'utilisateur a commencé à taper, ouvrir l'activité SearchActivity
                    val intent = Intent(this@MainActivity, SearchActivity::class.java)
                    intent.putExtra("search_query", query)  // Passer la requête de recherche
                    intent.putExtra("USER_EMAIL", userEmail)  // Passer la requête de recherche
                    Log.d("user email a search", "  lemail est : $userEmail")

                    startActivity(intent)
                    searchInput.setText("")

                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })



        // Afficher la ProgressBar avant de charger les données
        progressBar.visibility = View.VISIBLE
        progressBar2.visibility = View.VISIBLE
        progressBar3.visibility = View.VISIBLE
        progressBar4.visibility = View.VISIBLE
        progressBar5.visibility = View.VISIBLE
        progressBar6.visibility = View.VISIBLE
        progressBar7.visibility = View.VISIBLE





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

        val favButton: LinearLayout = findViewById(R.id.favbutton)
        val shopbutton: LinearLayout = findViewById(R.id.shopbutton)
        val profilebutton: LinearLayout = findViewById(R.id.profile)

        // Ajouter un listener de clic
        favButton.setOnClickListener {
            val userEmail : String? = intent.getStringExtra("USER_EMAIL")

            // Créer une intention pour démarrer l'activité Favoris
            val intent = Intent(this, whatchList::class.java)
            intent.putExtra("USER_EMAIL",userEmail)
            startActivity(intent)
        }
        profilebutton.setOnClickListener {
            val userEmail : String? = intent.getStringExtra("USER_EMAIL")

            // Créer une intention pour démarrer l'activité Favoris
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("USER_EMAIL",userEmail)
            startActivity(intent)
        }
        shopbutton.setOnClickListener {
            val userEmail : String? = intent.getStringExtra("USER_EMAIL")

            // Créer une intention pour démarrer l'activité Favoris
            val intent = Intent(this, ShoppingFragment::class.java)
            intent.putExtra("USER_EMAIL",userEmail)
            startActivity(intent)
        }

    }



    private fun loadMovies() {
        // Affiche la ProgressBar au début du chargement
        progressBar.visibility = View.VISIBLE
        progressBar2.visibility = View.VISIBLE
        progressBar3.visibility = View.VISIBLE
        progressBar4.visibility = View.VISIBLE
        progressBar5.visibility = View.VISIBLE
        progressBar6.visibility = View.VISIBLE
        progressBar7.visibility = View.VISIBLE



        // Lance une coroutine sur le Dispatcher IO pour effectuer la récupération des films
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Appel de la fonction suspendue pour récupérer les films
                Log.d("LoadMovies", "Avant l'appel à getMovies()")
                val movies = getMovies()
                Log.e("testispr","$movies")



                val actionMovies = movies.filter { it.category == Category.ACTION }
                Log.d("category", "$actionMovies")

                val comedyMovies = movies.filter { it.category == Category.COMEDY }
                val dramaMovies = movies.filter { it.category== Category.DRAMA }
                val horrorMovies = movies.filter { it.category == Category.HORROR }
                val thrillerMovies = movies.filter { it.category == Category.THRILLER }
                val romanceMovies = movies.filter { it.category == Category.ROMANCE }
                val documentaryMovies = movies.filter { it.category == Category.DOCUMENTARY }
                val userEmail : String? = intent.getStringExtra("USER_EMAIL")

                withContext(Dispatchers.Main) {
                    val actionAdapter = MovieAdapter(actionMovies,userEmail ,isPremium)
                    val recyclerView1 = findViewById<RecyclerView>(R.id.action)
                    recyclerView1.adapter = actionAdapter
                    recyclerView1.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

                    // Adapter pour les films de comédie
                    val comedyAdapter = MovieAdapter(comedyMovies,userEmail,isPremium)
                    val recyclerView2 = findViewById<RecyclerView>(R.id.comedy)
                    recyclerView2.adapter = comedyAdapter
                    recyclerView2.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

                    // Adapter pour les films dramatiques
                    val dramaAdapter = MovieAdapter(dramaMovies,userEmail,isPremium)
                    val recyclerView3 = findViewById<RecyclerView>(R.id.drama)
                    recyclerView3.adapter = dramaAdapter
                    recyclerView3.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

                    // Adapter pour les films d'horreur
                    val horrorAdapter = MovieAdapter(horrorMovies,userEmail,isPremium)
                    val recyclerView4 = findViewById<RecyclerView>(R.id.horror)
                    recyclerView4.adapter = horrorAdapter
                    recyclerView4.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

                    // Adapter pour les films de thriller
                    val thrillerAdapter = MovieAdapter(thrillerMovies,userEmail,isPremium)
                    val recyclerView5 = findViewById<RecyclerView>(R.id.thriller)
                    recyclerView5.adapter = thrillerAdapter
                    recyclerView5.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

                    // Adapter pour les films de romance
                    val romanceAdapter = MovieAdapter(romanceMovies,userEmail,isPremium)
                    val recyclerView6 = findViewById<RecyclerView>(R.id.romance)
                    recyclerView6.adapter = romanceAdapter
                    recyclerView6.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

                    // Adapter pour les films documentaires
                    val documentaryAdapter = MovieAdapter(documentaryMovies,userEmail,isPremium)
                    val recyclerView7 = findViewById<RecyclerView>(R.id.documentary)
                    recyclerView7.adapter = documentaryAdapter
                    recyclerView7.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                    // Masquer la ProgressBar après le chargement des films
                    progressBar.visibility = View.GONE
                    progressBar2.visibility = View.GONE
                    progressBar3.visibility = View.GONE
                    progressBar4.visibility = View.GONE
                    progressBar5.visibility = View.GONE
                    progressBar6.visibility = View.GONE
                    progressBar7.visibility = View.GONE
                }
            } catch (e: Exception) {
                // Gérer les erreurs éventuelles lors de la récupération des films
                withContext(Dispatchers.Main) {
                    // Masquer les ProgressBars et afficher un message d'erreur
                    progressBar.visibility = View.GONE
                    progressBar2.visibility = View.GONE
                    progressBar3.visibility = View.GONE
                    progressBar4.visibility = View.GONE
                    progressBar5.visibility = View.GONE
                    progressBar6.visibility = View.GONE
                    progressBar7.visibility = View.GONE


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
