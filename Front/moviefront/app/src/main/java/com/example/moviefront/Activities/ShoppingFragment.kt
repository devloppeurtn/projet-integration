package com.example.moviefront.Activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShoppingFragment : AppCompatActivity() {
    private lateinit var searchInput: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnpanier: ImageView
    private lateinit var adapter: ProductAdapter

    private val fullProductList = mutableListOf<Product>() // Liste complète des produits

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_fragment)

        // Initialiser les vues
        searchInput = findViewById(R.id.searchInput)
        progressBar = findViewById(R.id.progressBarPr)
        recyclerView = findViewById(R.id.productview)
        btnpanier = findViewById(R.id.panier)

        setupRecyclerView()
        loadProductsFromApi()

        // Ajouter un listener de recherche
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                filterProducts(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Listener pour le bouton panier
        btnpanier.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Boutons supplémentaires (back et favoris)
        val backButton: LinearLayout = findViewById(R.id.acbtn)
        backButton.setOnClickListener { finish() }

        val favButton: LinearLayout = findViewById(R.id.favbutton)
        favButton.setOnClickListener {
            val userEmail: String? = intent.getStringExtra("USER_EMAIL")
            val intent = Intent(this, whatchList::class.java)
            intent.putExtra("USER_EMAIL", userEmail)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)    }

    private fun loadProductsFromApi() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        RetrofitInstance.apiProduct.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                    val products = response.body() ?: emptyList()
                    fullProductList.clear()
                    fullProductList.addAll(products)

                    adapter = ProductAdapter(
                        products,
                        ::onProductClick,
                        ::addToCart
                    )
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(
                        this@ShoppingFragment,
                        "Erreur lors de la récupération des produits",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(
                    this@ShoppingFragment,
                    "Erreur: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        })
    }

    private fun filterProducts(query: String) {
        val filteredList = if (query.isEmpty()) {
            fullProductList // Si la recherche est vide, afficher tous les produits
        } else {
            fullProductList.filter { product ->
                // Vérifie si le mot-clé est présent dans le nom ou la description
                product.name.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true)
            }
        }
        adapter.updateProducts(filteredList) // Met à jour le RecyclerView avec les résultats filtrés
    }


    private fun addToCart(product: Product) {
        val existingProduct = CartManager.getCartItems().find { it.productName == product.name }

        if (existingProduct != null) {
            Toast.makeText(this, "${product.name} est déjà dans le panier", Toast.LENGTH_SHORT).show()
        } else {
            val cartItem = CartItem(
                productName = product.name ?: "Produit inconnu",
                productPrice = product.price,
                productSize = "Taille par défaut",
                productImageUrl = product.imageResId.toString(),
                quantity = 1
            )
            CartManager.addItemToCart(cartItem)
            Toast.makeText(this, "${product.name} ajouté au panier", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onProductClick(product: Product) {
        val intent = Intent(this, product_detail::class.java).apply {
            putExtra("product_name", product.name)
            putExtra("product_image", product.imageResId)
            putExtra("product_price", product.price)
            putExtra("product_description", product.description)
        }
        startActivity(intent)
    }
}
