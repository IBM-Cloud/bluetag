package com.bluetag.api.game.database;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseClass {
	private static HashMap<String, ArrayList<String>> taggableDB = new HashMap<String, ArrayList<String>>();
	private static HashMap<String, ArrayList<String>> distancesDB = new HashMap<String, ArrayList<String>>();

	public static synchronized void setTaggabledDB(HashMap<String, ArrayList<String>> addDB) {
		taggableDB.clear(); 
		for(String user: addDB.keySet()){
			taggableDB.put(user, addDB.get(user));
		}
	}
	
	public static synchronized void setDistancesDB(HashMap<String, ArrayList<String>> addDB) {
		distancesDB.clear(); 
		for(String user: addDB.keySet()){
			distancesDB.put(user, addDB.get(user));
		}
	}
	
	public static synchronized ArrayList<String> getTaggableList(String username){
		return taggableDB.get(username);
	}
	
	public static synchronized ArrayList<String> getDistancesList(String username){
		return distancesDB.get(username);
	}
}
