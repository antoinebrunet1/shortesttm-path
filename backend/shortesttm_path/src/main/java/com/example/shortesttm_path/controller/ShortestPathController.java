package com.example.shortesttm_path.controller;

import com.example.shortesttm_path.data.ShortestPathBean;
import com.example.shortesttm_path.exception.StationsOnSameLineException;
import com.example.shortesttm_path.util.ShortestPathUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shortest_path")
public class ShortestPathController {
    @ExceptionHandler(StationsOnSameLineException.class)
    public ResponseEntity<String> handle() {
        return ResponseEntity.badRequest().body("Provided stations are on the same line");
    }

    @GetMapping()
    public ResponseEntity<ShortestPathBean> getShortestPath(@RequestParam String startingStation,
                                                            @RequestParam String destinationStation) {
        HttpHeaders headers = new HttpHeaders();
        ShortestPathBean path = ShortestPathUtil.getShortestPath(startingStation, destinationStation);

        return new ResponseEntity<>(path, headers, HttpStatus.CREATED);
    }
}
