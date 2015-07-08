package com.bluetag.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bluetag.rest.database.DatabaseClass;
import com.bluetag.rest.model.Player;

public class PlayerService {

	private Map<String, Player> players = DatabaseClass.getPlayers();

	public PlayerService() {
	}

	public List<Player> getAllPlayers() {
		return new ArrayList<Player>(players.values());
	}

	public Player getPlayer(String name) {
		if(players.containsKey(name)){
			return players.get(name);
		}
		else{
			return new Player("PLAYER_DOES_NOT_EXIST");
		}
	}

	public Player addPlayer(String name) {
		if (!players.containsKey(name)) {
			Player p = new Player(name);
			players.put(p.getName(), p);
			return p;
		} else {
			return new Player("PLAYER_ALREADY_EXISTS");
		}
	}

	public Player tagPlayer(String name){
		if(players.containsKey(name)){
			Player p = players.get(name);
			p.incrementTags();
			players.put(p.getName(), p);
			return p;
		}
		else{
			return new Player("PLAYER_DOES_NOT_EXIST");
		}
	}
}
