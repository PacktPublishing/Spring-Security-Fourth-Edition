package com.packtpub.springsecurity.service;

import java.util.List;

import com.packtpub.springsecurity.dataaccess.CalendarUserDao;
import com.packtpub.springsecurity.dataaccess.EventDao;
import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.domain.Event;

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
	 * Instantiates a new Default calendar service.
	 *
	 * @param eventDao the event dao
	 * @param userDao  the user dao
	 */
	public DefaultCalendarService(EventDao eventDao, CalendarUserDao userDao) {
		if (eventDao == null) {
			throw new IllegalArgumentException("eventDao cannot be null");
		}
		if (userDao == null) {
			throw new IllegalArgumentException("userDao cannot be null");
		}
		this.eventDao = eventDao;
		this.userDao = userDao;
	}

	/**
	 * Gets event.
	 *
	 * @param eventId the event id
	 * @return the event
	 */
	public Event getEvent(int eventId) {
		return eventDao.getEvent(eventId);
	}

	/**
	 * Create event int.
	 *
	 * @param event the event
	 * @return the int
	 */
	public int createEvent(Event event) {
		return eventDao.createEvent(event);
	}

	/**
	 * Find for user list.
	 *
	 * @param userId the user id
	 * @return the list
	 */
	public List<Event> findForUser(int userId) {
		return eventDao.findForUser(userId);
	}

	/**
	 * Gets events.
	 *
	 * @return the events
	 */
	public List<Event> getEvents() {
		return eventDao.getEvents();
	}

	/**
	 * Gets user.
	 *
	 * @param id the id
	 * @return the user
	 */
	public CalendarUser getUser(int id) {
		return userDao.getUser(id);
	}

	/**
	 * Find user by email calendar user.
	 *
	 * @param email the email
	 * @return the calendar user
	 */
	public CalendarUser findUserByEmail(String email) {
		return userDao.findUserByEmail(email);
	}

	/**
	 * Find users by email list.
	 *
	 * @param partialEmail the partial email
	 * @return the list
	 */
	public List<CalendarUser> findUsersByEmail(String partialEmail) {
		return userDao.findUsersByEmail(partialEmail);
	}

	/**
	 * Create user int.
	 *
	 * @param user the user
	 * @return the int
	 */
	public int createUser(CalendarUser user) {
		return userDao.createUser(user);
	}
}