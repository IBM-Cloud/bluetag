package com.bluetag.api.search.resources;

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


import com.bluetag.search.model.SearchModel;
import com.bluetag.api.search.service.SearchService;

import com.google.gson.Gson;

@ApplicationScoped
@ServerEndpoint(value = "/SearchWS")
public class SearchResourceWS {
	private Session currentSession = null;

	//private final String updateLocation = "/UPDATE";

	SearchService searchService;
	Gson gson;

	@OnOpen
	public void onOpen(Session session, EndpointConfig ec) {

		System.out.println(session.getId() + " has opened a connection");
		searchService = new SearchService();
		gson = new Gson();

		try	{
			session.getBasicRemote().sendText("Connection Established");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@OnMessage
	public String onSearch (String searchString, Session session) {
		System.out.println("Search from " + session.getId() + " : " + searchString);
		
		//return searchService.searchUsers(gson.fromJson(searchString, searchStringModel.class));
		//append "name:" and "*" to search string required format for cloudant query 
		return searchService.searchUsers("name:" + searchString + "*");
	}

	@OnError
	public void onError (Throwable error) {
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		System.out.println("Session " + session.getId() + " has ended. Reason: " + reason);
	}
}