package com.example.servicesfilm.Repository;

import com.example.servicesfilm.Entity.film;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FilmCustomRepository {
    private final MongoTemplate mongoTemplate;

    public FilmCustomRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<film> searchByAllCriteria(String keyword) {
        Query query = new Query();

        // Construire des critères pour tous les champs potentiels
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("title").regex(keyword, "i"),         // Rechercher dans le titre
                Criteria.where("description").regex(keyword, "i"),   // Rechercher dans la description
                Criteria.where("releaseYear").is(keyword),           // Recherche exacte pour l'année
                Criteria.where("category").regex(keyword, "i"),      // Rechercher dans la catégorie
                Criteria.where("vote_average").is(parseDouble(keyword)), // Recherche exacte pour la note
                Criteria.where("productionCompanyNames").regex(keyword, "i") // Rechercher dans les noms des compagnies
        );

        query.addCriteria(criteria);

        return mongoTemplate.find(query, film.class);
    }

    // Helper pour convertir un keyword en double pour la recherche des notes
    private Double parseDouble(String keyword) {
        try {
            return Double.parseDouble(keyword);
        } catch (NumberFormatException e) {
            return null; // Si ce n'est pas un nombre, retourner null
        }
    }
}
