// package com.security.firstapp;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class MySecurityConfigReference {
// //Classe apenas para referencia
// 	@Bean
// 	UserDetailsService userDetailsService() {
// 		InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
// 		UserDetails user = User.withUsername("tom").password(passwordEncoder().encode("cruise")).authorities("read")
// 				.build();
// 		userDetailsService.createUser(user);
// 		return userDetailsService;
// 	}

// 	@Bean
// 	BCryptPasswordEncoder passwordEncoder() {
// 		return new BCryptPasswordEncoder();
// 	}
	
// 	// Método antes do Spring Security 3.2
// //	@Bean
// //	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
// //		http.httpBasic();
// //		http.authorizeHttpRequests().anyRequest().authenticated();
// //		return http.build();
// //
// //	}
	
// 	@Bean
// 	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
// 		http.httpBasic(Customizer.withDefaults());
// 		http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
// 		return http.build();

// 	}

	
// }
