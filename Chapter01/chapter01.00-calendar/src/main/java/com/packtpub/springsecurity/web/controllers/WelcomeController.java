package com.packtpub.springsecurity.web.controllers;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This displays the welcome screen that shows what will be happening in this chapter.
 *
 * @author Rob Winch
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
	@Autowired
    ApplicationContext context;


	/**
	 * Welcome string.
	 *
	 * @return the string
	 */
	@GetMapping(value="/")
    public String welcome() {
        String name = context.getMessage("customer.name",
                new Object[] { 46,"http://www.baselogic.com" }, Locale.US);

		logger.info("Customer name (English) : " + name);
        logger.info("*** welcome(): {}", name);
        return "index";
    }
}