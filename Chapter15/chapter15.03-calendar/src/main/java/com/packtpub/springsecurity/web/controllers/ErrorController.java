package com.packtpub.springsecurity.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This is Advice to handle INTERNAL_SERVER_ERROR
 *
 *  @author bnasslahsen
 */
@ControllerAdvice
public class ErrorController {

	private static Logger logger = LoggerFactory.getLogger(ErrorController.class);

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String  exception(final Throwable throwable, final Model model) {
		logger.error("Exception during execution of SpringSecurity application", throwable);
		StringBuilder sb = new StringBuilder();
		sb.append("Exception during execution of Spring Security application!  ");

		sb.append((throwable != null && throwable.getMessage() != null ? throwable.getMessage() : "Unknown error"));

		if (throwable != null && throwable.getCause() != null) {
			sb.append("\n\nroot cause: ").append(throwable.getCause());
		}
		model.addAttribute("error", sb.toString());
		return "error";
	}

} 
