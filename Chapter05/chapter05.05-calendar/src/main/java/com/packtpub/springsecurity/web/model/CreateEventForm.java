package com.packtpub.springsecurity.web.model;

import java.time.LocalDateTime;

import com.packtpub.springsecurity.domain.Event;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * A form object that is used for creating a new {@link Event}. Using a different object is one way of preventing
 * malicious users from filling out field that they should not (i.e. fill out a different owner field).
 *
 * @author bnasslahsen
 * @since chapter 01.00
 */
public class CreateEventForm {

	@NotEmpty(message = "Attendee Email is required")
	@Email(message = "Attendee Email must be a valid email")
	private String attendeeEmail;

	@NotEmpty(message = "Summary is required")
	private String summary;

	@NotEmpty(message = "Description is required")
	private String description;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@NotNull(message = "Event Date/Time is required")
	private LocalDateTime when;

	public String getAttendeeEmail() {
		return attendeeEmail;
	}

	public void setAttendeeEmail(String attendeeEmail) {
		this.attendeeEmail = attendeeEmail;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getWhen() {
		return when;
	}

	public void setWhen(LocalDateTime when) {
		this.when = when;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}