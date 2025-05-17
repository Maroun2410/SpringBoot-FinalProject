package com.example.finalprojectmarountheresa.controller;

import com.mongodb.MongoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        try {
            mongoTemplate.getDb().listCollectionNames().first();
            return ResponseEntity.ok("MongoDB connection is OK ✅");
        } catch (MongoException ex) {
            return ResponseEntity.status(500).body("MongoDB connection FAILED ❌: " + ex.getMessage());
        }
    }
}

