package com.example.shortesttmpath.controller;

import com.example.shortesttmpath.data.ShortestPathBean;
import com.example.shortesttmpath.exception.StationsOnSameLineException;
import com.example.shortesttmpath.util.ShortestPathUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller related with providing the shortest metro path (the one with the least
 * stations) between two STM
 * metro stations.
 */
@RestController
@RequestMapping("/shortest_path")
public class ShortestPathController {
  /**
   * The default constructor.
   */
  public ShortestPathController() {
  }

  /**
   * Returns a 400 bad request with an error message if the StationsOnSameLineException exception
   * was thrown meaning
   * that the two provided stations were on the same line. This includes the same station given
   * twice and neighbor
   * stations.
   *
   * @return A 400 bad request with an error message.
   */
  @ExceptionHandler(StationsOnSameLineException.class)
  public ResponseEntity<String> handle() {
    return ResponseEntity.badRequest().body("Provided stations are on the same line");
  }

  /**
   * Returns the shortest metro path (the one with the least stations) between two STM metro
   * stations.
   *
   * @param startingStation    The starting station.
   * @param destinationStation The destination station.
   * @return The shortest metro path (the one with the least stations) between two STM metro
   *     stations.
   */
  @GetMapping()
  public ResponseEntity<ShortestPathBean> getShortestPath(@RequestParam String startingStation,
                                                          @RequestParam String destinationStation) {
    HttpHeaders headers = new HttpHeaders();
    ShortestPathBean path = ShortestPathUtil.getShortestPath(startingStation, destinationStation);
    return new ResponseEntity<>(path, headers, HttpStatus.OK);
  }
}
