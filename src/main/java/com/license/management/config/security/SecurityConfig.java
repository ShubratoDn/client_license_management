package com.license.management.config.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.license.management.config.jwt.JwtAuthenticationEntryPoint;
import com.license.management.config.jwt.JwtAuthenticationFilter;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;
	
	// List of whitelisted endpoints that are allowed without authentication
	public static final String[] ENDPOINTS_WHITELIST = {
        "/api/v1/login",
        "/api/v1/register",
        "/api/v1/test",
//        "/api/v1/delete/user/**",       
    };	
	
	/**
     * Configures the security filter chain.
     *
     * @param http The HttpSecurity object to configure.
     * @return The configured security filter chain.
     * @throws Exception If an error occurs while configuring the security filter chain.
     */	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors() // Enable Cross-Origin Resource Sharing (CORS)
			.and().csrf().disable() // Disable CSRF protection
			.authorizeHttpRequests(auth ->
				auth.requestMatchers(ENDPOINTS_WHITELIST).permitAll() // Permit whitelisted endpoints without authentication					
					.requestMatchers("/api/v1/auth/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
					.anyRequest().authenticated()) // Require authentication for other endpoints
			.exceptionHandling()
			.authenticationEntryPoint(authenticationEntryPoint) // Configure authentication entry point for handling unauthenticated requests				
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Use stateless sessions
		
		// Configure authentication provider
		http.authenticationProvider(authenticationProvider());
		
		// Configure JWT filter
		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		DefaultSecurityFilterChain build = http.build();
		return build;
	}
	
	@Autowired
	CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

	
	/**
     * Configures and returns the DaoAuthenticationProvider.
     *
     * @return The configured DaoAuthenticationProvider.
     */
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customUserDetailsServiceImpl); // Set custom user details service
		authenticationProvider.setPasswordEncoder(this.passwordEncoder()); // Set password encoder
		return authenticationProvider;
	}
	
	
	/**
    * Configures and returns the password encoder.
    *
    * @return The configured password encoder.
    */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Use BCryptPasswordEncoder for password hashing
	}
	
	/**
     * Configures and returns the AuthenticationManager.
     *
     * @return The configured AuthenticationManager.
     */
	@Bean
	AuthenticationManager authenticationManager() {
		ProviderManager providerManager = new ProviderManager(Collections.singletonList(authenticationProvider()));
		return providerManager; // Configure and return authentication manager
	}
}
