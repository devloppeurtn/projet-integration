package com.example.moviefront.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefront.Domian.Product
import com.example.moviefront.R

class CartAdapter(private val cartItems: List<Product>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = cartItems[position]
        holder.productTitle.text = product.name  // Assurez-vous d'utiliser le bon nom pour votre produit
        holder.productPrice.text = "₹${String.format("%.2f", product.price)}"
        holder.productQuantity.text = "Qty: ${product.quantity}"
        holder.productImage.setImageResource(product.imageResId)  // Assurez-vous de mettre l'ID d'image correct
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }//cart movi

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.cartItemImage) // Utilisez le bon ID d'image
        val productTitle: TextView = itemView.findViewById(R.id.cartItemName)  // Assurez-vous de renommer en conséquence
        val productPrice: TextView = itemView.findViewById(R.id.cartItemPrice)  // ID du prix dans le layout
        val productQuantity: TextView = itemView.findViewById(R.id.cartItemQuantity)  // ID de la quantité
    }
}