package com.example.shortesttmpath.util;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileUtilTest {
  @Test
  public void getLinesReturnsCorrectLinesThreeLines() throws IOException {
    String fileName = "test/getLinesReturnsCorrectLinesThreeLines.txt";
    List<String> expectedResult = List.of(
        "Testing",
        "FileUtil",
        "class"
    );
    List<String> actualResult = FileUtil.getLines(fileName);

    Assertions.assertEquals(expectedResult, actualResult);
  }
}
