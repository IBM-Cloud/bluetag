package com.bluetag.api.game.database;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseClass {
	private static HashMap<String, ArrayList<String>> taggableDB = new HashMap<String, ArrayList<String>>();

	public static synchronized void setTaggabledDB(HashMap<String, ArrayList<String>> taggedDB) {
		DatabaseClass.taggableDB = taggedDB;
	} 
	
	public static synchronized ArrayList<String> getTaggableList(String username){
		return taggableDB.get(username);
	}
}
