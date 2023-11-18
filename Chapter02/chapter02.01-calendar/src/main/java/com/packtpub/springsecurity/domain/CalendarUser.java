package com.packtpub.springsecurity.domain;


import java.security.Principal;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link CalendarUser} is this applications notion of a user. It is good to use your own objects to interact with a
 * user especially in large applications. This ensures that as you evolve your security requirements (update Spring
 * Security, leverage new Spring Security modules, or even swap out security implementations) you can do so easily.
 *
 *  @author bnasslahsen
 */

public class CalendarUser implements Principal {
	private Integer id;

	private String firstName;

	private String lastName;

	private String email;

	private String password;

	public CalendarUser(Integer id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore
	public String getName() {
		return lastName + ", " + firstName;
	}
}