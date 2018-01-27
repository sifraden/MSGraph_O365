package com.demo.model.msgraph;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rooms {
	
	@JsonProperty("@odata.context")
	private String context;
	@JsonProperty("@odata.type")
	private String data_type;
	@JsonProperty("value")
	private List<Room> rooms;
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public List<Room> getRooms() {
		return rooms;
	}
	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}
	@Override
	public String toString() {
		return "Rooms [context=" + context + ", data_type=" + data_type + ", rooms=" + rooms + "]";
	}
	
	
	
	
	

}
