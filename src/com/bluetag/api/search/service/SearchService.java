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
