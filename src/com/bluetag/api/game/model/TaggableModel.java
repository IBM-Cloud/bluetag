package com.bluetag.api.game.model;

import java.util.ArrayList;

public class TaggableModel {
	private ArrayList<String> taggable;

	public TaggableModel(ArrayList<String> taggable){
		this.taggable = taggable;
	}
	
	public ArrayList<String> getTaggable() {
		return taggable;
	}

	public void setTaggable(ArrayList<String> taggable) {
		this.taggable = taggable;
	}
}
