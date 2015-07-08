package com.bluetag.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bluetag.rest.model.Player;
import com.bluetag.rest.service.PlayerService;

@Path("/players")
public class PlayerResource {

	PlayerService playerService = new PlayerService();
	
	@GET 
	@Produces(MediaType.APPLICATION_JSON)
	public List<Player> getPlayers(){
		return playerService.getAllPlayers();
	}
	
	@GET 
	@Path("/{playerName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Player getPlayer(@PathParam("playerName") String name){
		return playerService.getPlayer(name);
	}
	
	@POST 
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Player addPlayer(String name){
		return playerService.addPlayer(name);
	}
	
	@PUT 
	@Path("/{playerName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Player tagPlayer(@PathParam("playerName") String name){
		return playerService.tagPlayer(name);
	}
}
