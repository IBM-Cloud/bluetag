package com.bluetag.api.admin.model;

public class NewTagModel {
	private String _id;
	private String username;
	
	public NewTagModel(){
		
	}
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
