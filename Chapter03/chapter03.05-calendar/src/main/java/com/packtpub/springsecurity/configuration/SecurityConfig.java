package com.packtpub.springsecurity.configuration;

import com.packtpub.springsecurity.authentication.CalendarUserAuthenticationProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * Spring Security Config Class
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * The Cuap.
	 */
	private final CalendarUserAuthenticationProvider cuap;

	/**
	 * Instantiates a new Security config.
	 *
	 * @param cuap the cuap
	 */
	public SecurityConfig(CalendarUserAuthenticationProvider cuap) {
		this.cuap = cuap;
	}


	/**
	 * Auth manager authentication manager.
	 *
	 * @param http the http
	 * @return the authentication manager
	 * @throws Exception the exception
	 */
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder =
				http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(cuap);
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
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests((authz) -> authz
						.requestMatchers(antMatcher("/webjars/**")).permitAll()
						.requestMatchers(antMatcher("/css/**")).permitAll()
						.requestMatchers(antMatcher("/favicon.ico")).permitAll()
						// H2 console:
						.requestMatchers(antMatcher("/admin/h2/**")).permitAll()
						.requestMatchers(antMatcher("/")).permitAll()
						.requestMatchers(antMatcher("/login/*")).permitAll()
						.requestMatchers(antMatcher("/logout")).permitAll()
						.requestMatchers(antMatcher("/signup/*")).permitAll()
						.requestMatchers(antMatcher("/errors/**")).permitAll()
						.requestMatchers(antMatcher("/admin/*")).hasRole("ADMIN")
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
		http.securityContext(securityContext -> securityContext.requireExplicitSave(false));
		http.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable));
		return http.build();
	}

}
