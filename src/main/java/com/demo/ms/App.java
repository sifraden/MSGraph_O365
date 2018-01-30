package com.demo.ms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.ServiceUnavailableException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.api.MSGraphAPI;
import com.demo.utils.HttpClientHelper;
import com.demo.utils.JSONHelper;
import com.demo.utils.User;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;

public class App {
	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	private final static String AUTHORITY = "https://login.microsoftonline.com/common";
	private final static String CLIENT_ID = "fae922e0-45d7-4fab-9cc8-0d038c8f1ce1"; // PWD: dgyPCK09*uhbvCCLF171}?*
	private final static String RESOURCE = "https://graph.microsoft.com";
	private final static String USERNAME = "Soufiane@myseatsas.onmicrosoft.com";
	private final static String PASSWORD = "IngoreWegrind00";

	public static void main(String[] args) {
		LOG.info("*****************************************************");
		LOG.info("***************  Starting Process ... ***************");
		LOG.info("*****************************************************");

		MSGraphAPI msGraphApi = new MSGraphAPI();
		AuthenticationResult result;
		try {
			result = msGraphApi.getAccessTokenFromUserCredentials(USERNAME, PASSWORD);
			LOG.info("Access Token - " + result.getAccessToken());
			LOG.info("Refresh Token - " + result.getRefreshToken());
			LOG.info("ID Token - " + result.getIdToken());
			LOG.info("*****************************************************");
			LOG.info("***********************   ROOMS   *******************");
			LOG.info(msGraphApi.getListRooms(result.getAccessToken(), "myseatsas.onmicrosoft.com"));
			LOG.info("*****************************************************");

			LOG.info("***********************   EVENTS   *******************");
			LOG.info(msGraphApi.getAllEvents(result.getAccessToken(), "myseatsas.onmicrosoft.com"));
			LOG.info("*****************************************************");

			LOG.info("******************   EVENTS FOR A ROOM  **************");
			LOG.info(msGraphApi.getAllEventsByRoom(result.getAccessToken(), "myseatsas.onmicrosoft.com",
					"meetingroom2@myseatsas.onmicrosoft.com"));
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

}
