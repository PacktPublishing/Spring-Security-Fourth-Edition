package com.packtpub.springsecurity.configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.saml2.provider.service.authentication.OpenSaml4AuthenticationProvider;
import org.springframework.security.saml2.provider.service.authentication.OpenSaml4AuthenticationProvider.ResponseToken;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * Spring Security Config Class
 *
 * @since chapter010.00
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
						.requestMatchers(antMatcher("/webjars/**")).permitAll()
						.requestMatchers(antMatcher("/css/**")).permitAll()
						.requestMatchers(antMatcher("/favicon.ico")).permitAll()
						// H2 console:
						.requestMatchers(antMatcher("/admin/h2/**")).access("isFullyAuthenticated()")
						.requestMatchers(antMatcher("/")).permitAll()
						.requestMatchers(antMatcher("/login/*")).permitAll()
						.requestMatchers(antMatcher("/logout")).permitAll()
						.requestMatchers(antMatcher("/signup/*")).permitAll()
						.requestMatchers(antMatcher("/errors/**")).permitAll()
						.requestMatchers(antMatcher("/events/")).hasRole("ADMIN")
						.requestMatchers(antMatcher("/**")).hasAuthority("Everyone"))

				.exceptionHandling(exceptions -> exceptions
						.accessDeniedPage("/errors/403"))

				.logout(form -> form
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.permitAll())
				// CSRF is enabled by default, with Java Config
				.csrf(AbstractHttpConfigurer::disable);

		OpenSaml4AuthenticationProvider authenticationProvider = new OpenSaml4AuthenticationProvider();
		authenticationProvider.setResponseAuthenticationConverter(groupsConverter());

		// @formatter:off
		http
				.saml2Login(saml2 -> saml2
						.authenticationManager(new ProviderManager(authenticationProvider)))
				.saml2Logout(withDefaults());
		// For H2 Console
		http.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable));
		return http.build();
	}

	private Converter<ResponseToken, Saml2Authentication> groupsConverter() {
		Converter<ResponseToken, Saml2Authentication> delegate =
				OpenSaml4AuthenticationProvider.createDefaultResponseAuthenticationConverter();
		return (responseToken) -> {
			Saml2Authentication authentication = delegate.convert(responseToken);
			Saml2AuthenticatedPrincipal principal = (Saml2AuthenticatedPrincipal) authentication.getPrincipal();
			List<String> groups = principal.getAttribute("groups");
			Set<GrantedAuthority> authorities = new HashSet<>();
			if (groups != null) {
				groups.stream().map(SimpleGrantedAuthority::new).forEach(authorities::add);
			} else {
				authorities.addAll(authentication.getAuthorities());
			}
			return new Saml2Authentication(principal, authentication.getSaml2Response(), authorities);
		};
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
