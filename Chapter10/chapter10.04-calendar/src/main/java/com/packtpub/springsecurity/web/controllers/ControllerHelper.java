package com.packtpub.springsecurity.web.controllers;

import java.util.function.Function;

/**
 *  @author bnasslahsen
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
