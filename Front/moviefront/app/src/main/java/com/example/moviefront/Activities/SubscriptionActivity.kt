package com.example.moviefront.Activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowInsetsAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moviefront.R
import com.example.moviefront.models.User
import com.example.moviefront.objecte.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)

        // Récupérer l'email de l'utilisateur passé via l'intent
        val userEmail = intent.getStringExtra("USER_EMAIL")

        // Appliquer la logique de gestion des bordures système (navigation, statut bar, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialisation du bouton d'abonnement
        val subscribeButton: Button = findViewById(R.id.subscribeButton)
    // Remplacez avec votre champ Email

        subscribeButton.setOnClickListener {
            val userEmail = "houssem.hfaissi@outlook.fr"

            if (userEmail.isEmpty()) {
                Toast.makeText(this, "L'email est requis", Toast.LENGTH_SHORT).show()
            } else {
                // Appel API via RetrofitInstance pour s'abonner
                RetrofitInstance.api.subscribeToPremium(userEmail).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful && response.body() != null) {
                            val user = response.body()!!

                            // Si isPremiumMember est un Double, vous devrez peut-être le convertir en Boolean :
                            val isPremium = user.isPremiumMember == true // Si l'API renvoie un Double

                            if (isPremium) {
                                Toast.makeText(this@SubscriptionActivity, "Abonnement Premium réussi !", Toast.LENGTH_SHORT).show()
                                // Rediriger vers l'écran principal
                                val intent = Intent(this@SubscriptionActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@SubscriptionActivity, "Échec de l'abonnement", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@SubscriptionActivity, "Erreur lors de l'abonnement", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@SubscriptionActivity, "Problème de connexion", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}
