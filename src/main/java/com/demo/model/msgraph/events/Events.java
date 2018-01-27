package com.demo.model.msgraph.events;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Events {
	
	@JsonProperty("@odata.context")
	private String context;
	
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

	@Override
	public String toString() {
		return "Events [context=" + context + ", events=" + events + "]";
	}
	
	

}
