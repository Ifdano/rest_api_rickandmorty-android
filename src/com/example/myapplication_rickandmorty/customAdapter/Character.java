//Класс POJO для создания данных персонажей, чтобы поместить их в список

package com.example.myapplication_rickandmorty.customAdapter;

public class Character {

	//данные персонажей
	private String characterName;
	private String characterAvatarUrl;
	
	public Character(String characterName, String characterAvatarUrl){
		this.characterName = characterName;
		this.characterAvatarUrl = characterAvatarUrl;
	}
	
	//получение данных
	public String getCharacterName(){ return characterName; }
	public String getCharacterAvatarUrl(){ return characterAvatarUrl; }
}
