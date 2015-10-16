package com.bluetag.api.admin.resources;

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

import com.bluetag.api.admin.service.LocationService;
import com.bluetag.api.admin.model.LocationModel;
import com.google.gson.Gson;

@ApplicationScoped
@ServerEndpoint(value = "/wsLocationResource")
public class WSLocationResource {
	private Session currentSession = null;
	
	private final String updateLocation = "/UPDATE";
	
	LocationService locationService;
	Gson gson;
	
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
		
		return locationService.updateLocation(gson.fromJson(location, LocationModel.class));
	}
	
	@OnError
	public void onError (Throwable error) {
		
	}
	
	@OnClose
	public void onClose(Session session, CloseReason reason) {
		System.out.println("Session " + session.getId() + " has ended. Reason: " + reason);
	}
}
