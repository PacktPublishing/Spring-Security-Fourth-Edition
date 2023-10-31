package com.packtpub.springsecurity.web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.userdetails.InetOrgPerson;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * A controller that allows for viewing of account information.
 *
 * @author Rob Winch
 *
 */
@Controller
public class AccountController {

	private final UserDetailsService userDetailsService;

	public AccountController(UserDetailsService userDetailsService) {
		if (userDetailsService == null) {
			throw new IllegalArgumentException("userDetailsService cannot be null");
		}
		this.userDetailsService = userDetailsService;
	}

	@RequestMapping("/accounts/my")
	public String view(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null) {
			throw new IllegalStateException("authentication cannot be null. Make sure you are logged in.");
		}
		Object principal = userDetailsService.loadUserByUsername(authentication.getName());
		model.addAttribute("user", principal);
		model.addAttribute("isLdapUserDetails", principal instanceof LdapUserDetails);
		model.addAttribute("isLdapPerson", principal instanceof Person);
		model.addAttribute("isLdapInetOrgPerson", principal instanceof InetOrgPerson);
		return "accounts/show";
	}
}
