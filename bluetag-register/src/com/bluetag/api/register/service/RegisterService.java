package com.bluetag.api.register.service;

import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.bluetag.api.register.resources.CloudantCredential;
import com.bluetag.model.AllLocationDocsModel;
import com.bluetag.model.LocationModel;
import com.bluetag.model.LocationRowModel;
import com.bluetag.model.MarkitModel;
import com.bluetag.model.SearchIndexModel;
import com.bluetag.model.TagModel;
import com.bluetag.model.UserModel;
import com.google.gson.Gson;

public class RegisterService {

	private String successJson = "{\"result\": \"success\"}";
	private String alreadyExistsNotInUseJson = "{\"result\": \"user already exists; not in use\"}";
	private String alreadyExistsInUseJson = "{\"result\": \"user already exists; in use\"}";
	private String failJson = "{\"result\": \"something has gone horribly wrong. Please try again\"}";
	private String authHeaderKey = "Authorization";
	private String acceptHeaderKey = "Accept";
	private String acceptHeaderValue = "application/json";
	private String contentHeaderKey = "Content-Type";
	private String contentHeaderValue = "application/json";
	
	private static final Logger LOGGER = Logger.getLogger(CloudantCredential.class.getName());
	
	
	CloudantCredential cc = new CloudantCredential();
	//private String toConvert = cc.getCloudantUsername() + ":" + cc.getCloudantPassword();
	private String authHeaderValue = "Basic " + DatatypeConverter.printBase64Binary((cc.getCloudantUsername() + ":" + cc.getCloudantPassword()).getBytes());
	private String cloudantURI = cc.getCloudantURI();
	
