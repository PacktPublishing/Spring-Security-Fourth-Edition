package com.packtpub.springsecurity.web.controllers;

import java.util.List;

import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.domain.Event;
import com.packtpub.springsecurity.service.CalendarService;
import com.packtpub.springsecurity.service.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventsController {

	private static final Logger logger = LoggerFactory
			.getLogger(EventsController.class);
	
	private final CalendarService calendarService;

	private final UserContext userContext;

	public EventsController(CalendarService calendarService, UserContext userContext) {
		this.calendarService = calendarService;
		this.userContext = userContext;
	}

	@GetMapping("/")
	public List<Event> events() {
		logger.info("events()" + calendarService.getEvents());
		return calendarService.getEvents();
	}

	@GetMapping("/my")
	public List<Event> myEvents() {
		CalendarUser currentUser = userContext.getCurrentUser();
		Integer currentUserId = currentUser.getId();
		return calendarService.findForUser(currentUserId);
	}

	@GetMapping("/{eventId}")
	public Event show(@PathVariable int eventId) {
		return calendarService.getEvent(eventId);
	}
	
}