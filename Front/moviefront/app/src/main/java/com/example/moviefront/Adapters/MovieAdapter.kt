package com.example.moviefront.Adapters

import Movie
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviefront.Activities.Details
import com.example.moviefront.R

class MovieAdapter(private val movies: List<Movie>,private val userEmail: String? ) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    // ViewHolder pour chaque élément de la liste
    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.movieImage)
        val titleView: TextView = itemView.findViewById(R.id.titlemoviee) // TextView pour le titre

        init {
            itemView.setOnClickListener(this) // Lier le clic à l'élément entier, pas seulement l'image
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val movie = movies[position] // Récupérer l'objet Movie à la position

                // Créer l'intent pour passer à la page de détails
                val context = itemView.context
                val intent = Intent(context, Details::class.java)
                intent.putExtra("id", movie.id)
                intent.putExtra("title", movie.title)
                intent.putExtra("description", movie.description)
                intent.putExtra("releaseYear", movie.releaseYear)
                intent.putExtra("srcImage", movie.srcImage)
                intent.putExtra("srcTrailler", movie.srcTrailler)
                intent.putExtra("srcGeo", movie.srcGeo)
                intent.putExtra("category", movie.category.displayName)
                intent.putExtra("USER_EMAIL",userEmail)
                intent.putStringArrayListExtra("productionCompanyNames", ArrayList(movie.productionCompanyNames)) // Passer la liste des noms des entreprises
                intent.putStringArrayListExtra("productionCompanyLogos", ArrayList(movie.productionCompanyLogos)) // Passer la liste des logos
                intent.putExtra("vote_average", movie.vote_average)

                context.startActivity(intent)
            }
        }
    }

    // Créer le ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    // Lier les données à chaque ViewHolder
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        // Charger l'image avec Coil ou Glide
        holder.imageView.load(movie.srcImage) {
            placeholder(R.drawable.image1) // Image par défaut pendant le chargement
            error(R.drawable.image2) // Image d'erreur en cas d'échec
        }

        // Afficher le titre du film
        holder.titleView.text = movie.title
    }

    // Retourner le nombre d'éléments dans la liste
    override fun getItemCount(): Int {
        return movies.size
    }
}
