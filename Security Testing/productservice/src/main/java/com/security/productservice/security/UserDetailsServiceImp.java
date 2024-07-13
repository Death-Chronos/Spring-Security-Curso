package com.security.productservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.productservice.models.User;
import com.security.productservice.repositories.UserRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService{

    @Autowired
    UserRepository repo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = repo.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário com email " + username + " não encontrado");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getRoles());
    }
    
}
