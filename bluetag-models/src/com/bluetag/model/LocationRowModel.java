package com.bluetag.model;

import com.bluetag.model.LocationModel;

public class LocationRowModel {
	private String id;
	private String key;
	private Value value;
	private LocationModel doc;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public LocationModel getDoc() {
		return doc;
	}
	public void setDoc(LocationModel doc) {
		this.doc = doc;
	}
	
	@SuppressWarnings("unused")
	private class Value{
		private String rev;
	
		public String getRev() {
			return rev;
		}

		public void setRev(String rev) {
			this.rev = rev;
		}
	}
}
