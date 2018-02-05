package com.demo.model.msgraph.events;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Events {
	
	@JsonProperty("@odata.context")
	private String context;
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("subject")
	private String subject;
	
	@JsonProperty("start")
	private DateMapper start;
	
	@JsonProperty("end")
	private DateMapper end;
	
	@JsonProperty("location")
	private LocationMapper location;
	
	@JsonProperty("attendees")
	private List<Attendees> attendees;
	
	@JsonProperty("organizer")
	private Organizer organizer;
	
	@JsonProperty("value")
	private List<Event> events;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public DateMapper getStart() {
		return start;
	}

	public void setStart(DateMapper start) {
		this.start = start;
	}

	public DateMapper getEnd() {
		return end;
	}

	public void setEnd(DateMapper end) {
		this.end = end;
	}

	public LocationMapper getLocation() {
		return location;
	}

	public void setLocation(LocationMapper location) {
		this.location = location;
	}

	public List<Attendees> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<Attendees> attendees) {
		this.attendees = attendees;
	}

	public Organizer getOrganizer() {
		return organizer;
	}

	public void setOrganizer(Organizer organizer) {
		this.organizer = organizer;
	}

	@Override
	public String toString() {
		return "Events [context=" + context + ", id=" + id + ", subject=" + subject + ", start=" + start + ", end="
				+ end + ", location=" + location + ", attendees=" + attendees + ", organizer=" + organizer + ", events="
				+ events + "]";
	}
	
	

}
