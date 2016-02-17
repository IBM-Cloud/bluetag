// COPYRIGHT LICENSE: This information contains sample code provided in source
// code form. You may copy, modify, and distribute these sample programs in any 
// form without payment to IBM for the purposes of developing, using, marketing 
// or distributing application programs conforming to the application programming 
// interface for the operating platform for which the sample code is written. 
// 
// Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE 
// ON AN "AS IS" BASIS AND IBM DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING, 
// BUT NOT LIMITED TO, ANY IMPLIED WARRANTIES OR CONDITIONS OF MERCHANTABILITY, 
// SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND ANY WARRANTY OR 
// CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR
// OPERATION OF THE SAMPLE SOURCE CODE. IBM HAS NO OBLIGATION TO PROVIDE
// MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS OR MODIFICATIONS TO THE SAMPLE
// SOURCE CODE.
// 
// (C) Copyright IBM Corp. 2015.
// 
//All Rights Reserved. Licensed Materials - Property of IBM. 

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
