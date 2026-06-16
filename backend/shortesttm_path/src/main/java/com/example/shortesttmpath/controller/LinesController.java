package com.example.shortesttmpath.controller;

import com.example.shortesttmpath.util.LinesUtil;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller related to lines.
 */
@RestController
@RequestMapping("/lines")
public class LinesController {
  /**
   * The default constructor.
   */
  public LinesController() {
  }

  /**
   * Returns the name of each metro line.
   *
   * @return The name of each metro line.
   */
  @GetMapping()
  public ResponseEntity<List<String>> getAllLines() {
    HttpHeaders headers = new HttpHeaders();
    List<String> allLines = LinesUtil.getAllLines();

    return new ResponseEntity<>(allLines, headers, HttpStatus.OK);
  }
}
