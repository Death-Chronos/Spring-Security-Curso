package com.security.couponservice.security.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.security.couponservice.security.UserDetailsServiceImpl;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Esses dois métodos abaixo servem para fazer a autenticação customizada.
	// Lembre se de tira-las caso vá usar a padrão(Ollhe a aula 55 do curso, caso
	// não saiba como, um cometário dá um exemplo de como fazer)
	// Atualização, fiz uma branch que já faz isso
	@Bean
	SecurityContextRepository securityContextRepository() {
		return new DelegatingSecurityContextRepository(
				new RequestAttributeSecurityContextRepository(),
				new HttpSessionSecurityContextRepository());
	}

	@Bean
	AuthenticationManager authManager() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());

		return new ProviderManager(provider);
	}

	//Método de fazer um CORS(permitir a um outro serviço usufluir do nosso quando temos segurança) com @Bean(Só passar esse Bean lá no http.cors em vez de criar um novo)
	// @Bean
	// CorsConfigurationSource corsConfigurationSource() {
	// 		CorsConfiguration configuration = new CorsConfiguration();
	// 		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
	// 		configuration.setAllowedMethods(Arrays.asList("GET",'POST'));
	// 		configuration.setAllowedHeaders(Arrays.asList("*")); //Aqui configuramos os métodos e etc
	// 		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	// 		source.registerCorsConfiguration("/**", configuration); //Aqui as URLs
	// 	return source;
	// }

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults());

	 	http.authorizeHttpRequests(authorize -> authorize
	 		.requestMatchers(HttpMethod.GET, "/couponapi/coupons/{code:^[A-Z]*$}", "showGetCoupon", "/getCoupon","index")
	 			.permitAll()
	 		.requestMatchers(HttpMethod.GET, "/showCreateCoupon", "/createCoupon", "/createResponse")
					.hasRole("ADMIN")
	 		.requestMatchers(HttpMethod.POST, "/couponapi/coupons", "/saveCoupon")
	 			.hasRole("ADMIN")
	 		.requestMatchers(HttpMethod.POST, "/getCoupon")
	 			.hasAnyRole("USER", "ADMIN")
	 		.requestMatchers(HttpMethod.GET, "/", "/login", "/showReg", "/registerUser")
	 			.permitAll()
	 		.requestMatchers(HttpMethod.POST, "/login", "/registerUser")
	 			.permitAll());

		//Método de fazer um CORS(permitir a um outro serviço usufluir do nosso quando temos segurança) no Filter Chain
		// http.cors(cors->{
		// 	CorsConfigurationSource source = request ->{
		// 		CorsConfiguration configuration = new CorsConfiguration();
		// 		configuration.setAllowedOrigins(List.of("http://localhost:3000"));
		// 		configuration.setAllowedMethods(List.of("GET"));
		// 		return configuration;
		// 	};
		// 	cors.configurationSource(source);
		// });

		http.logout(logout -> logout.logoutSuccessUrl("/"));

		http.securityContext(security -> security.requireExplicitSave(true));

		return http.build();

	}
}
