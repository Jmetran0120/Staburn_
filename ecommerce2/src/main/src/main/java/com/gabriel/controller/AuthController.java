package com.gabriel.controller;

import com.gabriel.entity.UserData;
import com.gabriel.model.User;
import com.gabriel.repository.UserDataRepository;
import com.gabriel.serviceimpl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * AuthController
 * Handles user authentication (login and signup)
 */
@RestController
@CrossOrigin(origins = "*")
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserServiceImpl userService;
    
    @Autowired
    private UserDataRepository userDataRepository;

    /**
     * POST /api/auth/signup - Register a new user
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            String name = request.get("name");
            String role = request.get("role"); // "customer" or "admin"
            
            if (email == null || password == null || name == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email, password, and name are required"));
            }
            
            // Check if user already exists
            if (userDataRepository.existsByEmail(email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "User with this email already exists"));
            }
            
            // Create user
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setRole(role != null ? role : "customer");
            
            User createdUser = userService.create(user, password);
            
            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("user", createdUser);
            response.put("token", "dummy-token-" + createdUser.getId()); // In production, use JWT
            
            log.info("User signed up successfully: {}", email);
            return ResponseEntity.ok(response);
            
        } catch (Exception ex) {
            log.error("Signup failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Sign up failed. Please try again."));
        }
    }

    /**
     * POST /api/auth/login - Authenticate user
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            if (email == null || password == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email and password are required"));
            }
            
            // Find user by email
            UserData userData = userService.getUserDataByEmail(email);
            if (userData == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
            }
            
            // Verify password
            if (!userService.verifyPassword(password, userData.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
            }
            
            // Convert to User model (without password)
            User user = new User();
            user.setId(userData.getId());
            user.setEmail(userData.getEmail());
            user.setName(userData.getName());
            user.setRole(userData.getRole().toString());
            
            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("token", "dummy-token-" + user.getId()); // In production, use JWT
            
            log.info("User logged in successfully: {}", email);
            return ResponseEntity.ok(response);
            
        } catch (Exception ex) {
            log.error("Login failed: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Login failed. Please try again."));
        }
    }
}

