package com.bluetag.api.admin.model;

import java.util.ArrayList;

public class TagModel {
	private String _id;
	private String _rev;
	private ArrayList<String> tagged;
	
	public TagModel(){
	}
	
	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_rev() {
		return _rev;
	}

	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	public ArrayList<String> getTagged() {
		return tagged;
	}

	public void setTagged(ArrayList<String> tagged) {
		this.tagged = tagged;
	}
}
