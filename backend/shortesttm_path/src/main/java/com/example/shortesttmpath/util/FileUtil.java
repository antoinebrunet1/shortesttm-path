package com.example.shortesttmpath.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.springframework.core.io.ClassPathResource;

public class FileUtil {
  public static List<String> getLinesFromFileInStaticResources(String fileName) throws IOException {
    ClassPathResource resource = new ClassPathResource("static/" + fileName);

    return new BufferedReader(new InputStreamReader(resource.getInputStream())).lines().toList();
  }
}
