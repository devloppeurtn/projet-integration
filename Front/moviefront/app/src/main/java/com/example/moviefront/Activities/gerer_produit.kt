package com.example.moviefront.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.moviefront.Adapters.ProductAdapters
import com.example.moviefront.Domian.Product
import com.example.moviefront.R
import com.example.moviefront.objecte.APIConfig

class gerer_produit : AppCompatActivity() {
    private lateinit var rvProduits: RecyclerView
    private lateinit var btnAjouterProduit: Button
    private lateinit var productAdapter: ProductAdapters
    private val produits = mutableListOf<Product>()

    companion object {
        const val REQUEST_CODE_ADD_PRODUCT = 1
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerer_produit)

        rvProduits = findViewById(R.id.rvProduits)
        btnAjouterProduit = findViewById(R.id.btnAjouterProduit)

        productAdapter = ProductAdapters(produits, this)
        rvProduits.layoutManager = LinearLayoutManager(this)
        rvProduits.adapter = productAdapter

        btnAjouterProduit.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_PRODUCT)
        }

        chargerProduits()
    }

    private fun chargerProduits() {
        val queue = Volley.newRequestQueue(this)
        val url = "${APIConfig.BASE_URL}/api/produits"

        val request = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                try {
                    produits.clear()
                    for (i in 0 until response.length()) {
                        val produit = response.getJSONObject(i)
                        val id = produit.optString("id", "Inconnu")
                        val name = produit.optString("name", "Nom inconnu")
                        val description = produit.optString("description", "Pas de description")
                        val price = produit.optDouble("price", 0.0)
                        val image = produit.optString("imageResId", "").replace("\\", "")
                        val quantity = produit.optInt("quantity", 0)

                        produits.add(Product(id, name, image, price, description, quantity))
                    }
                    productAdapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    Log.e("JSON Error", "Erreur JSON : ${e.message}")
                }
            },
            { error ->
                Log.e("API Error", "Erreur API : ${error.message}")
                Toast.makeText(this, "Erreur lors du chargement des produits", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD_PRODUCT && resultCode == RESULT_OK) {
            chargerProduits()
        }
    }
}
