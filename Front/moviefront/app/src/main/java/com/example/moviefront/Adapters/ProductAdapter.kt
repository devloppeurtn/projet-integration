package com.example.moviefront.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviefront.Activities.product_detail
import com.example.moviefront.Domian.Product
import com.example.moviefront.R

class ProductAdapter(private var productList: List<Product>, private val onProductClick: (Product) -> Unit,  private val onAddToCartClick: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.title)
        val productImage: ImageView = view.findViewById(R.id.img)
        val productPrice: TextView = view.findViewById(R.id.pricetxt)
        val addToCartButton: TextView = view.findViewById(R.id.Addbtn)  // Bouton "Ajouter au panier"

        init {
            // Clic sur l'élément produit
            view.setOnClickListener {
                val product = productList[adapterPosition]
                onProductClick(product)
            }
            addToCartButton.setOnClickListener {
                val product = productList[adapterPosition]
                onAddToCartClick(product)  // Appel de la fonction pour ajouter au panier
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.productName.text = product.name

        // Chargez l'image avec Glide si c'est une URL
        Glide.with(holder.productImage.context)
            .load(product.imageResId)  // Cela peut être une URL ou un chemin vers une ressource
            .into(holder.productImage)

        holder.productPrice.text = "$${String.format("%.2f", product.price)}"
    }

    override fun getItemCount(): Int = productList.size

    // Méthode pour mettre à jour la liste des produits
    fun updateProducts(newProducts: List<Product>) {
        productList = newProducts
        notifyDataSetChanged()  // Notifie que les données ont changé et actualise la RecyclerView
    }
}
