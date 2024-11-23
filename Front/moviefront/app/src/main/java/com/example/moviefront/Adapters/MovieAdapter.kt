package com.example.moviefront.Adapters

import Movie
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviefront.Activities.Details
import com.example.moviefront.Activities.SubscriptionActivity
import com.example.moviefront.R

class MovieAdapter(
    private val movies: List<Movie>,
    private val userEmail: String?,
    private val isPremiumUser: Boolean,
    // Vérifie si l'utilisateur est premium
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.movieImage)
        val premiumIcon: ImageView = itemView.findViewById(R.id.premiumIcon) // Icône premium
        val titleView: TextView = itemView.findViewById(R.id.titlemoviee) // Titre du film

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val movie = movies[position]
                val context = itemView.context
                Log.e("testif","premium user $isPremiumUser et film premium ${movie.isPremium}")

                // Si l'utilisateur n'est pas premium et que le film est premium, afficher un dialog
                if (!isPremiumUser && movie.isPremium) {
                    Log.e("testif","premium user $isPremiumUser et film premium ${movie.isPremium}")
                    showPremiumDialog(context) // Vous pouvez afficher une fenêtre de dialogue ici
                } else {
                    // Naviguer vers les détails du film
                    navigateToDetails(context, movie)
                }

                // Appeler le callback passé à l'adaptateur pour la gestion de l'événement clic

            }
        }


        private fun showPremiumDialog(context: Context) {
            AlertDialog.Builder(context)
                .setTitle("Film Premium")
                .setMessage("Ce film est réservé aux utilisateurs premium. Voulez-vous vous abonner ?")
                .setPositiveButton("S'abonner") { _, _ ->
                    // Redirigez l'utilisateur vers la page de paiement
                    val intent = Intent(context, SubscriptionActivity::class.java)
                    intent.putExtra("USER_EMAIL", userEmail)
                    context.startActivity(intent)
                }
                .setNegativeButton("Annuler", null)
                .show()
        }

        private fun navigateToDetails(context: Context, movie: Movie) {
            val intent = Intent(context, Details::class.java)
            intent.putExtra("id", movie.id)
            intent.putExtra("title", movie.title)
            intent.putExtra("description", movie.description)
            intent.putExtra("releaseYear", movie.releaseYear)
            intent.putExtra("srcImage", movie.srcImage)
            intent.putExtra("srcTrailler", movie.srcTrailler)
            intent.putExtra("srcGeo", movie.srcGeo)
            intent.putExtra("category", movie.category.displayName)
            intent.putExtra("USER_EMAIL", userEmail)
            intent.putStringArrayListExtra("productionCompanyNames", ArrayList(movie.productionCompanyNames))
            intent.putStringArrayListExtra("productionCompanyLogos", ArrayList(movie.productionCompanyLogos))
            intent.putExtra("vote_average", movie.vote_average)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]


        // Charger l'image avec Coil
        holder.imageView.load(movie.srcImage) {
            placeholder(R.drawable.image1)
            error(R.drawable.image2)
        }

        // Afficher ou masquer l'icône premium
        if (!isPremiumUser && movie.isPremium) {
            holder.premiumIcon.visibility = View.VISIBLE
        } else {
            holder.premiumIcon.visibility = View.GONE
        }

        // Afficher le titre du film
        holder.titleView.text = movie.title
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}
