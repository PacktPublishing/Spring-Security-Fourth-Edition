package com.packtpub.springsecurity.domain;

import java.util.Calendar;

import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * An {@link Event} is an item on a calendar that contains an owner (the person who created it), an attendee
 * (someone who was invited to the event), when the event will occur, a summary, and a description. For simplicity, all
 * fields are required.
 *
 * @author Rob Winch
 * @author bassLahsen
 */
public class Event {

	/**
	 * The Id.
	 */
	private Integer id;

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
	 * The Date when.
	 */
	@NotNull(message = "When is required")
    private Calendar dateWhen;

	/**
	 * The Owner.
	 */
	@NotNull(message = "Summary is required")
    private CalendarUser owner;

	/**
	 * The Attendee.
	 */
	private CalendarUser attendee;

	/**
	 * The identifier for the {@link Event}. Must be null when creating a new {@link Event}, otherwise non-null.
	 *
	 * @return id id
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
	 * The summary of the event.
	 *
	 * @return summary summary
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
	 * The detailed description of the event.
	 *
	 * @return description description
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
	 * When this event is happening.
	 *
	 * @return date when
	 */
	public Calendar getDateWhen() {
        return dateWhen;
    }

	/**
	 * Sets date when.
	 *
	 * @param dateWhen the date when
	 */
	public void setDateWhen(Calendar dateWhen) {
        this.dateWhen = dateWhen;
    }

	/**
	 * The owner (who created the Event)
	 *
	 * @return owner owner
	 */
	public CalendarUser getOwner() {
        return owner;
    }

	/**
	 * Sets owner.
	 *
	 * @param owner the owner
	 */
	public void setOwner(CalendarUser owner) {
        this.owner = owner;
    }

	/**
	 * The user that was invited to the event.
	 *
	 * @return attendee attendee
	 */
	public CalendarUser getAttendee() {
        return attendee;
    }

	/**
	 * Sets attendee.
	 *
	 * @param attendee the attendee
	 */
	public void setAttendee(CalendarUser attendee) {
        this.attendee = attendee;
    }


    // --- override Object ---

	/**
	 * Hash code int.
	 *
	 * @return the int
	 */
	@Override
    public int hashCode() {
        //return HashCodeBuilder.reflectionHashCode(this);
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

	/**
	 * Equals boolean.
	 *
	 * @param obj the obj
	 * @return the boolean
	 */
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

	/**
	 * To string string.
	 *
	 * @return the string
	 */
	@Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

} // The End...
