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

package com.bluetag.search.model;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/* 
 * The representation of one row of user table search results
 */
public class SearchRowModel {
	public String id;
	public int[] order;
	
	public SearchRowFieldsModel fields; 

	// simple test
	public static void main(String[] args) {
		SearchRowModel row = new SearchRowModel();
		row.id = "rowId";
		row.order = new int[2];
		row.order[0] = 1;
		row.order[1] = 2;
		//row.fields = new ArrayList<String>(Arrays.asList("name","tom")); // TODO not right -
		row.fields = new SearchRowFieldsModel();
		row.fields.name = "aName";
		// TODO comes out as [name,tom] instead of [name:tom]
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        System.out.println("SearchRowModel: " + gson.toJson(row));		
	}
}
