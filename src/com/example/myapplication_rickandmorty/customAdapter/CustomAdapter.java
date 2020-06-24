//Класс для кастомного адаптера

package com.example.myapplication_rickandmorty.customAdapter;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.ArrayAdapter;

import android.widget.TextView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import java.util.ArrayList;

import android.util.Log;

import com.example.myapplication_rickandmorty.R;

public class CustomAdapter extends ArrayAdapter{
	
	//данные для передачи адаптеру
	//контекст
	private Context context;
	//макет с кастомным списком
	private int layoutId;
	//массив с элементами для списка
	private ArrayList<Character> characters;
	
	//в конструктор передаем контекст, макет списка и массив с данными
	public CustomAdapter(Context context, int layoutId, ArrayList<Character> characters){
		super(context, layoutId, characters);
		
		//получаем данные
		this.context = context;
		this.layoutId = layoutId;
		this.characters = characters;
	}
	
	//метод для установка данных персонажей в ячейки списка
	public View getView(int position, View customView, ViewGroup container){
		//определяем макет списка и его пустые ячейки
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(layoutId, container, false);
		
		//берем объекты из списка
		Character character = characters.get(position);
		
		//находим компоненты из макета списка
		TextView text = (TextView)view.findViewById(R.id.text);
		ImageView image = (ImageView)view.findViewById(R.id.image);
		
		//устанавливаем данные объектов [аватарки и имена] в поля
		//устанавливаем имена
		text.setText(character.getCharacterName());
		
		//создаем Callback для нашего Picasso
		Callback callback = new Callback(){
			public void onSuccess(){
				Log.v("IMAGE LOAD", "SUCCESS");
			}
			
			public void onError(){
				Log.v("IMAGE LOAD", "ERROR");
			}
		};
		
		//устанавливаем аватарки
		Picasso
			.with(context)
			.load(character.getCharacterAvatarUrl())
			.placeholder(R.drawable.loadingimage)
			.error(R.drawable.errorimage)
			.into(image, callback);
		
		return view;
	}
}
