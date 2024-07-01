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

@Controller
public class UserController {

    @Autowired
    UserRepository repo;

    @Autowired
    BCryptPasswordEncoder encoder;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/showReg")
    public String registerForm() {
        return "registerUser";
    }

    @PostMapping("/register")
    public String register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));

        HashSet<Role> roles = new HashSet<Role>();
        Role role = new Role();
        role.setId(2l);
        roles.add(role);

        user.setRoles(roles);

        repo.save(user);
        
        return "redirect:/login";
    }
    
    
    

}
