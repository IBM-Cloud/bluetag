package com.bluetag.model;

public class LocationModel {
	private String _id;
	private String _rev;
	private double latitude; 
	private double longitude;
	private double altitude;
	private double distance;
	
	public LocationModel(String _id){
		this._id = _id;
		this.latitude = 0;
		this.longitude = 0;
		this.altitude = 0;
		this.distance = 0;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
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

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	
	public String get_rev() {
		return _rev;
	}

	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	} 
}