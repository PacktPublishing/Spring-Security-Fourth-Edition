package com.packtpub.springsecurity.configuration;

import com.packtpub.springsecurity.authentication.CalendarUserAuthenticationProvider;
import com.packtpub.springsecurity.web.authentication.DomainUsernamePasswordAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
	 * Filter chain security filter chain.
	 *
	 * @param http        the http
	 * @param authManager the auth manager
	 * @return the security filter chain
	 * @throws Exception the exception
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
		http.authorizeHttpRequests( authz -> authz
						.requestMatchers("/webjars/**").permitAll()
						.requestMatchers("/css/**").permitAll()
						.requestMatchers("/favicon.ico").permitAll()
						// H2 console:
						.requestMatchers("/admin/h2/**").permitAll()
						.requestMatchers("/").permitAll()
						.requestMatchers("/login/*").permitAll()
						.requestMatchers("/logout").permitAll()
						.requestMatchers("/signup/*").permitAll()
						.requestMatchers("/errors/**").permitAll()
						.requestMatchers("/admin/*").hasRole("ADMIN")
						.requestMatchers("/events/").hasRole("ADMIN")
						.requestMatchers("/**").hasRole("USER"))

				.exceptionHandling(exceptions -> exceptions
						.accessDeniedPage("/errors/403")
						.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login/form")))
				.logout(form -> form
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login/form?logout")
						.permitAll())
				// CSRF is enabled by default, with Java Config
				.csrf(AbstractHttpConfigurer::disable)
				// Add custom DomainUsernamePasswordAuthenticationFilter
				.addFilterAt(domainUsernamePasswordAuthenticationFilter(authManager), UsernamePasswordAuthenticationFilter.class);

		http.securityContext(securityContext -> securityContext.requireExplicitSave(false));
		http.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable));
		return http.build();
	}


	/**
	 * Domain username password authentication filter domain username password authentication filter.
	 *
	 * @param authManager the auth manager
	 * @return the domain username password authentication filter
	 */
	@Bean
	public DomainUsernamePasswordAuthenticationFilter domainUsernamePasswordAuthenticationFilter(AuthenticationManager authManager) {
		DomainUsernamePasswordAuthenticationFilter dupaf = new
				DomainUsernamePasswordAuthenticationFilter(authManager);
		dupaf.setFilterProcessesUrl("/login");
		dupaf.setUsernameParameter("username");
		dupaf.setPasswordParameter("password");
		dupaf.setAuthenticationSuccessHandler(new SavedRequestAwareAuthenticationSuccessHandler() {{
			setDefaultTargetUrl("/default");
		}});
		dupaf.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler() {{
			setDefaultFailureUrl("/login/form?error");
		}});
		dupaf.afterPropertiesSet();
		return dupaf;
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

}
