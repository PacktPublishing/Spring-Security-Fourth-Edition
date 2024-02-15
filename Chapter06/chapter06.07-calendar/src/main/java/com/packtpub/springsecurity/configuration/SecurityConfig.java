package com.packtpub.springsecurity.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security Config Class
 *
 * @since chapter06.00
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${spring.ldap.embedded.port}")
	private int ldapPort;
	
	/**
	 * LDAP Server Context
	 * @return
	 */
	@Bean
	public DefaultSpringSecurityContextSource contextSource() {
		DefaultSpringSecurityContextSource defaultSpringSecurityContextSource =	 new DefaultSpringSecurityContextSource(
				List.of("ldap://localhost:" + ldapPort), "dc=jbcpcalendar,dc=com");
		defaultSpringSecurityContextSource.setUserDn("uid=admin,ou=system");
		defaultSpringSecurityContextSource.setPassword("secret");
		return defaultSpringSecurityContextSource;
	}
	
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
	public LdapAuthoritiesPopulator authoritiesPopulator(BaseLdapPathContextSource contextSource){
		DefaultLdapAuthoritiesPopulator defaultLdapAuthoritiesPopulator = new DefaultLdapAuthoritiesPopulator(contextSource,"ou=Groups");
		defaultLdapAuthoritiesPopulator.setGroupSearchFilter("(uniqueMember={0})");
		return defaultLdapAuthoritiesPopulator;
	}

	@Bean
	public UserDetailsContextMapper userDetailsContextMapper(){
		return new InetOrgPersonContextMapper();
	}

	@Bean
	public UserDetailsService userDetailsService(FilterBasedLdapUserSearch filterBasedLdapUserSearch,
			LdapAuthoritiesPopulator authoritiesPopulator, UserDetailsContextMapper userDetailsContextMapper) {
		LdapUserDetailsService ldapUserDetailsService = new LdapUserDetailsService(filterBasedLdapUserSearch, authoritiesPopulator);
		ldapUserDetailsService.setUserDetailsMapper(userDetailsContextMapper);
		return ldapUserDetailsService;
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
		
		// For H2 Console
		http.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable));
		return http.build();
	}

	/**
	 * Encoder password encoder.
	 *
	 * @return the password encoder
	 */
	@Bean
	public PasswordEncoder encoder() {
		return new LdapShaPasswordEncoder();
	}

	
}
