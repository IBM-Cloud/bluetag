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

package com.bluetag.api.tag.service;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.bluetag.api.tag.resources.CloudantCredential;

public class QueryService {
	private String failJson = "{\"result\": \"something has gone horribly wrong. Please try again\"}";
	private String authHeaderKey = "Authorization";
	private String acceptHeaderKey = "Accept";
	private String acceptHeaderValue = "application/json";
	private String contentHeaderKey = "Content-Type";
	private String contentHeaderValue = "application/json";
	
	CloudantCredential cc = new CloudantCredential();
	private String authHeaderValue = "Basic " + DatatypeConverter.printBase64Binary((cc.getCloudantUsername() + ":" + cc.getCloudantPassword()).getBytes());
	private String cloudantURI = cc.getCloudantURI();
	
	public String getUserInfo(String username){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet queryInfoGet = new HttpGet(cloudantURI + "/info/" + username);
		queryInfoGet.addHeader(authHeaderKey, authHeaderValue);
		queryInfoGet.addHeader(acceptHeaderKey, acceptHeaderValue);
		queryInfoGet.addHeader(contentHeaderKey, contentHeaderValue);
		
		try {
			HttpResponse queryInfoResp = httpclient.execute(queryInfoGet); 
			String info = EntityUtils.toString(queryInfoResp.getEntity());
			httpclient.close();
			return info;
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
	
	public String getTagged(String username){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet getTaggedGet = new HttpGet(cloudantURI + "/tag/" + username);
		getTaggedGet.addHeader(authHeaderKey, authHeaderValue);
		getTaggedGet.addHeader(acceptHeaderKey, acceptHeaderValue);
		getTaggedGet.addHeader(contentHeaderKey, contentHeaderValue);
		
		try {
			HttpResponse getTaggedResp = httpclient.execute(getTaggedGet); 
			String tagged = EntityUtils.toString(getTaggedResp.getEntity());
			httpclient.close();
			return tagged;
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
