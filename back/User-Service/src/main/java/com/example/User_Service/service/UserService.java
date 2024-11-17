package com.example.User_Service.service;

import com.example.User_Service.entity.PasswordResetToken;
import com.example.User_Service.entity.User;
import com.example.User_Service.repository.PasswordResetTokenRepository;
import com.example.User_Service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
public class UserService {

    private final EmailService emailService;

    @Autowired
    public UserService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
    public String addMovieToFavorites(String userEmail, String movieId) {
        // Recherche de l'utilisateur par email
        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé avec l'email : " + userEmail);
        }

        // Vérifier si le film est déjà dans les favoris
        if (user.getFavoriteMovies().contains(movieId)) {
            throw new RuntimeException("Le film est déjà dans les favoris.");
        }
        // Ajouter l'ID du film aux favoris de l'utilisateur
        user.getFavoriteMovies().add(movieId);
        userRepository.save(user); // Sauvegarde de l'utilisateur

        return "Film ajouté aux favoris avec succès.";
    }
    public List<Movie> getFavoriteMovies(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return user.getFavoriteMovies(); // Supposant que l'entité User a une relation avec les films favoris
        }
        return Collections.emptyList();
    }
}

