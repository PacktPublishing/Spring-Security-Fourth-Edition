package com.packtpub.springsecurity.domain;


import java.util.Calendar;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * An {@link Event} is an item on a calendar that contains an owner (the person who created it), an attendee
 * (someone who was invited to the event), when the event will occur, a summary, and a description. For simplicity, all
 * fields are required.
 *
 * @author Rob Winch
 * @author bnasslahsen
 */

public class Event {
	private Integer id;

	@NotEmpty(message = "Summary is required")
	private String summary;

	@NotEmpty(message = "Description is required")
	private String description;

	@NotNull(message = "When is required")
	private Calendar dateWhen;

	@NotNull(message = "Owner is required")
	private CalendarUser owner;

	private CalendarUser attendee;

	// Constructors, getters, and setters

	public Event() {
	}

	public Event(Integer id, String summary, String description, Calendar dateWhen, CalendarUser owner, CalendarUser attendee) {
		this.id = id;
		this.summary = summary;
		this.description = description;
		this.dateWhen = dateWhen;
		this.owner = owner;
		this.attendee = attendee;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Calendar getDateWhen() {
		return dateWhen;
	}

	public void setDateWhen(Calendar dateWhen) {
		this.dateWhen = dateWhen;
	}

	public CalendarUser getOwner() {
		return owner;
	}

	public void setOwner(CalendarUser owner) {
		this.owner = owner;
	}

	public CalendarUser getAttendee() {
		return attendee;
	}

	public void setAttendee(CalendarUser attendee) {
		this.attendee = attendee;
	}
}
