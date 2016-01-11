package com.bluetag.api.register.service;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.bluetag.api.register.resources.CloudantCredential;
import com.bluetag.model.LocationModel;
import com.bluetag.model.MarkitModel;
import com.bluetag.model.TagModel;
import com.bluetag.model.UserModel;
import com.google.gson.Gson;

public class RegisterService {

	private String successJson = "{\"result\": \"success\"}";
	private String alreadyExistsJson = "{\"result\": \"user already exists\"}";
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
	
	
	public String registerUser(String userInfo) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		try {
			// convert json to object
			Gson gson = new Gson();
			UserModel user = gson.fromJson(userInfo, UserModel.class);
			String userID = user.get_id();

			// check if userID already exists in database
			String checkExistsUri = cloudantURI + "/info/" + userID;
			HttpGet checkExistsGet = new HttpGet(checkExistsUri);
			checkExistsGet.addHeader(authHeaderKey, authHeaderValue);
			checkExistsGet.addHeader(acceptHeaderKey, acceptHeaderValue);
			checkExistsGet.addHeader(contentHeaderKey, contentHeaderValue);
			HttpResponse checkExistsResp = httpclient.execute(checkExistsGet);

			// if userID exists, return already exists
			if (checkExistsResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				httpclient.close();
				return alreadyExistsJson;
			}
			httpclient.close();
			
			// otherwise attempt to create user
			HttpPost createUserPost = new HttpPost(cloudantURI + "/info");
			createUserPost.addHeader(authHeaderKey, authHeaderValue);
			createUserPost.addHeader(acceptHeaderKey, acceptHeaderValue);
			createUserPost.addHeader(contentHeaderKey, contentHeaderValue);
			createUserPost.setEntity(new StringEntity(userInfo));
			httpclient = HttpClients.createDefault();
			HttpResponse createUserResp = httpclient.execute(createUserPost);
			if (!(createUserResp.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED)) {
				String createUserEntity = EntityUtils.toString(createUserResp.getEntity());
				httpclient.close();
				return createUserEntity;
			}
			httpclient.close();
			
			// create tagged entry
			HttpPost createTaggedPost = new HttpPost(cloudantURI + "/tag");
			createTaggedPost.addHeader(authHeaderKey, authHeaderValue);
			createTaggedPost.addHeader(acceptHeaderKey, acceptHeaderValue);
			createTaggedPost.addHeader(contentHeaderKey, contentHeaderValue);
			TagModel tagmodel = new TagModel(userID);
			createTaggedPost.setEntity(new StringEntity(gson.toJson(tagmodel)));
			httpclient = HttpClients.createDefault();
			HttpResponse createTaggedResp = httpclient.execute(createTaggedPost);
			if (!(createTaggedResp.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED)) {
				String createTaggedEntity = EntityUtils.toString(createTaggedResp.getEntity());
				httpclient.close();
				return createTaggedEntity;
			}
			httpclient.close();
			
			//create location entry
			HttpPost createLocationPost = new HttpPost(cloudantURI + "/location");
			createLocationPost.addHeader(authHeaderKey, authHeaderValue);
			createLocationPost.addHeader(acceptHeaderKey, acceptHeaderValue);
			createLocationPost.addHeader(contentHeaderKey, contentHeaderValue);
			LocationModel locModel = new LocationModel(userID);
			createLocationPost.setEntity(new StringEntity(gson.toJson(locModel)));
			httpclient = HttpClients.createDefault();
			HttpResponse createLocResp = httpclient.execute(createLocationPost);
			if (!(createLocResp.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED)) {
				String createLocEntity = EntityUtils.toString(createLocResp.getEntity());
				httpclient.close();
				return createLocEntity;
			}
			httpclient.close();
			
			//create marked locations entry
			HttpPost createMarkitPost = new HttpPost(cloudantURI + "/markedlocations");
			createMarkitPost.addHeader(authHeaderKey, authHeaderValue);
			createMarkitPost.addHeader(acceptHeaderKey, acceptHeaderValue);
			createMarkitPost.addHeader(contentHeaderKey, contentHeaderValue);
			MarkitModel markModel = new MarkitModel(userID);
			createMarkitPost.setEntity(new StringEntity(gson.toJson(markModel)));
			httpclient = HttpClients.createDefault();
			HttpResponse createMarkitResponse = httpclient.execute(createMarkitPost);
			if(!(createMarkitResponse.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED)) {
				String createMarkitEntity = EntityUtils.toString(createMarkitResponse.getEntity());
				httpclient.close();
				return createMarkitEntity;
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