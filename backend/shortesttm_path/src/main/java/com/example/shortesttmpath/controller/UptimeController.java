package com.example.shortesttmpath.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller to make sure the HEAD request from UptimeRobot does not return 404.
 */
@RestController
public class UptimeController {
  /**
   * The default constructor.
   */
  public UptimeController() {
  }

  /**
   * To make sure the HEAD request from UptimeRobot does not return 404.
   *
   * @return 200 OK with a success message.
   */
  @GetMapping()
  public ResponseEntity<String> uptime() {
    HttpHeaders headers = new HttpHeaders();

    return new ResponseEntity<>("Ok", headers, HttpStatus.OK);
  }
}
