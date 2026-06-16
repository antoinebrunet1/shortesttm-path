package com.example.shortesttmpath.util;

import java.util.List;

/**
 * The util related to the metro lines.
 */
public class LinesUtil {
  private static final List<String> ALL_LINES = List.of(
      "Blue",
      "Green",
      "Orange",
      "Yellow"
  );

  /**
   * The default constructor.
   */
  public LinesUtil() {
  }

  /**
   * Returns the name of each metro line.
   *
   * @return The name of each metro line.
   */
  public static List<String> getAllLines() {
    return ALL_LINES;
  }
}
