import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviefront.Activities.Details
import com.example.moviefront.R

class SearchAdapter(private var movies: List<Movie>, private val context: Context,private var userEmail :String?) : RecyclerView.Adapter<SearchAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val moviePoster: ImageView = view.findViewById(R.id.ivMoviePoster)
        val movieTitle: TextView = view.findViewById(R.id.tvMovieTitle)
        val movieCategory: TextView = view.findViewById(R.id.tvMovieCategory)
        val movieRating: TextView = view.findViewById(R.id.tvMovieRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_result, parent, false)
        return MovieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.movieTitle.text = movie.title
        holder.movieCategory.text = movie.category.name
        holder.movieRating.text = "Rating: ${movie.vote_average}"

        Glide.with(holder.itemView.context)
            .load(movie.srcImage)
            .into(holder.moviePoster)

        // Gérer le clic sur un élément de la liste pour afficher les détails
        holder.itemView.setOnClickListener {
            val intent = Intent(context, Details::class.java).apply {
                putExtra("id", movie.id)
                putExtra("title", movie.title)
                putExtra("description", movie.description)
                putExtra("srcImage", movie.srcImage)
                putExtra("srcTrailler", movie.srcTrailler)
                putExtra("vote_average", movie.vote_average)
                putExtra("productionCompanyNames", ArrayList(movie.productionCompanyNames))
                putExtra("USER_EMAIL",userEmail)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = movies.size
    // Méthode pour mettre à jour la liste des films et notifier l'adaptateur
    fun updateMovies(newMovies: List<Movie>) {
        this.movies = newMovies
        notifyDataSetChanged() // Notifie l'adaptateur que les données ont changé et rafraîchit la vue
    }
}
