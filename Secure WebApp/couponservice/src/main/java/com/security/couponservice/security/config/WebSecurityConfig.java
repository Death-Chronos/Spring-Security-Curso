package com.security.couponservice.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class WebSecurityConfig {

	@Autowired
	UserDetailsService userDetailsService;

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//Esses dois métodos abaixo servem para fazer a autenticação customizada. Lembre se de tira-las caso vá usar a padrão(Ollhe a aula 55 do curso, caso não saiba como, um cometário dá um exemplo de como fazer)
	//Atualização, fiz uma branch que já faz isso
	@Bean
	SecurityContextRepository securityContextRepository() {
		return new DelegatingSecurityContextRepository(
				new RequestAttributeSecurityContextRepository(), 
				new HttpSessionSecurityContextRepository()
				);
	}

	@Bean
	AuthenticationManager authManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());

		return new ProviderManager(provider);
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(authorize -> authorize
			.requestMatchers(HttpMethod.GET, "/couponapi/coupons/{code:^[A-Z]*$}", "showGetCoupon", "/getCoupon","index")
				.hasAnyRole("USER", "ADMIN")
			.requestMatchers(HttpMethod.GET, "/showCreateCoupon", "/createCoupon", "/createResponse")
				.hasRole("ADMIN")
			.requestMatchers(HttpMethod.POST, "/couponapi/coupons", "/saveCoupon", "/saveCoupon")
				.hasRole("ADMIN")
			.requestMatchers(HttpMethod.POST, "/getCoupon")
				.hasAnyRole("USER", "ADMIN")
			.requestMatchers(HttpMethod.GET, "/","/login", "/showReg", "/registerUser")
				.permitAll()
			.requestMatchers(HttpMethod.POST,"/login", "/registerUser")
				.permitAll());
			
		http.logout(logout -> logout.logoutSuccessUrl("/"));		

		http.securityContext(security -> security.requireExplicitSave(true));
		http.csrf(csrf -> csrf.disable());

		return http.build();

	}
}
