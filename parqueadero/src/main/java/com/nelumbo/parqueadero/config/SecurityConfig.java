package com.nelumbo.parqueadero.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nelumbo.parqueadero.components.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final UserDetailsService userDetailsService;

	public SecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder()).and().build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter)
			throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(authz -> authz
				.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()
				.requestMatchers("/h2-console/**").permitAll()
				.requestMatchers("/auth/login**").permitAll()

				.requestMatchers("/parqueadero/**").hasRole("ADMIN")
				.requestMatchers("/auth/registro**").hasRole("ADMIN")				
				.requestMatchers("/vehiculo/**").hasRole("SOCIO")				
		        .requestMatchers("/historial/**").hasAnyRole("ADMIN", "SOCIO")
				.anyRequest().authenticated())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.headers().frameOptions().sameOrigin();

		return http.build();
	}
}
