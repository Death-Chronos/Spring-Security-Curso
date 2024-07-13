package com.security.couponservice.controllers;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.security.couponservice.models.Role;
import com.security.couponservice.models.User;
import com.security.couponservice.repositories.UserRepository;
import com.security.couponservice.security.SecurityServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Controller
public class UserController {

    @Autowired
    private SecurityServiceImpl security;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/showReg")
    public String showRegistrationPage() {
        return "registerUser";
    }
    @PostMapping("registerUser")
    public String register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = new Role();
        role.setId(2l);

        HashSet<Role> roles = new HashSet<Role>();
        roles.add(role);
        
        user.setRoles(roles);

        userRepository.save(user);
        return "login";
    }
    
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
