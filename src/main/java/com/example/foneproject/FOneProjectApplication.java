package com.example.foneproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FOneProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(FOneProjectApplication.class, args);
    }
}
