package com.demo.api;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.ServiceUnavailableException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.demo.model.msgraph.Rooms;
import com.demo.utils.HttpClientHelper;
import com.demo.utils.JSONHelper;
import com.demo.utils.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;

public class MSGraphAPI {
	
    private final static String AUTHORITY = "https://login.microsoftonline.com/common";
    private final static String CLIENT_ID = "fae922e0-45d7-4fab-9cc8-0d038c8f1ce1"; // PWD: dgyPCK09*uhbvCCLF171}?*
    private final static String RESOURCE = "https://graph.microsoft.com";
    private final static String USERNAME = "Soufiane@myseatsas.onmicrosoft.com";
    private final static String PASSWORD = "IngoreWegrind00";
    
    
	public MSGraphAPI() {
	}
	
    public AuthenticationResult getAccessTokenFromUserCredentials(
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
    
	public String getUsernamesFromGraph(String accessToken, String tenant) throws Exception {
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
	
	public String getListRooms(String accessToken, String tenant) throws Exception {
		URL url = new URL(String.format("https://graph.microsoft.com/beta/me/findRooms", tenant,
                accessToken));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // Set the appropriate header fields in the request header.
        //conn.setRequestProperty("api-version", "1.5");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept", "application/json;odata.metadata=full");
        
        String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
        System.out.println("Response for All Rooms - " + goodRespStr);

        ObjectMapper objectMapper = new ObjectMapper();
        Rooms rooms = objectMapper.readValue(goodRespStr, Rooms.class);

        return rooms.toString();
		
	}
	
	public String getAllEvents(String accessToken, String tenant) throws Exception {
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
	
	public boolean deleteEvent(String accessToken, String tenant, String id) throws IOException {
		URL url = new URL(String.format("https://graph.microsoft.com/v1.0/me/events/"+id, tenant,
                accessToken));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // Set the appropriate header fields in the request header.
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        
        String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
        System.out.println("Response for Delete Event - " + goodRespStr);
        
        return true;
	}
	
	public boolean cancelEvent(String accessToken, String tenant, String id) throws IOException {
		URL url = new URL(String.format("https://graph.microsoft.com/beta/me/events/"+id+"/cancel", tenant,
                accessToken));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // Set the appropriate header fields in the request header.
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Content-Type", "application/json");
        String input = "{\"Comment\": \"Cancelling for this week due to all hands\"}";
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        os.write(input.getBytes());
        os.flush();
        //conn.setRequestProperty("Content-Type", "{\"Comment\": \"Cancelling for this week due to all hands\"}");

        
        String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
        System.out.println("Response for Delete Event - " + goodRespStr);
        
        return true;
	}
    
    
    

}