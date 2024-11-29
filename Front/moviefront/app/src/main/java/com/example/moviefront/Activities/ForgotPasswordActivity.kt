package com.example.moviefront.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moviefront.R
import com.example.moviefront.models.ForgotPasswordRequest
import com.example.moviefront.objecte.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var resetPasswordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        emailEditText = findViewById(R.id.emailEditText)
        resetPasswordButton = findViewById(R.id.resetPasswordButton)

        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString()

            if (email.isNotEmpty()) {
                val forgotPasswordRequest = ForgotPasswordRequest(email)

                RetrofitInstance.apiUser.forgotPassword(forgotPasswordRequest).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@ForgotPasswordActivity, "Email sent if account exists", Toast.LENGTH_SHORT).show()
                            finish() // Fermer l'activité après succès
                        } else {
                            // Log de l'erreur spécifique dans la réponse
                            val errorMessage = response.errorBody()?.string()
                            Toast.makeText(this@ForgotPasswordActivity, "Error in sending email: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // Gestion de l'erreur réseau
                        Toast.makeText(this@ForgotPasswordActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }
}