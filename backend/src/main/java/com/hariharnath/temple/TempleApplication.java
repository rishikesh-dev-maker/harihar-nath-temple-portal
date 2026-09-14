package com.hariharnath.temple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Spring Boot Application Entry Point for Baba Hariharnath Temple.
 * Location: Sonepur, Saran, Bihar
 */
@SpringBootApplication
@EnableAsync
public class TempleApplication {

    public static void main(String[] args) {
        SpringApplication.run(TempleApplication.class, args);
    }
}
