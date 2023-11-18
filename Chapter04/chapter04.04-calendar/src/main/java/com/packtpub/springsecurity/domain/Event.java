package com.packtpub.springsecurity.domain;


import java.util.Calendar;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * An {@link Event} is an item on a calendar that contains an owner (the person who created it), an attendee
 * (someone who was invited to the event), when the event will occur, a summary, and a description. For simplicity, all
 * fields are required.
 *
 *  @author bnasslahsen
 */
public record Event(
		Integer id,
		@NotEmpty(message = "Summary is required") String summary,
		@NotEmpty(message = "Description is required") String description,
		@NotNull(message = "When is required") Calendar dateWhen,
		@NotNull(message = "Owner is required") CalendarUser owner,
		CalendarUser attendee
) {}