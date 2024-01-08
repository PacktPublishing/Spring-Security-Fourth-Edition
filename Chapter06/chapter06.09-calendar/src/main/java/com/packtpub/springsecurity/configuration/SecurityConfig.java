package com.packtpub.springsecurity.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
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
	public AuthenticationProvider authenticationProvider(){
		ActiveDirectoryLdapAuthenticationProvider ap = new ActiveDirectoryLdapAuthenticationProvider(
				"corp.jbcpcalendar.com",
				"ldap://corp.jbcpcalendar.com");
		ap.setConvertSubErrorCodesToExceptions(true);
		return ap;
	}

	@Bean
	public DefaultSpringSecurityContextSource contextSource() {
		DefaultSpringSecurityContextSource defaultSpringSecurityContextSource =	 new DefaultSpringSecurityContextSource(
				List.of("ldap://corp.jbcpcalendar.com"), "dc=corp,dc=jbcpcalendar,dc=com");
		defaultSpringSecurityContextSource.setUserDn("CN=bnl,CN=Users,DC=corp,DC=jbcpcalendar,DC=com");
		defaultSpringSecurityContextSource.setPassword("admin123!");
		return defaultSpringSecurityContextSource;
	}

	@Bean
	public FilterBasedLdapUserSearch filterBasedLdapUserSearch(BaseLdapPathContextSource contextSource) {
		return new FilterBasedLdapUserSearch("CN=Users", //user-search-base
				"(sAMAccountName={0})", //user-search-filter
				contextSource); //ldapServer
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
						.requestMatchers(antMatcher("/admin/*")).hasAuthority("ADMIN")
						.requestMatchers(antMatcher("/events/")).hasAuthority("ADMIN")
						.requestMatchers(antMatcher("/**")).hasAuthority("USER"))

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
