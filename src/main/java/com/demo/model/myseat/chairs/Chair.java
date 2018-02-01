package com.demo.model.myseat.chairs;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Chair implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("QRCode")
	private String qrCode;
	
	@JsonProperty("sensor")
	private String sensor;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("id_geometry")
	private int id_geometry;
	
	@JsonProperty("last_communication")
	private String last_communication;
	
	@JsonProperty("previous_communication")
	private String previous_communication;
	
	
	public String getLast_communication() {
		return last_communication;
	}
	public void setLast_communication(String last_communication) {
		this.last_communication = last_communication;
	}
	public String getPrevious_communication() {
		return previous_communication;
	}
	public void setPrevious_communication(String previous_communication) {
		this.previous_communication = previous_communication;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getId_geometry() {
		return id_geometry;
	}
	public void setId_geometry(int id_geometry) {
		this.id_geometry = id_geometry;
	}
	public String getSensor() {
		return sensor;
	}
	public void setSensor(String sensor) {
		this.sensor = sensor;
	}
	@Override
	public String toString() {
		return "Chair [name=" + name + ", qrCode=" + qrCode + ", sensor=" + sensor + ", status=" + status
				+ ", id_geometry=" + id_geometry + ", last_communication=" + last_communication
				+ ", previous_communication=" + previous_communication + "]";
	}
	

}
