package com.packtpub.springsecurity.configuration;

import com.packtpub.springsecurity.repository.RememberMeTokenRepository;
import com.packtpub.springsecurity.web.authentication.rememberme.JpaPersistentTokenRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * Spring Security Config Class
 *
 * @since chapter07.00
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {


	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder =
				http.getSharedObject(AuthenticationManagerBuilder.class);
		return authenticationManagerBuilder.build();
	}

	/**
	 * Filter chain security filter chain.
	 *
	 * @param http the http
	 * @return the security filter chain
	 * @throws Exception the exception
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, 
			PersistentTokenRepository persistentTokenRepository, RememberMeServices rememberMeServices) throws Exception {
		http.authorizeHttpRequests( authz -> authz
						.requestMatchers(antMatcher("/webjars/**")).permitAll()
						.requestMatchers(antMatcher("/css/**")).permitAll()
						.requestMatchers(antMatcher("/favicon.ico")).permitAll()
						// H2 console:
						.requestMatchers(antMatcher("/admin/h2/**"))
						.access(new WebExpressionAuthorizationManager("isFullyAuthenticated() and hasRole('ADMIN')"))
						.requestMatchers(antMatcher("/")).permitAll()
						.requestMatchers(antMatcher("/login/*")).permitAll()
						.requestMatchers(antMatcher("/logout")).permitAll()
						.requestMatchers(antMatcher("/signup/*")).permitAll()
						.requestMatchers(antMatcher("/errors/**")).permitAll()
						.requestMatchers(antMatcher("/events/")).hasRole("ADMIN")
						.requestMatchers(antMatcher("/**")).hasRole("USER"))

				.exceptionHandling(exceptions -> exceptions
						.accessDeniedPage("/errors/403"))
				.formLogin(form -> form
						.loginPage("/login/form")
						.loginProcessingUrl("/login")
						.failureUrl("/login/form?error")
						.usernameParameter("username")
						.passwordParameter("password")
						.defaultSuccessUrl("/default", true)
						.permitAll())
				.logout(form -> form
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login/form?logout")
						.permitAll())
				// CSRF is enabled by default, with Java Config
				.csrf(AbstractHttpConfigurer::disable);
		// For H2 Console
		http.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable));
		
		// Remember Me
		http.rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer
				.key("jbcpCalendar")
				.rememberMeServices(rememberMeServices)
				.tokenRepository(persistentTokenRepository));
		
		return http.build();
	}


	/**
	 * Standard SHA-256 Password Encoder
	 *
	 * @return ShaPasswordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository(
			RememberMeTokenRepository rmtr) {
		return new JpaPersistentTokenRepository(rmtr);
	}

	@Bean
	public RememberMeServices rememberMeServices (PersistentTokenRepository ptr, UserDetailsService  userDetailsService){
		PersistentTokenBasedRememberMeServices rememberMeServices = new
				PersistentTokenBasedRememberMeServices("jbcpCalendar",
				userDetailsService, ptr);
		rememberMeServices.setAlwaysRemember(true);
		return rememberMeServices;
	}
}
