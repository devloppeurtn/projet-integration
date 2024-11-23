
import android.util.Log
import com.example.moviefront.models.Category
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.*
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.json.Json
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.os.Parcelable

// Définir la classe de données Movie en tant que Parcelable pour une compatibilité avec RecyclerView
@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val releaseYear: Int,
    val srcImage: String,
    val srcTrailler: String,
    val srcGeo: String?,
    val category: Category,
    val vote_average: Double,
    val productionCompanyNames: List<String>, // Liste des noms des sociétés de production
    val productionCompanyLogos: List<String?> ,// Liste des logos des sociétés (peut contenir null)
    val isPremium : Boolean
) : Parcelable

// Configuration du client HTTP Ktor
val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true }) // Ignore les champs inconnus pour éviter les erreurs de désérialisation
    }
}

// Fonction pour récupérer la liste complète des films
suspend fun getMovies(): List<Movie> {
    return try {
        val response = client.get("http://10.0.2.2:8081/api/films")
        val responseBody = response.bodyAsText() // Obtenez la réponse en texte brut
        Log.d("LoadMovies", "Réponse brute du serveur : $responseBody")

        // Désérialisation avec Gson
        val gson = Gson()
        val movieListType = object : TypeToken<List<Movie>>() {}.type
        gson.fromJson(responseBody, movieListType)

    } catch (e: Exception) {
        Log.e("LoadMovies", "Erreur lors de la récupération des films : ${e.message}")
        emptyList()
    }
}

// Fonction pour effectuer une recherche de films par mot-clé
suspend fun searchMovies(url: String): List<Movie> {
    return try {
        val response = client.get(url)  // Utilisation de l'URL encodée
        val responseBody = response.bodyAsText()
        Log.d("SearchMovies", "Réponse brute du serveur : $responseBody")

        // Désérialiser le JSON en liste de films
        val gson = Gson()
        val movieListType = object : TypeToken<List<Movie>>() {}.type
        gson.fromJson(responseBody, movieListType) // Retourner la liste des films
    } catch (e: Exception) {
        Log.e("SearchMovies", "Erreur lors de la recherche de films : ${e.message}")
        emptyList()
    }
}


// Exemple de fonction principale pour tester les fonctionnalités
suspend fun main() {
    try {
        // Test de la récupération de tous les films
        val movies = getMovies()
        println("Liste des films : $movies")

        // Test de la recherche
        val keyword = "Venom" // Remplacez par votre mot-clé
        val searchResults = searchMovies(keyword)
        println("Résultats de la recherche pour '$keyword' : $searchResults")

    } catch (e: Exception) {
        println("Erreur lors de la récupération des films : ${e.message}")
    } finally {
        client.close()
    }
}
