package com.packtpub.springsecurity.web.model;

import java.util.Calendar;

import com.packtpub.springsecurity.domain.Event;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * A form object that is used for creating a new {@link Event}. Using a different object is one way of preventing
 * malicious users from filling out field that they should not (i.e. fill out a different owner field).
 *
 *  @author bnasslahsen
 */
public class CreateEventForm {

	/**
	 * The Attendee email.
	 */
	@NotEmpty(message = "Attendee Email is required")
	@Email(message = "Attendee Email must be a valid email")
	private String attendeeEmail;

	/**
	 * The Summary.
	 */
	@NotEmpty(message = "Summary is required")
	private String summary;

	/**
	 * The Description.
	 */
	@NotEmpty(message = "Description is required")
	private String description;

	/**
	 * The When.
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@NotNull(message = "Event Date/Time is required")
	private Calendar when;

	/**
	 * Gets attendee email.
	 *
	 * @return the attendee email
	 */
	public String getAttendeeEmail() {
		return attendeeEmail;
	}

	/**
	 * Sets attendee email.
	 *
	 * @param attendeeEmail the attendee email
	 */
	public void setAttendeeEmail(String attendeeEmail) {
		this.attendeeEmail = attendeeEmail;
	}

	/**
	 * Gets summary.
	 *
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * Sets summary.
	 *
	 * @param summary the summary
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * Gets description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets description.
	 *
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets when.
	 *
	 * @return the when
	 */
	public Calendar getWhen() {
		return when;
	}

	/**
	 * Sets when.
	 *
	 * @param when the when
	 */
	public void setWhen(Calendar when) {
		this.when = when;
	}
}