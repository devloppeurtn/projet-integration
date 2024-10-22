package com.example.ServiceUser.Service;

import com.example.ServiceUser.Entity.User;
import com.example.ServiceUser.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addToWatchlist(String userId, String movieId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.getWatchlist().add(movieId);
        userRepository.save(user);
    }

    public void removeFromWatchlist(String userId, String movieId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.getWatchlist().remove(movieId);
        userRepository.save(user);
    }

    // Ajouter d'autres méthodes pour les fonctionnalités requises...
}
