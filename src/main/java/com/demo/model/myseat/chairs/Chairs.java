package com.demo.model.myseat.chairs;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Chairs implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("Chairs")
	private List<Chair> chairs;

	public List<Chair> getChairs() {
		return chairs;
	}

	public void setChairs(List<Chair> chairs) {
		this.chairs = chairs;
	}

	@Override
	public String toString() {
		return "Chairs [chairs=" + chairs + "]";
	}
	
}
