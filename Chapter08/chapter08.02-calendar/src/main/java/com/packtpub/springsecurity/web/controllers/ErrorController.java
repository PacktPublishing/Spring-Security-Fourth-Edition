package com.packtpub.springsecurity.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

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
	public ModelAndView exception(final Throwable throwable, final Model model) {
		logger.error("Exception during execution of SpringSecurity application", throwable);
		StringBuffer sb = new StringBuffer();
		sb.append("Exception during execution of Spring Security application!  ");

		sb.append((throwable != null ? throwable.getMessage() : "Unknown error"));
		sb.append(", root cause: ").append((throwable != null ? throwable.getCause() : "Unknown cause"));

		model.addAttribute("error", sb.toString());

		ModelAndView mav = new ModelAndView();
		mav.addObject("error", sb.toString());
		mav.setViewName("error");

		return mav;
	}

} 
