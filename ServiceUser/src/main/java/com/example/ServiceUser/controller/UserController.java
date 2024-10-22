package com.example.ServiceUser.controller;

import com.example.ServiceUser.Entity.User;
import com.example.ServiceUser.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id).orElse(null));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/{userId}/watchlist")
    public ResponseEntity<Void> addToWatchlist(@PathVariable String userId, @RequestBody String movieId) {
        userService.addToWatchlist(userId, movieId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/watchlist")
    public ResponseEntity<Void> removeFromWatchlist(@PathVariable String userId, @RequestBody String movieId) {
        userService.removeFromWatchlist(userId, movieId);
        return ResponseEntity.ok().build();
    }
}
