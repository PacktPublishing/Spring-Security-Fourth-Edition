package com.packtpub.springsecurity.dataaccess;

import java.util.List;

import com.packtpub.springsecurity.domain.CalendarUser;

import org.springframework.dao.EmptyResultDataAccessException;

/**
 * An interface for managing {@link CalendarUser} instances.
 *
 * @author Rob Winch
 * @author bassLahsen
 */
public interface CalendarUserDao {

	/**
	 * Gets a {@link CalendarUser} for a specific {@link CalendarUser#id()}.
	 *
	 * @param id the {@link CalendarUser#id()} of the {@link CalendarUser} to find.
	 * @return a {@link CalendarUser} for the given id. Cannot be null.
	 * @throws EmptyResultDataAccessException if the {@link CalendarUser} cannot be found
	 */
	CalendarUser getUser(int id);

	/**
	 * Finds a given {@link CalendarUser} by email address.
	 *
	 * @param email the email address to use to find a {@link CalendarUser}. Cannot be null.
	 * @return a {@link CalendarUser} for the given email or null if one could not be found.
	 * @throws IllegalArgumentException if email is null.
	 */
	CalendarUser findUserByEmail(String email);


	/**
	 * Finds any {@link CalendarUser} that has an email that starts with {@code partialEmail}.
	 *
	 * @param partialEmail the email address to use to find {@link CalendarUser}s. Cannot be null or empty String.
	 * @return a List of {@link CalendarUser}s that have an email that starts with given partialEmail. The returned value         will never be null. If no results are found an empty List will be returned.
	 * @throws IllegalArgumentException if email is null or empty String.
	 */
	List<CalendarUser> findUsersByEmail(String partialEmail);

	/**
	 * Creates a new {@link CalendarUser}.
	 *
	 * @param user the new {@link CalendarUser} to create. The {@link CalendarUser#id()} must be null.
	 * @return the new {@link CalendarUser#id()}.
	 * @throws IllegalArgumentException if {@link CalendarUser#id()} is non-null.
	 */
	int createUser(CalendarUser user);
}