	public void clearLocations() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
	try {
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
			
			for(LocationRowModel row1: allDocs.getRows()) {
				LocationModel loc1 = row1.getDoc();
				LOGGER.info("Resetting for " + loc1.get_id());
				loc1.setLatitude(0.0);
				loc1.setLongitude(0.0);
				loc1.setAltitude(0.0);
				
				
				HttpPut closingLocPut = new HttpPut(cloudantURI + "/location/" + loc1.get_id());
				closingLocPut.addHeader(authHeaderKey, authHeaderValue);
				closingLocPut.addHeader(acceptHeaderKey, acceptHeaderValue);
				closingLocPut.addHeader(contentHeaderKey, contentHeaderValue);
				closingLocPut.setEntity(new StringEntity(gson.toJson(loc1)));
				httpclient = HttpClients.createDefault();
				HttpResponse closingLocResp = httpclient.execute(closingLocPut);
				
				
				
				
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	public String registerUser(String userInfo) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		try {
			// convert json to object
			Gson gson = new Gson();
			UserModel user = gson.fromJson(userInfo, UserModel.class);
			String userID = user.get_id();
			
			//check if DBs exist in cloudant service
			String checkDbExistsUri = cloudantURI + "/_all_dbs";
			HttpGet checkDbExistsGet = new HttpGet(checkDbExistsUri);
			checkDbExistsGet.addHeader(authHeaderKey, authHeaderValue);
			checkDbExistsGet.addHeader(acceptHeaderKey, acceptHeaderValue);
			checkDbExistsGet.addHeader(contentHeaderKey, contentHeaderValue);
			HttpResponse checkExistsDbResp = httpclient.execute(checkDbExistsGet);
			String resp = EntityUtils.toString(checkExistsDbResp.getEntity());
			
			//what DBs exist
			LOGGER.info("DBs are: " + resp);
			
			if (!resp.contains("info") && !resp.contains("location") && !resp.contains("markedlocations") && !resp.contains("tag")) {
				LOGGER.info("One or more dbs do not exist. Creating info, location, markedlocations and tag");
				
				//one or more DBs are missing so create all of them
				
				//PUT request to create info
				HttpPut createInfoDbPut = new HttpPut(cloudantURI + "/info");
				createInfoDbPut.addHeader(authHeaderKey, authHeaderValue);
				createInfoDbPut.addHeader(acceptHeaderKey, acceptHeaderValue);
				createInfoDbPut.addHeader(contentHeaderKey, contentHeaderValue);
				HttpResponse createInfoDbResp = httpclient.execute(createInfoDbPut);
				LOGGER.info(EntityUtils.toString(createInfoDbResp.getEntity()));
				//httpclient.close();
				
				//PUT request to create search index on info
				SearchIndexModel sim = new SearchIndexModel();
				HttpPut createSearchIndexPut = new HttpPut(cloudantURI + "/info/_design/info/");
				createSearchIndexPut.addHeader(authHeaderKey, authHeaderValue);
				createSearchIndexPut.addHeader(acceptHeaderKey, acceptHeaderValue);
				createSearchIndexPut.addHeader(contentHeaderKey, contentHeaderValue);
				createSearchIndexPut.setEntity( new StringEntity(gson.toJson(sim)));
				HttpResponse createSearchIndexPutResp = httpclient.execute(createSearchIndexPut);
				LOGGER.info(EntityUtils.toString(createSearchIndexPutResp.getEntity()));
				
				LOGGER.info("Created info db with search index");
				
				HttpPut createLocationDbPut = new HttpPut(cloudantURI + "/location");
				createLocationDbPut.addHeader(authHeaderKey, authHeaderValue);
				createLocationDbPut.addHeader(acceptHeaderKey, acceptHeaderValue);
				createLocationDbPut.addHeader(contentHeaderKey, contentHeaderValue);
				HttpResponse createLocationsDbResp = httpclient.execute(createLocationDbPut);
				LOGGER.info(EntityUtils.toString(createLocationsDbResp.getEntity()));
				//httpclient.close();
				
				LOGGER.info("Created locations db");
				
				HttpPut createMarkedLocationsDbPut = new HttpPut(cloudantURI + "/markedlocations");
				createMarkedLocationsDbPut.addHeader(authHeaderKey, authHeaderValue);
				createMarkedLocationsDbPut.addHeader(acceptHeaderKey, acceptHeaderValue);
				createMarkedLocationsDbPut.addHeader(contentHeaderKey, contentHeaderValue);
				HttpResponse createMarkedLocationsDbResp = httpclient.execute(createMarkedLocationsDbPut);
				LOGGER.info(EntityUtils.toString(createMarkedLocationsDbResp.getEntity()));
				//httpclient.close();
				
				LOGGER.info("Created markedlocations db");
				
				HttpPut createTagDbPut = new HttpPut(cloudantURI + "/tag");
				createTagDbPut.addHeader(authHeaderKey, authHeaderValue);
				createTagDbPut.addHeader(acceptHeaderKey, acceptHeaderValue);
				createTagDbPut.addHeader(contentHeaderKey, contentHeaderValue);
				HttpResponse createTagDbResp = httpclient.execute(createTagDbPut);
				LOGGER.info(EntityUtils.toString(createTagDbResp.getEntity()));
				//httpclient.close();
				
				LOGGER.info("Created tag db");
			}

			// check if userID already exists in database
			String checkExistsUri = cloudantURI + "/info/" + userID;
			HttpGet checkExistsGet = new HttpGet(checkExistsUri);
			checkExistsGet.addHeader(authHeaderKey, authHeaderValue);
			checkExistsGet.addHeader(acceptHeaderKey, acceptHeaderValue);
			checkExistsGet.addHeader(contentHeaderKey, contentHeaderValue);
			HttpResponse checkExistsResp = httpclient.execute(checkExistsGet);
			
			//if userID exists - check if it's currently in use by polling user location - if it's something other than 0,0 - consider userID to be in use
		if (checkExistsResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			LOGGER.info(userID + " exits. Checking if this ID is in use");
			
			HttpGet userLocCheckGet = new HttpGet(cloudantURI + "/location/" + userID);
			userLocCheckGet.addHeader(authHeaderKey, authHeaderValue);
			userLocCheckGet.addHeader(acceptHeaderKey, acceptHeaderValue);
			userLocCheckGet.addHeader(contentHeaderKey, contentHeaderValue);
			HttpResponse userLocCheckGetResp = httpclient.execute(userLocCheckGet);
			LocationModel userLocCheck = gson.fromJson(EntityUtils.toString(userLocCheckGetResp.getEntity()), LocationModel.class);
			httpclient.close();
			
			if (userLocCheck.getLatitude() == 0.0 && userLocCheck.getLongitude() == 0.0) {
				LOGGER.info(userID + " exists and is not in use." + " latitude check is " + userLocCheck.getLatitude() + " longitude check is " + userLocCheck.getLongitude());
				return alreadyExistsNotInUseJson;
			} else {
				//user lat and lon are 0,0
				LOGGER.info("User is in use, lat lon are not 0,0: " + "Lat - " + userLocCheck.getLatitude() + " Lon - " + userLocCheck.getLongitude());
				return alreadyExistsInUseJson;
			}
		}
			
//			if (userLocCheck.getLatitude() == 0 && userLocCheck.getLongitude() == 0 && 
//					checkExistsResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
//				//User exists and is not in use
//				return alreadyExistsJson;
//			} else if (checkExistsResp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				// if userID exists, return already exists and in use
//				httpclient.close();
//				return alreadyExistsInUseJson;
//			}
			httpclient.close();
			
			// otherwise attempt to create user
			LOGGER.info(userID + " doesn't exist; going to create");
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
			LOGGER.info("Created info entry for " + userID);
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
			LOGGER.info("Created tag entry for " + userID);
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
			LOGGER.info("Created location entry for " + userID);
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
			LOGGER.info("Created markedlocations entry for " + userID);
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