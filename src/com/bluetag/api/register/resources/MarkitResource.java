package com.bluetag.api.register.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bluetag.api.register.service.MarkitService;
import com.bluetag.model.NewMarkedLocationModel;
import com.google.gson.Gson;

@Path("/markit")
public class MarkitResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/marked/{username}")
	public String getMarkedLocs(@PathParam("username") String username) {
		MarkitService markitService = new MarkitService();
		 
		return markitService.getMarkedLocs(username);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/newmark")
	public String updateMarked(String markitInfo) {
		MarkitService markitService = new MarkitService();
		Gson gson = new Gson();
		return markitService.updateMarked(gson.fromJson(markitInfo, NewMarkedLocationModel.class));
		
	}

}
