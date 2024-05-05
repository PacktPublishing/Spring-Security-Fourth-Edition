package com.packtpub.springsecurity.web.controllers;


import java.util.UUID;

import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.service.CalendarService;
import com.packtpub.springsecurity.service.UserContext;
import com.packtpub.springsecurity.web.model.SignupForm;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;

@Controller
public class SignupController {
	
	private final UserContext userContext;

	private final CalendarService calendarService;

	@Autowired
	public SignupController(UserContext userContext, CalendarService calendarService) {
		if (userContext == null) {
			throw new IllegalArgumentException("userContext cannot be null");
		}
		if (calendarService == null) {
			throw new IllegalArgumentException("calendarService cannot be null");
		}
		this.userContext = userContext;
		this.calendarService = calendarService;
	}

	@RequestMapping("/signup/form")
	public String signup(@ModelAttribute SignupForm signupForm) {
		return "signup/form";
	}

	@RequestMapping(value = "/signup/new", method = RequestMethod.POST)
	public Mono<String> signup(@Valid SignupForm signupForm, BindingResult result, ServerWebExchange exchange) {
		if (result.hasErrors()) {
			return Mono.just("signup/form");
		}

		String email = signupForm.getEmail();
		Mono<CalendarUser> existingUserMono = calendarService.findUserByEmail(email);
		existingUserMono.doOnNext(existingUser -> {
			if (existingUser != null) {
				result.rejectValue("email", "errors.signup.email", "Email address is already in use.");
			}
		}).subscribe();

		if (result.hasErrors()) {
			return Mono.just("signup/form");
		}

		CalendarUser user = new CalendarUser();
		user.setEmail(email);
		user.setFirstName(signupForm.getFirstName());
		user.setLastName(signupForm.getLastName());
		user.setPassword(signupForm.getPassword());

		Mono<UUID> uuidMono = calendarService.createUser(user);
		return uuidMono.flatMap(uuid -> exchange.getSession()
				.doOnNext(session -> {
					userContext.setCurrentUser(user, session);
				})
				.flatMap(WebSession::changeSessionId)
				.then(Mono.just("redirect:/")));
	}
}
