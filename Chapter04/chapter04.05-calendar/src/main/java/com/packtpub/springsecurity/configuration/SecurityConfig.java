package com.packtpub.springsecurity.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security Config Class
 *
 * @since chapter04.00
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {


	/**
	 * The constant CUSTOM_USERS_BY_USERNAME_QUERY.
	 */
	private static String CUSTOM_USERS_BY_USERNAME_QUERY = "select email, password, true " +
			"from calendar_users where email = ?";

	/**
	 * The constant CUSTOM_AUTHORITIES_BY_USERNAME_QUERY.
	 */
	private static String CUSTOM_AUTHORITIES_BY_USERNAME_QUERY = "select cua.id, cua.authority " +
			"from calendar_users cu, calendar_user_authorities " +
			"cua where cu.email = ? " +
			"and cu.id = cua.calendar_user";


	/**
	 * User details service user details manager.
	 *
	 * @param dataSource the data source
	 * @return the user details manager
	 */
	@Bean
	public UserDetailsManager userDetailsService(DataSource dataSource) {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.setUsersByUsernameQuery(CUSTOM_USERS_BY_USERNAME_QUERY);
		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(CUSTOM_AUTHORITIES_BY_USERNAME_QUERY);
		return jdbcUserDetailsManager;
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
		http.securityContext(securityContext -> securityContext.requireExplicitSave(false));
		http.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable));
		return http.build();
	}


	/**
	 *
	 * @return ShaPasswordEncoder password encoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new StandardPasswordEncoder();
	}
}
