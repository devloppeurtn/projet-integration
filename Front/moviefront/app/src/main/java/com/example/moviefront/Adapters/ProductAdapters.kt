package com.example.moviefront.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.moviefront.Activities.AddProductActivity
import com.example.moviefront.Activities.gerer_produit
import com.example.moviefront.Domian.Product
import com.example.moviefront.R
import com.example.moviefront.objecte.APIConfig

class ProductAdapters(
    private val produits: MutableList<Product>,
    private val context: Context
) : RecyclerView.Adapter<ProductAdapters.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val produit = produits[position]
        Log.d("Image URL", "URL utilisée : ${produit.imageResId}")

        // Remplissage des données
        holder.tvNomProduit.text = produit.name
        holder.tvDescriptionProduit.text = produit.description
        holder.tvPrixProduit.text = "Prix: ${produit.price} €"
        holder.tvQuantiteProduit.text = "Quantité: ${produit.quantity}"

        // Chargement de l'image avec Glide
        Glide.with(context)
            .load(produit.imageResId) // ImageResId sans antislashs
            .placeholder(android.R.color.darker_gray)
            .error(R.drawable.image2)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
                ): Boolean {
                    Log.e("Glide Error", "Erreur de chargement de l'image : ${e?.message}")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean
                ): Boolean {
                    Log.d("Glide Success", "Image chargée avec succès.")
                    return false
                }
            })
            .into(holder.imgProduit)

        // Bouton "Modifier"
        holder.btnModifierProduit.setOnClickListener {
            val intent = Intent(context, AddProductActivity::class.java)
            intent.putExtra("produitId", produit.id)
            if (context is gerer_produit) {
                context.startActivityForResult(intent, gerer_produit.REQUEST_CODE_ADD_PRODUCT)
            }
        }

        // Bouton "Supprimer"
        holder.btnSupprimerProduit.setOnClickListener {
            supprimerProduit(produit.id, position)
        }
    }

    override fun getItemCount(): Int = produits.size

    private fun supprimerProduit(id: String, position: Int) {
        val queue = Volley.newRequestQueue(context)
        val url = "${APIConfig.BASE_URL}/api/produits/$id"

        val request = StringRequest(Request.Method.DELETE, url,
            {
                if (position in produits.indices) {
                    produits.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, produits.size)
                }
                Toast.makeText(context, "Produit supprimé avec succès", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Log.e("API Error", "Erreur lors de la suppression : ${error.message}")
                Toast.makeText(context, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNomProduit: TextView = view.findViewById(R.id.tvNomProduit)
        val tvDescriptionProduit: TextView = view.findViewById(R.id.tvDescriptionProduit)
        val tvPrixProduit: TextView = view.findViewById(R.id.tvPrixProduit)
        val tvQuantiteProduit: TextView = view.findViewById(R.id.tvQuantiteProduit)
        val imgProduit: ImageView = view.findViewById(R.id.ivImageProduit)
        val btnModifierProduit: Button = view.findViewById(R.id.btnModifierProduit)
        val btnSupprimerProduit: Button = view.findViewById(R.id.btnSupprimerProduit)
    }
}
