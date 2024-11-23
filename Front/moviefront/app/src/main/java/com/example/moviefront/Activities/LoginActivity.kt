package com.example.moviefront.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moviefront.R
import com.example.moviefront.models.LoginRequest
import com.example.moviefront.models.User
import com.example.moviefront.objecte.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpTextView: TextView
    private lateinit var rememberMeCheckbox: CheckBox
    private lateinit var passwordToggle: ImageView
    private lateinit var forgotPassword: TextView // Déclaration ajoutée

    private var isPasswordVisible = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialisation des composants UI
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        signUpTextView = findViewById(R.id.signupTextView)
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox)
        passwordToggle = findViewById(R.id.passwordToggle)
        forgotPassword = findViewById(R.id.forgotPassword) // Initialisation ajoutée

        // Vérifier si l'utilisateur souhaite rester connecté
        checkRememberMe()

        // Gestion du clic sur le bouton "Connexion"
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        // Gestion du clic sur le texte "Inscription"
        signUpTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Gestion du clic sur le texte "Mot de passe oublié"
        forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        // Gestion du toggle de la visibilité du mot de passe
        passwordToggle.setOnClickListener {
            togglePasswordVisibility()
        }

    }

    // Fonction pour connecter l'utilisateur
    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        RetrofitInstance.api.loginUser(loginRequest).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body() // Récupère l'objet User
                    Log.e("isp", "rep :${user}")

                    Toast.makeText(this@LoginActivity, "Connexion réussie", Toast.LENGTH_SHORT).show()

                    // Sauvegarde des informations si "Remember Me" est activé
                    if (rememberMeCheckbox.isChecked) {
                        saveRememberMe(email, password)
                    } else {
                        clearRememberMe()
                    }

                    // Redirection vers l'activité principale
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("USER_EMAIL", email)
                    if (user != null) {
                        intent.putExtra("IS_PREMIUM",user.isPremiumMember)
                        Log.e("isp", "rep :${user.isPremiumMember}")

                    }
                    startActivity(intent)
                    finish() // Ferme l'activité actuelle
                } else {
                    val statusCode = response.code()
                    val errorMessage = response.errorBody()?.string() ?: "Erreur inconnue"
                    Log.e("LoginError", "Erreur $statusCode : $errorMessage")
                    Toast.makeText(this@LoginActivity, "Erreur $statusCode : $errorMessage", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Erreur de connexion : ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("LoginError", "Erreur de connexion", t)
            }
        })
    }

    // Fonction pour gérer la visibilité du mot de passe
    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordToggle.setImageResource(R.drawable.ic_eye)  // Icone "cacher" le mot de passe
        } else {
            passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            passwordToggle.setImageResource(R.drawable.ic_eye_off)  // Icone "montrer" le mot de passe
        }
        passwordEditText.setSelection(passwordEditText.text.length)  // Placer le curseur à la fin
        isPasswordVisible = !isPasswordVisible
    }

    // Vérifier les préférences "Remember Me"
    private fun checkRememberMe() {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val savedEmail = sharedPreferences.getString("email", "")
        val savedPassword = sharedPreferences.getString("password", "")

        if (!savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            emailEditText.setText(savedEmail)
            passwordEditText.setText(savedPassword)
            rememberMeCheckbox.isChecked = true
        }
    }

    // Sauvegarder les informations de l'utilisateur
    private fun saveRememberMe(email: String, password: String) {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    // Effacer les informations de l'utilisateur
    private fun clearRememberMe() {
        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("email")
        editor.remove("password")
        editor.apply()
    }
}
