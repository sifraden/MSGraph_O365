package com.demo.handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demo.api.msgraph.MSGraphAPIService;
import com.demo.api.myseat.MySeatAPIService;
import com.demo.model.msgraph.events.Attendees;
import com.demo.model.msgraph.events.Event;
import com.demo.model.msgraph.events.Events;
import com.demo.model.msgraph.rooms.Room;
import com.demo.model.msgraph.rooms.Rooms;
import com.demo.model.myseat.chairs.Chair;
import com.demo.model.myseat.chairs.Content;
import com.microsoft.aad.adal4j.AuthenticationResult;

@Service
public class RoomBookingHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RoomBookingHandler.class);

	@Value("${event.cancel.action.minute}")
	private int minuteToStartCanceling;

	private static String AUTHORITY;
	private static String CLIENT_ID; // PWD: dgyPCK09*uhbvCCLF171}?*
	private static String RESOURCE;
	private static String USERNAME;
	private static String PASSWORD;
	private static String API_KEY;

	@Autowired
	private MSGraphAPIService graphAPIService;
	@Autowired
	private MySeatAPIService mySeatAPIService;

	TimeZone tz = TimeZone.getTimeZone("UTC");
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.s", Locale.CANADA_FRENCH);
	SimpleDateFormat dfMs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public void launchRoomBookingProcess() {
		df.setTimeZone(tz);
		AuthenticationResult result;
		try {
			result = graphAPIService.getAccessTokenFromUserCredentials(getUsername(), getPassword());
			LOG.info("Access Token - {}", result.getAccessToken());
			LOG.info("Refresh Token - {}", result.getRefreshToken());
			LOG.info("ID Token - {}", result.getIdToken());

			LOG.info("*****************************************************");
			LOG.info("*****************************************************");
			LOG.info("*****************************************************");
			LOG.info("*****************************************************");

			// LOG.info("******************** DELETE EVENT ****************");
			// LOG.info("Delete events - " + msGraphApi.deleteEvent(result.getAccessToken(),
			// "myseatsas.onmicrosoft.com",
			// "AAMkADkyMDg4Zjk2LThhZjAtNDc4Mi05M2VjLTZjMWZjYjdkNzdmNABGAAAAAAAHXEJrEOrfQIvHEY4DAEATBwA6X8Ke3ThcSb0X2vhIiqCTAAAAAAENAAA6X8Ke3ThcSb0X2vhIiqCTAAAJQQY4AAA="));
			// LOG.info("*****************************************************");
			// LOG.info("************** DELETE EVENT OF A ROOM *************");
			/*
			 * msGraphApi.verifyAndDeleteEventByRoom(result.getAccessToken(),
			 * "myseatsas.onmicrosoft.com", "meetingroom2@myseatsas.onmicrosoft.com",
			 * "AAMkADc1MGU2YzY4LTRmYmItNGFkMC04NWJhLWMyN2ZiMWY0NjRkNABGAAAAAACaUNJInvR4To1pe8tf_0DwBwBKXwdtgSEoQp-cjax6mKnZAAAAAAENAABKXwdtgSEoQp-cjax6mKnZAAAJMiC4AAA="
			 * ); LOG.info("*****************************************************");
			 */

			Rooms rooms = graphAPIService.getListRooms(result.getAccessToken(), "myseatsas.onmicrosoft.com");
			for (Room room : rooms.getRooms()) {
				LOG.info("Name: {}", room.getName());
				LOG.info("Address: {}", room.getAddress());
				LOG.info("Type: {}", room.getType());

				Events eventsForRoom = graphAPIService.getAllEventsByRoom(result.getAccessToken(),
						"myseatsas.onmicrosoft.com", room.getAddress());

				if (eventsForRoom.getId() == null) {
					LOG.info("No events found for this room address {} ! ", room.getAddress());
				} else {
					for (Event event : eventsForRoom.getEvents()) {
						LOG.info("******************   EVENTS FOR A ROOM  **************");
						LOG.info("ID: {}", event.getId());
						LOG.info("Subject: {}", event.getSubject());
						LOG.info("Organizer: {}", event.getOrganizer().getEmailAddress().getName() + " - "
								+ event.getOrganizer().getEmailAddress().getAddress());
						LOG.info("Date début: {}", dfMs.format(df.parse(event.getStart().getDatetime())));
						LOG.info("Date fin: {}", dfMs.format(df.parse(event.getEnd().getDatetime())));

						LOG.info("Attendees: {}", event.getAttendees().size());
						for (Attendees attendee : event.getAttendees()) {
							LOG.info("Attendee: {}", attendee.getEmailAddress().getName() + " - "
									+ attendee.getEmailAddress().getAddress());
						}
						LOG.info("*****************************************************");

						verifyAndDeleteEventByRoom(result.getAccessToken(), "myseatsas.onmicrosoft.com",
								room.getAddress(), event.getId(), mySeatAPIService.getChairsInGroup(API_KEY, "244"));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void verifyAndDeleteEventByRoom(String accessToken, String tenant, String roomAddress, String eventId,
			Content chairs) throws Exception {
		LOG.info(" [ verifyAndDeleteEventByRoom for this room address {} and this event ID {} ]", roomAddress, eventId);
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.s", Locale.CANADA_FRENCH);
		SimpleDateFormat dfMs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(tz);
		Events events = graphAPIService.getOnlyEventByRoomAddressAndId(accessToken, tenant, roomAddress, eventId);
		boolean deleted = false;
		boolean toRemove = false;
		if (events != null) {
			toRemove = checkSensorState(dfMs.format(df.parse(events.getStart().getDatetime())), chairs);
			if (toRemove)
				deleted = graphAPIService.deleteEventOfRoom(accessToken, tenant, eventId, roomAddress);

			if (deleted)
				LOG.info("The event {} was deleted successfully !", eventId);
		} else
			LOG.info("Events are Empty for this room address {} ", roomAddress);

	}

	public boolean checkSensorState(String dateStartEvent, Content chairs) {
		boolean toRemove = false;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime startDateEvent = LocalDateTime.parse(dateStartEvent, formatter);
		for (Chair chair : chairs.getChairs().getChairs()) {
			LocalDateTime dateLastCommunitationSensor = LocalDateTime.parse(chair.getLast_communication(), formatter);
			long diffInMinute = Duration.between(startDateEvent, dateLastCommunitationSensor).toMinutes();
			LOG.info("Difference between dateStartEvent {} and lastComSensor {} is: {}", startDateEvent,
					dateLastCommunitationSensor, diffInMinute);
			if (diffInMinute >= minuteToStartCanceling) {
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

	public static String getAuthority() {
		return AUTHORITY;
	}

	@Value("${microsoft.graph.api.authority}")
	public void setAuthority(String authority) {
		AUTHORITY = authority;
	}

	public static String getUsername() {
		return USERNAME;
	}

	@Value("${microsoft.graph.api.username}")
	public void setUsername(String username) {
		USERNAME = username;
	}

	public static String getPassword() {
		return PASSWORD;
	}

	@Value("${microsoft.graph.api.password}")
	public void setPassword(String password) {
		PASSWORD = password;
	}

	public static String getClientId() {
		return CLIENT_ID;
	}

	@Value("${microsoft.graph.api.client.id}")
	public void setClientId(String client_id) {
		CLIENT_ID = client_id;
	}

	@Value("${microsoft.graph.api.resource}")
	public static void setResource(String resource) {
		RESOURCE = resource;
	}

	public static String getResource() {
		return RESOURCE;
	}

	public static String getApiKey() {
		return API_KEY;
	}

	@Value("${myseat.api.key}")
	public void setApiKeyY(String api_key) {
		API_KEY = api_key;
	}
}
