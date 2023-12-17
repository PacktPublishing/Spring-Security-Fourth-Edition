package com.packtpub.springsecurity.service;

import com.packtpub.springsecurity.domain.CalendarUser;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

/**
 * @author bnasslahsen
 */
@Component
public class CalendarOidcUserService extends OidcUserService {

	private final CalendarService calendarService;

	public CalendarOidcUserService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}

	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser user = super.loadUser(userRequest);
		String email = user.getEmail();
		CalendarUser calendarUser = calendarService.findUserByEmail(email);
		if (calendarUser == null) {
			calendarUser = new CalendarUser();
			calendarUser.setEmail(email);
			calendarUser.setProvider(userRequest.getClientRegistration().getRegistrationId());
			calendarUser.setExternalId(user.getAttribute("sub"));
			calendarUser.setFirstName(user.getGivenName());
			calendarUser.setLastName(user.getFamilyName());
			calendarService.createUser(calendarUser);
		}
		return user;
	}
}
