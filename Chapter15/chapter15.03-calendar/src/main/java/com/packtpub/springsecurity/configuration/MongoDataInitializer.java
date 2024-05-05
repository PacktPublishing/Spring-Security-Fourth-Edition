package com.packtpub.springsecurity.configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.domain.Event;
import com.packtpub.springsecurity.domain.Role;
import com.packtpub.springsecurity.repository.CalendarUserRepository;
import com.packtpub.springsecurity.repository.EventRepository;
import com.packtpub.springsecurity.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Configuration;


/**
 * Initialize the initial data in the MongoDb
 * This replaces data.sql
 */
@Configuration
public class MongoDataInitializer {

	private static final Logger logger = LoggerFactory.getLogger(MongoDataInitializer.class);

	private RoleRepository roleRepository;

	private CalendarUserRepository calendarUserRepository;

	private EventRepository eventRepository;

	public MongoDataInitializer(RoleRepository roleRepository, CalendarUserRepository calendarUserRepository, EventRepository eventRepository) {
		this.roleRepository = roleRepository;
		this.calendarUserRepository = calendarUserRepository;
		this.eventRepository = eventRepository;
	}

	@PostConstruct
	public void setUp() {
		logger.info("*******************************************************");
		logger.info("clean the database"); calendarUserRepository.deleteAll();
		roleRepository.deleteAll(); eventRepository.deleteAll();
		logger.info("seedRoles"); seedRoles();
		logger.info("seedCalendarUsers"); seedCalendarUsers(); logger.info("seedEvents");
		seedEvents();
		logger.info("*******************************************************");
	}

	CalendarUser user, admin, user2;

	// CalendarUsers
	{
		user = new CalendarUser(UUID.randomUUID(), "user1@example.com", "$2a$04$qr7RWyqOnWWC1nwotUW1nOe1RD5.mKJVHK16WZy6v49pymu1WDHmi", "User", "1");
		admin = new CalendarUser(UUID.randomUUID(), "admin1@example.com", "$2a$04$0CF/Gsquxlel3fWq5Ic/ZOGDCaXbMfXYiXsviTNMQofWRXhvJH3IK", "Admin", "1");
		user2 = new CalendarUser(UUID.randomUUID(), "user2@example.com", "$2a$04$PiVhNPAxunf0Q4IMbVeNIuH4M4ecySWHihyrclxW..PLArjLbg8CC", "User2", "2");

	}

	Role user_role, admin_role;

	/**
	 * -- ROLES --
	 * insert into role(id, name) values (0, "ROLE_USER");
	 * insert into role(id, name) values (1, "ROLE_ADMIN");
	 */
	private void seedRoles() {
		user_role = new Role(0, "ROLE_USER"); 
		admin_role = new Role(1, "ROLE_ADMIN");
		Mono<Role> saveUserRoleMono = roleRepository.save(user_role);
		Mono<Role> saveAdminRoleMono = roleRepository.save(admin_role);
		// Subscribing to the Monos to trigger the saving operations
		saveUserRoleMono.subscribe(savedUserRole -> {
			// Handle savedUserRole if needed
			System.out.println("User role saved: " + savedUserRole);
		});
		saveAdminRoleMono.subscribe(savedAdminRole -> {
			// Handle savedAdminRole if needed
			System.out.println("Admin role saved: " + savedAdminRole);
		});
	}


	/**
	 * Seed initial events
	 */
	private void seedEvents() {

		// Event 1
		Event event1 = new Event(UUID.randomUUID(), "Birthday Party", "This is going to be a great birthday", LocalDateTime.of(2023, 6, 3, 6, 36, 00), user, admin);

		// Event 2
		Event event2 = new Event(UUID.randomUUID(), "Conference Call", "Call with the client", LocalDateTime.of(2023, 11, 23, 13, 00, 00), user2, user);

		// Event 3
		Event event3 = new Event(UUID.randomUUID(), "Vacation", "Paragliding in Greece", LocalDateTime.of(2023, 8, 14, 11, 30, 00), admin, user2);

		// Saving events
		Mono<Event> saveEvent1Mono = eventRepository.save(event1);
		Mono<Event> saveEvent2Mono = eventRepository.save(event2);
		Mono<Event> saveEvent3Mono = eventRepository.save(event3);

		// Subscribing to the Monos to trigger the saving operations
		saveEvent1Mono.subscribe(savedEvent1 -> {
			// Handle savedEvent1 if needed
			System.out.println("Event 1 saved: " + savedEvent1);
		});

		saveEvent2Mono.subscribe(savedEvent2 -> {
			// Handle savedEvent2 if needed
			System.out.println("Event 2 saved: " + savedEvent2);
		});

		saveEvent3Mono.subscribe(savedEvent3 -> {
			// Handle savedEvent3 if needed
			System.out.println("Event 3 saved: " + savedEvent3);
		});

		Flux<Event> events = eventRepository.findAll();

		logger.info("Events: {}", events);

	}


	private void seedCalendarUsers() {

		// user1
		user.addRole(user_role);

		// admin2
		admin.addRole(user_role); admin.addRole(admin_role);

		// user2
		user2.addRole(user_role);

		// CalendarUser
		Mono<CalendarUser> saveUser1Mono = calendarUserRepository.save(user);
		Mono<CalendarUser> saveAdminMono = calendarUserRepository.save(admin);
		Mono<CalendarUser> saveUser2Mono = calendarUserRepository.save(user2);

		saveUser1Mono.subscribe(savedUser1 -> {
			// Handle savedUser1 if needed
			System.out.println("User 1 saved: " + savedUser1);
		});

		saveAdminMono.subscribe(savedAdmin -> {
			// Handle savedAdmin if needed
			System.out.println("Admin saved: " + savedAdmin);
		});

		saveUser2Mono.subscribe(savedUser2 -> {
			// Handle savedUser2 if needed
			System.out.println("User 2 saved: " + savedUser2);

		});

	}

}
