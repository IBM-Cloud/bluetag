package com.bluetag.search.model;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/* Search results as stored and retrieved from Cloudant have the following json:
 *  
 * SearchModel 
 * 	{total_rows,  
 * 	 bookmark,
 *   rows:
 *  	[SearchModelRow]
 *  }
 *
 * Example of Search results:
 * 
 *	{ "total_rows":2,
 *	  "bookmark":"really-long-uuid",
 *	  "rows":
 *		[
 *	        {"id":"tom",
 *	         "order":[1.0,0],
 *	         "fields":
 *	        	{"name":"tom"}
 *			},
 *	        {"id":"tom.seelbach@gmail.com",
 *	         "order":[1.0,1],
 *	         "fields":
 *	        	{"name":"tom.seelbach@gmail.com"}
 *	        }
 *		]
 *	}
*/
/*
 * The representation of User database search results
 */
public class SearchModel {
	public Gson gson = new Gson();
	public String total_rows;
	public String bookmark;
	public ArrayList<SearchRowModel> rows;
	
	public String toString() {
		return gson.toJson(this);
	}

	//TODO decide if setter/getter and private fields is better
	//public SearchModel(String _id){
	//	this._id = _id;
	//	tagged = new ArrayList<String>();
	//}
	//public String get_id() {
	//	return _id;
	//}
	//public void set_id(String _id) {
	//	this._id = _id;
	//}
	
	// simple test
	public static void main(String[] args) {		
		
		SearchModel search = new SearchModel();
		search.total_rows = "2";
		search.bookmark = "aBookmark";
		
		SearchRowModel row = new SearchRowModel();
		row.id = "rowId";
		row.order = new int[2];
		row.order[0] = 1;
		row.order[1] = 2;
		//row.fields = new ArrayList<String>(Arrays.asList("name","tom")); // TODO not right?
		//row.fields = "fields";
		row.fields = new SearchRowFieldsModel();
		row.fields.name = "aName";
		search.rows = new ArrayList<SearchRowModel>();
		search.rows.add(row); // = new ArrayList<SearchRowModel>(row);
		
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        System.out.println("SearchModel: " + gson.toJson(search));
	}
}
