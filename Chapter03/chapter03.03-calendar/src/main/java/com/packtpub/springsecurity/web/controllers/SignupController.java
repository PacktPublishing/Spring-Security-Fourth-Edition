package com.packtpub.springsecurity.web.controllers;


import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.service.CalendarService;
import com.packtpub.springsecurity.service.UserContext;
import com.packtpub.springsecurity.web.model.SignupForm;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * The type Signup controller.
 */
@Controller
public class SignupController {

	/**
	 * The User context.
	 */
	private final UserContext userContext;

	/**
	 * The Calendar service.
	 */
	private final CalendarService calendarService;


	/**
	 * Instantiates a new Signup controller.
	 *
	 * @param userContext     the user context
	 * @param calendarService the calendar service
	 */
	public SignupController(final UserContext userContext,
			final CalendarService calendarService) {
		if (userContext == null) {
			throw new IllegalArgumentException("userContext cannot be null");
		}
		if (calendarService == null) {
			throw new IllegalArgumentException("calendarService cannot be null");
		}
		this.userContext = userContext;
		this.calendarService = calendarService;
	}

	/**
	 * Signup string.
	 *
	 * @param signupForm the signup form
	 * @return the string
	 */
	@GetMapping("/signup/form")
	public String signup(final @ModelAttribute SignupForm signupForm) {
		return "signup/form";
	}

	/**
	 * Signup string.
	 *
	 * @param signupForm         the signup form
	 * @param result             the result
	 * @param redirectAttributes the redirect attributes
	 * @return the string
	 */
	@PostMapping("/signup/new")
	public String signup(final @Valid SignupForm signupForm,
			final BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "signup/form";
		}

		String email = signupForm.getEmail();
		if (calendarService.findUserByEmail(email) != null) {
			result.rejectValue("email", "errors.signup.email", "Email address is already in use. FOO");
			redirectAttributes.addFlashAttribute("error", "Email address is already in use. FOO");
			return "signup/form";
		}

		CalendarUser user = new CalendarUser(null, signupForm.getFirstName(), signupForm.getLastName(), email, signupForm.getPassword());
		int id = calendarService.createUser(user);
		user.setId(id);
		userContext.setCurrentUser(user);

		redirectAttributes.addFlashAttribute("message", "You have successfully signed up and logged in.");
		return "redirect:/";
	}
}
