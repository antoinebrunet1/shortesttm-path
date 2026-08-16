package com.example.shortesttmpath.controller;

import com.example.shortesttmpath.service.StationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
  @Autowired
  private StationService stationService;

  /**
   * The default constructor.
   */
  public StationsController() {
  }

  /**
   * Returns all the stations in alphabetical order.
   *
   * @return All the stations in alphabetical order.
   */
  @GetMapping("alphabetical-order")
  public ResponseEntity<List<String>> getAllStationsInAlphabeticalOrder() {
    return new ResponseEntity<>(stationService.getAllStationsInAlphabeticalOrder(),
        new HttpHeaders(), HttpStatus.OK);
  }
}
