//Запрос по api

package com.example.myapplication_rickandmorty.characterApi;

import retrofit2.http.GET;
import retrofit2.http.Query;

import io.reactivex.Flowable;

public interface CharactersAPI {
	//общий запрос будет: https://rickandmortyapi.com/api/character/?page=23
	@GET("character/")
	Flowable<Characters> getCharacters(
			@Query("page") int page 
		);
}