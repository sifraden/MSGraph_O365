package com.demo.api.msgraph;

import java.io.IOException;

import com.microsoft.aad.adal4j.AuthenticationResult;

public interface MSGraphAPIService {
	
	public AuthenticationResult getAccessTokenFromUserCredentials(String username, String password) throws Exception;
	
	public String getUsernamesFromGraph(String accessToken, String tenant) throws Exception;
	
	public String getListRooms(String accessToken, String tenant) throws Exception;
	
	public String getAllEvents(String accessToken, String tenant) throws Exception;
	
	public boolean deleteEvent(String accessToken, String tenant, String id) throws IOException;
	
	public boolean cancelEvent(String accessToken, String tenant, String id) throws IOException;
	
	public boolean deleteEventOfRoom(String accessToken, String tenant, String id, String roomAddress) throws IOException;
	
	public String getAllEventsByRoom(String accessToken, String tenant, String roomAddress) throws Exception;

}
