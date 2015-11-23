package com.bluetag.search.model;

import java.util.ArrayList;

public class SearchResultsRowModel {
	
	private String id;
	private ArrayList<Integer> order;
	
	private SearchResultsFieldsModel fields;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public ArrayList<Integer> getOrder() {
		return order;
	}
	
	public void setOrder(ArrayList<Integer> order) {
		this.order = order;
	}
	
	public SearchResultsFieldsModel getFields() {
		return fields;
	}
	
	public void setFields(SearchResultsFieldsModel fields) {
		this.fields = fields;
	}
	

}
