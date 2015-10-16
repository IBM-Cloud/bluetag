package com.bluetag.api.search.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bluetag.api.search.service.SearchService;

@Path("/search/users")
public class SearchResource {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{searchString}")
	public String searchUsers(@PathParam("searchString") String searchString){
		SearchService searchService = new SearchService();
		System.out.println("SearchResource.searchUsers - searchString: "+ searchString);
		return searchService.searchUsers(searchString);
	}
}
