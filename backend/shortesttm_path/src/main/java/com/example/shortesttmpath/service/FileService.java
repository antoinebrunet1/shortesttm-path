package com.example.shortesttmpath.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.springframework.core.io.ClassPathResource;

/**
 * Service to handle static resources.
 */
public class FileService {
  /**
   * The default constructor.
   */
  public FileService() {
  }

  /**
   * Returns a List containing the lines of a file in resources/static.
   *
   * @param fileName The name of the file in resources/static.
   * @return A List containing the lines of the file.
   * @throws IOException IOException.
   */
  public static List<String> getLines(String fileName) throws IOException {
    ClassPathResource resource = new ClassPathResource("static/" + fileName);

    return new BufferedReader(new InputStreamReader(resource.getInputStream())).lines().toList();
  }
}
