//Класс для обновление данных выбранного персонажа по id

package com.example.myapplication_rickandmorty;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;

import android.content.Intent;
import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import com.example.myapplication_rickandmorty.data.RickandMortyContract.RickandMortyEntry;
import com.example.myapplication_rickandmorty.data.RickandMortyDbHelper;

//диалоговые окна
import com.example.myapplication_rickandmorty.dialogs.UpdateDialog;
import com.example.myapplication_rickandmorty.dialogs.BackDialog;
import com.example.myapplication_rickandmorty.dialogs.BackMainDialog;

public class UpdateData extends FragmentActivity implements OnTouchListener{
	/*для получения/передачи ID персонажа из базы данных, которого мы выбрали,
	  чтобы знать данные какого персонажа нужно вывести*/
	public static final String KEY_ID = "key_id";
	
	private Button buttonUpdate;
	private Button buttonBack;
	private Button buttonMain;
	
	private ImageView avatarImage;
	
	private EditText editName;
	private EditText editSpecies;
	private EditText editGender;
	private EditText editImageUrl;
	
	private Intent intent;
	
	private RickandMortyDbHelper DbHelper;
	
	//индекс персонажа, данные которого мы хотим изменить
	private int currentId;

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		
		init();
		//получение индекса персонажа для удаления
		getCharacterId();
	}
	
	//начальная инициализация
	public void init(){
		//находим компоненты
		buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
		buttonBack = (Button)findViewById(R.id.buttonBack);
		buttonMain = (Button)findViewById(R.id.buttonMain);
		
		avatarImage = (ImageView)findViewById(R.id.avatarImage);
		
		editName = (EditText)findViewById(R.id.editName);
		editSpecies = (EditText)findViewById(R.id.editSpecies);
		editGender = (EditText)findViewById(R.id.editGender);
		editImageUrl = (EditText)findViewById(R.id.editImageUrl);
		
		//устанавливаем слушателей
		buttonUpdate.setOnTouchListener(this);
		buttonBack.setOnTouchListener(this);
		buttonMain.setOnTouchListener(this);
		
		//создаем базу данных
		DbHelper = new RickandMortyDbHelper(this);
	}
	
	//получаем id персонажа, который мы передали в это окно
	public void getCharacterId(){
		intent = getIntent();
		
		currentId = intent.getIntExtra(KEY_ID, -1);
		
		if(currentId > 0){
			displayDatabaseInfo();
		}else{
			Toast.makeText(
					getApplicationContext(),
					"Ошибка получения ID!",
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
				RickandMortyEntry.COLUMN_IMAGE_URL,
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
				
				Picasso
					.with(getApplicationContext())
					.load(currentImageUrl)
					.placeholder(R.drawable.loadingimage)
					.error(R.drawable.errorimage)
					.into(avatarImage);
				
				//устанавливаем данные в поля, чтобы потом их изменять
				editImageUrl.setText(currentImageUrl);
				editName.setText(currentName);
				editSpecies.setText(currentSpecies);
				editGender.setText(currentGender);
			}
			
		}catch(Exception ex){
			
		}finally{
			db.close();
			cursor.close();
		}
	}
	
	//обновление данных персонажа в базе данных
	public void updateCharacter(){
		//берем базу данных для записи
		SQLiteDatabase db = DbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		//получаем данные из полей
		String tempImageUrl = editImageUrl.getText().toString().trim();
		String tempName = editName.getText().toString().trim();
		String tempSpecies = editSpecies.getText().toString().trim();
		String tempGender = editGender.getText().toString().trim();
		
		/*если все данные полей заполнены, то обновляем данные
		  в противном случае сообщаем, что нужно заполнить все поля*/
		if(tempImageUrl.length() > 0 && tempName.length() > 0 && 
		   tempSpecies.length() > 0 && tempGender.length() > 0){
			
			//устанавливаем обновленные данные
			values.put(RickandMortyEntry.COLUMN_NAME, tempName);
			values.put(RickandMortyEntry.COLUMN_SPECIES, tempSpecies);
			values.put(RickandMortyEntry.COLUMN_GENDER, tempGender);
			values.put(RickandMortyEntry.COLUMN_IMAGE_URL, tempImageUrl);
			
			//далем запрос: UPDATE TABLE_NAME SET values WHERE ID = currentId
			db.update(
					RickandMortyEntry.TABLE_NAME,
					values,
					RickandMortyEntry._ID + "=?",
					new String[]{"" + currentId}
				);
			
			Toast.makeText(
					getApplicationContext(),
					"Обновлено!",
					Toast.LENGTH_LONG
				).show();
			
			//после обновления - возвращаемся на предыдущий экран
			intent = new Intent(this, CurrentData.class);
			
			//не забыв передать id персонажа, которого мы обновили
			intent.putExtra(KEY_ID, currentId);
			startActivity(intent);
			
		}else{
			Toast.makeText(
					getApplicationContext(),
					"Заполните все поля!",
					Toast.LENGTH_LONG
				).show();
		}
		
	}
	
	/*метод, который вызывается после подтверждения [в диалоговом окне]
	  для возврата на предыдущий экран*/
	public void setBack(){
		intent = new Intent(this, CurrentData.class);
		
		//не забываем передать id персонажа
		intent.putExtra(KEY_ID, currentId);
		
		startActivity(intent);
	}
	
	/*метод, который вызывается после подтверждения [в диалоговом окне]
	  для возврата на главный экран*/
	public void setBackMain(){
		intent = new Intent(this, Main.class);
		startActivity(intent);
	}
	
	public boolean onTouch(View view, MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//кнопка для обновления данных
			if(view == buttonUpdate){
				//диалоговое окно для подтверждения обновления
				FragmentManager manager = getSupportFragmentManager();
				UpdateDialog dialog = new UpdateDialog();
				dialog.show(manager, "update_dialog");
			}
			
			//кнопка возврата на предыдущее окно
			if(view == buttonBack){
				//диалоговое окно для подтверждения возврата
				FragmentManager manager = getSupportFragmentManager();
				BackDialog dialog = new BackDialog();
				dialog.show(manager, "back_dialog");
			}
			
			//кнопка возврата на главный экран
			if(view == buttonMain){
				//диалоговое окно для подтверждения возврата
				FragmentManager manager = getSupportFragmentManager();
				BackMainDialog dialog = new BackMainDialog();
				dialog.show(manager, "back_main_dialog");
			}
		}
		
		return false;
	}
}