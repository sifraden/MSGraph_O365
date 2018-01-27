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


public class App 
{
	private static final Logger LOG = LoggerFactory.getLogger(App.class);		

    private final static String AUTHORITY = "https://login.microsoftonline.com/common";
    private final static String CLIENT_ID = "fae922e0-45d7-4fab-9cc8-0d038c8f1ce1"; // PWD: dgyPCK09*uhbvCCLF171}?*
    private final static String RESOURCE = "https://graph.microsoft.com";
    private final static String USERNAME = "Soufiane@myseatsas.onmicrosoft.com";
    private final static String PASSWORD = "IngoreWegrind00";
    
    
    public static void main( String[] args )
    {
    		LOG.info("*****************************************************");
    		LOG.info("***************  Starting Process ... ***************");

    		MSGraphAPI msGraphApi = new MSGraphAPI();
    		AuthenticationResult result;
    		 try {
 				result = msGraphApi.getAccessTokenFromUserCredentials(
 						USERNAME, PASSWORD);
 				LOG.info("Access Token - " + result.getAccessToken());
 				LOG.info("Refresh Token - " + result.getRefreshToken());
 				LOG.info("ID Token - " + result.getIdToken());
 	    			LOG.info("*****************************************************");

 	    			LOG.info("Rooms - " + msGraphApi.getListRooms(result.getAccessToken(), "myseatsas.onmicrosoft.com"));
 	    			LOG.info("Events - " + msGraphApi.getAllEvents(result.getAccessToken(), "myseatsas.onmicrosoft.com"));
 	    			LOG.info("Delete events - " + msGraphApi.cancelEvent(result.getAccessToken(), "myseatsas.onmicrosoft.com", "AAMkADU4YTVhMzFhLWI1MWQtNDQzMy04MmI3LTU0ZDUzNDdhNzY5YwBGAAAAAAB0LSQqtZF4TISKEhFm1VeXBwDkUx4kEA4WTbFMqc9mcB_2AAAAAAENAADkUx4kEA4WTbFMqc9mcB_2AAAAy5UfAAA="));
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
    		
    		
/*		AuthenticationResult result;

 * 			try {
				result = getAccessTokenFromUserCredentials(
						USERNAME, PASSWORD);
	            System.out.println("Access Token - " + result.getAccessToken());
	            System.out.println("Refresh Token - " + result.getRefreshToken());
	            System.out.println("ID Token - " + result.getIdToken());
	            
	            System.out.println("Users - " + getListRooms(result.getAccessToken(), "myseatsas.onmicrosoft.com"));
	            System.out.println("Events - " + getAllEvents(result.getAccessToken(), "myseatsas.onmicrosoft.com"));
	            System.out.println("Delete events - " + deleteEvent(result.getAccessToken(), "myseatsas.onmicrosoft.com", "AAMkADU4YTVhMzFhLWI1MWQtNDQzMy04MmI3LTU0ZDUzNDdhNzY5YwBGAAAAAAB0LSQqtZF4TISKEhFm1VeXBwDkUx4kEA4WTbFMqc9mcB_2AAAAAAENAADkUx4kEA4WTbFMqc9mcB_2AAAAy5UdAAA="));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
*/
    }
    
/*    private static AuthenticationResult getAccessTokenFromUserCredentials(
            String username, String password) throws Exception {
        AuthenticationContext context = null;
        AuthenticationResult result = null;
        ExecutorService service = null;
        try {
            service = Executors.newFixedThreadPool(1);
            context = new AuthenticationContext(AUTHORITY, false, service);
            Future<AuthenticationResult> future = context.acquireToken(
                    RESOURCE, CLIENT_ID, username, password, null);
            result = future.get();
        } finally {
            service.shutdown();
        }

        if (result == null) {
            throw new ServiceUnavailableException(
                    "authentication result was null");
        }
        return result;
    }
    
	private static String getUsernamesFromGraph(String accessToken, String tenant) throws Exception {
        URL url = new URL(String.format("https://graph.windows.net/%s/users?api-version=1.5", tenant,
                accessToken));

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
	
	private static String getListRooms(String accessToken, String tenant) throws Exception {
		URL url = new URL(String.format("https://graph.microsoft.com/beta/me/findRooms", tenant,
                accessToken));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // Set the appropriate header fields in the request header.
        //conn.setRequestProperty("api-version", "1.5");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept", "application/json;odata.metadata=full");
        
        String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
        System.out.println("Response for All Rooms - " + goodRespStr);

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
	
	private static String getAllEvents(String accessToken, String tenant) throws Exception {
		URL url = new URL(String.format("https://graph.microsoft.com/v1.0/me/events?$select=subject,body,bodyPreview,organizer,attendees,start,end,location", tenant,
                accessToken));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // Set the appropriate header fields in the request header.
        //conn.setRequestProperty("api-version", "1.5");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept", "application/json;odata.metadata=full");
        //conn.setRequestProperty("Prefer", "outlook.timezone=\"Pacific Standard Time\"");

        
        String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
        System.out.println("Response for All Events - " + goodRespStr);

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
	
	private static boolean deleteEvent(String accessToken, String tenant, String id) throws IOException {
		URL url = new URL(String.format("https://graph.microsoft.com/v1.0/me/events/"+id, tenant,
                accessToken));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // Set the appropriate header fields in the request header.
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        
        String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
        System.out.println("Response for Delete Event - " + goodRespStr);
        
        return true;
	}*/
	
	
}
