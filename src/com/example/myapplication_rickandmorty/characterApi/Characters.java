//Класс POJO для данных из Json

package com.example.myapplication_rickandmorty.characterApi;

import com.google.gson.annotations.SerializedName;

public class Characters {
	@SerializedName("results")
	private Results[] results;
	
	public Characters(){}
	
	public Results[] getResults(){ return results; }
}