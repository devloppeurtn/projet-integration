package com.example.moviefront.Adapters

import Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviefront.R

class FavoriteApadter(private val movies: List<Movie> , private val onRemoveClick: (Movie) -> Unit ) : RecyclerView.Adapter<FavoriteApadter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_favorite, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titlemoviee)
        //private val descriptionTextView: TextView = itemView.findViewById(R.id.movie_description)
        private val imageView: ImageView = itemView.findViewById(R.id.movieImage)
        private val removeButton: ImageView = itemView.findViewById(R.id.removeFavorite)

        fun bind(movie: Movie) {
            titleTextView.text = movie.title
           // descriptionTextView.text = movie.description
            imageView.load(movie.srcImage) {
                placeholder(R.drawable.image1)
                error(R.drawable.image1)
            }
            removeButton.setOnClickListener {
                onRemoveClick(movie) // Appeler le callback pour supprimer le film
            }
        }
    }
}
