package com.packtpub.springsecurity.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	 * Welcome string.
	 *
	 * @return the string
	 */
	@RequestMapping("/")
	public String welcome() {
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