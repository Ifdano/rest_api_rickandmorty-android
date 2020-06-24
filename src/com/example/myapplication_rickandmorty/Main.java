//Главное окно со списком персонажей

package com.example.myapplication_rickandmorty;

import android.os.Bundle;

//компоненты окна
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Spinner;

//для работы с обычным списком и выпадающим списком
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

//обработка касаний
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

//переход на другое окно
import android.content.Intent;
//для изменения и удаления данных из базы данных
import android.content.ContentValues;

//для работы с базой данных
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.util.Log;

//подписчики
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

//для многопоточности
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.annotations.NonNull;

import java.util.ArrayList;

//для кастомного адаптера списка
import com.example.myapplication_rickandmorty.customAdapter.CustomAdapter;
import com.example.myapplication_rickandmorty.customAdapter.Character;

//для базы данных
import com.example.myapplication_rickandmorty.data.RickandMortyContract.RickandMortyEntry;
import com.example.myapplication_rickandmorty.data.RickandMortyDbHelper;

//диалоговое окно для удаления
import com.example.myapplication_rickandmorty.dialogs.DeleteDialog;

//для получения данных по api
import com.example.myapplication_rickandmorty.characterApi.Characters;
import com.example.myapplication_rickandmorty.characterApi.CharactersService;
import com.example.myapplication_rickandmorty.characterApi.Results;

//основной класс, реализующий интерфейсы обработки касаний экрана, списка и выпадающего списка 
public class Main extends FragmentActivity implements OnTouchListener, OnItemClickListener, OnItemLongClickListener, OnItemSelectedListener{
	/*для передачи ID элемента из базы данных в другое окно,
	 чтобы мы знали данные какого персонажа нужно будет вывести на экран*/ 
	public static final String KEY_ID = "key_id";

	//кнопки полного очищения данных и удаления
	private Button buttonClearData;
	private Button buttonDeleteData;
	
	//список с персонажами
	private ListView listCharacters;
	//массив с данными персонажей
	private ArrayList<Character> characters;
	//кастомный адаптер
	private CustomAdapter customAdapter;
	//для хранения id элементов
	private ArrayList<Integer> charactersId;
	
	//выпадающий список для выбора страницы загрузки
	private Spinner spinnerPages;
	private ArrayAdapter<String> pagesAdapter;
	
	private Intent intent;
	
	//база данных
	private RickandMortyDbHelper DbHelper;
	
	//id персонажа, которого нужно будет удалить
	private int characterIdDelete;
	
	/*страница с персонажами, которую нужно будет вывести
	 в данном API 30 страниц [1-30]*/
	private int pageForDisplayData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//начальная инициализация
		init();
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
		//обнуляем/очищаем массивы данных
		charactersId.clear();
		characters.clear();
		
