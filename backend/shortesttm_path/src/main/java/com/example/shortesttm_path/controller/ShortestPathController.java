package com.example.shortesttm_path.controller;

import com.example.shortesttm_path.data.ShortestPathBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ShortestPathController {
    @GetMapping("/greet")
    public String greetTheUser() {
        return "Hello User";
    }

    @GetMapping()
    public ResponseEntity<ShortestPathBean> getShortestPath(@RequestParam String startingStation,
                                                            @RequestParam String destinationStation) {
        ShortestPathBean path = new ShortestPathBean();
        path.setStartingStation(startingStation);
        path.setDestinationStation(destinationStation);
        path.setStationsToSwitchLines(new ArrayList<>()); // TODO : Use the real list.

        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<>(path, headers, HttpStatus.CREATED);
    }
}
