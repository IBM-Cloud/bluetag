package com.bluetag.api.game.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bluetag.api.game.database.DatabaseClass;
import com.bluetag.api.game.model.TaggableModel;
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
}
