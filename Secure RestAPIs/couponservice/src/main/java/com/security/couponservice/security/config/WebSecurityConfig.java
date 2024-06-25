package com.security.couponservice.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    
    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults());
		
		http.authorizeHttpRequests(authorize -> authorize
		.requestMatchers(HttpMethod.GET,"/couponapi/coupons/{code:^[A-Z]*$}")
			.hasAnyRole("USER","ADMIN")
		.requestMatchers(HttpMethod.POST,"/couponapi/coupons")
			.hasRole("ADMIN"));
		//No método acima foi usado uma expressão regular, para padronizar os nomes dos cupons, em vez de só deixar **(deixa qualquer texto), o code é o code da requisição
		http.csrf(csrf->csrf.disable());
		
		return http.build();
		
	}
}
