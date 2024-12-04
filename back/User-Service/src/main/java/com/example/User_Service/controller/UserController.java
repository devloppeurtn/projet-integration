package com.example.User_Service.controller;

import com.example.User_Service.dto.ForgotPasswordRequest;
import com.example.User_Service.dto.LoginRequest;
import com.example.User_Service.dto.PasswordChangeRequest;
import com.example.User_Service.dto.ResetPasswordRequest;
import com.example.User_Service.entity.*;
import com.example.User_Service.repository.UserRepository;
import com.example.User_Service.service.EmailService;
import com.example.User_Service.service.UserService;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String userId) {
        try {
            User user = userService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (user != null) {
            return ResponseEntity.ok(user);  // Connexion réussie
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect");
        }
    }

    @PostMapping("/logout")
    public String logout() {
        userService.logoutUser();
        return "User logged out successfully";
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String token = userService.createPasswordResetToken(request.getEmail());
        if (token == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email non trouvé");
        }
        boolean emailSent;
        if (EmailService.sendEmail(request.getEmail(), "Réinitialisation du mot de passe",
                "Cliquez sur ce lien pour réinitialiser votre mot de passe : [lien]")) emailSent = true;
        else emailSent = false;
        if (emailSent) {
            return ResponseEntity.ok("Email envoyé pour réinitialiser le mot de passe");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'envoi de l'email");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, @RequestBody ResetPasswordRequest request) {
        boolean result = userService.resetPassword(token, request.getNewPassword());
        return result ? ResponseEntity.ok("Mot de passe réinitialisé")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalide ou expiré");
    }
    @PostMapping("/{email}/favoritesss")
    public ResponseEntity<Void> addToFavorites(
            @PathVariable String email,
            @RequestParam String movieId) {

        // Appel au service pour ajouter le film aux favoris
        boolean isAdded = userService.addMovieToFavorites(email, movieId);

        if (isAdded) {
            return ResponseEntity.ok().build(); // Succès
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Utilisateur introuvable
        }
    }

    @GetMapping("/{email}/favoritess")
    public ResponseEntity<List<film>> getFavoriteMovies(@PathVariable String email) {
        List<film> favoriteMovies = userService.getFavoriteMoviesByEmail(email);

        if (favoriteMovies == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Utilisateur introuvable
        }

        return ResponseEntity.ok(favoriteMovies);
    }
    @GetMapping("/message") // Définit l'endpoint à "/message"
    public String getMessage() {
        return "Hello, this is a simple message!"; // Message à retourner
    }



    @PostMapping("/subscribe")
    public User subscribeToPremium(@RequestHeader("User-Email") String email) {
        return userService.subscribeToPremium(email);
    }
    @DeleteMapping("/{email}/deletefavorite/{movieId}")
    public ResponseEntity<String> removeFavoriteMovie(@PathVariable String email, @PathVariable String movieId) {
        boolean success = userService.removeFavoriteMovie(email, movieId);
        if (success) {
            return ResponseEntity.ok("Movie removed from favorites successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to remove movie from favorites.");
        }
    }
    @GetMapping("/by-email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé avec l'email : " + email);
        }
        return user;
    }
    @PutMapping("/by-email/{email}")
    public ResponseEntity<User> updateUserByEmail(
            @PathVariable String email,
            @RequestBody User user) {

        // Appel du service pour mettre à jour l'utilisateur par email
        User updatedUser = userService.updateUserByEmail(email, user);

        if (updatedUser == null) {
            // Si l'utilisateur n'est pas trouvé, renvoyer un code 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Retourner l'utilisateur mis à jour avec un code 200 OK
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/update-password")
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody PasswordChangeRequest request) {
        String responseMessage = userService.updatePassword(request.getEmail(), request.getOldPassword(), request.getNewPassword());

        Map<String, String> response = new HashMap<>();
        if (responseMessage.equals("Mot de passe modifié avec succès")) {
            response.put("message", responseMessage);
            return ResponseEntity.ok(response);  // Succès
        } else {
            response.put("error", responseMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // Erreur
        }
    }



}

