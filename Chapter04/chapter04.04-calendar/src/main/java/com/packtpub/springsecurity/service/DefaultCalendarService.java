package com.packtpub.springsecurity.service;

import java.util.List;

import com.packtpub.springsecurity.dataaccess.CalendarUserDao;
import com.packtpub.springsecurity.dataaccess.EventDao;
import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.domain.Event;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

/**
 * A default implementation of {@link CalendarService} that delegates to {@link EventDao} and {@link CalendarUserDao}.
 *
 *  @author bnasslahsen
 */
@Repository
public class DefaultCalendarService implements CalendarService {
	/**
	 * The Event dao.
	 */
	private final EventDao eventDao;

	/**
	 * The User dao.
	 */
	private final CalendarUserDao userDao;

	/**
	 * The Jdbc operations.
	 */
	private final JdbcOperations jdbcOperations;

	/**
	 * The Password encoder.
	 */
	private final PasswordEncoder passwordEncoder;

	/**
	 * Instantiates a new Default calendar service.
	 *
	 * @param eventDao        the event dao
	 * @param userDao         the user dao
	 * @param jdbcOperations  the jdbc operations
	 * @param passwordEncoder the password encoder
	 */
	public DefaultCalendarService(final EventDao eventDao,
			final CalendarUserDao userDao,
			final JdbcOperations jdbcOperations,
			final PasswordEncoder passwordEncoder) {
		if (eventDao == null) {
			throw new IllegalArgumentException("eventDao cannot be null");
		}
		if (userDao == null) {
			throw new IllegalArgumentException("userDao cannot be null");
		}
		if (jdbcOperations == null) {
			throw new IllegalArgumentException("jdbcOperations cannot be null");
		}
		if (passwordEncoder == null) {
			throw new IllegalArgumentException("passwordEncoder cannot be null");
		}
		this.eventDao = eventDao;
		this.userDao = userDao;
		this.jdbcOperations = jdbcOperations;
		this.passwordEncoder = passwordEncoder;
	}

	public Event getEvent(int eventId) {
		return eventDao.getEvent(eventId);
	}

	public int createEvent(Event event) {
		return eventDao.createEvent(event);
	}

	public List<Event> findForUser(int userId) {
		return eventDao.findForUser(userId);
	}

	public List<Event> getEvents() {
		return eventDao.getEvents();
	}

	public CalendarUser getUser(int id) {
		return userDao.getUser(id);
	}

	public CalendarUser findUserByEmail(String email) {
		return userDao.findUserByEmail(email);
	}

	public List<CalendarUser> findUsersByEmail(String partialEmail) {
		return userDao.findUsersByEmail(partialEmail);
	}

	public int createUser(CalendarUser user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		int userId = userDao.createUser(user);
		jdbcOperations.update("insert into calendar_user_authorities(calendar_user,authority) values (?,?)", userId,
				"ROLE_USER");
		return userId;
	}
}