		//выводим данные из базы данных на экран
		displayDatabaseInfo();
	}
	
	//начальная инициализация
	public void init(){
		//находим компоненты
		buttonClearData = (Button)findViewById(R.id.buttonClear);
		buttonDeleteData = (Button)findViewById(R.id.buttonDelete);
		
		listCharacters = (ListView)findViewById(R.id.listCharacters);
		
		spinnerPages = (Spinner)findViewById(R.id.spinner);
		
		//устанавливаем слушателей
		buttonClearData.setOnTouchListener(this);
		buttonDeleteData.setOnTouchListener(this);
		
		listCharacters.setOnItemClickListener(this);
		listCharacters.setOnItemLongClickListener(this);
		
		spinnerPages.setOnItemSelectedListener(this);
		
		//создаем базу данных
		DbHelper = new RickandMortyDbHelper(this);
		
		//создаеи массивы для хранения персонажей и их id
		characters = new ArrayList<Character>();
		charactersId = new ArrayList<Integer>();
		
		/*установим выпадающий список
		  берем данные из ресурсов, массив pages*/
		String[] pages = getResources().getStringArray(R.array.pages);
		
		/*устанавливаем адаптер
		передаем контекст, наш кастомный макет для выпадающего списка, 
		id текстового поля [в нашем кастомном макете] и массив с данными*/
		pagesAdapter = new ArrayAdapter<String>(
				this,
				R.layout.new_spinner_item,
				R.id.text,
				pages
			);
		
		//связываем массив данных с выпадающим списком
		spinnerPages.setAdapter(pagesAdapter);
		
		/*в самом начале, нет персонажей дла удаления и нет
		  страницы для отображения персонажей*/
		characterIdDelete = -1;
		pageForDisplayData = -1;
	}
	
	//для удаления элемента из базы данных
	public void deleteCharacter(){
		//берем базу данных для записи
		SQLiteDatabase db = DbHelper.getWritableDatabase();
		
		/*если у нас есть персонаж для удаления [если id > 0], 
		 то удаляем его из базы данных*/
		if(characterIdDelete > 0){
			/*удаляем персонажа по id
			 Общий запрос: DELETE FROM TABLE_NAME WHERE ID = characterIdDelete*/ 
			db.delete(
					RickandMortyEntry.TABLE_NAME,
					RickandMortyEntry._ID + "=?",
					new String[]{"" + characterIdDelete}
					);
		
			//обнуляем массивы, чтобы позже обновить содержимое
			characters.clear();
			charactersId.clear();
		
			//сообщаем адаптеру, что данные изменилис
			customAdapter.notifyDataSetChanged();
		
			//заново выводим обновленные данные
			displayDatabaseInfo();
			
			//сообщаем об удалении
			Toast.makeText(
					this,
					"УДАЛЕНО!",
					Toast.LENGTH_LONG
				).show();
			
			//персонажа для удаления пока нет
			characterIdDelete = -1;
		}else{
			/*при ошибке удаления, например когда мы не выбрали  
			  персонажа для удаления - выводим сообщение об ошибке*/
			Toast.makeText(
					this,
					"Ошибка удаления!",
					Toast.LENGTH_LONG
				).show();
		}
	}
	
	//полное очищение персонажей из базы данных
	public void deleteCharactersAll(){
		//берем базу данных для записи
		SQLiteDatabase db = DbHelper.getWritableDatabase();
		
		/*если в базе данных есть данные по персонажам, 
		  значит и массив с id персонажами будет заполнен*/
		if(charactersId.size() > 0){
			/*пробегаемся по всем доступным id персонажей
			  и удаляем по одному
			  Общий запрос: DELETE FROM TABLE_NAME WHERE ID = characterId.get(i)*/
			for(int i = 0; i < charactersId.size(); i++)
			db.delete(
					RickandMortyEntry.TABLE_NAME,
					RickandMortyEntry._ID + "=?",
					new String[]{"" + charactersId.get(i)}
					);
		
			//обнуляем массивы
			characters.clear();
			charactersId.clear();
		
			//сообщаем адаптеру, что данные изменилис
			customAdapter.notifyDataSetChanged();
		
			//заново выводим обновленные данные
			displayDatabaseInfo();
			
			//сообщаем об очищении
			Toast.makeText(
					this,
					"Данные очищены!",
					Toast.LENGTH_LONG
				).show();
		}else{
			/*сообщаем об ошибке, например, когда данных 
			  для удаления вообще нет*/
			Toast.makeText(
					this,
					"Нет данных для удаления!",
					Toast.LENGTH_LONG
				).show();
		}
	}
	
	//отображение данных персонажей из базы данных
	public void displayDatabaseInfo(){
		//берем базу данных для чтения
		SQLiteDatabase db = DbHelper.getReadableDatabase();
		
		/*данные по каким столбцам мы хотим получить?
		  в нашем случае это id по базе данных, id по api, имя персонажа
		  и ссылка на изображение */
		String[] projection = {
				RickandMortyEntry._ID,
				RickandMortyEntry.COLUMN_ID,
				RickandMortyEntry.COLUMN_NAME,
				RickandMortyEntry.COLUMN_IMAGE_URL
		};
		
		//условия вывода
		Cursor cursor = db.query(
					RickandMortyEntry.TABLE_NAME, 
					projection,
					null,
					null,
					null,
					null,
					null
				);
		
		try{
			
			//получаем индексы столбцов
			int idIndex = cursor.getColumnIndex(RickandMortyEntry._ID);
			int idMainIndex = cursor.getColumnIndex(RickandMortyEntry.COLUMN_ID);
			int nameIndex = cursor.getColumnIndex(RickandMortyEntry.COLUMN_NAME);
			int imageIndex = cursor.getColumnIndex(RickandMortyEntry.COLUMN_IMAGE_URL);
			
			//пробегаемся по всем данным из базы данных
			while(cursor.moveToNext()){
				//получаем нужные нам данные из базы данных
				int currentId = cursor.getInt(idIndex);
				int currentIdMain = cursor.getInt(idMainIndex);
				String currentName = cursor.getString(nameIndex);
				String currentImageUrl = cursor.getString(imageIndex);
				
				/*добавляем персонажа с изображением и именем в наш массив,
				  чтобы потом вывести его на экран, в виде списка*/
				characters.add(new Character(currentIdMain + ". " + currentName, currentImageUrl));
				
				/*запоминаем id элементов, которые есть на экране
				  это нужно, чтобы после удаления элементов из списка - 
				  мы знали какие именно элементы остались, по id базы данных, а 
				  не по id самого списка*/
				charactersId.add(currentId);
				
				//создаем адаптер для списка
				customAdapter = new CustomAdapter(
						this,
						R.layout.new_list_item,
						characters
					);
			
				//связываем список с данными персонажей
				listCharacters.setAdapter(customAdapter);
				
				//обновляем адаптер
				customAdapter.notifyDataSetChanged();
			}
			
		}catch(Exception ex){
			
		}finally{
			//закрываем базу данных
			db.close();
			cursor.close();
		}
	}
	
	/*получение данных персонажей по api
	  выбираем одну страницу с персонажами [с помощью выпадающего списка] и
	  запрашиваем данные*/
	public void getCharacterData1(){
		/*если мы вабрали страницу в выпадающем списке,
		  значит нам можно получить данные по странице*/
		if(pageForDisplayData >= 0){
			//очищаем базу данных, чтобы данные не смешивались
			deleteCharactersAll();
			/*можно полностью удалить базу данных и создать новую
			  SQLiteDatabase db = DbHelper.getWritableDatabase();
			  DbHelper.onUpgrade(db, 1, 1);
			
			  charactersId.clear();
			  characters.clear();
			
			  displayDatabaseInfo();*/
			
			//данные получит наш подписчик
			Subscriber<Characters> subscriber = new Subscriber<Characters>(){
				//загрузка данных
				public void onNext(@NonNull Characters charactersAll){
					//получаем всех персонажей на странице
					Results[] results = charactersAll.getResults();
					
					//пробегаемся по всем полученным персонажам
					for(int i = 0; i < results.length; i++){
						//берем персонажа
						Results result = results[i];
						
						//получаем данные персонажа
						int characterId = result.getId();
						String characterName = result.getName();
						String characterSpecies = result.getSpecies();
						String characterGender = result.getGender();
						String characterImage = result.getImage();
						
						//и сразу добавляем в базу данных
						SQLiteDatabase db = DbHelper.getWritableDatabase();
						ContentValues values = new ContentValues();
						
						values.put(RickandMortyEntry.COLUMN_ID, characterId);
						values.put(RickandMortyEntry.COLUMN_NAME, characterName);
						values.put(RickandMortyEntry.COLUMN_SPECIES, characterSpecies);
						values.put(RickandMortyEntry.COLUMN_GENDER, characterGender);
						values.put(RickandMortyEntry.COLUMN_IMAGE_URL, characterImage);
						
						long newRowId = db.insert(
									RickandMortyEntry.TABLE_NAME,
									null,
									values
								);
						
						//очищаем списки с персонажами и их id, для обновления
						characters.clear();
						charactersId.clear();
						
						//заново отображаем данные из базы данных
						displayDatabaseInfo();
					}
				}
				
				//ошибки при загрузки данных 
				public void onError(Throwable e){
					//выводим сообщение об ошибке
					Toast.makeText(
							getApplicationContext(),
							"Ошибка загрузки данных!",
							Toast.LENGTH_LONG
						).show();
				}
				
				//удачное завершение загрузки данных
				public void onComplete(){
					//выводим сообщение об удачном завершении загрузки
					Toast.makeText(
							getApplicationContext(),
							"Данные будут загружены!",
							Toast.LENGTH_LONG
						).show();
				}
				
				//нужно явно запросить данные
				public void onSubscribe(Subscription s){
					s.request(Long.MAX_VALUE);
				}
			};
			
			/*запрос api, используя паттерн "Одиночка"
			  загружаем по выбранной странице pageForDisplayData
			  общий запрос: https://rickandmortyapi.com/api/character/?page=23*/
			CharactersService
					.getInstance()
					.getCharactersApi()
					.getCharacters(pageForDisplayData)
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(subscriber);
		}
	}
	
	/*при долгом нажатии на персонажа из списка,
	  будем удалять персонажа из базы данных*/
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
		//получаем id персонажа, на которого мы нажали
		characterIdDelete = charactersId.get(position);
		
		//диалоговое окно для подтверждения удаления персонажа 
		FragmentManager manager = getSupportFragmentManager();
		DeleteDialog dialog = new DeleteDialog();
		dialog.show(manager, "delete_dialog");
		
		return true;
	}
	
	/*при обычно нажатии на персонажа из списка,
	  будет сделан переход на другое окно, для отображения
	  данных по выбранному персонажу*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		//переход на окно CurrentData
		intent = new Intent(this, CurrentData.class);
		
		//получаем id персонажа из списка, на которого мы нажали
		int characterId = charactersId.get(position);
		
		/*передаем в другое окно id персонажа [из базы данных]
		  чтобы знать данные какого персонажа выводить*/
		intent.putExtra(KEY_ID, characterId);
		
		startActivity(intent);
	}
	
	//обработка выбора в выпадающем списке
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
		//получаем выбранный пункт в выпадающем списке
		pageForDisplayData = position-1;
		//вызываем метод для получения данных по api, с выбранной страницей
		getCharacterData1();
	}
	
	//обработка при отсутствии выбора в выпадающем списке
	@Override
	public void onNothingSelected(AdapterView<?> parent){}
	
	//обработка касаний экрана
	@Override
	public boolean onTouch(View view, MotionEvent event){
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//кнопка очищения данных из базы данных
			if(view == buttonClearData){
				//возвращаем выпадающий список в начальное положение
				spinnerPages.setSelection(0);
				//вызываем метод для очищения данных из базы данных
				deleteCharactersAll();
				
				/*можно полностью удалить базу данных и создать новую
				  SQLiteDatabase db = DbHelper.getWritableDatabase();
				  DbHelper.onUpgrade(db, 1, 1);
				
				  charactersId.clear();
				  characters.clear();
				
				  displayDatabaseInfo();*/
			}
			
			//кнопка удаления персонажа
			if(view == buttonDeleteData){
				//выводим сообщение с указаниями для удаления
				Toast.makeText(
							this,
							"Для удаление элемента - удерживайте его нажатым",
							Toast.LENGTH_LONG
						).show();
			}
		}
		
		return false;
	}
}