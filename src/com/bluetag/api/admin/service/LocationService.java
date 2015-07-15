package com.bluetag.api.admin.service;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.bluetag.api.admin.model.LocationModel;
import com.google.gson.Gson;

public class LocationService {

	private String successJson = "{\"result\": \"success\"}";
	private String failJson = "{\"result\": \"something has gone horribly wrong. Please try again\"}";
	private String authHeaderKey = "Authorization";
	private String toConvert = "ce70b172-450a-4bb0-a7b0-1c57c500f8f7-bluemix:c2858c25dda5de8d0679be0fa50e7801046ac74de2fa80dda1cb5cb585150328";
	private String authHeaderValue = "Basic "
			+ DatatypeConverter.printBase64Binary(toConvert.getBytes());
	private String acceptHeaderKey = "Accept";
	private String acceptHeaderValue = "application/json";
	private String contentHeaderKey = "Content-Type";
	private String contentHeaderValue = "application/json";
	private String cloudantURI = "https://ce70b172-450a-4bb0-a7b0-1c57c500f8f7-bluemix:c2858c25dda5de8d0679be0fa50e7801046ac74de2fa80dda1cb5cb585150328@ce70b172-450a-4bb0-a7b0-1c57c500f8f7-bluemix.cloudant.com";

	public String updateLocation(LocationModel newloc){

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// get old location from cloudant
			HttpGet oldLocGet = new HttpGet(cloudantURI + "/location/"
					+ newloc.get_id());
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

}
