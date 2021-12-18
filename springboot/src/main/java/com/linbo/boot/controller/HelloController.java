package com.linbo.boot.controller;

import com.linbo.boot.bean.Pet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String handle01() {
        return "Hello, Spring Boot 2!";
    }

    @GetMapping("/test")
    public Pet debug() {
        return new Pet("zs");
    }
}
