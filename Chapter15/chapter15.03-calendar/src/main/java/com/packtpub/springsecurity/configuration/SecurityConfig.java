package com.packtpub.springsecurity.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler;

/**
 * Spring Security Config Class
 *
 * @since chapter15.00
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	

	@Bean
	public ReactiveAuthenticationManager reactiveAuthenticationManager(final ReactiveUserDetailsService userDetailsService,
			final PasswordEncoder passwordEncoder) {
		UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
		authenticationManager.setPasswordEncoder(passwordEncoder);
		return authenticationManager;
	}

	@Bean
	public SecurityWebFilterChain filterChain(ServerHttpSecurity http)  {
		http.authorizeExchange(exchanges -> exchanges
						.pathMatchers("/webjars/**").permitAll()
						.pathMatchers("/css/**").permitAll()
						.pathMatchers("/favicon.ico").permitAll()
						// H2 console:
						.pathMatchers("/admin/h2/**").permitAll()
						.pathMatchers("/").permitAll()
						.pathMatchers("/login/*").permitAll()
						.pathMatchers("/logout").permitAll()
						.pathMatchers("/signup/*").permitAll()
						.pathMatchers("/errors/**").permitAll()
						.pathMatchers("/admin/*").hasRole("ADMIN")
						.pathMatchers("/events/").hasRole("ADMIN")
						.pathMatchers("/**").hasRole("USER"));

		http.formLogin(Customizer.withDefaults());
		http.exceptionHandling(exceptions -> exceptions
				.accessDeniedHandler(new HttpStatusServerAccessDeniedHandler(HttpStatus.FORBIDDEN)));

		return http.build();
	}


	/**
	 * Standard BCrypt Password Encoder
	 *
	 * @return PasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}

}
