package com.example.moviefront.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.moviefront.R
import com.example.moviefront.objecte.APIConfig
import org.json.JSONObject

class AddProductActivity : AppCompatActivity() {

    private lateinit var etNomProduit: EditText
    private lateinit var etDescriptionProduit: EditText
    private lateinit var etPrixProduit: EditText
    private lateinit var etQuantiteProduit: EditText
    private lateinit var etImageProduit: EditText
    private lateinit var btnEnregistrerProduit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // Initialisation des vues
        etNomProduit = findViewById(R.id.etNomProduit)
        etDescriptionProduit = findViewById(R.id.etDescriptionProduit)
        etPrixProduit = findViewById(R.id.etPrixProduit)
        etQuantiteProduit = findViewById(R.id.etQuantiteProduit)
        etImageProduit = findViewById(R.id.etImageProduit)
        btnEnregistrerProduit = findViewById(R.id.btnEnregistrerProduit)

        val produitId = intent.getStringExtra("produitId")
        if (produitId != null) {
            chargerProduit(produitId)
        }

        btnEnregistrerProduit.setOnClickListener {
            if (validerChamps()) {
                if (produitId == null) {
                    creerProduit()
                } else {
                    modifierProduit(produitId)
                }
            }
        }
    }

    private fun chargerProduit(id: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "${APIConfig.BASE_URL}/api/produits/$id"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                etNomProduit.setText(response.optString("name", ""))
                etDescriptionProduit.setText(response.optString("description", ""))
                etPrixProduit.setText(response.optDouble("price", 0.0).toString())
                etQuantiteProduit.setText(response.optInt("quantity", 0).toString())
                etImageProduit.setText(response.optString("imageResId", ""))
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Erreur lors du chargement du produit", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    private fun creerProduit() {
        val queue = Volley.newRequestQueue(this)
        val url = "${APIConfig.BASE_URL}/api/produits/creer"

        val produit = JSONObject().apply {
            put("name", etNomProduit.text.toString())
            put("description", etDescriptionProduit.text.toString())
            put("price", etPrixProduit.text.toString().toDouble())
            put("quantity", etQuantiteProduit.text.toString().toInt())
            put("imageResId", etImageProduit.text.toString())
        }

        val request = JsonObjectRequest(Request.Method.POST, url, produit,
            {
                Toast.makeText(this, "Produit ajouté avec succès", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Erreur lors de l'ajout du produit", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    private fun modifierProduit(id: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "${APIConfig.BASE_URL}/api/produits/$id"

        val produit = JSONObject().apply {
            put("name", etNomProduit.text.toString())
            put("description", etDescriptionProduit.text.toString())
            put("price", etPrixProduit.text.toString().toDouble())
            put("quantity", etQuantiteProduit.text.toString().toInt())
            put("imageResId", etImageProduit.text.toString())
        }

        val request = JsonObjectRequest(Request.Method.PUT, url, produit,
            {
                Toast.makeText(this, "Produit modifié avec succès", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Erreur lors de la modification du produit", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    private fun validerChamps(): Boolean {
        if (etNomProduit.text.isNullOrEmpty() ||
            etDescriptionProduit.text.isNullOrEmpty() ||
            etPrixProduit.text.isNullOrEmpty() ||
            etQuantiteProduit.text.isNullOrEmpty()
        ) {
            Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show()
            return false
        }

        try {
            etPrixProduit.text.toString().toDouble()
            etQuantiteProduit.text.toString().toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Le prix et la quantité doivent être des valeurs valides", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
