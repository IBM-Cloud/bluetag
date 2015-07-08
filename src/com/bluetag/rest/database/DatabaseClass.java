package com.bluetag.rest.database;

import java.util.HashMap;
import java.util.Map;

import com.bluetag.rest.model.Player;

public class DatabaseClass {
	
	private static Map<String, Player> players = new HashMap<String, Player>();
	
	public static Map<String, Player> getPlayers(){
		return players;
	}
	
}
