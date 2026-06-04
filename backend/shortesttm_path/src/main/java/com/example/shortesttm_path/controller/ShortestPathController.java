package com.example.shortesttm_path.controller;

import com.example.shortesttm_path.service.ShortestPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortestPathController {
    private final ShortestPathService shortestPathService;

    @Autowired
    public ShortestPathController(ShortestPathService shortestPathService) {
        this.shortestPathService = shortestPathService;
    }

    @GetMapping("/greet")
    public String greetTheUser() {
        return shortestPathService.getGreeting();
    }
}
