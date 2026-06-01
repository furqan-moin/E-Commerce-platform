package com.furqan.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping
    public String home() {
        return "Welcome to my E-Commerce Platform!";
    }
}
