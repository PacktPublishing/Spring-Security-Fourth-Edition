package com.packtpub.springsecurity.service;

import com.packtpub.springsecurity.domain.CalendarUser;

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
		return (CalendarUser) authentication.getPrincipal();
	}

}
