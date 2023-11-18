package com.packtpub.springsecurity.service;

import com.packtpub.springsecurity.dataaccess.CalendarUserDao;
import com.packtpub.springsecurity.domain.CalendarUser;

import org.springframework.stereotype.Component;

/**
 * Returns the same user for every call to {@link #getCurrentUser()}. This is used prior to adding security, so that the
 * rest of the application can be used.
 *
 *  @author bnasslahsen
 */
@Component
public class UserContextStub implements UserContext {
	/**
	 * The User service.
	 */
	private final CalendarUserDao userService;

	/**
	 * The {@link CalendarUser#getId()} for the user that is representing the currently logged in user. This can be
	 * modified using {@link #setCurrentUser(CalendarUser)}
	 */
	private int currentUserId = 0;

	/**
	 * Instantiates a new User context stub.
	 *
	 * @param userService the user service
	 */
	public UserContextStub(CalendarUserDao userService) {
		if (userService == null) {
			throw new IllegalArgumentException("userService cannot be null");
		}
		this.userService = userService;
	}

	/**
	 * Gets current user.
	 *
	 * @return the current user
	 */
	@Override
	public CalendarUser getCurrentUser() {
		return userService.getUser(currentUserId);
	}

	/**
	 * Sets current user.
	 *
	 * @param user the user
	 */
	@Override
	public void setCurrentUser(CalendarUser user) {
		if (user == null) {
			throw new IllegalArgumentException("user cannot be null");
		}
		Integer currentId = user.getId();
		if (currentId == null) {
			throw new IllegalArgumentException("user.id() cannot be null");
		}
		this.currentUserId = currentId;
	}
}