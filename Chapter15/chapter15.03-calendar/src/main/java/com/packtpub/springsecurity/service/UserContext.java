package com.packtpub.springsecurity.service;

import com.packtpub.springsecurity.domain.CalendarUser;
import reactor.core.publisher.Mono;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;

/**
 * Manages the current {@link CalendarUser}. This demonstrates how in larger applications it is good to abstract out
 * accessing the current user to return the application specific user rather than interacting with Spring Security
 * classes directly.
 *
 *  @author bnasslahsen
 */
public interface UserContext {

	/**
	 * Gets the currently logged in {@link CalendarUser} or null if there is no authenticated user.
	 *
	 * @return
	 */
	Mono<CalendarUser> getCurrentUser();

	/**
	 * Sets the currently logged in {@link CalendarUser}.
	 *
	 * @param user    the logged in {@link CalendarUser}. Cannot be null.
	 * @param session the session
	 * @return the current user
	 * @throws IllegalArgumentException if the {@link CalendarUser} is null.
	 */
	Mono<Void> setCurrentUser(CalendarUser user, WebSession session);
}
