package com.example.shortesttmpath.controller;

import com.example.shortesttmpath.exception.InvalidLineException;
import com.example.shortesttmpath.util.ShortestPathUtil;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    List<String> allLines = List.of(
        "Blue",
        "Green",
        "Orange",
        "Yellow"
    );

    return new ResponseEntity<>(allLines, headers, HttpStatus.OK);
  }

  /**
   * Returns a 400 bad request with an error message if the InvalidLineException exception was
   * thrown meaning that the provided metro line is invalid.
   *
   * @return A 400 bad request with an error message.
   */
  @ExceptionHandler(InvalidLineException.class)
  public ResponseEntity<String> handle() {
    return ResponseEntity.badRequest().body("Provided line is invalid");
  }

  /**
   * Returns all the stations for a metro line.
   *
   * @param line The metro line.
   * @return All the stations for the metro line.
   */
  @GetMapping("/stations/{line}")
  public ResponseEntity<List<String>> getAllStations(@PathVariable String line) {
    HttpHeaders headers = new HttpHeaders();
    List<String> allStations = ShortestPathUtil.getAllStations(line);

    return new ResponseEntity<>(allStations, headers, HttpStatus.OK);
  }
}
