package com.example.shortesttm_path.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortestPathController {
    @GetMapping("/greet")
    public String greetTheUser() {
        return "Hello User";
    }
}
