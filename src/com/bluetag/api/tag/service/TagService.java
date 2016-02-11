package com.bluetag.api.tag.service;

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

import com.bluetag.api.tag.resources.CloudantCredential;
import com.bluetag.model.NewTagModel;
import com.bluetag.model.TagModel;
import com.google.gson.Gson;

public class TagService {
	private String successJson = "{\"result\": \"success\"}";
	private String alreadyTaggedJson = "{\"result\": \"already tagged\"}";
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
	private final static Logger LOGGER = Logger.getLogger(TagService.class.getName());
	public String updateTagged(NewTagModel newtag) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// get old tagged from cloudant
			//in NewTagModel - get_id() returns person initiating tag - getUsername() returns person to tag
			HttpGet oldTagGet = new HttpGet(cloudantURI + "/tag/"
					+ newtag.get_id());
			oldTagGet.addHeader(authHeaderKey, authHeaderValue);
			oldTagGet.addHeader(acceptHeaderKey, acceptHeaderValue);
			oldTagGet.addHeader(contentHeaderKey, contentHeaderValue);
			HttpResponse oldTagResp = httpclient.execute(oldTagGet);
			Gson gson = new Gson();
			TagModel updatedtag = gson.fromJson(
					EntityUtils.toString(oldTagResp.getEntity()),
					TagModel.class);
			httpclient.close();

			
			//check for and don't allow retagging - currently front-end design shouldn't allow for this but needs to be checked on server side as well
			if (updatedtag.getTagged().contains(newtag.getUsername())) {
				LOGGER.info(newtag.getUsername() + " already exists in tagged list for " + updatedtag.get_id());
				return alreadyTaggedJson;
			}
			
			// update array of tagged in updatedtag and update entry in cloudant
			updatedtag.getTagged().add(newtag.getUsername());
			HttpPut updatedTagPut = new HttpPut(cloudantURI + "/tag/"
					+ newtag.get_id());
			updatedTagPut.addHeader(authHeaderKey, authHeaderValue);
			updatedTagPut.addHeader(acceptHeaderKey, acceptHeaderValue);
			updatedTagPut.addHeader(contentHeaderKey, contentHeaderValue);
			updatedTagPut.setEntity(new StringEntity(gson.toJson(updatedtag)));
			httpclient = HttpClients.createDefault();
			HttpResponse updatedTagResp = httpclient.execute(updatedTagPut);
			if (!(updatedTagResp.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED)) {
				String updatedTagEntity = EntityUtils.toString(updatedTagResp
						.getEntity());
				httpclient.close();
				return updatedTagEntity;
			}
			httpclient.close();
			LOGGER.info(newtag.get_id() + " tagged " + newtag.getUsername());
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
