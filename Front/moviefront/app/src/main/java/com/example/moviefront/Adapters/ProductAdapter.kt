package com.example.moviefront.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefront.Activities.product_detail
import com.example.moviefront.Domian.Product
import com.example.moviefront.R

class ProductAdapter(private val productList: List<Product>, kFunction1: (Product) -> Unit) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName: TextView = view.findViewById(R.id.title)
        val productImage: ImageView = view.findViewById(R.id.img)
        val productPrice: TextView = view.findViewById(R.id.pricetxt)  // Assuming you have a price TextView

        init {
            // Set click listener to navigate to ProductDetailActivity
            view.setOnClickListener {
                val context = view.context
                val product = productList[adapterPosition]
                val intent = Intent(context, product_detail::class.java).apply {
                    putExtra("product_name", product.name)
                    putExtra("product_image", product.imageResId)
                    putExtra("product_price", product.price)
                    putExtra("product_description", product.description)
                }
                context.startActivity(intent)
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
        holder.productImage.setImageResource(product.imageResId)
        holder.productPrice.text = "$${String.format("%.2f", product.price)}"  // Display the price
    }

    override fun getItemCount(): Int = productList.size
}
