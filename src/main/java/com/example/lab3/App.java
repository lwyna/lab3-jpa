package com.example.lab3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// On exclut Spring Security pour éviter l'erreur 401 Unauthorized
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class App {

    public static void main(String[] args) {
        // C'est CETTE ligne qui démarre le serveur Tomcat et Spring Boot !
        SpringApplication.run(App.class, args);
    }
}