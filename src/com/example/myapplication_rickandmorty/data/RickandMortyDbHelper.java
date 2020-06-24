//Класс для создания базы данных

package com.example.myapplication_rickandmorty.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;

import com.example.myapplication_rickandmorty.data.RickandMortyContract.RickandMortyEntry;

public class RickandMortyDbHelper extends SQLiteOpenHelper{
	//название базы данных
	public static final String DATABASE_NAME = "rickandmorty.db";
	//версия
	public static final int DATABASE_VERSION = 1;
	
	//запрос на создание таблицы в базе данных
	public static final String SQL_TABLE = "CREATE TABLE " + 
			RickandMortyEntry.TABLE_NAME + "(" +
			RickandMortyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			RickandMortyEntry.COLUMN_ID + " INTEGER NOT NULL, " +
			RickandMortyEntry.COLUMN_NAME + " TEXT NOT NULL, " +
			RickandMortyEntry.COLUMN_SPECIES + " TEXT, " +
			RickandMortyEntry.COLUMN_GENDER + " TEXT, " +
			RickandMortyEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL);";
			;
	
	public RickandMortyDbHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		//создаем таблицу
		db.execSQL(SQL_TABLE);
	}

	//обновление базы данных
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		//удаляем таблицу
		db.execSQL("DROP TABLE IF EXISTS " + RickandMortyEntry.TABLE_NAME + ";");
		//создаем новую
		onCreate(db);
	}
}