package com.example.User_Service.controller;

import com.example.User_Service.entity.ForgotPasswordRequest;
import com.example.User_Service.entity.LoginRequest;
import com.example.User_Service.entity.ResetPasswordRequest;
import com.example.User_Service.entity.User;
import com.example.User_Service.service.EmailService;
import com.example.User_Service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

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
    @PostMapping("/{userEmail}/favorites")
    public ResponseEntity<String> addMovieToFavorites(
            @PathVariable String userEmail,
            @RequestParam String movieId) {
        try {
            String message = userService.addMovieToFavorites(userEmail, movieId);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

