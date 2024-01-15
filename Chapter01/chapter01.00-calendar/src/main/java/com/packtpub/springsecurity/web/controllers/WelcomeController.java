package com.packtpub.springsecurity.web.controllers;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This displays the welcome screen that shows what will be happening in this chapter.
 *
 * @author bnasslahsen
 * @author bassLahsen
 */
@Controller
public class WelcomeController {

	/**
	 * The constant logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(WelcomeController.class);

	/**
	 * The Context.
	 */
	private ApplicationContext context;

	/**
	 * Instantiates a new Welcome controller.
	 *
	 * @param context the context
	 */
	public WelcomeController(ApplicationContext context) {
		this.context = context;
	}

	/**
	 * Welcome string.
	 *
	 * @return the string
	 */
	@GetMapping(value = "/")
	public String welcome() {
		String name = context.getMessage("customer.name",
				new Object[] { 46, "https://www.packtpub.com" }, Locale.US);

		logger.info("Customer name (English) : " + name);
		logger.info("*** welcome(): {}", name);
		return "index";
	}

	/**
	 * Return no favicon.
	 */
	@GetMapping(path = { "favicon.ico", "favicon-16x16.jpg" })
	@ResponseBody
	void returnNoFavicon() {
	}
}