package com.example.shortesttmpath.service;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileServiceTest {
  @Test
  public void getLinesReturnsCorrectLinesThreeLines() throws IOException {
    String fileName = "test/getLinesReturnsCorrectLinesThreeLines.txt";
    List<String> expectedResult = List.of(
        "Testing",
        "FileUtil",
        "class"
    );
    List<String> actualResult = new FileService().getLines(fileName);

    Assertions.assertEquals(expectedResult, actualResult);
  }
}
