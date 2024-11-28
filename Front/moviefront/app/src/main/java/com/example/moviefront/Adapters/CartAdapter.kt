package com.example.moviefront.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviefront.Domian.CartItem
import com.example.moviefront.Domian.Product
import com.example.moviefront.R

class CartAdapter(
    private val cartItems: List<CartItem>,
    private val onQuantityChange: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.ivCartProduct)
        val productName: TextView = view.findViewById(R.id.tvCartProductName)
        val productPrice: TextView = view.findViewById(R.id.tvCartProductPrice)
        val productSize: TextView = view.findViewById(R.id.tvCartProductSize)
        val quantity: TextView = view.findViewById(R.id.tvCartItemCount)
        val addButton: Button = view.findViewById(R.id.btnCartItemAdd)
        val minusButton: Button = view.findViewById(R.id.btnCartItemMinus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cartproduct_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]

        // Charger l'image depuis une URL
        Glide.with(holder.itemView.context)
            .load(item.productImageUrl)
            .into(holder.productImage)

        holder.productName.text = item.productName
        holder.productPrice.text = "₹${item.productPrice}"
        holder.productSize.text = item.productSize
        holder.quantity.text = item.quantity.toString()

        holder.addButton.setOnClickListener {
            item.quantity++
            onQuantityChange(item)
            notifyItemChanged(position)
        }

        holder.minusButton.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity--
                onQuantityChange(item)
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size
}
