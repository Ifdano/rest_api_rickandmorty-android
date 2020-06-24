//Класс с реализацией паттерна "Одиночка"

package com.example.myapplication_rickandmorty.characterApi;

//для использования Retrofit
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class CharactersService {
	//основная часть ссылки для запроса по api
	private static final String BASE_URL = "https://rickandmortyapi.com/api/";
	
	private static CharactersService mInstance;
	private Retrofit retrofit;
	
	private CharactersService(){
		//создаем Retrofit
		retrofit = new Retrofit
							.Builder()
							.baseUrl(BASE_URL)
							.addConverterFactory(GsonConverterFactory.create())
							.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
							.build();
	}
	
	//получаем единственный экземпляр
	public static CharactersService getInstance(){
		if(mInstance == null)
			mInstance = new CharactersService();
		
		return mInstance;
	}
	
	//получаем api
	public CharactersAPI getCharactersApi(){
		return retrofit.create(CharactersAPI.class);
	}
}