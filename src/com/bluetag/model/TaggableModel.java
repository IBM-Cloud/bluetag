package com.bluetag.model;

import java.util.ArrayList;

public class TaggableModel {
	private ArrayList<LocationModel> taggable;
	private ArrayList<String> distances;

	public TaggableModel(ArrayList<LocationModel> taggable){
		this.taggable = taggable;
	}

	public TaggableModel(ArrayList<LocationModel> taggable, ArrayList<String> distances){
		this.distances = distances;
		this.taggable = taggable;
	}
	
	public ArrayList<LocationModel> getTaggable() {
		return taggable;
	}

	public void setTaggable(ArrayList<LocationModel> taggable) {
		this.taggable = taggable;
	}
	
	public ArrayList<String> getDistances() {
		return distances;
	}

	public void setDistances(ArrayList<String> distances) {
		this.distances = distances;
	}
}
