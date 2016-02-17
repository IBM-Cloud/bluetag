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

package com.bluetag.api.tag.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.bluetag.api.tag.service.MarkitService;
import com.bluetag.model.NewMarkedLocationModel;
import com.google.gson.Gson;

@Path("/markit")
public class MarkitResource {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/marked/{username}")
	public String getMarkedLocs(@PathParam("username") String username) {
		MarkitService markitService = new MarkitService();
		 
		return markitService.getMarkedLocs(username);
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/newmark")
	public String updateMarked(String markitInfo) {
		MarkitService markitService = new MarkitService();
		Gson gson = new Gson();
		return markitService.updateMarked(gson.fromJson(markitInfo, NewMarkedLocationModel.class));
		
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/removemark")
	public String deleteMarked(String markitInfo) {
		MarkitService markitService = new MarkitService();
		Gson gson = new Gson();
		return markitService.deleteMarked(gson.fromJson(markitInfo, NewMarkedLocationModel.class));
		
	}

}