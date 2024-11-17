import android.util.Log
import com.example.moviefront.models.Category
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.engine.cio.*
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import android.os.Parcelable


// Définir la classe de données Movie en tant que Serializable
@Parcelize
data class Movie( val id: Int,
                  val title: String,
                  val description: String,
                  val releaseYear: Int,
                  val srcImage: String,
                  val srcTrailler: String,
                  val srcGeo: String?,
                  val category: Category,
                  val vote_average: Double,
                  val productionCompanyNames: List<String>, // Liste des noms des companies
                  val productionCompanyLogos: List<String?>
): Parcelable

// Configuration du client HTTP Ktor
val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true }) // Ignore les champs inconnus pour éviter les erreurs de désérialisation
    }
}

// Fonction pour récupérer la liste des films
// Remplacez Json.decodeFromString par une instance locale
suspend fun getMovies(): List<Movie> {
    return try {
        val response = client.get("http://10.0.2.2:8081/api/films")
        val responseBody = response.bodyAsText() // Obtenez la réponse en texte brut
        Log.d("LoadMovies", "Réponse brute du serveur : $responseBody")

        // Utilisez Gson pour désérialiser le JSON en une liste de `Movie`
        val gson = Gson()
        val movieListType = object : TypeToken<List<Movie>>() {}.type
        gson.fromJson(responseBody, movieListType)

    } catch (e: Exception) {
        Log.e("LoadMovies", "Erreur lors de la récupération des films : ${e.message}")
        emptyList()
    }
}





suspend fun main() {
    try {
        val movies = getMovies()
        println("Liste des films : $movies")
    } catch (e: Exception) {
        println("Erreur lors de la récupération des films : ${e.message}")
    } finally {
        client.close()
    }
}
