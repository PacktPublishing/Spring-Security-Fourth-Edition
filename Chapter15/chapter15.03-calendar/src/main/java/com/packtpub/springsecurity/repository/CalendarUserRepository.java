package com.packtpub.springsecurity.repository;

import java.util.UUID;

import com.packtpub.springsecurity.domain.CalendarUser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CalendarUserRepository extends ReactiveMongoRepository<CalendarUser, UUID> {
	Mono<CalendarUser> findByEmail(String email);

	Flux<CalendarUser> findByIdNot(UUID userId);

}
