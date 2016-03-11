package com.bluetag.api.location.resources;

import java.io.IOException;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.enterprise.context.ApplicationScoped;

import com.bluetag.api.location.service.LocationService;
import com.bluetag.model.LocationModel;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

@ApplicationScoped
@ServerEndpoint(value = "/wsLocationResource")
public class WSLocationResource {
	private Session currentSession = null;
	
	private final String updateLocation = "/UPDATE";
	
	private String user;
	
	LocationService locationService;
	Gson gson;
	JsonParser jparser = new JsonParser();
	
	@OnOpen
	public void onOpen(Session session, EndpointConfig ec) {
		
		System.out.println(session.getId() + " has opened a connection");
		locationService = new LocationService();
		gson = new Gson();
		
		try	{
			session.getBasicRemote().sendText("Connection Established");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	@OnMessage
	public String onReceiveLocation (String location, Session session) {
		System.out.println("Message from " + session.getId() + " : " + location);
		
		user = jparser.parse(location).getAsJsonObject().get("_id").getAsString();
		//had to have received atleast one location update to know who user is
		System.out.println("Updating location for: " + user);
		
		return locationService.updateLocation(gson.fromJson(location, LocationModel.class));
	}
	
	@OnError
	public void onError (Throwable error) {
		
	}
	
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		System.out.println("Session " + session.getId() + " has ended. Reason: " + reason);
		
		
		if (user != null) {
			locationService.sendClosingLocation(user);
			System.out.println("Closed out user: " + user);
		} else {
			System.out.println("Something went wrong with setting the user");
			
		}
	}
}
