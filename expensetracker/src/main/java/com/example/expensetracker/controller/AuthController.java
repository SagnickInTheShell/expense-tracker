package com.example.expensetracker.controller;

import com.example.expensetracker.entity.User;
import com.example.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(originPatterns = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> request) {
        try {
            if (userRepository.existsByEmail(request.get("email"))) {
                return ResponseEntity.badRequest().body("Email exists");
            }

            User user = new User();
            user.setEmail(request.get("email"));
            user.setName(request.get("name"));
            user.setPassword(request.get("password"));
            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getId());
            response.put("name", user.getName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            User user = userRepository.findByEmail(request.get("email"))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.getPassword().equals(request.get("password"))) {
                return ResponseEntity.badRequest().body("Wrong password");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getId());
            response.put("name", user.getName());
            response.put("budget", user.getMonthlyBudget());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}