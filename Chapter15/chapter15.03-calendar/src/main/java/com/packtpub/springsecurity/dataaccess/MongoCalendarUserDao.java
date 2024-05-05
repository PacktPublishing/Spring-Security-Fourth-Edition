package com.packtpub.springsecurity.dataaccess;

import java.util.UUID;

import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.domain.Role;
import com.packtpub.springsecurity.repository.CalendarUserRepository;
import com.packtpub.springsecurity.repository.RoleRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

/**
 * A jdbc implementation of {@link CalendarUserDao}.
 *
 * @author bnasslahsen
 */
@Repository
public class MongoCalendarUserDao implements CalendarUserDao {

	private final CalendarUserRepository repository;

	private final RoleRepository roleRepository;

	private final CalendarUserRepository calendarUserRepository;

	public MongoCalendarUserDao(CalendarUserRepository repository, RoleRepository roleRepository, CalendarUserRepository calendarUserRepository) {
		this.repository = repository;
		this.roleRepository = roleRepository;
		this.calendarUserRepository = calendarUserRepository;
	}

	@Override
	public Mono<CalendarUser> getUser(UUID id) {
		return repository.findById(id);
	}

	@Override
	public Mono<CalendarUser> findUserByEmail(String email) {
		return repository.findByEmail(email);
	}

	@Override
	public Flux<CalendarUser> findUsersByEmail(String email) {
		if (email == null || email.isEmpty()) {
			return Flux.empty();
		}
		return repository.findAll();
	}

	@Override
	public Mono<UUID> createUser(CalendarUser userToAdd) {
		if (userToAdd == null) {
			return Mono.error(new IllegalArgumentException("userToAdd cannot be null"));
		}
		Mono<Role> roleMono = roleRepository.findById(0);
		// Subscribe to the roleMono to get the role and then save the user
		return roleMono.flatMap(role -> {
			userToAdd.getRoles().add(role);
			return repository.save(userToAdd).map(CalendarUser::getId);
		});
	}

	@Override
	public Mono<CalendarUser> getAttendee(UUID userId) {
		return calendarUserRepository.findByIdNot(userId).next();
	}
}