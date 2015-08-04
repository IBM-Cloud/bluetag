package com.bluetag.api.register.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bluetag.api.register.service.QueryService;

@Path("/query")
public class QueryResource {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{username}")
	public String getUserInfo(@PathParam("username") String username){
		QueryService queryService = new QueryService();
		return queryService.getUserInfo(username);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/tagged/{username}")
	public String getTagged(@PathParam("username") String username){
		QueryService queryService = new QueryService();
		return queryService.getTagged(username);
	}
}
