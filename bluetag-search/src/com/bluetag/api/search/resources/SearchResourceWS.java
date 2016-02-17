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