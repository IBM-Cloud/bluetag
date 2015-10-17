package com.bluetag.api.search.service;

import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SearchService {
	private static final Logger log = Logger.getLogger(SearchService.class.getName());
	private String failJson = "{\"result\": \"something has gone horribly wrong. Please try again\"}";
	private String authHeaderKey = "Authorization";
	private String toConvert = "9885315c-7077-4788-bb1d-cecd6a3530ff-bluemix:3a27472537c70e3bd9dbf474a06bd0660b4bd08783176d168c2d1f51e1b24943";
	private String authHeaderValue = "Basic "
			+ DatatypeConverter.printBase64Binary(toConvert.getBytes());
	private String acceptHeaderKey = "Accept";
	private String acceptHeaderValue = "application/json";
	private String contentHeaderKey = "Content-Type";
	private String contentHeaderValue = "application/json";
	private String cloudantURI = "https://9885315c-7077-4788-bb1d-cecd6a3530ff-bluemix:3a27472537c70e3bd9dbf474a06bd0660b4bd08783176d168c2d1f51e1b24943@9885315c-7077-4788-bb1d-cecd6a3530ff-bluemix.cloudant.com";
	private String searchPath = "/info/_design/info/_search/nameSearch/?q=";
	
	public String searchUsers(String queryString){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet queryInfoGet = new HttpGet(cloudantURI + searchPath + queryString);
		queryInfoGet.addHeader(authHeaderKey, authHeaderValue);
		queryInfoGet.addHeader(acceptHeaderKey, acceptHeaderValue);
		queryInfoGet.addHeader(contentHeaderKey, contentHeaderValue);
		System.out.println("searchUsers queryString: "  + searchPath + queryString) ;
		try {
			HttpResponse queryInfoResp = httpclient.execute(queryInfoGet); 
			String info = EntityUtils.toString(queryInfoResp.getEntity());
			System.out.println("Entity info: " + info);
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
}
