package com.packtpub.springsecurity.web.controllers;

import java.util.function.Function;

/**
 * @author Mick Knutson
 */
public class ControllerHelper {

	/**
	 * Redirect helper
	 * Usage:
	 * ControllerHelper.redirect() -> "/";
	 * Result:
	 * "redirect:/"
	 */
//    @FunctionalInterface
	public static Function<String, String> redirect = (path) -> "redirect:" + path;

}
