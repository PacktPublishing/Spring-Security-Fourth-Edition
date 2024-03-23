package com.packtpub.springsecurity.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Spring Security Config Class
 *
 * @since chapter17.00
 */
@Configuration
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.oauth2Client(withDefaults());
		return http.build();
	}

	@Bean
	public RestTemplate oauth2RestTemplate(OAuth2HttpRequestInterceptor oAuth2HttpRequestInterceptor) {
		RestTemplate restTemplate = new RestTemplate();
		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
		if (CollectionUtils.isEmpty(interceptors)) {
			interceptors = new ArrayList<>();
		}
		interceptors.add(oAuth2HttpRequestInterceptor);
		restTemplate.setInterceptors(interceptors);
		return restTemplate;
	}
}
