package com.packtpub.springsecurity.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link CalendarUser} is this applications notion of a user. It is good to use your own objects to interact with a
 * user especially in large applications. This ensures that as you evolve your security requirements (update Spring
 * Security, leverage new Spring Security modules, or even swap out security implementations) you can do so easily.
 *
 *  @author bnasslahsen
 */
public class CalendarUser implements Serializable {
	/**
	 * The Id.
	 */
	private Integer id;

	/**
	 * The First name.
	 */
	private String firstName;

	/**
	 * The Last name.
	 */
	private String lastName;

	/**
	 * The Email.
	 */
	private String email;

	/**
	 * The Password.
	 */
	private String password;

	/**
	 * Instantiates a new Calendar user.
	 *
	 * @param id        the id
	 * @param firstName the first name
	 * @param lastName  the last name
	 * @param email     the email
	 * @param password  the password
	 */
	public CalendarUser(Integer id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets id.
	 *
	 * @param id the id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets first name.
	 *
	 * @param firstName the first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets last name.
	 *
	 * @param lastName the last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets email.
	 *
	 * @param email the email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets password.
	 *
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	@JsonIgnore
	public String getName() {
		return lastName + ", " + firstName;
	}
}