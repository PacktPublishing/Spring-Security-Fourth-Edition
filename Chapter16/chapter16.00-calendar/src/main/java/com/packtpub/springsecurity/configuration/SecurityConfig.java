package com.packtpub.springsecurity.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Spring Security Config Class
 *
 * @since chapter16.00
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


	@Description("Configure HTTP Security")
	@Override
	protected void configure(final HttpSecurity http) throws Exception {

		//NOSONAR
		http.authorizeRequests(authorizeRequests -> authorizeRequests

				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/favicon.ico").permitAll()

				.antMatchers("/actuator/**").permitAll()
				.antMatchers("/signup/*").permitAll()
				.antMatchers("/").permitAll()
				.antMatchers("/login/*").permitAll()
				.antMatchers("/logout/*").permitAll()
				.antMatchers("/admin/h2/**").access("isFullyAuthenticated() and hasRole('ADMIN')")
				.antMatchers("/admin/*").hasRole("ADMIN")
				.antMatchers("/events/").hasRole("ADMIN")
				.antMatchers("/**").hasRole("USER")
		);

		// The default AccessDeniedException
		http.exceptionHandling(handler -> handler
				.accessDeniedPage("/errors/403")
		);

		// Login Configuration
		http.formLogin(form -> form
				.loginPage("/login/form")
				.loginProcessingUrl("/login")
				.failureUrl("/login/form?error")
				.usernameParameter("username") // redundant
				.passwordParameter("password") // redundant
				.defaultSuccessUrl("/default", true)
				.permitAll()
		);

		// Logout Configuration
		http.logout(form -> form
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login/form?logout")
				.permitAll()
		);


		// Allow anonymous users
		http.anonymous();

		// CSRF is enabled by default, with Java Config
		//NOSONAR
		http.csrf().disable();

		// Cross Origin Resource Sharing
		http.cors().disable();

		// HTTP Security Headers
		http.headers().disable();

		// Enable <frameset> in order to use H2 web console
		http.headers().frameOptions().disable();


	} // end configure

	/**
	 *
	 * @return passwordEncoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}



}
