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

package com.bluetag.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Permission;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.bluetag.search.model.SearchModel;
import com.bluetag.search.model.SearchRowModel;
import com.google.gson.Gson;


public class Search {
	private static final String hostname = "http://bluetagsearch1.mybluemix.net";
	private static final String method = "/api/search/users/";
	private static final String query = "?q=name:tom*";
	private URL url;
	private HttpURLConnection conn;
	private InputStream in;
	private OutputStream out;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		if (in != null) { in.close(); }
	    if (out != null) { out.close(); }
	    if (conn != null) { conn.disconnect(); }
	}

	
	/**
	 * corresponding URL: http://localhost:7650/autoStats/AutoStatService/all
	 * @throws Exception
	 */
	@Test
	public final void testGetSearchUsers() throws Exception {
		Gson gson = new Gson();
		SearchModel sm = null;
		SearchRowModel srm = null;
		url = new URL(hostname + method + query);
	    conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.connect();
	    in = conn.getInputStream();
	 
	    testHttpURLConnection(conn); //TODO handle exceptions from this call
	 
	    assertTrue(conn.getContentType().equals("application/json"));
    	Object content =conn.getContent();
    	//System.out.println ("Content type: " + content.getClass().getName() );
    	//System.out.println ("Content.toString(): " + content.toString());
    	
    	// read the output from the server
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
   
        String line = null;
        while ((line = reader.readLine()) != null) {
          stringBuilder.append(line + "\n");
        }
        // ensure we can build the SearchModel and Row from json, 
        // and that it contains the the expected row 
        System.out.println("stringBuilder: " + stringBuilder.toString());
        try {
        	sm = gson.fromJson(stringBuilder.toString(), SearchModel.class);
        	System.out.println("SearchModel: " + sm.toString() );
        } catch (Exception e){
        	System.out.println("E" + e);
        	fail();
        }
        assertNotNull(sm.rows);
    	for ( SearchRowModel s :sm.rows) {
    		System.out.println ("s.id: " + s.id);
    		if (s.id.equals("tom")) {
    			return;		
    		}
    		fail("tom not found");
    	}
	 }
	/**
	 * @param connection
	 * @throws Exception
	 */
	 private void testHttpURLConnection(HttpURLConnection connection) throws Exception {
	     boolean connAllowUserInteraction = connection.getAllowUserInteraction();
	     String connContentType = connection.getContentType();
	     String connContentEncoding = connection.getContentEncoding();
	     String connRequestMethod = connection.getRequestMethod();
	     boolean connDoInput = connection.getDoInput();
	     boolean connDoOutput = connection.getDoOutput();
	     Permission connPermission = connection.getPermission();
	     URL connURL = connection.getURL();
	     Map<String, List<String>> connHeaderFields = connection.getHeaderFields();
	 
	     System.out.println("connAllowUserInteraction: " + connAllowUserInteraction);
	     System.out.println("connContentType: " + connContentType);
	     System.out.println("connContentEncoding: " + connContentEncoding);
	     System.out.println("connRequestMethod: " + connRequestMethod);
	     System.out.println("connDoInput: " + connDoInput);
	     System.out.println("connDoOutput: " + connDoOutput);
	     System.out.println("connPermission: " + connPermission);
	     System.out.println("connURL: " + connURL);
	 
	     if (connHeaderFields != null) {
	         Set<Entry<String, List<String>>> connHeaderFieldsEntries = connHeaderFields.entrySet();
	         for (Entry<String, List<String>> entry : connHeaderFieldsEntries) {
	             System.out.println("connHeaderField: " + entry);
	         }
	     }
	 }
}
