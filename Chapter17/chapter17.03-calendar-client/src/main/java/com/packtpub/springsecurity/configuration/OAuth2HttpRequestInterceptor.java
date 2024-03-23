package com.packtpub.springsecurity.configuration;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;

/**
 * @author bnasslahsen
 */
@Component
public class OAuth2HttpRequestInterceptor implements ClientHttpRequestInterceptor {

	private final OAuth2AuthorizedClientManager authorizedClientManager;
	private final ClientRegistrationRepository clientRegistrationRepository;

	public OAuth2HttpRequestInterceptor(OAuth2AuthorizedClientManager authorizedClientManager, ClientRegistrationRepository clientRegistrationRepository) {
		this.authorizedClientManager = authorizedClientManager;
		this.clientRegistrationRepository = clientRegistrationRepository;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("calendar-client");
		OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
				.withClientRegistrationId(clientRegistration.getRegistrationId())
				.principal(clientRegistration.getClientId())
				.build();
		OAuth2AuthorizedClient client = authorizedClientManager.authorize(oAuth2AuthorizeRequest);
		String accessToken = client.getAccessToken().getTokenValue();	
		request.getHeaders().setBearerAuth(accessToken);
		return execution.execute(request, body);
	}
	
}