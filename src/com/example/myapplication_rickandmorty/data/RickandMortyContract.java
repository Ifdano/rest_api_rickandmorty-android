//Класс с данные по таблицами базы данных

package com.example.myapplication_rickandmorty.data;

import android.provider.BaseColumns;

public final class RickandMortyContract {

	//класс с данными таблицы базы данных
	public static final class RickandMortyEntry implements BaseColumns{
		//название таблицы
		public static final String TABLE_NAME = "characters";
		
		//названия столбцов
		public static final String _ID = BaseColumns._ID;
		public static final String COLUMN_ID = "id_main";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_SPECIES = "species";
		public static final String COLUMN_GENDER = "gender";
		public static final String COLUMN_IMAGE_URL = "avatar_url";
	}
}