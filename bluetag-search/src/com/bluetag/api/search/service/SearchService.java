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

package com.bluetag.api.search.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.bluetag.api.search.resources.CloudantCredential;
import com.bluetag.search.model.ProcessedSearchResultsModel;
import com.bluetag.search.model.SearchResultsModel;
import com.bluetag.search.model.SearchResultsRowModel;
import com.google.gson.Gson;

public class SearchService {
	private static final Logger log = Logger.getLogger(SearchService.class.getName());
	private String failJson = "{\"result\": \"something has gone horribly wrong. Please try again\"}";
	private String dbAuthFail = "{\"result\": \"Cloudant authorization failure\"}";
	private String authHeaderKey = "Authorization";
	private String acceptHeaderKey = "Accept";
	private String acceptHeaderValue = "application/json";
	private String contentHeaderKey = "Content-Type";
	private String contentHeaderValue = "application/json";
	private String searchPath = "/info/_design/info/_search/nameSearch/?q=";
	private ArrayList<String> searchList = new ArrayList<String>();
	private ProcessedSearchResultsModel gsonSearchList = new ProcessedSearchResultsModel();
	
	CloudantCredential cc = new CloudantCredential();
	//private String toConvert = cc.getCloudantUsername() + ":" + cc.getCloudantPassword();
	private String authHeaderValue = "Basic " + DatatypeConverter.printBase64Binary((cc.getCloudantUsername() + ":" + cc.getCloudantPassword()).getBytes());
	private String cloudantURI = cc.getCloudantURI();
	
	public String searchUsers(String queryString){

		
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet queryInfoGet = new HttpGet(cloudantURI + searchPath + queryString);
		queryInfoGet.addHeader(authHeaderKey, authHeaderValue);
		queryInfoGet.addHeader(acceptHeaderKey, acceptHeaderValue);
		queryInfoGet.addHeader(contentHeaderKey, contentHeaderValue);
		System.out.println("searchUsers queryString: "  + searchPath + queryString) ;
		try {
			HttpResponse queryInfoResp = httpclient.execute(queryInfoGet); 
			
			Gson gson = new Gson();
			SearchResultsModel gsonSearchResults = gson.fromJson(EntityUtils.toString(queryInfoResp.getEntity()), SearchResultsModel.class);
			searchList.clear();
			for (SearchResultsRowModel row : gsonSearchResults.getRows() ) {
				searchList.add(row.getId());
				System.out.println(searchList);
			}
			
			gsonSearchList.setProcessedResults(searchList);
			
			System.out.println("Entity info: " + gson.toJson(gsonSearchResults));
			System.out.println("Processed search results: " + gson.toJson(searchList));
			httpclient.close();
			return gson.toJson(gsonSearchList);
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
