// COPYRIGHT LICENSE: This information contains sample code provided in source
// code form. You may copy, modify, and distribute these sample programs in any 
// form without payment to IBM for the purposes of developing, using, marketing 
// or distributing application programs conforming to the application programming 
// interface for the operating platform for which the sample code is written. 
// 
// Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE 
// ON AN "AS IS" BASIS AND IBM DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING, 
// BUT NOT LIMITED TO, ANY IMPLIED WARRANTIES OR CONDITIONS OF MERCHANTABILITY, 
// SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND ANY WARRANTY OR 
// CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR
// OPERATION OF THE SAMPLE SOURCE CODE. IBM HAS NO OBLIGATION TO PROVIDE
// MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS OR MODIFICATIONS TO THE SAMPLE
// SOURCE CODE.
// 
// (C) Copyright IBM Corp. 2015.
// 
//All Rights Reserved. Licensed Materials - Property of IBM. 

package com.bluetag.api.game.database;

import java.util.ArrayList;
import java.util.HashMap;

import com.bluetag.model.LocationModel;

public class DatabaseClass {
	private static HashMap<String, ArrayList<LocationModel>> taggableDB = new HashMap<String, ArrayList<LocationModel>>();
	private static HashMap<String, ArrayList<String>> distancesDB = new HashMap<String, ArrayList<String>>();

	public static synchronized void setTaggabledDB(HashMap<String, ArrayList<LocationModel>> addDB) {
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
	
	public static synchronized ArrayList<LocationModel> getTaggableList(String username){
		return taggableDB.get(username);
	}
	
	public static synchronized ArrayList<String> getDistancesList(String username){
		return distancesDB.get(username);
	}
}
