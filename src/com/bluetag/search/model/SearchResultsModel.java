package com.bluetag.search.model;

import java.util.ArrayList;

public class SearchResultsModel {
	
	private int total_rows;
	private String bookmark;
	
	private ArrayList<SearchResultsRowModel> rows;
	
	public int getTotalRows() {
		return total_rows;
	}
	
	public void setTotalRows(int total_rows) {
		this.total_rows = total_rows;
	}
	
	public String getBookmark() {
		return bookmark;
	}
	
	public void setBookmark(String bookmark) {
		this.bookmark = bookmark;
	}
	
	public ArrayList<SearchResultsRowModel> getRows() {
		return rows;
	}
	
	public void setRows(ArrayList<SearchResultsRowModel> rows) {
		this.rows = rows;
	}

}
