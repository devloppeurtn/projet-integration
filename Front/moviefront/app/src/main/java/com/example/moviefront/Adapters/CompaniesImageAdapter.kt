package com.example.moviefront.Adapters

// CompaniesImageAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviefront.R

class CompaniesImageAdapter(private val imageUrls: List<String>) : RecyclerView.Adapter<CompaniesImageAdapter.ImageViewHolder>() {

    // ViewHolder pour chaque image
    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_companies, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]

        // Utiliser Glide pour charger l'image (logo de la société de production)
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .circleCrop() // Transforme l'image en cercle
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }
}
