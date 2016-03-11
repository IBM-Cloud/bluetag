package com.bluetag.api.location.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bluetag.api.location.service.LocationService;

@Path("/getlocation/{username}")
public class LocationResource {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String updateLocation(@PathParam("username") String username){
		LocationService locationService = new LocationService();
		return locationService.getLocation(username);
	}

}
