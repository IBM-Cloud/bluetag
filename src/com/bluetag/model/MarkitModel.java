package com.bluetag.model;

import java.util.ArrayList;

public class MarkitModel {
	public String _id;
	public String _rev;
	public ArrayList<NewMarkedLocationModel> marked;
	
	public MarkitModel(String _id) {
		this._id = _id;
		marked = new ArrayList<NewMarkedLocationModel>();
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
	
	public ArrayList<NewMarkedLocationModel> getMarked() {
		return marked;
	}
	
	public void setMarked(ArrayList<NewMarkedLocationModel> marked) {
		this.marked = marked;
	}
}
