package com.bluetag.api.tag.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bluetag.model.NewTagModel;
import com.bluetag.api.tag.service.TagService;
import com.google.gson.Gson;

@Path("/tag")
public class TagResource {
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateTagged(String tagInfo){
		TagService tagService = new TagService();
		Gson gson = new Gson();
		return tagService.updateTagged(gson.fromJson(tagInfo, NewTagModel.class));
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/untag")
	public String unTag(String tagInfo) {
		TagService tagService = new TagService();
		Gson gson = new Gson();
		return tagService.unTag(gson.fromJson(tagInfo, NewTagModel.class));
	}
}
