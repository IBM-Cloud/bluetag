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

package com.bluetag.api.register.resources;

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
