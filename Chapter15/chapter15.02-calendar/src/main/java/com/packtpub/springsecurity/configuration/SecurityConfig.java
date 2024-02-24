package com.packtpub.springsecurity.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.CACHE;
import static org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive.COOKIES;

/**
 * Spring Security Config Class
 *
 * @since chapter15.00
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * Filter chain security filter chain.
	 *
	 * @param http the http
	 * @return the security filter chain
	 * @throws Exception the exception
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests( authz -> authz
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
						//.requestMatchers("/events/")).hasRole("ADMIN")
						.requestMatchers("/**").hasRole("USER"))
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
						.permitAll());
		
		// CSRF is enabled by default, with Java Config
		http
				.csrf(csrf -> csrf
						.ignoringRequestMatchers(toH2Console())
						.disable());

		// Custom HTTP Strict Transport Security
		http
				.headers(headers -> headers
						.httpStrictTransportSecurity(hsts -> hsts
								.includeSubDomains(true)
								.preload(true)
								.maxAgeInSeconds(31536000)
						)
				);

		// Feature Policy
		http
				.headers(headers -> headers
						.featurePolicy("geolocation 'self'")
				);

		// Permissions Policy
		http
				.headers(headers -> headers
						.permissionsPolicy(permissions -> permissions
								.policy("geolocation=(self)")
						)
				);

		// Clear Site Data
		http
				.logout(logout -> logout
						.addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(CACHE, COOKIES)))
				);
		
		// Frame Options
		http.headers(headers -> headers
				.frameOptions(FrameOptionsConfig::sameOrigin));

		// Static Headers
		http.headers(headers -> headers
				.addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy","default-src 'self'"))
				.addHeaderWriter(new StaticHeadersWriter("X-WebKit-CSP","default-src 'self'")));

		// add DelegatingRequestMatcherHeaderWriter
		DelegatingRequestMatcherHeaderWriter headerWriter =
				new DelegatingRequestMatcherHeaderWriter(
						new AntPathRequestMatcher("/login"),
						new XFrameOptionsHeaderWriter());
		// add DelegatingRequestMatcherHeaderWriter
		http.headers(headers -> headers
				.addHeaderWriter(headerWriter));
		
		return http.build();
	}
	
	/**
	 *
	 * @return passwordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}

}


