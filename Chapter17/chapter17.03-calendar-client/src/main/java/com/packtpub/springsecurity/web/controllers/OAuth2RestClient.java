package com.packtpub.springsecurity.web.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
@RestController
public class OAuth2RestClient {

	private final RestTemplate oauth2RestTemplate	;

	public OAuth2RestClient(RestTemplate oauth2RestTemplate) {
		this.oauth2RestTemplate = oauth2RestTemplate;
	}
	
    @Value("${jbcp-calendar.events.api}")
    private String eventsApi;
	
	@GetMapping("/")
    public  String apiCheck() {
		return oauth2RestTemplate.getForObject(eventsApi, String.class);
    }

} 
