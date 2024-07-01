package com.security.couponservice.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
public class WebSecurityConfig {

	@Autowired
	UserDetailsService userDetailsService;

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.formLogin(login -> login
		.loginPage("/login")
		.defaultSuccessUrl("/index", true)
		.permitAll());


		http.authorizeHttpRequests(authorize -> authorize
			.requestMatchers(HttpMethod.GET, "/couponapi/coupons/{code:^[A-Z]*$}", "showGetCoupon", "/getCoupon","/index")
				.hasAnyRole("USER", "ADMIN")
			.requestMatchers(HttpMethod.GET, "/showCreateCoupon", "/createCoupon", "/createResponse")
				.hasRole("ADMIN")
			.requestMatchers(HttpMethod.POST, "/couponapi/coupons", "/saveCoupon", "/saveCoupon")
				.hasRole("ADMIN")
			.requestMatchers(HttpMethod.POST, "/getCoupon","/login")
				.hasAnyRole("USER", "ADMIN")
			.requestMatchers(HttpMethod.GET, "/login", "/showReg")
				.permitAll()
			.requestMatchers(HttpMethod.POST, "/register")
				.permitAll());
			
		http.logout(logout -> logout.logoutSuccessUrl("/login").permitAll());		

		//As URLs que não usam th:action em seus forms, não recebem o código csrf automaticamente, por isso é necessário fazer este procedimento
		http.csrf(csrf-> csrf
			.ignoringRequestMatchers(
				new RegexRequestMatcher("/getCoupon", "POST"),
				new RegexRequestMatcher("/registerUser", "POST"))
			.ignoringRequestMatchers("/couponapi/coupons/{code:^[A-Z]*$}","/couponapi/coupons")
		);

		return http.build();

	}
}
