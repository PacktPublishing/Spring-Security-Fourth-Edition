package com.packtpub.springsecurity.domain;


import java.io.Serializable;
import java.util.Calendar;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * An {@link Event} is an item on a calendar that contains an owner (the person who created it), an attendee
 * (someone who was invited to the event), when the event will occur, a summary, and a description. For simplicity, all
 * fields are required.
 *
 *  @author bnasslahsen
 */
@Entity
@Table(name = "events")
public class Event implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotEmpty(message = "Summary is required")
	private String summary;
	@NotEmpty(message = "Description is required")
	private String description;
	@NotNull(message = "When is required")
	private Calendar dateWhen;


	@NotNull(message = "Owner is required")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="owner", referencedColumnName="id")
	private CalendarUser owner;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="attendee", referencedColumnName="id")
	private CalendarUser attendee;

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
	/**
	 * The identifier for the {@link Event}. Must be null when creating a new {@link Event}, otherwise non-null.
	 *
	 * @return
	 */
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * The summary of the event.
	 * @return
	 */
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}


	/**
	 * The detailed description of the event.
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * When this event is happening.
	 * @return
	 */
	public Calendar getDateWhen() {
		return dateWhen;
	}
	public void setDateWhen(Calendar when) {
		this.dateWhen = when;
	}

	/**
	 * The owner (who created the Event)
	 * @return
	 */
	public CalendarUser getOwner() {
		return owner;
	}
	public void setOwner(CalendarUser owner) {
		this.owner = owner;
	}

	/**
	 * The user that was invited to the event.
	 * @return
	 */
	public CalendarUser getAttendee() {
		return attendee;
	}
	public void setAttendee(CalendarUser attendee) {
		this.attendee = attendee;
	}


	// --- override Object ---

	@Override
	public int hashCode() {
		//return HashCodeBuilder.reflectionHashCode(this);
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		//return EqualsBuilder.reflectionEquals(this, obj);
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}