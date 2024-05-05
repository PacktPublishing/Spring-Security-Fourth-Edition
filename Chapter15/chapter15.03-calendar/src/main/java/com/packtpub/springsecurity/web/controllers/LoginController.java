package com.packtpub.springsecurity.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory
			.getLogger(LoginController.class);

	@GetMapping(value = "/login")
	public String login(Model model,
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {

		logger.info("******login(error): {} ***************************************", error);
		logger.info("******login(logout): {} ***************************************", logout);

		if (error != null) {
			model.addAttribute("error", "Invalid username and password!");
		}

		if (logout != null) {
			model.addAttribute("message", "You've been logged out successfully.");
		}

		return "login";

	}

}
