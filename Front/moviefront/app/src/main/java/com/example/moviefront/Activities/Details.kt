package com.example.moviefront.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefront.Adapters.CompaniesImageAdapter
import com.example.moviefront.Adapters.WordAdapter
import com.example.moviefront.R

class Details : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var wordAdapter: WordAdapter

    private lateinit var recyclerView2: RecyclerView
    private lateinit var companiesImageAdapter: CompaniesImageAdapter
    private var values = arrayListOf("Drama", "Action") // Liste initiale des éléments
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recategory)
        wordAdapter = WordAdapter(values)
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter = wordAdapter


        val imageUrls = listOf(
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3
            // Ajoutez d'autres URLs ou utilisez des ressources locales si besoin
        )

        recyclerView2 = findViewById(R.id.RecyclerView2)
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        companiesImageAdapter = CompaniesImageAdapter(imageUrls)
        recyclerView2.adapter = companiesImageAdapter
        val goback = findViewById<ImageView>(R.id.goback)

        goback.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }

    }



}