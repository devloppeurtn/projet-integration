package com.example.moviefront.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefront.Activities.Details
import com.example.moviefront.R
import com.example.moviefront.models.Movie

class MovieAdapter(private val movieList: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    // ViewHolder pour chaque élément de la liste
    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.movieImage)

        init {
            imageView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Créer un Intent pour passer à la MovieDetailActivity
                val intent = Intent(itemView.context, Details::class.java) // Utilisez itemView.context ici
                // Démarrer l'activité
                itemView.context.startActivity(intent)
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
        val movie = movieList[position]
        holder.imageView.setImageResource(movie.imageResId)
    }

    // Retourner le nombre d'éléments dans la liste
    override fun getItemCount(): Int {
        return movieList.size
    }
}
