//Класс POJO для ключа results из Json

package com.example.myapplication_rickandmorty.characterApi;

import com.google.gson.annotations.SerializedName;

public class Results {
	@SerializedName("id")
	private int id;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("species")
	private String species;
	
	@SerializedName("gender")
	private String gender;
	
	@SerializedName("image")
	private String image;
	
	public Results(){}
	
	public int getId(){ return id; }
	public String getName(){ return name; }
	public String getSpecies(){ return species; }
	public String getGender(){ return gender; }
	public String getImage(){ return image; }
}