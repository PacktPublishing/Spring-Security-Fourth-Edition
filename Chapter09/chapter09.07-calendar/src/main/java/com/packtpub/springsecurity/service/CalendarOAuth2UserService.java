package com.packtpub.springsecurity.service;

import com.packtpub.springsecurity.domain.CalendarUser;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 *  @author bnasslahsen
 */
@Component
public class CalendarOAuth2UserService implements OAuth2UserService {

	private final CalendarService calendarService;

	public CalendarOAuth2UserService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
		OAuth2User user = delegate.loadUser(userRequest);
		String email = user.getAttribute("email");
		CalendarUser calendarUser = calendarService.findUserByEmail(email);

		if (calendarUser ==null) {
			calendarUser = new CalendarUser();
			calendarUser.setEmail(email);
			calendarUser.setProvider(userRequest.getClientRegistration().getRegistrationId());
			if ("github".equals(userRequest.getClientRegistration().getRegistrationId())) {
				calendarUser.setExternalId(user.getAttribute("id").toString());
				calendarUser.setFirstName( user.getAttribute("name"));
				calendarUser.setLastName(user.getAttribute("name"));
			}
			calendarService.createUser(calendarUser);
		}
		return user;
	}
}
