package com.demo.model.msgraph;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Room {
	
	@JsonProperty("@odata.type")
	private String type;
	@JsonProperty("name")
	private String name;
	@JsonProperty("address")
	private String address;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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
		return "Room [type=" + type + ", name=" + name + ", address=" + address + "]";
	}
	
}
