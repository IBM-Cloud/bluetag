package com.bluetag.api.location.resources;

import java.io.IOException;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import java.util.logging.Logger;



public class CloudantCredential {
	private JSONArray cloudant;
	private JSONObject cloudantInstance;
	private JSONObject cloudantCredentials;
	
	private String cloudantUsername;
	private String cloudantPassword;
	private String cloudantURI;
	
	private static final Logger LOGGER = Logger.getLogger(CloudantCredential.class.getName());
	
	public CloudantCredential () {
		
		//Get VCAP_SERVICES environment varialbe from Bluemix - cloudant service should be bound to this service for
		//environment variable to be present
		//
		//Alternatively - provide a manifest.yml where environment variables are provided
		String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
		
	if (VCAP_SERVICES != null) {	
		try {
			System.out.println("vcap is: " + VCAP_SERVICES);
			JSONObject vcap = (JSONObject) JSONObject.parse(VCAP_SERVICES);
			JSONArray credentials = (JSONArray) vcap.get("cloudantNoSQLDB");
			
			 cloudantInstance = (JSONObject) credentials.get(0);
			 cloudantCredentials = (JSONObject) cloudantInstance.get("credentials");
			
			//System.out.println(cloudantCredentials);
			 LOGGER.info("username is from VCAP_SERVICES: " + cloudantCredentials.get("username").toString());
			 LOGGER.info("password is from VCAP_SERVICES: " + (cloudantPassword == null ? "null" : "******"));
			 
			 
			//System.out.println(cloudantCredentials.get("password"));
			
			cloudantUsername = cloudantCredentials.get("username").toString();
			cloudantPassword = cloudantCredentials.get("password").toString();
			cloudantURI = cloudantCredentials.get("url").toString();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	 } else {
		 
		 //VCAP services does not exist so look for dbUsername, dbPassword and dbURI environment variables
		 //When running locally - these environment variables need to be defined by user. Values should be
		 //from a Cloudant service running in Bluemix
		 cloudantUsername = System.getenv("dbUsername");
		 LOGGER.info("username is from environment variable dbUsername: " + cloudantUsername);
		 cloudantPassword = System.getenv("dbPassword");
		 LOGGER.info("password is from environment variable dbPassword: " + (cloudantPassword == null ? "null" : "*****"));
		 cloudantURI = System.getenv("dbURI");
		
		 
	 }
		
	}
	
	public String getCloudantUsername() {
		return cloudantUsername;
	}
	
	public String getCloudantPassword() {
		return cloudantPassword;
	}
	
	public String getCloudantURI() {
		return cloudantURI;
	}
	


}

