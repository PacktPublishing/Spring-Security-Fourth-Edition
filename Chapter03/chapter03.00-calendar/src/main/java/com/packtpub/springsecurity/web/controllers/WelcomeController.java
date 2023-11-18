package com.packtpub.springsecurity.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This displays the welcome screen that shows what will be happening in this chapter.
 *
 *  @author bnasslahsen
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
}