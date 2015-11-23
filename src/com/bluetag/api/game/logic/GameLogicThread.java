package com.bluetag.api.game.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.bluetag.api.game.database.DatabaseClass;
import com.bluetag.model.LocationModel;
import com.bluetag.model.TagModel;
import com.bluetag.model.AllLocationDocsModel;
import com.bluetag.model.AllTagDocsModel;
import com.bluetag.model.LocationRowModel;
import com.bluetag.model.TagRowModel;
import com.google.gson.Gson;

public class GameLogicThread extends Thread {
	private final static Logger LOGGER = Logger.getLogger(GameLogicThread.class.getName());
	private static String authHeaderKey = "Authorization";
	private static String toConvert = "9885315c-7077-4788-bb1d-cecd6a3530ff-bluemix:3a27472537c70e3bd9dbf474a06bd0660b4bd08783176d168c2d1f51e1b24943";
	private static String authHeaderValue = "Basic "
			+ DatatypeConverter.printBase64Binary(toConvert.getBytes());
	private static String acceptHeaderKey = "Accept";
	private static String acceptHeaderValue = "application/json";
	private static String contentHeaderKey = "Content-Type";
	private static String contentHeaderValue = "application/json";
	private static String cloudantURI = "https://9885315c-7077-4788-bb1d-cecd6a3530ff-bluemix:3a27472537c70e3bd9dbf474a06bd0660b4bd08783176d168c2d1f51e1b24943@9885315c-7077-4788-bb1d-cecd6a3530ff-bluemix.cloudant.com";

	HashMap<String, ArrayList<String>> taggableDB = new HashMap<String, ArrayList<String>>();
	HashMap<String, ArrayList<String>> distancesDB = new HashMap<String, ArrayList<String>>();
	private final int maxTaggableDistance = 10;

	public void run() {

		while (true) {
			CloseableHttpClient httpclient = HttpClientBuilder.create().setMaxConnPerRoute(100).setMaxConnTotal(100).build();
			HashMap<String, ArrayList<String>> taggableDB = new HashMap<String, ArrayList<String>>();
			HashMap<String, ArrayList<String>> distancesDB = new HashMap<String, ArrayList<String>>();
			try {
				// get all location info from cloudantDB
				HashMap<String, ArrayList<String>> taggedDB = getTagged(httpclient);
				LOGGER.info("Tagged: " + taggedDB.toString());
				HttpGet locInfoGet = new HttpGet(cloudantURI
						+ "/location/_all_docs?include_docs=true");
				locInfoGet.addHeader(authHeaderKey, authHeaderValue);
				locInfoGet.addHeader(acceptHeaderKey, acceptHeaderValue);
				locInfoGet.addHeader(contentHeaderKey, contentHeaderValue);
				HttpResponse locInfoResp = httpclient.execute(locInfoGet);
				Gson gson = new Gson();
				AllLocationDocsModel allDocs = gson.fromJson(
						EntityUtils.toString(locInfoResp.getEntity()),
						AllLocationDocsModel.class);
				httpclient.close();
				
				for (LocationRowModel row1 : allDocs.getRows()) {
					LocationModel loc1 = row1.getDoc();
					taggableDB.put(loc1.get_id(), new ArrayList<String>());
					distancesDB.put(loc1.get_id(), new ArrayList<String>());
					for (LocationRowModel row2 : allDocs.getRows()) {
						LocationModel loc2 = (LocationModel) row2.getDoc();
						// if person2 is standing less than 3 meters away, add
						// them to taggable
						if (!loc1.get_id().equals(loc2.get_id())) {
							
							
							double d = distance(loc1.getLatitude(),
									loc2.getLatitude(),
									loc1.getLongitude(),
									loc2.getLongitude(),
									0, 0);
							//LOGGER.info("Distance between " + loc1.get_id() + " and " + loc2.get_id() + " is " + d);
										
							if (d<= maxTaggableDistance) {
								if (taggedDB.get(loc1.get_id()) != null && !taggedDB.get(loc1.get_id()).contains(loc2.get_id()))  {
									taggableDB.get(loc1.get_id()).add(loc2.get_id());
									distancesDB.get(loc1.get_id()).add(truncate(new Double(distance(loc1.getLatitude(), loc2.getLatitude(), loc1.getLongitude(), loc2.getLongitude(),0, 0)).toString()));				
								}	
							}
						}
					}
				}
				LOGGER.info("Taggable: " + taggableDB.toString());
				LOGGER.info("Distances: " + distancesDB.toString());

				// update global database
				DatabaseClass.setTaggabledDB(taggableDB);
				DatabaseClass.setDistancesDB(distancesDB);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static HashMap<String, ArrayList<String>> getTagged(CloseableHttpClient httpclient){
		HashMap<String, ArrayList<String>> tagged = new HashMap<String, ArrayList<String>>();
		HttpGet tagInfoGet = new HttpGet(cloudantURI
				+ "/tag/_all_docs?include_docs=true");
		tagInfoGet.addHeader(authHeaderKey, authHeaderValue);
		tagInfoGet.addHeader(acceptHeaderKey, acceptHeaderValue);
		tagInfoGet.addHeader(contentHeaderKey, contentHeaderValue);
		try {
			HttpResponse tagInfoResp = httpclient.execute(tagInfoGet);
			Gson gson = new Gson();
			AllTagDocsModel allDocs = gson.fromJson(
					EntityUtils.toString(tagInfoResp.getEntity()),
					AllTagDocsModel.class);
			for(TagRowModel row1 : allDocs.getRows()){
				TagModel tag1 = row1.getDoc();
				tagged.put(tag1.get_id(), tag1.getTagged());
			}
			return tagged;
		} catch (Exception e) {
			e.printStackTrace();
			return tagged;
		}
	}
	
	private static String truncate(String str){
	boolean done=false;
	int i = 0;
	while(!done){
		if(str.charAt(i) == '.'){
			i=i+1;
			done=true;
		}
		i++;
	}
	return str.substring(0, i);
	}

	/*
	 * Calculate distance between two points in latitude and longitude taking
	 * into account height difference. If you are not interested in height
	 * difference pass 0.0. Uses Haversine method as its base.
	 * 
	 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
	 * el2 End altitude in meters
	 * 
	 * @returns Distance in Meters
	 */
	private double distance(double lat1, double lat2, double lon1, double lon2,
			double el1, double el2) {

		final int R = 6371; // Radius of the earth

		Double latDistance = Math.toRadians(lat2 - lat1);
		Double lonDistance = Math.toRadians(lon2 - lon1);
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2)
				* Math.sin(lonDistance / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c * 1000; // convert to meters

		double height = el1 - el2;

		distance = Math.pow(distance, 2) + Math.pow(height, 2);

		return Math.sqrt(distance);
	}
}