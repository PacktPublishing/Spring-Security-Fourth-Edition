package com.packtpub.springsecurity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link CalendarUser} is this applications notion of a user. It is good to use your own objects to interact with a
 * user especially in large applications. This ensures that as you evolve your security requirements (update Spring
 * Security, leverage new Spring Security modules, or even swap out security implementations) you can do so easily.
 *
 * @author Rob Winch
 * @author bnasslahsen
 */

public record CalendarUser(
		Integer id,
		String firstName,
		String lastName,
		String email,
		String password
) {

	@JsonIgnore
	public String getName() {
		return lastName + ", " + firstName;
	}
}