package com.demo.model.msgraph.events;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationMapper {
	
	@JsonProperty("displayName")
	private String displayName;
	
	@JsonProperty("address")
	private Address address;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "LocationMapper [displayName=" + displayName + ", address=" + address + "]";
	}
	
	

}
