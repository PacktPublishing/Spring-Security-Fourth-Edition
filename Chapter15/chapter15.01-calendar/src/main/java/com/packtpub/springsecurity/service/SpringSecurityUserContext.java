package com.packtpub.springsecurity.service;

import com.packtpub.springsecurity.domain.CalendarUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * An implementation of {@link UserContext} that looks up the {@link CalendarUser} using the Spring Security's
 * {@link Authentication} by principal name.
 *
 * @author bnasslahsen
 */
@Component
public class SpringSecurityUserContext implements UserContext {

	private static final Logger logger = LoggerFactory.getLogger(SpringSecurityUserContext.class);

	private final CalendarService calendarService;

	public SpringSecurityUserContext(final CalendarService calendarService) {
		this.calendarService = calendarService;
	}

	/**
	 * Get the {@link CalendarUser} by obtaining the currently logged in Spring Security user's
	 * {@link Authentication#getName()} and using that to find the {@link CalendarUser} by email address (since for our
	 * application Spring Security usernames are email addresses).
	 */
	@Override
	public CalendarUser getCurrentUser() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		if (authentication == null) {
			return null;
		}

		CalendarUser user = (CalendarUser) authentication.getPrincipal();
		String email = user.getEmail();
		if (email == null) {
			return null;
		}
		CalendarUser result = calendarService.findUserByEmail(email);
		if (result == null) {
			throw new IllegalStateException(
					"Spring Security is not in synch with CalendarUsers. Could not find user with email " + email);
		}

		logger.info("CalendarUser: {}", result);
		return result;
	}

}
