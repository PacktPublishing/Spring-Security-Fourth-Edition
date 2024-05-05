package com.packtpub.springsecurity.web.controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.domain.Event;
import com.packtpub.springsecurity.service.CalendarService;
import com.packtpub.springsecurity.service.UserContext;
import com.packtpub.springsecurity.web.model.CreateEventForm;
import org.thymeleaf.util.StringUtils;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/events")
public class EventsController {

	private final CalendarService calendarService;

	private final UserContext userContext;

	@Autowired
	public EventsController(CalendarService calendarService, UserContext userContext) {
		this.calendarService = calendarService;
		this.userContext = userContext;
	}

	@GetMapping("/")
	public String events(Model model) {
		model.addAttribute("events", calendarService.getEvents());
		return "events/list";
	}

	@GetMapping("/my")
	public Mono<String> myEvents(Model model) {
		return userContext.getCurrentUser()
				.flatMap(currentUser -> {
					UUID currentUserId = currentUser.getId();
					return calendarService.findForUser(currentUserId)
							.collectList()
							.doOnNext(events -> {
								model.addAttribute("events", events);
								model.addAttribute("currentUser", currentUser);
							})
							.thenReturn("events/my");
				})
				.switchIfEmpty(Mono.just("events/my"));
	}

	@GetMapping("/{eventId}")
	public Mono<String> show(@PathVariable UUID eventId, Model model) {
		return calendarService.getEvent(eventId)
				.doOnNext(event -> model.addAttribute("event", event))
				.thenReturn("events/show");
	}

	@GetMapping("/form")
	public String createEventForm(@ModelAttribute CreateEventForm createEventForm) {
		return "events/create";
	}
	
	@PostMapping(value = "/new")
	public Mono<String> createEvent(@ModelAttribute CreateEventForm createEventForm, BindingResult result) {
		if (!StringUtils.isEmpty(createEventForm.getAuto())) {
			createEventForm.setSummary("A new event....");
			createEventForm.setDescription("This was autopopulated to save time creating a valid event.");
			createEventForm.setWhen(LocalDateTime.now());

			// make the attendee not the current user
			return userContext.getCurrentUser()
					.flatMap(currentUser -> {
						UUID userId = currentUser.getId();
						return calendarService.getAttendee(userId);
					})
					.flatMap(attendee -> {
						createEventForm.setAttendeeEmail(attendee.getEmail());
						result.getModel().clear();
						return Mono.just("events/create");
					});
		}

		Mono<CalendarUser> attendeeMono = calendarService.findUserByEmail(createEventForm.getAttendeeEmail());
		attendeeMono.doOnNext(attendee -> {
			if (attendee == null) {
				result.rejectValue("attendeeEmail", "attendeeEmail.missing",
						"Could not find a user for the provided Attendee Email");
			}
		}).subscribe();

		return userContext.getCurrentUser()
				.flatMap(currentUser -> {
					return calendarService.findUserByEmail(createEventForm.getAttendeeEmail())
							.flatMap(attendees -> {
								Event event = new Event();
								event.setAttendee(attendees);
								event.setDescription(createEventForm.getDescription());
								event.setOwner(currentUser);
								event.setSummary(createEventForm.getSummary());
								event.setDateWhen(createEventForm.getWhen());
								return calendarService.createEvent(event);
							})
							.then(Mono.just("redirect:/events/my"));
				});
	}
}