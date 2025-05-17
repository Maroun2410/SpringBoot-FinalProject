package com.example.finalprojectmarountheresa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class FinalProjectMarounTheresaApplication {
    private static final Logger logger = LoggerFactory.getLogger(FinalProjectMarounTheresaApplication.class);

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    public static void main(String[] args) {
        SpringApplication.run(FinalProjectMarounTheresaApplication.class, args);
    }

    @PostConstruct
    public void logMongoUri() {
        logger.info("📡 MongoDB URI in use: {}", mongoUri);
    }
}
