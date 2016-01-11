package com.bluetag.api.location.service;

import java.io.IOException;
import java.util.Base64;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.bluetag.api.location.resources.CloudantCredential;
import com.bluetag.model.LocationModel;
import com.google.gson.Gson;

public class LocationService {

	private String successJson = "{\"result\": \"success\"}";
	private String failJson = "{\"result\": \"something has gone horribly wrong. Please try again\"}";
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

}
