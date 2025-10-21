package com.example.expensetracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "running");
        response.put("message", "Expense Tracker API is live!");
        response.put("endpoints", "/expenses, /health");
        return response;
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}