package com.bluetag.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Player {
	private String name;
	private int tags; 
	
	public Player(){
		
	}
	
	public Player(String name){
		this.name = name;
		this.tags = 0;
	}
	
	public String getName(){
		return name;
	}
	
	public int getTags(){
		return tags;
	}
	
	public void incrementTags(){
		tags = tags + 1;
	}

}
