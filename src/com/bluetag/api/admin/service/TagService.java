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

import com.bluetag.api.admin.model.NewTagModel;
import com.bluetag.api.admin.model.TagModel;
import com.google.gson.Gson;

public class TagService {
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

	public String updateTagged(NewTagModel newtag) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// get old tagged from cloudant
			HttpGet oldTagGet = new HttpGet(cloudantURI + "/tag/"
					+ newtag.get_id());
			oldTagGet.addHeader(authHeaderKey, authHeaderValue);
			oldTagGet.addHeader(acceptHeaderKey, acceptHeaderValue);
			oldTagGet.addHeader(contentHeaderKey, contentHeaderValue);
			HttpResponse oldTagResp = httpclient.execute(oldTagGet);
			Gson gson = new Gson();
			TagModel updatedtag = gson.fromJson(
					EntityUtils.toString(oldTagResp.getEntity()),
					TagModel.class);
			httpclient.close();

			// update array of tagged in updatedtag and update entry in cloudant
			updatedtag.getTagged().add(newtag.getUsername());
			HttpPut updatedTagPut = new HttpPut(cloudantURI + "/tag/"
					+ newtag.get_id());
			updatedTagPut.addHeader(authHeaderKey, authHeaderValue);
			updatedTagPut.addHeader(acceptHeaderKey, acceptHeaderValue);
			updatedTagPut.addHeader(contentHeaderKey, contentHeaderValue);
			updatedTagPut.setEntity(new StringEntity(gson.toJson(updatedtag)));
			httpclient = HttpClients.createDefault();
			HttpResponse updatedTagResp = httpclient.execute(updatedTagPut);
			if (!(updatedTagResp.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED)) {
				String updatedTagEntity = EntityUtils.toString(updatedTagResp
						.getEntity());
				httpclient.close();
				return updatedTagEntity;
			}
			httpclient.close();

			return successJson;
		} catch (Exception e) {
			try {
				httpclient.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return failJson;
		}
	}
}
