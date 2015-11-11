package com.bluetag.api.search.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bluetag.api.search.service.SearchService;

@Path("/search/users/")
public class SearchResource {
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String searchUsers(@QueryParam("q") String queryString){
		SearchService searchService = new SearchService();
		System.out.println("SearchResource.searchUsers - queryString: "+ queryString);
		return searchService.searchUsers(queryString);
	}
}
