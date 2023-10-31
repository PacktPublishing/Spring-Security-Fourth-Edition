package com.packtpub.springsecurity.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.ldap.LdapPasswordComparisonAuthenticationManagerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
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

	@Value("${spring.ldap.embedded.port}")
	private int LDAP_PORT;

	private BaseLdapPathContextSource getDefaultSpringSecurityContextSource () {
		DefaultSpringSecurityContextSource defaultSpringSecurityContextSource =	 new DefaultSpringSecurityContextSource(
				List.of("ldap://localhost:" + LDAP_PORT), "dc=jbcpcalendar,dc=com");
		defaultSpringSecurityContextSource.setUserDn("uid=admin,ou=system");
		defaultSpringSecurityContextSource.setPassword("secret");
		defaultSpringSecurityContextSource.afterPropertiesSet();
		return defaultSpringSecurityContextSource;
	}

	
	@Bean
	LdapAuthoritiesPopulator authorities() {
		String groupSearchBase = "ou=Groups";
		DefaultLdapAuthoritiesPopulator authorities =
				new DefaultLdapAuthoritiesPopulator(this.getDefaultSpringSecurityContextSource(), groupSearchBase);
		authorities.setGroupSearchFilter("(uniqueMember={0})");
		return authorities;
	}

	@Bean
	AuthenticationManager authenticationManager(LdapAuthoritiesPopulator authorities) {
		LdapPasswordComparisonAuthenticationManagerFactory factory = new LdapPasswordComparisonAuthenticationManagerFactory(
				this.getDefaultSpringSecurityContextSource(), new LdapShaPasswordEncoder());
		factory.setUserSearchBase("");
		factory.setUserDetailsContextMapper(new InetOrgPersonContextMapper());
		factory.setUserSearchFilter("(uid={0})");
		factory.setLdapAuthoritiesPopulator(authorities);
		factory.setPasswordAttribute("userPassword");
		return factory.createAuthenticationManager();
	}

	@Bean
	public UserDetailsService userDetailsService(LdapAuthoritiesPopulator authorities) {
		return new LdapUserDetailsService(new FilterBasedLdapUserSearch("", "(uid={0})", this.getDefaultSpringSecurityContextSource()), authorities);
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
		http.authorizeRequests( authz -> authz
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
