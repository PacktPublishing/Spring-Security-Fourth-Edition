package com.packtpub.springsecurity.web.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This is Advice to handle INTERNAL_SERVER_ERROR
 *
 *  @author bnasslahsen
 */
@RestControllerAdvice
public class ErrorController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<String> exception(final Throwable throwable) {
		logger.error("An error occurred: {}", throwable.getMessage(), throwable);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<String> exception(final AccessDeniedException accessDeniedException) {
		logger.error("An error occurred: {}", accessDeniedException.getMessage(), accessDeniedException);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(accessDeniedException.getMessage());
	}
} 
