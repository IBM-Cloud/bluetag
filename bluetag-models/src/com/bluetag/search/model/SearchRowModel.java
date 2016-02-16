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
