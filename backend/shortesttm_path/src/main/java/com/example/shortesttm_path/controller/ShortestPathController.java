package com.example.shortesttm_path.controller;

import com.example.shortesttm_path.data.ShortestPathBean;
import com.example.shortesttm_path.util.ShortestPathUtil;
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
        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<>(ShortestPathUtil.printShortestPath(startingStation, destinationStation), headers, HttpStatus.CREATED);
    }
}
