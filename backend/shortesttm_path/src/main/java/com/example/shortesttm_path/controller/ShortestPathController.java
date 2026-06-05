package com.example.shortesttm_path.controller;

import com.example.shortesttm_path.service.ShortestPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortestPathController {
    @GetMapping("/greet")
    public String greetTheUser() {
        return "Hello User";
    }
}
