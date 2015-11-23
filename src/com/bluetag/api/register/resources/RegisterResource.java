package com.bluetag.api.register.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bluetag.api.register.service.RegisterService;

@Path("/register")
public class RegisterResource {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String register(String userInfo){
		RegisterService registerService = new RegisterService();
		return registerService.registerUser(userInfo);
	}
}
