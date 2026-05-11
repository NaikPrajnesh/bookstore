package com.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * BookstoreApplication — Spring Boot entry point.
 *
 * @SpringBootApplication enables:
 *   - @Configuration       : marks this as a configuration class
 *   - @EnableAutoConfiguration : auto-configures Spring beans
 *   - @ComponentScan       : scans this package and sub-packages for components
 *
 * Run this class to start the server on http://localhost:8080
 */
@SpringBootApplication
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }
}
