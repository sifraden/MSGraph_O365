package com.demo.model.myseat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Content {
	
	@JsonProperty("Content")
	private Chairs chairs;

	public Chairs getChairs() {
		return chairs;
	}

	public void setChairs(Chairs chairs) {
		this.chairs = chairs;
	}

	@Override
	public String toString() {
		return "Content [chairs=" + chairs + "]";
	}
	
	

}
