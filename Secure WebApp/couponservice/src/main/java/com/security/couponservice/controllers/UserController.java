package com.security.couponservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.security.couponservice.security.SecurityServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class UserController {

    @Autowired
    private SecurityServiceImpl security;
    
    @GetMapping("/")
    public String showLoginPage () {
        return "login";
    }
    @PostMapping("/login")
    public String login(String email, String password, HttpServletRequest request, HttpServletResponse response) {
        boolean loginResponse = security.login(email, password, request, response);
        if (loginResponse) {
            return "index";
        }
        return "login";
    }
    
}
