package com.demo.api.msgraph;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.ServiceUnavailableException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demo.api.myseat.MySeatAPIService;
import com.demo.model.msgraph.events.Attendees;
import com.demo.model.msgraph.events.Event;
import com.demo.model.msgraph.events.Events;
import com.demo.model.msgraph.rooms.Room;
import com.demo.model.msgraph.rooms.Rooms;
import com.demo.model.myseat.chairs.Chair;
import com.demo.model.myseat.chairs.Content;
import com.demo.utils.HttpClientHelper;
import com.demo.utils.JSONHelper;
import com.demo.utils.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;

@Service
public class MSGraphAPIServiceImpl implements MSGraphAPIService {
	private static final Logger LOG = LoggerFactory.getLogger(MSGraphAPIServiceImpl.class);

	@Value("${microsoft.graph.api.authority}")
	private String AUTHORITY;
	@Value("${microsoft.graph.api.client.id}")
	private String CLIENT_ID;
	@Value("${microsoft.graph.api.resource}")
	private String RESOURCE;
	@Value("${microsoft.office.api.find.rooms}")
	private String getRoomsUri;
	@Value("${microsoft.office.api.get.events}")
	private String getEventsUri;
	@Value("${microsoft.office.api.delete.event}")
	private String deleteEventUri;
	@Value("${microsoft.office.api.delete.event}")
	private String cancelEventUri;
	@Value("${microsoft.office.api.delete.or.get.event.room}")
	private String getEventsByRoomUri;
	@Value("${microsoft.office.api.delete.or.get.event.room}")
	private String deleteEventOfRoomUri;

	@Autowired
	private MySeatAPIService mySeatAPIService;

	public MSGraphAPIServiceImpl() {
	}

	public AuthenticationResult getAccessTokenFromUserCredentials(String username, String password) throws Exception {
		AuthenticationContext context = null;
		AuthenticationResult result = null;
		ExecutorService service = null;
		try {
			service = Executors.newFixedThreadPool(1);
			context = new AuthenticationContext(AUTHORITY, false, service);
			Future<AuthenticationResult> future = context.acquireToken(RESOURCE, CLIENT_ID, username, password, null);
			result = future.get();
		} finally {
			service.shutdown();
		}

		if (result == null) {
			throw new ServiceUnavailableException("authentication result was null");
		}
		return result;
	}

	public String getUsernamesFromGraph(String accessToken, String tenant) throws Exception {
		URL url = new URL(String.format("https://graph.windows.net/%s/users?api-version=1.5", tenant, accessToken));

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// Set the appropriate header fields in the request header.
		conn.setRequestProperty("api-version", "1.5");
		conn.setRequestProperty("Authorization", accessToken);
		conn.setRequestProperty("Accept", "application/json;odata=minimalmetadata");
		String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);

		int responseCode = conn.getResponseCode();
		JSONObject response = HttpClientHelper.processGoodRespStr(responseCode, goodRespStr);
		JSONArray users = new JSONArray();

		users = JSONHelper.fetchDirectoryObjectJSONArray(response);

