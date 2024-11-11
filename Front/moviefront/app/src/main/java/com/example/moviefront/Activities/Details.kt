package com.example.moviefront.Activities

import Movie
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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



class Details : AppCompatActivity() {
    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var imageView: ImageView
    private lateinit var vote_average: TextView
    private lateinit var namecompanies: TextView

    private lateinit var yearView: TextView
    private lateinit var categoryView: TextView
    private lateinit var trailerView: TextView


    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var wordAdapter: WordAdapter

    private lateinit var recyclerView2: RecyclerView
    private lateinit var companiesImageAdapter: CompaniesImageAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
     //  val movie: Movie? = intent.getParcelableExtra("movie_data")

       // movie?.let {
            // Exemple : afficher les informations du film
         //   findViewById<TextView>(R.id.titledet).text = it.title
           // findViewById<TextView>(R.id.descdet).text = it.description
            //findViewById<ImageView>(R.id.imagedet).load(it.srcImage)
            // Ajoutez d'autres vues pour afficher les informations restantes
        //}
        titleView = findViewById(R.id.titledet)
        descriptionView = findViewById(R.id.descdet)
        imageView = findViewById(R.id.imagedet)
        vote_average =findViewById(R.id.vote_average)
        namecompanies =findViewById(R.id.namecompanies)
       // yearView = findViewById(R.id.releaseYearDetails)

        //trailerView = findViewById(R.id.trailerDetails)

        val intent = intent
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val releaseYear = intent.getIntExtra("releaseYear", 0) // Valeur par défaut 0
        val srcImage = intent.getStringExtra("srcImage")
        val category= intent.getStringExtra("category")
        Log.d("valcat", "val cat  : $category")

        val productionCompanyNames = intent.getStringArrayListExtra("productionCompanyNames") ?: arrayListOf()
        val productionCompanyLogos = intent.getStringArrayListExtra("productionCompanyLogos") ?: arrayListOf()



        val trailer = intent.getStringExtra("srcTrailler")
        val vote = intent.getDoubleExtra("vote_average",0.0)
        val formattedVote = String.format("%.1f", vote)
        val companyNamesText = productionCompanyNames.joinToString(", ")

        namecompanies.text=companyNamesText
        vote_average.text = formattedVote
        titleView.text = title
        descriptionView.text = description
        srcImage?.let {
            imageView.load(it) {
                placeholder(R.drawable.image1) // Image par défaut pendant le chargement
                error(R.drawable.image2) // Image d'erreur en cas d'échec
            }
        }
        val values = arrayListOf<String>()

// Vérifiez que category n'est pas nul avant de l'ajouter
        category?.let {
            values.add(it)  // Ajoute category à la liste values
        }
        Log.d("valv", "valeur de value : $values")

        recyclerView = findViewById(R.id.recategory)
        wordAdapter = WordAdapter(values)
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter = wordAdapter




        recyclerView2 = findViewById(R.id.RecyclerView2)
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        companiesImageAdapter = CompaniesImageAdapter(productionCompanyLogos)
        recyclerView2.adapter = companiesImageAdapter
        val goback = findViewById<ImageView>(R.id.goback)

        goback.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }

    }



}