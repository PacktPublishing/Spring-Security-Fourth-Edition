package com.packtpub.springsecurity.web.controllers;

import java.util.Calendar;

import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.domain.Event;
import com.packtpub.springsecurity.service.CalendarService;
import com.packtpub.springsecurity.service.UserContext;
import com.packtpub.springsecurity.web.model.CreateEventForm;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/events")
public class EventsController {
	    private final CalendarService calendarService;

	private final UserContext userContext;

	public EventsController(CalendarService calendarService, UserContext userContext) {
		this.calendarService = calendarService;
		this.userContext = userContext;
	}

	@GetMapping({"/", "/backdoor"})
	public ModelAndView events() {
		return new ModelAndView("events/list", "events", calendarService.getEvents());
	}

	@GetMapping("/my")
	public ModelAndView myEvents() {
		CalendarUser currentUser = userContext.getCurrentUser();
		Integer currentUserId = currentUser.getId();
		ModelAndView result = new ModelAndView("events/my", "events", calendarService.findForUser(currentUserId));
		result.addObject("currentUser", currentUser);
		return result;
	}
	
	@GetMapping(path ="/my", params="userId")
	public ModelAndView myEvents(@RequestParam int userId) {
		CalendarUser currentUser = calendarService.getUser(userId);
		ModelAndView result = new ModelAndView("events/my", "events", calendarService.findForUser(userId));
		result.addObject("currentUser", currentUser);
		return result;
	}

	@GetMapping("/{eventId}")
	public ModelAndView show(@PathVariable int eventId) {
		Event event = calendarService.getEvent(eventId);
		return new ModelAndView("events/show", "event", event);
	}

	@GetMapping("/form")
	public String createEventForm(@ModelAttribute CreateEventForm createEventForm) {
		return "events/create";
	}

	/**
	 * Populates the form for creating an event with valid information. Useful so that users do not have to think when
	 * filling out the form for testing.
	 *
	 * @param createEventForm
	 * @return
	 */
	@PostMapping(value = "/new", params = "auto")
	public String createEventFormAutoPopulate(@ModelAttribute CreateEventForm createEventForm) {
		// provide default values to make user submission easier
		createEventForm.setSummary("A new event....");
		createEventForm.setDescription("This was autopopulated to save time creating a valid event.");
		createEventForm.setWhen(Calendar.getInstance());

		// make the attendee not the current user
		CalendarUser currentUser = userContext.getCurrentUser();
		int attendeeId = currentUser.getId() == 0 ? 1 : 0;
		CalendarUser attendee = calendarService.getUser(attendeeId);
		createEventForm.setAttendeeEmail(attendee.getEmail());

		return "events/create";
	}

	@PostMapping(value = "/new")
	public String createEvent(@Valid CreateEventForm createEventForm, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "events/create";
		}
		CalendarUser attendee = calendarService.findUserByEmail(createEventForm.getAttendeeEmail());
		if (attendee == null) {
			result.rejectValue("attendeeEmail", "attendeeEmail.missing",
					"Could not find a user for the provided Attendee Email");
		}
		if (result.hasErrors()) {
			return "events/create";
		}
		Event event = new Event();
		event.setAttendee(attendee);
		event.setDescription(createEventForm.getDescription());
		event.setOwner(userContext.getCurrentUser());
		event.setSummary(createEventForm.getSummary());
		event.setDateWhen(createEventForm.getWhen());
		calendarService.createEvent(event);
		redirectAttributes.addFlashAttribute("message", "Successfully added the new event");
		return "redirect:/events/my";
	}
}