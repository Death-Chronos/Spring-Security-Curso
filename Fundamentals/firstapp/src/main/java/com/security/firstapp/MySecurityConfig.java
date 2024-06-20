package com.security.firstapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class MySecurityConfig {

    // @Bean
    // UserDetailsService userDetailsService(){
    //     InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
    //     //Criando um novo Usuário(codificando sua senha)
    //     UserDetails user = User.withUsername("Jv")
    //             .password(passwordEncoder().encode("senha1234"))
    //             .authorities("read")
    //             .build();
    //     //Adicionando ele a memória(em memória).
    //     userDetailsService.createUser(user);
    //     return userDetailsService;
    // }
    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.httpBasic(Customizer.withDefaults()); //Método de login/autenticação
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/hello").authenticated().anyRequest().denyAll()); //Autorização de Requisições
        http.addFilterBefore(new MySecurityFilter(), BasicAuthenticationFilter.class); //Filtros
        return http.build();
    }
}
