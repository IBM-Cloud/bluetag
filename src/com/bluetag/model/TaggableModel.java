package com.bluetag.model;

import java.util.ArrayList;

public class TaggableModel {
	private ArrayList<String> taggable;
	private ArrayList<String> distances;

	public TaggableModel(ArrayList<String> taggable){
		this.taggable = taggable;
	}

	public TaggableModel(ArrayList<String> taggable, ArrayList<String> distances){
		this.distances = distances;
		this.taggable = taggable;
	}
	
	public ArrayList<String> getTaggable() {
		return taggable;
	}

	public void setTaggable(ArrayList<String> taggable) {
		this.taggable = taggable;
	}
	
	public ArrayList<String> getDistances() {
		return distances;
	}

	public void setDistances(ArrayList<String> distances) {
		this.distances = distances;
	}
}