		StringBuilder builder = new StringBuilder();
		User user = null;
		for (int i = 0; i < users.length(); i++) {
			JSONObject thisUserJSONObject = users.optJSONObject(i);
			user = new User();
			JSONHelper.convertJSONObjectToDirectoryObject(thisUserJSONObject, user);
			builder.append(user.getUserPrincipalName() + "\n");
		}
		return builder.toString();
	}

	public Rooms getListRooms(String accessToken, String tenant) throws Exception {
		URL url = new URL(String.format(getRoomsUri, tenant, accessToken));

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// Set the appropriate header fields in the request header.
		// conn.setRequestProperty("api-version", "1.5");
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		conn.setRequestProperty("Accept", "application/json;odata.metadata=full");

		String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);

		ObjectMapper objectMapper = new ObjectMapper();
		Rooms rooms = objectMapper.readValue(goodRespStr, Rooms.class);

		return rooms;

	}

	public Events getAllEvents(String accessToken, String tenant) throws Exception {
		URL url = new URL(String.format(getEventsUri, tenant, accessToken));

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// Set the appropriate header fields in the request header.
		// conn.setRequestProperty("api-version", "1.5");
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		conn.setRequestProperty("Accept", "application/json;odata.metadata=full");
		// conn.setRequestProperty("Prefer", "outlook.timezone=\"Pacific Standard
		// Time\"");

		String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);

		ObjectMapper objectMapper = new ObjectMapper();
		Events events = objectMapper.readValue(goodRespStr, Events.class);

		return events;
	}

	public boolean deleteEvent(String accessToken, String tenant, String id) throws IOException {
		URL url = new URL(String.format(deleteEventUri + id, tenant, accessToken));

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// Set the appropriate header fields in the request header.
		conn.setRequestMethod("DELETE");
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);

		String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
		LOG.info("Response for Delete Event - " + goodRespStr);

		return true;
	}

	public boolean cancelEvent(String accessToken, String tenant, String id) throws IOException {
		URL url = new URL(String.format(cancelEventUri + id + "/cancel", tenant, accessToken));

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// Set the appropriate header fields in the request header.
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		conn.setRequestProperty("Content-Type", "application/json");
		String input = "{\"Comment\": \"Cancelling for this week due to all hands\"}";
		conn.setDoOutput(true);
		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();

		String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
		LOG.info("Response for Delete Event - " + goodRespStr);

		return true;
	}

	// https://graph.microsoft.com/beta/users/meetingroom1@myseatsas.onmicrosoft.com/events/AAMkADkyMDg4Zjk2LThhZjAtNDc4Mi05M2VjLTZjMWZjYjdkNzdmNABGAAAAAAAHXEJrEOrfQIvHEY4DAEATBwA6X8Ke3ThcSb0X2vhIiqCTAAAAAAENAAA6X8Ke3ThcSb0X2vhIiqCTAAAJQQY3AAA=
	public boolean deleteEventOfRoom(String accessToken, String tenant, String id, String roomAddress)
			throws IOException {
		URL url = new URL(String.format(deleteEventOfRoomUri + roomAddress + "/events/" + id, tenant, accessToken));

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// Set the appropriate header fields in the request header.
		conn.setRequestMethod("DELETE");
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);

		String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);

		return true;
	}

	public Events getAllEventsByRoom(String accessToken, String tenant, String roomAddress) throws Exception {
		URL url = new URL(String.format(getEventsByRoomUri + roomAddress + "/events", tenant, accessToken));

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// Set the appropriate header fields in the request header.
		// conn.setRequestProperty("api-version", "1.5");
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		conn.setRequestProperty("Accept", "application/json;odata.metadata=full");
		// conn.setRequestProperty("Prefer", "outlook.timezone=\"Pacific Standard
		// Time\"");

		String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);

		ObjectMapper objectMapper = new ObjectMapper();
		Events events = objectMapper.readValue(goodRespStr, Events.class);

		return events;
	}

	public Events getOnlyEventByRoomAddressAndId(String accessToken, String tenant, String roomAddress, String eventId)
			throws Exception {
		URL url = new URL(String.format("https://graph.microsoft.com/beta/users/" + roomAddress + "/events/" + eventId,
				tenant, accessToken));

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// Set the appropriate header fields in the request header.
		conn.setRequestProperty("Authorization", "Bearer " + accessToken);
		conn.setRequestProperty("Accept", "application/json;odata.metadata=full");

		Events events = new Events();
		try {

			String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
			ObjectMapper objectMapper = new ObjectMapper();
			events = objectMapper.readValue(goodRespStr, Events.class);

		} catch (Exception e) {
			LOG.info("No Event found for this ID: {}", eventId);
		}
		return events;

	}

	public void verifyAndDeleteEventByRoom(String accessToken, String tenant, String roomAddress, String eventId,
			Content chairs) throws Exception {
		LOG.info(" [ verifyAndDeleteEventByRoom for this room address {} and this event ID {} ]", roomAddress, eventId);
	    TimeZone tz = TimeZone.getTimeZone("UTC");
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.s", Locale.CANADA_FRENCH);
	    SimpleDateFormat dfMs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    df.setTimeZone(tz);
		Events events = getOnlyEventByRoomAddressAndId(accessToken, tenant, roomAddress, eventId);
		boolean deleted = false;
		boolean toRemove = false;
		if (events != null) {
			toRemove = checkSensorState(dfMs.format(df.parse(events.getStart().getDatetime())), chairs);

			if (toRemove)
				deleted = deleteEventOfRoom(accessToken, tenant, eventId, roomAddress);

			if (deleted)
				LOG.info("The event {} was deleted successfully !", eventId);
		} else
			LOG.info("Events are Empty for this room address {} ", roomAddress);

	}

	public boolean checkSensorState(String dateStartEvent, Content chairs) {
		boolean toRemove = false;
		int decision = 0;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startDateEvent = LocalDateTime.parse(dateStartEvent, formatter);
		for (Chair chair : chairs.getChairs().getChairs()) {
			LocalDateTime dateLastCommunitationSensor = LocalDateTime.parse(chair.getLast_communication(), formatter);
			long diffInMinute = Duration.between(startDateEvent, dateLastCommunitationSensor).toMinutes();
			LOG.info("Difference between dateStartEvent {} and lastComSensor {} is: {}", startDateEvent,
					dateLastCommunitationSensor, diffInMinute);
			if (diffInMinute >= 15) {
				if (chair.getStatus() == 1) {
					LOG.info("- The chair is busy -");
					break;
				} else {
					LOG.info("- The chair is free -");
					toRemove = true;
				}

			}
		}

		LOG.info("ToRemove {} ", toRemove);
		return toRemove;

	}

}
