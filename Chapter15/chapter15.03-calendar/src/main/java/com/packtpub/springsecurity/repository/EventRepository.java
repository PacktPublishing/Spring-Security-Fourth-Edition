package com.packtpub.springsecurity.repository;

import java.util.UUID;

import com.packtpub.springsecurity.domain.Event;
import reactor.core.publisher.Flux;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface EventRepository extends ReactiveMongoRepository<Event, UUID> {

	@Query("{'owner.id' : ?0}")
	Flux<Event> findByUser(UUID name);
}