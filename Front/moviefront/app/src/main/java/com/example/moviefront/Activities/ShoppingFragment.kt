package com.example.moviefront.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefront.Adapters.ProductAdapter
import com.example.moviefront.Domian.CartItem
import com.example.moviefront.Domian.CartManager
import com.example.moviefront.Domian.Product
import com.example.moviefront.R
import com.example.moviefront.objecte.RetrofitInstance
import com.example.moviefront.repository.ProductService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShoppingFragment : AppCompatActivity() {
    private lateinit var addToCartButton: Button
    private val cart = mutableListOf<Product>()  // Liste des produits dans le panier

    private lateinit var adapter: ProductAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnpanier: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_fragment)

        btnpanier = findViewById(R.id.panier)
        progressBar = findViewById(R.id.progressBarPr)
        recyclerView = findViewById(R.id.productview)

        setupRecyclerView()

        // Charger les produits depuis le backend
        loadProductsFromApi()

        btnpanier.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun loadProductsFromApi() {
        // Afficher la progress bar et masquer le RecyclerView pendant que les produits sont chargés
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        // Faire appel à l'API pour récupérer les produits
        RetrofitInstance.apiProduct.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    // Masquer la progress bar et afficher RecyclerView
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                    // Récupérer la liste des produits
                    val products = response.body() ?: emptyList()
                    // Mettre à jour l'adaptateur avec les produits
                    adapter = ProductAdapter(
                        products,
                        ::onProductClick,  // Fonction pour afficher les détails du produit
                        ::addToCart // Fonction pour ajouter au panier
                    )
                    recyclerView.adapter = adapter
                } else {
                    // Afficher un message d'erreur si la réponse n'est pas réussie
                    Toast.makeText(this@ShoppingFragment, "Erreur lors de la récupération des produits", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                // Afficher un message d'erreur en cas d'échec de la requête
                Toast.makeText(this@ShoppingFragment, "Erreur: ${t.message}", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        })
    }
    private fun addToCart(product: Product) {
        // Vérifier si le produit existe déjà dans le panier
        val existingProduct = CartManager.getCartItems().find { it.productName == product.name }

        if (existingProduct != null) {
            // Si le produit est déjà dans le panier, afficher un message
            Toast.makeText(this, "${product.name} est déjà dans le panier", Toast.LENGTH_SHORT).show()
        } else {
            // Sinon, ajouter le produit au panier
            val cartItem = CartItem(
                productName = product.name ?: "Unknown Product",
                productPrice = product.price,
                productSize = "Default Size", // Si vous avez des tailles de produits, ajustez ceci
                productImageUrl = product.imageResId.toString(), // Stocker l'URL ou l'ID de l'image
                quantity = 1 // Par défaut, la quantité est 1
            )

            // Ajouter le produit au panier
            CartManager.addItemToCart(cartItem)

            // Afficher un message de confirmation
            Toast.makeText(this, "${product.name} ajouté au panier", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onProductClick(product: Product) {
        val intent = Intent(this, product_detail::class.java).apply {
            putExtra("product_name", product.name)
            putExtra("product_image", product.imageResId)
            Log.e("vimage","url image =  ${product.imageResId}")
            putExtra("product_price", product.price)
            putExtra("product_description", product.description)
        }
        startActivity(intent)
    }
}
