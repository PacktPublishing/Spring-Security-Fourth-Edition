package com.packtpub.springsecurity.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * Spring Security Config Class
 *
 * @since chapter09.00
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
	public SecurityFilterChain filterChain(HttpSecurity http, 
			OAuth2AuthorizationRequestResolver pkceResolver) throws Exception {
		http.authorizeRequests(authz -> authz
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
						.requestMatchers(antMatcher("/**")).hasAnyAuthority("OIDC_USER", "OAUTH2_USER", "ROLE_USER"))

				.exceptionHandling(exceptions -> exceptions
						.accessDeniedPage("/errors/403"))

				.logout(form -> form
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login/form?logout")
						.permitAll())
				// CSRF is enabled by default, with Java Config
				.csrf(AbstractHttpConfigurer::disable);

		// For H2 Console
		http.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable));
		
		// OAuth2 Login
		http
				.oauth2Login(oauth2 -> oauth2
						.loginPage("/login/form")
						.authorizationEndpoint(authorization -> authorization.authorizationRequestResolver(pkceResolver))
						.defaultSuccessUrl("/default", true));
		return http.build();
	}

	@Bean
	public OAuth2AuthorizationRequestResolver pkceResolver(ClientRegistrationRepository clientRegistrationRepository) {
		DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
		resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());
		return resolver;
	}

}



