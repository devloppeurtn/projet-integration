package com.example.moviefront.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefront.R

class WordAdapter(
    private val items: ArrayList<String>
) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    // ViewHolder pour gérer chaque élément de la liste
    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.word)
    }

    // Création du ViewHolder pour chaque élément
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return WordViewHolder(view)
    }

    // Lier les données à chaque ViewHolder
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.textView.text = items[position]
    }

    // Retourne le nombre d'éléments dans la liste
    override fun getItemCount(): Int {
        return items.size
    }
}
