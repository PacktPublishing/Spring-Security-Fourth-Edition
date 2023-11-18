package com.packtpub.springsecurity.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Error controller.
 *
 *  @author bnasslahsen
 */
@ControllerAdvice
public class ErrorController {

	/**
	 * The constant logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

	/**
	 * Exception string.
	 *
	 * @param throwable the throwable
	 * @param model     the model
	 * @return the string
	 */
	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String exception(final Throwable throwable, final Model model) {
		logger.error("Exception during execution of SpringSecurity application", throwable);
		String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
		model.addAttribute("error", errorMessage);
		return "error";
	}

}