package com.demo.model.msgraph.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attendees {
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("status")
	private Status status;
	
	@JsonProperty("emailAddress")
	private EmailAddress emailAddress;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public EmailAddress getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(EmailAddress emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public String toString() {
		return "Attendees [type=" + type + ", status=" + status + ", emailAddress=" + emailAddress + "]";
	}
	

}
