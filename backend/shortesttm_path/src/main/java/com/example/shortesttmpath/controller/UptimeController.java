package com.example.shortesttmpath.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UptimeController {
  @GetMapping()
  public ResponseEntity<String> uptime() {
    HttpHeaders headers = new HttpHeaders();

    return new ResponseEntity<>("Ok", headers, HttpStatus.OK);
  }
}
