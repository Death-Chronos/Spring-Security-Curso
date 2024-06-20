package com.security.firstapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
    
    @GetMapping("/hello")
    public String hello() {
        return "Spring Security Course";
    }

    @GetMapping("/bye")
    public String bye() {
        return "bye";
    }
}
