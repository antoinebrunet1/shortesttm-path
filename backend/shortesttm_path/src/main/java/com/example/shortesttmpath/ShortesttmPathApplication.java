package com.example.shortesttmpath;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;

/**
 * The main class of the Spring Boot application.
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class,
    UserDetailsServiceAutoConfiguration.class})
public class ShortesttmPathApplication {
  /**
   * The default constructor.
   */
  public ShortesttmPathApplication() {
  }

  /**
   * The main method of the Spring Boot application.
   *
   * @param args The arguments.
   */
  public static void main(String[] args) {
    SpringApplication.run(ShortesttmPathApplication.class, args);
  }
}
