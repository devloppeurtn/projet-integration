package com.example.moviefront.Activities


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.moviefront.R
import com.example.moviefront.models.User
import com.example.moviefront.objecte.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val STORAGE_PERMISSION_REQUEST_CODE = 101

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userEmail : String = intent.getStringExtra("USER_EMAIL").toString()
        //if (userEmail.isEmpty()) {
          //  Toast.makeText(this, "Erreur : utilisateur non trouvé", Toast.LENGTH_SHORT).show()
            //finish()
           // return
       // }

        // Initialiser les vues
        imageView = findViewById(R.id.imageView2)
        val nameEditText = findViewById<EditText>(R.id.editTextPrenom)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val premiumEditText = findViewById<EditText>(R.id.editTextPremium)

        // Charger le profil existant
        loadProfile(userEmail)

        // Sauvegarder les modifications du profil
        findViewById<AppCompatButton>(R.id.modifyButton).setOnClickListener {
            saveProfile(userEmail, nameEditText, emailEditText, passwordEditText, premiumEditText)
        }

        // Charger l'image sauvegardée si elle existe
        val savedImageUri = loadImageUri()
        savedImageUri?.let {
            Glide.with(this)
                .load(Uri.parse(savedImageUri))
                .circleCrop()
                .into(imageView)
        }

        // Vérification des permissions
        checkPermissions()

        // Initialisation des launchers
        takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val photo: Bitmap? = result.data?.extras?.get("data") as? Bitmap
                if (photo != null) {
                    val uri = MediaStore.Images.Media.insertImage(contentResolver, photo, "ProfilePhoto", null)
                    saveImageUri(uri)

                    Glide.with(this)
                        .load(photo)
                        .circleCrop()
                        .into(imageView)
                }
            } else {
                Toast.makeText(this, "Échec de la capture de l'image.", Toast.LENGTH_SHORT).show()
            }
        }

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val selectedImageUri: Uri = result.data?.data!!
                saveImageUri(selectedImageUri.toString())

                Glide.with(this)
                    .load(selectedImageUri)
                    .circleCrop()
                    .into(imageView)
            }
        }

        // Boutons pour la caméra et la galerie
        findViewById<ImageView>(R.id.buttonTakePhoto).setOnClickListener { openCamera() }
        findViewById<ImageView>(R.id.buttonPickPhoto).setOnClickListener { openGallery() }

        // Gestion du clic sur l'icône du mot de passe
        val drawableStart = passwordEditText.compoundDrawables[0]
        passwordEditText.setOnTouchListener { v, event ->
            // Vérifier si l'icône est présente
            val drawableStart = passwordEditText.compoundDrawables[0]
            if (drawableStart == null) {
                Log.e("ProfileActivity", "L'icône de mot de passe n'est pas définie correctement.")
                return@setOnTouchListener false
            }

            // Vérification du clic sur l'icône
            if (event.action == MotionEvent.ACTION_UP) {
                val x = event.x.toInt()

                // Détecter si le clic a eu lieu dans la zone de l'icône
                val drawableWidth = drawableStart.intrinsicWidth
                if (x <= drawableWidth) {
                    // L'icône a été cliquée
                    val intent = Intent(this, ChangePasswordActivity::class.java)
                    startActivity(intent)
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }

    }

    private fun checkPermissions() {
        // Vérification des permissions pour la caméra et le stockage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST_CODE)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoLauncher.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun saveImageUri(uri: String) {
        val sharedPreferences = getSharedPreferences("ProfilePrefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("profileImageUri", uri).apply()
    }

    private fun loadImageUri(): String? {
        val sharedPreferences = getSharedPreferences("ProfilePrefs", MODE_PRIVATE)
        return sharedPreferences.getString("profileImageUri", null)
    }

    private fun loadProfile(userEmail: String) {
        lifecycleScope.launch {
            try {
                val user = RetrofitInstance.apiUser.getUserByEmail(userEmail)
                updateUI(user)
            } catch (e: Exception) {
                Log.e("ProfileActivity", "Error loading user profile", e)
                Toast.makeText(this@ProfileActivity, "Erreur de chargement", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(user: User) {
        findViewById<EditText>(R.id.editTextPrenom).setText(user.name)
        findViewById<EditText>(R.id.editTextEmail).setText(user.email)
        findViewById<EditText>(R.id.editTextPassword).setText("") // Clear password field

        findViewById<EditText>(R.id.editTextPremium).setText(if (user.isPremiumMember) "Oui" else "Non")

        user.profileImageUrl?.let { url ->
            Glide.with(this)
                .load(url)
                .circleCrop()
                .into(imageView)
        }
    }

    private fun saveProfile(
        userEmail: String,
        nameEditText: EditText,
        emailEditText: EditText,
        passwordEditText: EditText,
        premiumEditText: EditText
    ) {
        val updatedUser = User(
            id = null,
            name = nameEditText.text.toString(),
            email = emailEditText.text.toString(),
            password = passwordEditText.text.toString(),
           isPremiumMember = premiumEditText.text.toString().equals("Oui", true),
            profileImageUrl = loadImageUri()
        )

        lifecycleScope.launch {
            try {
                val response: Response<User> = RetrofitInstance.apiUser.updateUserByEmail(userEmail, updatedUser)
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "Profil mis à jour", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProfileActivity, "Erreur de mise à jour", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("ProfileActivity", "Error saving profile", e)
                Toast.makeText(this@ProfileActivity, "Erreur lors de la sauvegarde", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
