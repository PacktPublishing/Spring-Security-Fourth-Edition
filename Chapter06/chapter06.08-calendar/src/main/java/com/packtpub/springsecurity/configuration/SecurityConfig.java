package com.packtpub.springsecurity.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.UserDetailsServiceLdapAuthoritiesPopulator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * Spring Security Config Class
 *
 * @since chapter06.00
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public BindAuthenticator bindAuthenticator(FilterBasedLdapUserSearch userSearch, BaseLdapPathContextSource contextSource){
		BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource);
		bindAuthenticator.setUserSearch(userSearch);
		return bindAuthenticator;
	}

	@Bean
	public FilterBasedLdapUserSearch filterBasedLdapUserSearch(BaseLdapPathContextSource contextSource){
		return new FilterBasedLdapUserSearch("", //user-search-base
				"(uid={0})", //user-search-filter
				contextSource); //ldapServer
	}

	@Bean
	public LdapAuthoritiesPopulator authoritiesPopulator(UserDetailsService userDetailsService){
		return new UserDetailsServiceLdapAuthoritiesPopulator(userDetailsService);
	}

	@Bean
	public UserDetailsContextMapper userDetailsContextMapper(){
		return new InetOrgPersonContextMapper();
	}

	@Bean
	public UserDetailsManager userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("user1@example.com").password("user1").roles("USER").build());
		manager.createUser(User.withUsername("admin1@example.com").password("admin1").roles("USER", "ADMIN").build());
		return manager;
	}

	@Bean
	public LdapAuthenticationProvider authenticationProvider(BindAuthenticator ba,
			LdapAuthoritiesPopulator lap,
			UserDetailsContextMapper cm){
		LdapAuthenticationProvider ldapAuthenticationProvider =  new LdapAuthenticationProvider(ba, lap);
		ldapAuthenticationProvider.setUserDetailsContextMapper(cm);
		return ldapAuthenticationProvider;
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
		http.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable));
		return http.build();
	}
	
}
