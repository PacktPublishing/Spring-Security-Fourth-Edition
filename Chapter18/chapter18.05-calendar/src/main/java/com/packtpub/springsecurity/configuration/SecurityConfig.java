package com.packtpub.springsecurity.configuration;

import org.apereo.cas.client.session.SingleSignOutFilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * Spring Security Config Class
 *
 * @since chapter18.00
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${cas.logout.url}")
	private String casLogoutUrl;

	/**
	 * Filter chain security filter chain.
	 *
	 * @param http the http
	 * @return the security filter chain
	 * @throws Exception the exception
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http,
			CasAuthenticationEntryPoint casAuthenticationEntryPoint, 
			CasAuthenticationFilter casAuthenticationFilter, 
			LogoutFilter logoutFilter) throws Exception {
		http.authorizeHttpRequests(authz -> authz
						.requestMatchers("/webjars/**").permitAll()
						.requestMatchers("/css/**").permitAll()
						.requestMatchers("/favicon.ico").permitAll()
						.requestMatchers("/").permitAll()
						.requestMatchers("/login/*").permitAll()
						.requestMatchers("/logout").permitAll()
						.requestMatchers("/signup/*").permitAll()
						.requestMatchers("/errors/**").permitAll()
						// H2 console
						.requestMatchers("/admin/h2/**")
						.access(new WebExpressionAuthorizationManager("isFullyAuthenticated() and hasRole('ADMIN')"))
						.requestMatchers("/events/").hasRole("ADMIN")
						.requestMatchers("/**").hasRole("USER"))
				// Exception Handling
				.exceptionHandling(exceptions -> exceptions
						.authenticationEntryPoint(casAuthenticationEntryPoint)
						.accessDeniedPage("/errors/403"));

		// CAS Filter
		http.addFilterAt(casAuthenticationFilter, CasAuthenticationFilter.class);
        // Logout Filter
		http
				.addFilterBefore(new SingleSignOutFilter(), CasAuthenticationFilter.class)
				.addFilterBefore(logoutFilter, LogoutFilter.class);
		// Logout
		http.logout(form -> form
						.logoutUrl("/logout")
						.logoutSuccessUrl(casLogoutUrl))

				// CSRF is enabled by default, with Java Config
				.csrf(AbstractHttpConfigurer::disable);
		// For H2 Console
		http.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable));
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
