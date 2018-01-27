package com.demo.model.msgraph.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailAddress {
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("address")
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "EmailAddress [name=" + name + ", address=" + address + "]";
	}


	

}
