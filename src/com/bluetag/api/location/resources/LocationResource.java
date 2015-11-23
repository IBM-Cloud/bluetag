package com.bluetag.api.location.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bluetag.model.LocationModel;
import com.bluetag.api.location.service.LocationService;
import com.google.gson.Gson;

@Path("/location")
public class LocationResource {
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateLocation(String locationInfo){
		LocationService locationService = new LocationService();
		Gson gson = new Gson();
		return locationService.updateLocation(gson.fromJson(locationInfo, LocationModel.class));
	}

}
