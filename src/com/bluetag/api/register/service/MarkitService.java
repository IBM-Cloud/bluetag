package com.bluetag.api.register.service;

import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.bluetag.api.register.resources.CloudantCredential;
import com.bluetag.model.MarkitModel;
import com.bluetag.model.NewMarkedLocationModel;
import com.google.gson.Gson;

public class MarkitService {
	private final static Logger LOGGER = Logger.getLogger(MarkitService.class.getName());
	private String successJson = "{\"result\": \"success\"}";
	private String failJson = "{\"result\": \"something has gone horribly wrong with Markit. Please try again\"}";
	private String authHeaderKey = "Authorization";
	private String acceptHeaderKey = "Accept";
	private String acceptHeaderValue = "application/json";
	private String contentHeaderKey = "Content-Type";
	private String contentHeaderValue = "application/json";
	
	
	CloudantCredential cc = new CloudantCredential();
	private String authHeaderValue = "Basic " + DatatypeConverter.printBase64Binary((cc.getCloudantUsername() + ":" + cc.getCloudantPassword()).getBytes());
	private String cloudantURI = cc.getCloudantURI();
	
	public String getMarkedLocs(String username) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet queryMarkedGet = new HttpGet(cloudantURI + "/markedlocations/" + username);
		queryMarkedGet.addHeader(authHeaderKey, authHeaderValue);
		queryMarkedGet.addHeader(acceptHeaderKey, acceptHeaderValue);
		queryMarkedGet.addHeader(contentHeaderKey, contentHeaderValue);
		
		try {
			HttpResponse queryMarkedResponse = httpclient.execute(queryMarkedGet);
			String marked = EntityUtils.toString(queryMarkedResponse.getEntity());
			httpclient.close();
			return marked;
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
	
	
	public String updateMarked(NewMarkedLocationModel newmarkedloc) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		try {
			//get existing marked array
			HttpGet oldMarkedGet = new HttpGet(cloudantURI + "/markedlocations/" + newmarkedloc.get_id());
			oldMarkedGet.addHeader(authHeaderKey, authHeaderValue);
			oldMarkedGet.addHeader(acceptHeaderKey, acceptHeaderValue);
			oldMarkedGet.addHeader(contentHeaderKey, contentHeaderValue);
			HttpResponse oldMarkedResponse = httpclient.execute(oldMarkedGet);
			Gson gson = new Gson();
			MarkitModel updatedmarked = gson.fromJson(
					EntityUtils.toString(oldMarkedResponse.getEntity()), MarkitModel.class);
			httpclient.close();
			
			//update marked array
			updatedmarked.getMarked().add(newmarkedloc);
			HttpPut updatedMarkitPut = new HttpPut(cloudantURI + "/markedlocations/" + newmarkedloc.get_id());
			updatedMarkitPut.addHeader(authHeaderKey, authHeaderValue);
			updatedMarkitPut.addHeader(acceptHeaderKey, acceptHeaderValue);
			updatedMarkitPut.addHeader(contentHeaderKey, contentHeaderValue);
			//-----The lazy way to process extra "\" characters out of JSON response (to cloudant)
			//The right way to do this: change ArrayList<String> in MarkitModel to ArrayList<NewMarkedLocationModel>
			//Requires debug of previous 'MarkitModel updatedmarked = ...
//			String jsonupdatedmarked = gson.toJson(updatedmarked);
//			String processedupdatedmarked = jsonupdatedmarked.replace("\\", "");
//			String removequotesupdatedmarked = processedupdatedmarked.replace("ed\":[\"", "ed\":[");
//			String fixopeningbracketupdatedmarked = removequotesupdatedmarked.replace("\"{\"", "{\"");
//			String fixclosingbracketupdatedmarked = fixopeningbracketupdatedmarked.replace("}\",", "},");
//			String finalupdatedmarked = fixclosingbracketupdatedmarked.replace("}\"]", "}]");
//			LOGGER.info(finalupdatedmarked);
			//--------------------------------------------------------------------------------------------------------
			updatedMarkitPut.setEntity(new StringEntity(gson.toJson(updatedmarked)));
			httpclient = HttpClients.createDefault();
			HttpResponse updatedMarkitResponse = httpclient.execute(updatedMarkitPut);
			if(!(updatedMarkitResponse.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED)) {
				String updatedMarkitEntity = EntityUtils.toString(updatedMarkitResponse.getEntity());
				httpclient.close();
				return updatedMarkitEntity;
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
