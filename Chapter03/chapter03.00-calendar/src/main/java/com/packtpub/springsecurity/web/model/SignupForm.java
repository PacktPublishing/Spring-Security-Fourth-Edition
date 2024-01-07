package com.packtpub.springsecurity.web.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * The type Signup form.
 */
public class SignupForm {

	/**
	 * The First name.
	 */
	@NotEmpty(message = "First Name is required")
	private String firstName;

	/**
	 * The Last name.
	 */
	@NotEmpty(message = "Last Name is required")
	private String lastName;

	/**
	 * The Email.
	 */
	@Email(message = "Please provide a valid email address")
	@NotEmpty(message = "Email is required")
	private String email;

	/**
	 * The Password.
	 */
	@NotEmpty(message = "Password is required")
	private String password;

	/**
	 * Gets the email address for this user.
	 *
	 * @return email email
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
	 * Gets the first name of the user.
	 *
	 * @return first name
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
	 * Gets the last name of the user.
	 *
	 * @return last name
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
	 * Gets the password for this user.
	 *
	 * @return password password
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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
