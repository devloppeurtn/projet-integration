package com.example.User_Service.service;

import com.example.User_Service.entity.PasswordResetToken;
import com.example.User_Service.entity.User;
import com.example.User_Service.entity.film;
import com.example.User_Service.repository.PasswordResetTokenRepository;
import com.example.User_Service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final EmailService emailService;
    private final RestTemplate restTemplate;


    @Autowired
    public UserService(EmailService emailService, RestTemplate restTemplate) {
        this.emailService = emailService;
        this.restTemplate = restTemplate;
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPremiumMember(false); // Assurer que l'utilisateur est non-premium par défaut

        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public void logoutUser() {
        // Logique de déconnexion (token invalidation) si nécessaire
    }

    public String createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        PasswordResetToken token = new PasswordResetToken();
        token.setEmail(email);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusMinutes(15));  // Par exemple, expiration dans 15 minutes
        tokenRepository.save(token);
        return token.getToken();
    }

    public boolean resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;  // Token invalide ou expiré
        }

        User user = userRepository.findByEmail(resetToken.getEmail());
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            tokenRepository.delete(resetToken);  // Supprimer le token après utilisation
            return true;
        }
        return false;
    }

    // Modifier la méthode pour qu'elle retourne un boolean
    public boolean sendResetPasswordEmail(String email, String token) {
        String resetUrl = "http://localhost:8080/api/users/reset-password?token=" + token;
        String subject = "Réinitialisation de mot de passe";
        String text = "Cliquez sur le lien suivant pour réinitialiser votre mot de passe : " + resetUrl;

        // Appeler le service d'envoi d'email et retourner le résultat
        return emailService.sendEmail(email, subject, text);
    }
    public List<film> getFavoriteMoviesByEmail(String email) {
        // Récupérer l'utilisateur par email
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return null; // Utilisateur introuvable
        }

        // Obtenir les IDs des films favoris (assurez-vous que c'est une liste d'entiers)
        List<String> favoriteMovieIds = user.getFavoriteMovies();

        if (favoriteMovieIds == null || favoriteMovieIds.isEmpty()) {
            return new ArrayList<>(); // Pas de films favoris
        }

        // Convertir la liste de String en liste d'Integer
        List<Integer> movieIds = favoriteMovieIds.stream()
                .map(id -> {
                    try {
                        return Integer.parseInt(id); // Convertir String à Integer
                    } catch (NumberFormatException e) {
                        return null; // Gérer l'erreur de conversion si l'ID n'est pas un nombre valide
                    }
                })
                .filter(Objects::nonNull) // Filtrer les IDs qui n'ont pas pu être convertis
                .collect(Collectors.toList());

        if (movieIds.isEmpty()) {
            return new ArrayList<>(); // Aucune ID valide n'a pu être convertie
        }

        // Appeler le servicefilm pour récupérer les détails des films
        String serviceFilmUrl = "http://servicesfilm/api/films/details"; // SERVICEFILM est le nom Eureka

        try {
            // Envoi de la liste d'IDs dans le corps de la requête
            ResponseEntity<film[]> response = restTemplate.postForEntity(
                    serviceFilmUrl,              // Endpoint dans le servicefilm
                    movieIds,                    // Liste d'IDs envoyée dans le corps
                    film[].class
            );

            // Ajouter des logs pour inspecter la réponse
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Réponse du service film : " + Arrays.toString(response.getBody()));
                if (response.getBody() != null) {
                    return Arrays.asList(response.getBody()); // Retourner les films
                } else {
                    System.out.println("Le corps de la réponse est vide.");
                    return new ArrayList<>(); // Réponse vide du servicefilm
                }
            } else {
                System.out.println("Erreur dans la réponse du service film. Code: " + response.getStatusCode());
                return new ArrayList<>(); // Erreur du servicefilm
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // Gérer les exceptions d'appel réseau
        }
    }




    public boolean addMovieToFavorites(String email, String movieId) {
        // Récupérer l'utilisateur par email
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return false; // Utilisateur introuvable
        }

        // Ajouter l'ID du film aux favoris de l'utilisateur
        List<String> favoriteMovies = user.getFavoriteMovies();
        if (favoriteMovies == null) {
            favoriteMovies = new ArrayList<>();
        }

        // Vérifier si le film est déjà dans les favoris
        if (!favoriteMovies.contains(movieId)) {
            favoriteMovies.add(movieId);
        }

        // Sauvegarder l'utilisateur avec les favoris mis à jour
        user.setFavoriteMovies(favoriteMovies);
        userRepository.save(user);

        return true; // Succès
    }

    public User subscribeToPremium(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable");
        }
        user.setPremiumMember(true);
        return userRepository.save(user);
    }
    public boolean removeFavoriteMovie(String email, String movieId) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getFavoriteMovies() != null && user.getFavoriteMovies().contains(movieId)) {
                user.getFavoriteMovies().remove(movieId); // Remove the movie ID from the list
                userRepository.save(user); // Save the updated user
                return true; // Successfully removed
            }
        }
        return false; // User not found or movie ID not in the list
    }

}

