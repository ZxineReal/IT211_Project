package com.example.project2;

import com.example.project2.entity.User;
import com.example.project2.entity.enums.Role;
import com.example.project2.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Project2Application {

    public static void main(String[] args) {
        SpringApplication.run(Project2Application.class, args);
    }

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("admin123"))
                        .fullName("System Administrator")
                        .phoneNumber("0123456789")
                        .role(Role.ADMIN)
                        .enabled(true)
                        .accountNonLocked(true)
                        .createdAt(LocalDateTime.now())
                        .build();
                userRepository.save(admin);
                System.out.println(">>> Default admin user created (username: admin, password: admin123)");
            } else {
                System.out.println(">>> Admin user already exists. Skipping initialization.");
            }
        };
    }
}
