//Класс для отображения данных конкретного персонажа, по id

package com.example.myapplication_rickandmorty;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

//компоненты окна
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//для базы данных
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.content.Intent;

//загрузка изображений с Picasso
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import com.example.myapplication_rickandmorty.data.RickandMortyContract.RickandMortyEntry;
import com.example.myapplication_rickandmorty.data.RickandMortyDbHelper;

//диалоговое окно для удаления персонажа
import com.example.myapplication_rickandmorty.dialogs.DeleteDialogCurrent;

public class CurrentData extends FragmentActivity implements OnTouchListener{
	/*для получения/передачи ID персонажа из базы данных, которого мы выбрали,
	  чтобы знать данные какого персонажа нужно вывести*/
	public static final String KEY_ID = "key_id";
	
	//компоненты
	private Button buttonUpdate;
	private Button buttonDelete;
	private Button buttonBack;
	
	private ImageView avatarImage;
	
	private TextView textName;
	private TextView textSpecies;
	private TextView textGender;
	
	private Intent intent;
	
	//индекс персонажа, который мы выбрали для удаления
	private int currentId;
	
	//база данных
	private RickandMortyDbHelper DbHelper;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current);
		
		init();
		//получение индекса персонажа для удаления
		getCharacterId();
	}
	
	//начальная инициализация
	public void init(){
		//находим компоненты
		buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
		buttonDelete = (Button)findViewById(R.id.buttonDelete);
		buttonBack = (Button)findViewById(R.id.buttonBack);
		
		avatarImage = (ImageView)findViewById(R.id.avatarImage);
		
		textName = (TextView)findViewById(R.id.textName);
		textSpecies = (TextView)findViewById(R.id.textSpecies);
		textGender = (TextView)findViewById(R.id.textGender);
		
		//устанавливаем слушателей
		buttonUpdate.setOnTouchListener(this);
		buttonDelete.setOnTouchListener(this);
		buttonBack.setOnTouchListener(this);
		
		//создаем базу данных
		DbHelper = new RickandMortyDbHelper(this);
	}
	
	//получаем id персонажа, который мы передали в это окно
	public void getCharacterId(){
		intent = getIntent();
		
		//получаем id
		currentId = intent.getIntExtra(KEY_ID, -1);
		
		/*если мы получили id, то вызываем метод для вывода данных из
		 базы данных на экран*/
		if(currentId > 0){
			displayDatabaseInfo();
		}else{
			//если id не получено - сообщаем об этом
			Toast.makeText(
					getApplicationContext(),
					"Ошибка получения ID",
					Toast.LENGTH_LONG
				).show();
		}
	}
	
	//отображение данных персонажа из базы данных
	public void displayDatabaseInfo(){
		SQLiteDatabase db = DbHelper.getReadableDatabase();
		
		String[] projection = {
				RickandMortyEntry.COLUMN_NAME,
				RickandMortyEntry.COLUMN_SPECIES,
				RickandMortyEntry.COLUMN_GENDER,
				RickandMortyEntry.COLUMN_IMAGE_URL
		};
		
		/*выборка из базы данных
		  в нашем случае, мы находим данные персонажа по id*/
		String selection = RickandMortyEntry._ID + "=?";
		String[] selectionArgs = {"" + currentId};
		
		Cursor cursor = db.query(
				RickandMortyEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				null
			);
		
		try{
			
			int nameIndex = cursor.getColumnIndex(RickandMortyEntry.COLUMN_NAME);
			int speciesIndex = cursor.getColumnIndex(RickandMortyEntry.COLUMN_SPECIES);
			int genderIndex = cursor.getColumnIndex(RickandMortyEntry.COLUMN_GENDER);
			int imageUrlIndex = cursor.getColumnIndex(RickandMortyEntry.COLUMN_IMAGE_URL);
			
			while(cursor.moveToNext()){
				String currentName = cursor.getString(nameIndex);
				String currentSpecies = cursor.getString(speciesIndex);
				String currentGender = cursor.getString(genderIndex);
				String currentImageUrl = cursor.getString(imageUrlIndex);
				
				//устанавливаем изображение
				Callback callback = new Callback(){
					public void onSuccess(){}
					
					public void onError(){}
				};
				
				//загружаем изображение по ссылке
				Picasso
					.with(getApplicationContext())
					.load(currentImageUrl)
					.placeholder(R.drawable.loadingimage)
					.error(R.drawable.errorimage)
					.into(avatarImage);
				
				//устанавливаем данные в поля
				textName.setText(currentName);
				textSpecies.setText(currentSpecies);
				textGender.setText(currentGender);
			}
			
		}catch(Exception ex){
			
		}finally{
			db.close();
			cursor.close();
		}
	}
	
	//удаление персонажа
	public void deleteCharacter(){
		SQLiteDatabase db = DbHelper.getWritableDatabase();
		
		//удаляем по id персонажа
		db.delete(
				RickandMortyEntry.TABLE_NAME,
				RickandMortyEntry._ID + "=?",
				new String[]{"" + currentId}
			);
		
		Toast.makeText(
					getApplicationContext(),
					"УДАЛЕНО!",
					Toast.LENGTH_LONG
				).show();
		
		//после удаления - возвращаемся в главное окно
		intent = new Intent(this, Main.class);
		startActivity(intent);
	}
	
	public boolean onTouch(View view, MotionEvent event){
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//кнопка обновления данных персонажа
			if(view == buttonUpdate){
				//переходим в окно UpdateData
				intent = new Intent(this, UpdateData.class);
				
				//передаем id персонажа, данные которого мы хотим обновить
				intent.putExtra(KEY_ID, currentId);
				
				startActivity(intent);
			}
			
			//кнопка удаления персонажа
			if(view == buttonDelete){
				//диалоговое окно для подтверждения удаления
				FragmentManager manager = getSupportFragmentManager();
				DeleteDialogCurrent dialog = new DeleteDialogCurrent();
				dialog.show(manager, "delete_dialog_current");
			}
			
			//кнопка возвращения на главный экран
			if(view == buttonBack){
				intent = new Intent(this, Main.class);
				startActivity(intent);
			}
		}
		
		return false;
	}
}