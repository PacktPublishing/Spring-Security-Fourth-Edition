package com.packtpub.springsecurity.dataaccess;

import java.util.UUID;

import com.packtpub.springsecurity.domain.Event;
import com.packtpub.springsecurity.repository.EventRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
public class MongoEventDao implements EventDao {

	private final EventRepository repository;

	public MongoEventDao(EventRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Event> getEvent(UUID eventId) {
		return repository.findById(eventId);
	}

	@Override
	public Mono<UUID> createEvent(Event event) {
		if (event == null) {
			return Mono.error(new IllegalArgumentException("event cannot be null"));
		}
		if (event.getOwner() == null || event.getAttendee() == null || event.getDateWhen() == null) {
			return Mono.error(new IllegalArgumentException("event.getOwner(), event.getAttendee(), and event.getDateWhen() cannot be null"));
		}

		return repository.save(event).map(Event::getId);
	}

	@Override
	public Flux<Event> findForUser(UUID userId) {
		return repository.findByUser(userId);
	}

	@Override
	public Flux<Event> getEvents() {
		return repository.findAll();
	}
}
