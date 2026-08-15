package com.example.shortesttmpath.controller;

import com.example.shortesttmpath.util.ShortestPathUtil;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller related to stations.
 */
@RestController
@RequestMapping("/stations")
public class StationsController {
  /**
   * The default constructor.
   */
  public StationsController() {
  }

  /**
   * Returns all the stations in alphabetical order.
   *
   * @return All the stations in alphabetical order.
   * @throws IOException IOException.
   */
  @GetMapping("alphabetical-order")
  public ResponseEntity<List<String>> getAllStationsInAlphabeticalOrder() throws IOException {
    HttpHeaders headers = new HttpHeaders();
    List<String> allStationsInAlphabeticalOrder = new ShortestPathUtil()
        .getAllStationsInAlphabeticalOrder();

    return new ResponseEntity<>(allStationsInAlphabeticalOrder, headers, HttpStatus.OK);
  }
}
