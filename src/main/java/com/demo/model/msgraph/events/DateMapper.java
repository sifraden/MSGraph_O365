package com.demo.model.msgraph.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DateMapper {
	
	@JsonProperty("dateTime")
	private String datetime;
	
	@JsonProperty("timeZone")
	private String timezone;

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	@Override
	public String toString() {
		return "DateMapper [datetime=" + datetime + ", timezone=" + timezone + "]";
	}
	
	

}
