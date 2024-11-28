package com.example.moviefront.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moviefront.R

class homeadmin : AppCompatActivity() {
    private lateinit var cardAddMovie: CardView
    private lateinit var cardDeleteMovie: CardView
    private lateinit var cardModifyMovie: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_homeadmin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cardAddMovie = findViewById(R.id.cardAddMovie)
        cardDeleteMovie = findViewById(R.id.cardDeleteMovie)
        cardModifyMovie = findViewById(R.id.cardModifyMovie)

        cardAddMovie.setOnClickListener {
            startActivity(Intent(this, Addmovie::class.java))
        }

        // Navigate to Delete Movie
        cardDeleteMovie.setOnClickListener {
            startActivity(Intent(this, deletemovie::class.java))
        }

        // Navigate to Modify Movie
        cardModifyMovie.setOnClickListener {
            startActivity(Intent(this, Modify::class.java))
        }

    }
}