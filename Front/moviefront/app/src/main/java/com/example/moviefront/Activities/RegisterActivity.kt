package com.example.moviefront.Activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.moviefront.R
import com.example.moviefront.models.User
import com.example.moviefront.models.UserRole
import com.example.moviefront.objecte.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var roleSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialisation des vues
        roleSpinner = findViewById(R.id.roleSpinner)
        nameEditText = findViewById(R.id.nameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)

        // Configuration du Spinner des rôles
        val roles = UserRole.values().map { it.role } // Extraire les rôles sous forme de chaîne
        val roleAdapter = object : ArrayAdapter<String>(this, R.layout.role_spinner_item, roles) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(R.id.textViewRole)

                // Appliquer la couleur de texte à l'élément sélectionné
                textView.setTextColor(ContextCompat.getColor(context, R.color.spinner_item_color))
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view.findViewById<TextView>(R.id.textViewRole)

                // Appliquer la couleur de texte aux éléments du menu déroulant
                textView.setTextColor(ContextCompat.getColor(context, R.color.spinner_item_color))
                return view
            }
        }

        roleSpinner.adapter = roleAdapter


        // Gestion du clic sur le bouton d'inscription
        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val selectedRoleString = roleSpinner.selectedItem as String // Récupérer le rôle en tant que chaîne

            // Valider les champs
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convertir la chaîne en UserRole
            val selectedRole = UserRole.fromString(selectedRoleString)
            if (selectedRole == null) {
                Toast.makeText(this, "Rôle invalide sélectionné", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Créer un nouvel utilisateur
            val user = User(
                name = name,
                email = email,
                password = password,
                role = selectedRole // Passer le rôle sous forme d'objet UserRole
            )

            // Appeler l'API pour enregistrer l'utilisateur
            RetrofitInstance.apiUser.registerUser(user).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Inscription réussie", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Échec de l'inscription : ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Erreur : ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }}
