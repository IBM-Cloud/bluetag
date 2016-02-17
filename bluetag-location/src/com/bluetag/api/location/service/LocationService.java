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

package com.bluetag.api.location.service;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.bluetag.api.location.resources.CloudantCredential;
import com.bluetag.model.LocationModel;
import com.bluetag.model.UserModel;
import com.google.gson.Gson;

public class LocationService {

	private String successJson = "{\"result\": \"success\"}";
	private String failJson = "{\"result\": \"something has gone horribly wrong. Please try again\"}";
	private String closePlayerSuccess = "{\"result\": \"set location to 0\"}";
	private String closePlayerFail = "{\"result\": \"failed trying to set final location\"}";
	private String authHeaderKey = "Authorization";
	private String acceptHeaderKey = "Accept";
	private String acceptHeaderValue = "application/json";
	private String contentHeaderKey = "Content-Type";
	private String contentHeaderValue = "application/json";

	CloudantCredential cc = new CloudantCredential();
	//private String toConvert = cc.getCloudantUsername() + ":" + cc.getCloudantPassword();
	private String authHeaderValue = "Basic " + DatatypeConverter.printBase64Binary((cc.getCloudantUsername() + ":" + cc.getCloudantPassword()).getBytes());
	private String cloudantURI = cc.getCloudantURI();
	

	public String updateLocation(LocationModel newloc){

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// get old location from cloudant
			HttpGet oldLocGet = new HttpGet(cloudantURI + "/location/"
					+ newloc.get_id());
			//authHeaderValue.replace("\n","");
			//System.out.println("VKVK = "+ authHeaderValue);
			oldLocGet.addHeader(authHeaderKey, authHeaderValue);
			oldLocGet.addHeader(acceptHeaderKey, acceptHeaderValue);
			oldLocGet.addHeader(contentHeaderKey, contentHeaderValue);
			HttpResponse oldLocResp = httpclient.execute(oldLocGet);
			Gson gson = new Gson();
			LocationModel oldloc = gson.fromJson(
					EntityUtils.toString(oldLocResp.getEntity()),
					LocationModel.class);
			httpclient.close();
			
			// update _rev and update entry in cloudant
			newloc.set_rev(oldloc.get_rev());
			HttpPut newLocPut = new HttpPut(cloudantURI + "/location/" + newloc.get_id());
			newLocPut.addHeader(authHeaderKey, authHeaderValue);
			newLocPut.addHeader(acceptHeaderKey, acceptHeaderValue);
			newLocPut.addHeader(contentHeaderKey, contentHeaderValue);
			newLocPut.setEntity(new StringEntity(gson.toJson(newloc)));
			httpclient = HttpClients.createDefault();
			HttpResponse newLocResp = httpclient.execute(newLocPut);
			if (!(newLocResp.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED)) {
				String newLocEntity = EntityUtils.toString(newLocResp.getEntity());
				httpclient.close();
				return newLocEntity;
			}
			httpclient.close();
			
			return successJson;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				httpclient.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return failJson;
		}
	}
	
	
	public String getLocation(String playerToFind) {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		Gson gson = new Gson();
		
		String playerToFindLoc = null;
		try {
			HttpGet locGet = new HttpGet(cloudantURI + "/location/" + playerToFind);
			locGet.addHeader(authHeaderKey, authHeaderValue);
			locGet.addHeader(acceptHeaderKey, acceptHeaderValue);
			locGet.addHeader(contentHeaderKey, contentHeaderValue);
		
			HttpResponse locGetResp = httpclient.execute(locGet);	
			playerToFindLoc = EntityUtils.toString(locGetResp.getEntity());
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return playerToFindLoc;
	}
	
	public String sendClosingLocation(String playerToClose) {
		
		System.out.println("ClosingLocation entry");

		
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// get old location from cloudant
			HttpGet oldLocGet = new HttpGet(cloudantURI + "/location/"
					+ playerToClose);

			oldLocGet.addHeader(authHeaderKey, authHeaderValue);
			oldLocGet.addHeader(acceptHeaderKey, acceptHeaderValue);
			oldLocGet.addHeader(contentHeaderKey, contentHeaderValue);
			HttpResponse oldLocResp = httpclient.execute(oldLocGet);
			Gson gson = new Gson();
			LocationModel oldloc = gson.fromJson(
					EntityUtils.toString(oldLocResp.getEntity()),
					LocationModel.class);
			httpclient.close();
			
			// update _rev and update entry in cloudant
			oldloc.set_id(playerToClose);
			oldloc.setAltitude(0);
			oldloc.setLatitude(0);
			oldloc.setLongitude(0);
			oldloc.set_rev(oldloc.get_rev());
			
			System.out.println("Closing Location on: " + oldloc.get_id() + " with " 
					+ oldloc.getLatitude() + " " + oldloc.getLongitude());
					
			
			HttpPut closingLocPut = new HttpPut(cloudantURI + "/location/" + playerToClose);
			closingLocPut.addHeader(authHeaderKey, authHeaderValue);
			closingLocPut.addHeader(acceptHeaderKey, acceptHeaderValue);
			closingLocPut.addHeader(contentHeaderKey, contentHeaderValue);
			closingLocPut.setEntity(new StringEntity(gson.toJson(oldloc)));
			httpclient = HttpClients.createDefault();
			HttpResponse closingLocResp = httpclient.execute(closingLocPut);
			if (!(closingLocResp.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED)) {
				String closingLocEntity = EntityUtils.toString(closingLocResp.getEntity());
				httpclient.close();
				System.out.println(closingLocEntity);
				//return closingLocEntity;
			}
			httpclient.close();
			
			return closePlayerSuccess;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				httpclient.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return closePlayerFail;
		}
	}

}
