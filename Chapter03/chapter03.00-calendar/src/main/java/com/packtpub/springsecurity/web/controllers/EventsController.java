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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * The type Events controller.
 *
 *  @author bnasslahsen
 */
@Controller
@RequestMapping("/events")
public class EventsController {

	/**
	 * The Calendar service.
	 */
	private final CalendarService calendarService;

	/**
	 * The User context.
	 */
	private final UserContext userContext;

	/**
	 * Instantiates a new Events controller.
	 *
	 * @param calendarService the calendar service
	 * @param userContext     the user context
	 */
	public EventsController(CalendarService calendarService, UserContext userContext) {
		this.calendarService = calendarService;
		this.userContext = userContext;
	}

	/**
	 * Events model and view.
	 *
	 * @return the model and view
	 */
	@RequestMapping("/")
	public ModelAndView events() {
		return new ModelAndView("events/list", "events", calendarService.getEvents());
	}

	/**
	 * My events model and view.
	 *
	 * @return the model and view
	 */
	@RequestMapping("/my")
	public ModelAndView myEvents() {
		CalendarUser currentUser = userContext.getCurrentUser();
		Integer currentUserId = currentUser.getId();
		ModelAndView result = new ModelAndView("events/my", "events", calendarService.findForUser(currentUserId));
		result.addObject("currentUser", currentUser);
		return result;
	}

	/**
	 * Show model and view.
	 *
	 * @param eventId the event id
	 * @return the model and view
	 */
	@RequestMapping("/{eventId}")
	public ModelAndView show(@PathVariable int eventId) {
		Event event = calendarService.getEvent(eventId);
		return new ModelAndView("events/show", "event", event);
	}

	/**
	 * Create event form string.
	 *
	 * @param createEventForm the create event form
	 * @return the string
	 */
	@RequestMapping("/form")
	public String createEventForm(@ModelAttribute CreateEventForm createEventForm) {
		return "events/create";
	}

	/**
	 * Populates the form for creating an event with valid information. Useful so that users do not have to think when
	 * filling out the form for testing.
	 *
	 * @param createEventForm the create event form
	 * @return string string
	 */
	@RequestMapping(value = "/new", params = "auto")
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

	/**
	 * Create event string.
	 *
	 * @param createEventForm    the create event form
	 * @param result             the result
	 * @param redirectAttributes the redirect attributes
	 * @return the string
	 */
	@RequestMapping(value = "/new", method = RequestMethod.POST)
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
		Event event = new Event(null, createEventForm.getSummary(), createEventForm.getDescription(), createEventForm.getWhen(), userContext.getCurrentUser(), attendee);
		calendarService.createEvent(event);
		redirectAttributes.addFlashAttribute("message", "Successfully added the new event");
		return "redirect:/events/my";
	}
}