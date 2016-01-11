package com.bluetag.api.register.service;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.bluetag.api.register.resources.CloudantCredential;

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
