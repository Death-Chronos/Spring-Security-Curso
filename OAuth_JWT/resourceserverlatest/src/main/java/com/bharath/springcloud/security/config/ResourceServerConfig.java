package com.bharath.springcloud.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceServerConfig {

	@Bean // basicamente, é responsavel por pegar as Roles do JWt e transformar em GrantedAuthority(para que os hasRole e anyRole funcionem)
	JwtAuthenticationConverter jwtAuthConverter() {

		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); //Define o "atributo" a ser pego
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix(""); //Define o prefixo(nesse caso, nenhum)

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

		return jwtAuthenticationConverter;

	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//@formatter:off
        http.oauth2ResourceServer(server -> server
			.jwt(jwt -> jwt
				.jwtAuthenticationConverter(jwtAuthConverter()))); //Troca o conversor do jwt, para permitir a troca mencionada na função acima
		
		
		http.authorizeHttpRequests(authorize -> authorize
			.requestMatchers(HttpMethod.GET,"/couponapi/coupons/{code:^[A-Z]*$}")
				.hasAnyRole("USER","ADMIN")
			.requestMatchers(HttpMethod.POST,"/couponapi/coupons")
				.hasRole("ADMIN").anyRequest().authenticated()); //faz as config dos endpoints
		//@formatter:on
		http.csrf(csrf -> csrf.disable());

		return http.build();

	}
}
