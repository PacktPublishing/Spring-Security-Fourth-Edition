package com.packtpub.springsecurity.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The type Login controller.
 */
//@Controller
public class LoginController {

	/**
	 * The constant logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(LoginController.class);

	/**
	 * Login string.
	 *
	 * @param error the error
	 * @return the string
	 */
// Login form
//    @RequestMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error) {
		logger.info("******login(): {} ***************************************", error);
		logger.info("******login(): {} ***************************************", error);
		logger.info("******login(): {} ***************************************", error);
		logger.info("******login(): {} ***************************************", error);
		logger.info("******login(): {} ***************************************", error);
		return "login";
	}

	/**
	 * Login error string.
	 *
	 * @param model the model
	 * @return the string
	 */
// Login form with error
//    @RequestMapping("/login_error")
	public String loginError(Model model) {
		model.addAttribute("error", true);
		logger.info("******loginError() contains error? : {} ******************************************",
				model.containsAttribute("error"));
		return "login";
	}

	/**
	 * Logout string.
	 *
	 * @param model the model
	 * @return the string
	 */
// Login form with error
//    @RequestMapping("/logout")
	public String logout(Model model) {
		model.addAttribute("error", true);
		logger.info("******logout() contains error? : {} ******************************************",
				model.containsAttribute("error"));
		return "login";
	}
}
