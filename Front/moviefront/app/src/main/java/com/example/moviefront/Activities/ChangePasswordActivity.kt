package com.example.moviefront.Activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moviefront.databinding.ActivityChangePasswordBinding
import com.example.moviefront.models.PasswordChangeRequest
import com.example.moviefront.objecte.RetrofitInstance
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gestion du clic sur le bouton pour valider le changement de mot de passe
        binding.buttonValidatePassword.setOnClickListener {
            val oldPassword = binding.editTextOldPassword.text.toString().trim()
            val newPassword = binding.editTextNewPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()

            // Vérification des champs vides
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Vérification de la correspondance des mots de passe
            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val email = "hh@gmail.com" // Remplacez par l'email de l'utilisateur connecté
            val request = PasswordChangeRequest(email, oldPassword, newPassword)

            Log.d("PasswordChangeRequest", "Request: $request")

            // Lancer la requête API pour changer le mot de passe
            changePassword(request)
        }
    }

    private fun changePassword(request: PasswordChangeRequest) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = RetrofitInstance.apiUser.updatePassword(request)

                if (response.isSuccessful) {
                    // Réponse réussie
                    val apiResponse = response.body()
                    Log.d("PasswordChangeRequest", "Success: ${apiResponse?.message}")
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        apiResponse?.message ?: "Mot de passe modifié avec succès",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    // Lire le message d'erreur JSON
                    val errorJson = response.errorBody()?.string() ?: ""
                    val errorMessage = try {
                        val errorResponse = Gson().fromJson(errorJson, Map::class.java)
                        errorResponse["error"] as? String ?: "Erreur inconnue"
                    } catch (e: Exception) {
                        "Erreur inconnue"
                    }

                    Log.e("PasswordChangeRequest", "Error: $errorMessage")
                    Toast.makeText(this@ChangePasswordActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("PasswordChangeRequest", "Failure: ${e.message}")
                Toast.makeText(this@ChangePasswordActivity, "Échec de la demande : ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}