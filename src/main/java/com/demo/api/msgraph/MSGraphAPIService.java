package com.demo.api.msgraph;

import java.io.IOException;

import com.demo.model.msgraph.events.Events;
import com.demo.model.msgraph.rooms.Rooms;
import com.demo.model.myseat.chairs.Content;
import com.microsoft.aad.adal4j.AuthenticationResult;

public interface MSGraphAPIService {
	
	public AuthenticationResult getAccessTokenFromUserCredentials(String username, String password) throws Exception;
	
	public String getUsernamesFromGraph(String accessToken, String tenant) throws Exception;
	
	public Rooms getListRooms(String accessToken, String tenant) throws Exception;
	
	public Events getAllEvents(String accessToken, String tenant) throws Exception;
	
	public boolean deleteEvent(String accessToken, String tenant, String id) throws IOException;
	
	public boolean cancelEvent(String accessToken, String tenant, String id) throws IOException;
	
	public boolean deleteEventOfRoom(String accessToken, String tenant, String id, String roomAddress) throws IOException;
	
	public Events getAllEventsByRoom(String accessToken, String tenant, String roomAddress) throws Exception;
	
	public Events getOnlyEventByRoomAddressAndId(String accessToken, String tenant, String roomAddress, String eventId) throws Exception;
	
	public void verifyAndDeleteEventByRoom(String accessToken, String tenant, String roomAddress, String eventId, Content chairs) throws Exception;
	
	public boolean checkSensorState(String dateStartEvent, Content chairs);




}
