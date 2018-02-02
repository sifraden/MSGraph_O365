package com.demo.ms;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.demo.api.msgraph.MSGraphAPIService;
import com.demo.api.myseat.MySeatAPIService;
import com.demo.model.msgraph.events.Attendees;
import com.demo.model.msgraph.events.Event;
import com.demo.model.msgraph.events.Events;
import com.demo.model.msgraph.rooms.Room;
import com.demo.model.msgraph.rooms.Rooms;
import com.microsoft.aad.adal4j.AuthenticationResult;

@Configuration
@ComponentScan(basePackages = "com.demo")
@PropertySource("classpath:application.properties")
public class App extends TimerTask {
	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	private static String AUTHORITY;
	private static String CLIENT_ID; // PWD: dgyPCK09*uhbvCCLF171}?*
	private static String RESOURCE;
	private static String USERNAME;
	private static String PASSWORD;
	private static String API_KEY;
	
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public static void main(String[] args) {

		Timer timer = new Timer();
		timer.schedule(new App(), 0, 1000 * 60);
		
		//ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		//executor.scheduleAtFixedRate(new App(), 0, 60, TimeUnit.SECONDS);
	}


	@Override
	public void run() {
		LOG.info("*****************************************************");
		LOG.info("***************  Starting Process ... ***************");
		LOG.info("*****************************************************");
		ApplicationContext context = new AnnotationConfigApplicationContext(App.class);
		MSGraphAPIService msGraphApi = context.getBean(MSGraphAPIService.class);
		MySeatAPIService myseatApi = context.getBean(MySeatAPIService.class);

		AuthenticationResult result;
		try {
			result = msGraphApi.getAccessTokenFromUserCredentials(getUSERNAME(), getPASSWORD());
			LOG.info("Access Token - {}", result.getAccessToken());
			LOG.info("Refresh Token - {}", result.getRefreshToken());
			LOG.info("ID Token - {}", result.getIdToken());
			LOG.info("MYSEAT getChairs {}", myseatApi.getChairs(API_KEY));
			
			LOG.info("MYSEAT getChairsInGroup {}", myseatApi.getChairsInGroup(API_KEY, "244"));

			LOG.info("*****************************************************");
			LOG.info("***********************   ROOMS   *******************");
			Rooms rooms = msGraphApi.getListRooms(result.getAccessToken(), "myseatsas.onmicrosoft.com");
			StringBuilder sb = new StringBuilder();
			for (Room room : rooms.getRooms()) {
				sb.append("\nName: " + room.getName() + "\n");
				sb.append("Address: " + room.getAddress() + "\n");
				sb.append("Type: " + room.getType());

			}
			LOG.info(sb.toString());
			LOG.info("*****************************************************");

			LOG.info("***********************   EVENTS   *******************");
			Events events = msGraphApi.getAllEvents(result.getAccessToken(), "myseatsas.onmicrosoft.com");
			StringBuilder sbEvents = new StringBuilder();
			LOG.info("Events return {}", events.getEvents().size());
			for (Event event : events.getEvents()) {
				sb.append("\nID: " + event.getId());
				sb.append("\nSubject: " + event.getSubject());
				sb.append("\nOrganizer: " + event.getOrganizer().getEmailAddress().getName() + " - "
						+ event.getOrganizer().getEmailAddress().getAddress());
				sb.append("\nDate début: " + event.getStart().getDatetime());
				sb.append("\nDate fin: " + event.getEnd().getDatetime());
				sb.append("\nAttendees: " + event.getAttendees().size());
				for (Attendees attendee : event.getAttendees()) {
					sb.append("\nAttendee: " + attendee.getEmailAddress().getName() + " - "
							+ attendee.getEmailAddress().getAddress());
				}

			}
			LOG.info(sbEvents.toString());
			LOG.info("*****************************************************");

			LOG.info("******************   EVENTS FOR A ROOM  **************");
			Events eventsForRoom = msGraphApi.getAllEventsByRoom(result.getAccessToken(), "myseatsas.onmicrosoft.com",
					"meetingroom2@myseatsas.onmicrosoft.com");
			StringBuilder sbEventForRoom = new StringBuilder();
			LOG.info("Events return {}", eventsForRoom.getEvents().size());
			for (Event event : eventsForRoom.getEvents()) {
				sb.append("\nID: " + event.getId());
				sb.append("\nSubject: " + event.getSubject());
				sb.append("\nOrganizer: " + event.getOrganizer().getEmailAddress().getName() + " - "
						+ event.getOrganizer().getEmailAddress().getAddress());
				sb.append("\nDate début: " + event.getStart().getDatetime());
				sb.append("\nDate fin: " + event.getEnd().getDatetime());
				sb.append("\nAttendees: " + event.getAttendees().size());
				for (Attendees attendee : event.getAttendees()) {
					sb.append("\nAttendee: " + attendee.getEmailAddress().getName() + " - "
							+ attendee.getEmailAddress().getAddress());
				}

			}
			LOG.info(sbEventForRoom.toString());
			LOG.info("*****************************************************");

			// LOG.info("******************** DELETE EVENT ****************");
			// LOG.info("Delete events - " + msGraphApi.deleteEvent(result.getAccessToken(),
			// "myseatsas.onmicrosoft.com",
			// "AAMkADkyMDg4Zjk2LThhZjAtNDc4Mi05M2VjLTZjMWZjYjdkNzdmNABGAAAAAAAHXEJrEOrfQIvHEY4DAEATBwA6X8Ke3ThcSb0X2vhIiqCTAAAAAAENAAA6X8Ke3ThcSb0X2vhIiqCTAAAJQQY4AAA="));
			// LOG.info("*****************************************************");

			LOG.info("**************   DELETE EVENT OF A ROOM  *************");
			LOG.info("Delete events of a room - " + msGraphApi.deleteEventOfRoom(result.getAccessToken(),
					"myseatsas.onmicrosoft.com",
					"AAMkADc1MGU2YzY4LTRmYmItNGFkMC04NWJhLWMyN2ZiMWY0NjRkNABGAAAAAACaUNJInvR4To1pe8tf_0DwBwBKXwdtgSEoQp-cjax6mKnZAAAAAAENAABKXwdtgSEoQp-cjax6mKnZAAAJMiC4AAA=",
					"meetingroom2@myseatsas.onmicrosoft.com"));
			LOG.info("*****************************************************");

			LOG.info("*****************************************************");
			LOG.info("***************  Finish Process. ***************");
			LOG.info("*****************************************************");

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static String getAUTHORITY() {
		return AUTHORITY;
	}

	@Value( "${microsoft.graph.api.authority}" )
	public void setAUTHORITY(String aUTHORITY) {
		AUTHORITY = aUTHORITY;
	}

	public static String getUSERNAME() {
		return USERNAME;
	}

	@Value( "${microsoft.graph.api.username}" )
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public static String getPASSWORD() {
		return PASSWORD;
	}

	@Value( "${microsoft.graph.api.password}" )
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public static String getClientId() {
		return CLIENT_ID;
	}
	public static String getCLIENT_ID() {
		return CLIENT_ID;
	}

	@Value( "${microsoft.graph.api.client.id}" )
	public void setCLIENT_ID(String cLIENT_ID) {
		CLIENT_ID = cLIENT_ID;
	}

	public static String getRESOURCE() {
		return RESOURCE;
	}

	@Value( "${microsoft.graph.api.resource}" )
	public static void setRESOURCE(String rESOURCE) {
		RESOURCE = rESOURCE;
	}

	public static String getResource() {
		return RESOURCE;
	}

	public static String getAPI_KEY() {
		return API_KEY;
	}

	@Value( "${myseat.api.key}" )
	public void setAPI_KEY(String aPI_KEY) {
		API_KEY = aPI_KEY;
	}
	
	
}
