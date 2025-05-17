// File: com.example.finalprojectmarountheresa.config.DataInitializer.java
package com.example.finalprojectmarountheresa.config;

import com.example.finalprojectmarountheresa.model.User;
import com.example.finalprojectmarountheresa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("prod") // Ensures this only runs in production (e.g., on Render)
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRoles(List.of("ROLE_ADMIN"));
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("client").isEmpty()) {
            User client = new User();
            client.setUsername("client");
            client.setPassword(passwordEncoder.encode("password"));
            client.setRoles(List.of("ROLE_USER"));
            userRepository.save(client);
        }
    }
}
