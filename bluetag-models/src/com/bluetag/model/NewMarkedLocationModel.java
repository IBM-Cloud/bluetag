package com.bluetag.model;

public class NewMarkedLocationModel {
	private String _id;
	private String locname;
	private double latitude;
	private double longitude;
	
	public NewMarkedLocationModel() {
		
	}
	
	public String get_id() {
		return _id;
	}
	
	public void set_id() {
		this._id = _id;
	}
	
	public String getLocname() {
		return locname;
	}
	
	public void setLocname(String locname) {
		this.locname = locname;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude() {
		this.longitude = longitude;
	}

}
