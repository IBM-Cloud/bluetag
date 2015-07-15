package com.bluetag.api.game.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.bluetag.api.game.database.DatabaseClass;
import com.bluetag.api.game.model.AllDocsModel;
import com.bluetag.api.game.model.CloudantRowModel;
import com.bluetag.api.game.model.LocationModel;
import com.google.gson.Gson;

public class GameLogicThread extends Thread {
	private String authHeaderKey = "Authorization";
	private String toConvert = "ce70b172-450a-4bb0-a7b0-1c57c500f8f7-bluemix:c2858c25dda5de8d0679be0fa50e7801046ac74de2fa80dda1cb5cb585150328";
	private String authHeaderValue = "Basic "
			+ DatatypeConverter.printBase64Binary(toConvert.getBytes());
	private String acceptHeaderKey = "Accept";
	private String acceptHeaderValue = "application/json";
	private String contentHeaderKey = "Content-Type";
	private String contentHeaderValue = "application/json";
	private String cloudantURI = "https://ce70b172-450a-4bb0-a7b0-1c57c500f8f7-bluemix:c2858c25dda5de8d0679be0fa50e7801046ac74de2fa80dda1cb5cb585150328@ce70b172-450a-4bb0-a7b0-1c57c500f8f7-bluemix.cloudant.com";

	private HashMap<String, ArrayList<String>> taggableDB = new HashMap<String, ArrayList<String>>();
	private final int maxTaggableDistance = 10;

	public void run() {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			// get all location info from cloudantDB
			HttpGet locInfoGet = new HttpGet(cloudantURI
					+ "/location/_all_docs?include_docs=true");
			locInfoGet.addHeader(authHeaderKey, authHeaderValue);
			locInfoGet.addHeader(acceptHeaderKey, acceptHeaderValue);
			locInfoGet.addHeader(contentHeaderKey, contentHeaderValue);
			HttpResponse locInfoResp = httpclient.execute(locInfoGet);
			Gson gson = new Gson();
			AllDocsModel allDocs = gson.fromJson(
					EntityUtils.toString(locInfoResp.getEntity()),
					AllDocsModel.class);
			httpclient.close();

			// clear taggedDB and start updating
			taggableDB = new HashMap<String, ArrayList<String>>();

			for (CloudantRowModel row1 : allDocs.getRows()) {
				LocationModel loc1 = row1.getDoc();
				taggableDB.put(loc1.get_id(), new ArrayList<String>());
				for (CloudantRowModel row2 : allDocs.getRows()) {
					LocationModel loc2 = row2.getDoc();
					// if person2 is standing less than 3 meters away, add them
					// to taggable
					if (!loc1.get_id().equals(loc2.get_id())
							&& distance(loc1.getLatitude(), loc2.getLatitude(),
									loc1.getLongitude(), loc2.getLongitude(),
									loc1.getAltitude(), loc2.getAltitude()) <= maxTaggableDistance) {
						taggableDB.get(loc1.get_id()).add(loc2.get_id());
					}
				}
			}

			// update global database
			DatabaseClass.setTaggabledDB(taggableDB);

		} catch (Exception e) {
			try {
				httpclient.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

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
