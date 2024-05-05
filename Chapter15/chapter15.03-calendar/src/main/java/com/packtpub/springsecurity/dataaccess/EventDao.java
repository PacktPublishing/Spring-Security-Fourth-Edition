package com.packtpub.springsecurity.dataaccess;

import java.util.List;
import java.util.UUID;

import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.domain.Event;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * An interface for managing {@link Event}'s.
 *
 *  @author bnasslahsen
 */
public interface EventDao {

	/**
	 * Given an id gets an {@link Event}.
	 *
	 * @param eventId the {@link Event#getId()}
	 * @return the {@link Event}. Cannot be null.
	 * @throws RuntimeException if the {@link Event} cannot be found.
	 */
	Mono<Event> getEvent(UUID eventId);

	/**
	 * Creates a {@link Event} and returns the new id for that {@link Event}.
	 *
	 * @param event the {@link Event} to create. Note that the {@link Event#getId()} should be null.
	 * @return the new id for the {@link Event}
	 * @throws RuntimeException if {@link Event#getId()} is non-null.
	 */
	Mono<UUID> createEvent(Event event);

	/**
	 * Finds the {@link Event}'s that are intended for the {@link CalendarUser}.
	 *
	 * @param userId the {@link CalendarUser#getId()} to obtain {@link Event}'s for.
	 * @return a non-null {@link List} of {@link Event}'s intended for the specified {@link CalendarUser}. If the
	 * {@link CalendarUser} does not exist an empty List will be returned.
	 */
	Flux<Event> findForUser(UUID userId);

	/**
	 * Gets all the available {@link Event}'s.
	 *
	 * @return a non-null {@link List} of {@link Event}'s
	 */
	Flux<Event> getEvents();
}
