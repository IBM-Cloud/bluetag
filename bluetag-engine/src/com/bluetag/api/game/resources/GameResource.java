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

package com.bluetag.api.game.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bluetag.api.game.database.DatabaseClass;
import com.bluetag.model.TaggableModel;
import com.google.gson.Gson;

@Path("/taggable")
public class GameResource {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{username}")
	public String getTaggable(@PathParam("username") String username){
		Gson gson = new Gson();
		TaggableModel taggableModel = new TaggableModel(DatabaseClass.getTaggableList(username),DatabaseClass.getDistancesList(username));
		return gson.toJson(taggableModel);
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/count/{username}")
	public String getTaggableCount(@PathParam("username") String username){
		Gson gson = new Gson();
		int count = DatabaseClass.getTaggableList(username).size();
		return gson.toJson(count);
	}
}
