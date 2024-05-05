package com.packtpub.springsecurity.service;

import java.util.UUID;

import com.packtpub.springsecurity.dataaccess.CalendarUserDao;
import com.packtpub.springsecurity.dataaccess.EventDao;
import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.domain.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

/**
 * A default implementation of {@link CalendarService} that delegates to {@link EventDao} and {@link CalendarUserDao}.
 *
 * @author bnasslahsen
 *
 */
@Repository
public class DefaultCalendarService implements CalendarService {

	private final EventDao eventDao;
	private final CalendarUserDao userDao;
	private final PasswordEncoder passwordEncoder;

	public DefaultCalendarService(final EventDao eventDao,
			final CalendarUserDao userDao,
			final PasswordEncoder passwordEncoder) {
		if (eventDao == null) {
			throw new IllegalArgumentException("eventDao cannot be null");
		}
		if (userDao == null) {
			throw new IllegalArgumentException("userDao cannot be null");
		}
		if (passwordEncoder == null) {
			throw new IllegalArgumentException("passwordEncoder cannot be null");
		}
		this.eventDao = eventDao;
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Mono<Event> getEvent(UUID eventId) {
		return eventDao.getEvent(eventId);
	}

	@Override
	public Mono<UUID> createEvent(Event event) {
		return eventDao.createEvent(event);
	}

	@Override
	public Flux<Event> findForUser(UUID userId) {
		return eventDao.findForUser(userId);
	}

	@Override
	public Flux<Event> getEvents() {
		return eventDao.getEvents();
	}

	@Override
	public Mono<CalendarUser> getUser(UUID id) {
		return userDao.getUser(id);
	}

	@Override
	public Mono<CalendarUser> getAttendee(UUID userId) {
		return userDao.getAttendee(userId);
	}

	@Override
	public Mono<CalendarUser> findUserByEmail(String email) {
		return userDao.findUserByEmail(email);
	}


	@Override
	public Flux<CalendarUser> findUsersByEmail(String partialEmail) {
		return userDao.findUsersByEmail(partialEmail);
	}
	/**
	 * Create a new Signup User
	 *
	 * @param user the new {@link CalendarUser} to create. The {@link CalendarUser#getId()} must be null.
	 * @return
	 */
	public Mono<UUID> createUser(CalendarUser user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		return userDao.createUser(user);
	}


}
