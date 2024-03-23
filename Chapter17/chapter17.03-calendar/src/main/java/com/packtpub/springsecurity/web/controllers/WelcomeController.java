package com.packtpub.springsecurity.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This displays the welcome screen that shows what will be happening in this chapter.
 *
 *  @author bnasslahsen
 */
@RestController
public class WelcomeController {

	/**
	 * Welcome string.
	 *
	 * @return the string
	 */
	@GetMapping("/")
	public String welcome() {
		return "index";
	}

	/**
	 * Return no favicon.
	 */
	@GetMapping(path = { "favicon.ico", "favicon-16x16.jpg" })
	@ResponseBody
	void returnNoFavicon() {}

	//    @PreAuthorize("(authentication.principal.uuid == #uuid.toString()) or hasRole('ADMIN')")
//    User findByUuid(@Param("uuid") UUID uuid);
	@GetMapping("/api")
	public String api() {
		return "{'message': 'welcome to the JBCP Calendar Application API'}";
	}
}