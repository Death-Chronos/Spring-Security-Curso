package com.security.couponservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class SecurityServiceImpl implements SecurityService{

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    SecurityContextRepository securityContRepo;

    @Override
    public boolean login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        //Normalmente, isso é feito automaticamente, estamos fazendo a autenticação com nossas próprias mãos, nosso login customizado.
        //Lembre-se, provavelmente funciona sem usar toda essa baboseira.
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,password, userDetails.getAuthorities());
        authenticationManager.authenticate(token);

        boolean result = token.isAuthenticated();

        if (result) {
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(token);
            securityContRepo.saveContext(context, request, response);
        }
        return result;
    }
    
}
