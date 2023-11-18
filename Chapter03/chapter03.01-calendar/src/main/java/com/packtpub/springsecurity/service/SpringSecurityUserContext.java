package com.packtpub.springsecurity.service;

import com.packtpub.springsecurity.domain.CalendarUser;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * An implementation of {@link UserContext} that looks up the {@link CalendarUser} using the Spring Security's
 * {@link Authentication} by principal name.
 *
 *  @author bnasslahsen
 */
@Component
public class SpringSecurityUserContext implements UserContext {

	/**
	 * The Calendar service.
	 */
	private final CalendarService calendarService;

	/**
	 * The User details service.
	 */
	private final UserDetailsService userDetailsService;

	/**
	 * Instantiates a new Spring security user context.
	 *
	 * @param calendarService    the calendar service
	 * @param userDetailsService the user details service
	 */
	public SpringSecurityUserContext(final CalendarService calendarService,
			final UserDetailsService userDetailsService) {
		if (calendarService == null) {
			throw new IllegalArgumentException("calendarService cannot be null");
		}
		if (userDetailsService == null) {
			throw new IllegalArgumentException("userDetailsService cannot be null");
		}
		this.calendarService = calendarService;
		this.userDetailsService = userDetailsService;
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

		User user = (User) authentication.getPrincipal();
		String email = user.getUsername();

		if (email == null) {
			return null;
		}

		CalendarUser result = calendarService.findUserByEmail(email);
		if (result == null) {
			throw new IllegalStateException(
					"Spring Security is not in synch with CalendarUsers. Could not find user with email " + email);
		}
		return result;
	}

	@Override
	public void setCurrentUser(CalendarUser user) {
		throw new UnsupportedOperationException();
	}
}
