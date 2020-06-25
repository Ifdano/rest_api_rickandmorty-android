## Получение данных с сервера, с помощью Retrofit. А также, хранение данных в базе данных[Sqlite], использование RxJava/RxAndroid и загрузка изображения Picasso.
## Обобщенно: Retrofit + RxJava/RxAndroid + Sqlite + Picasso + ListView + Fragment + Singleton[Design Patterns].

### Данные по мультсериалу "Рик и Морти" будут получены по API: https://rickandmortyapi.com/api/character/?page=1
### Документация по API: https://rickandmortyapi.com/documentation/#get-all-characters

### Подробнее.

### 1] Главный экран, на котором отображены имена персонажей и их изображения, взятые из базы данных. Список прокручиваемый. Нужно выбрать страницу с персонажами [1-30], которые мы хотим просмотреть, чтобы сделать запрос: https://rickandmortyapi.com/api/character/?page=выбранная_нами_страница
![project_rickandmorty_main_ver'](https://user-images.githubusercontent.com/15383481/85540600-3c9b9e00-b628-11ea-9df6-61a508ebbb5a.png)
![project_rickandmorty_main_hor'](https://user-images.githubusercontent.com/15383481/85540607-3f968e80-b628-11ea-8600-f42eae7c1b86.png)
### Удалить персонажа можно продолжительным нажатием на самого персонажа. Будет выведено диалоговое окно о подтверждении удаления. Персонаж будет удален из базы данных и из списка. 
### Можно очистить всех персонажей из базы данных.
### Для получения подробной информации о персонаже - нужно выбрать его из списка.

### 2] Экран, на котором можно получить подробную информацию о выбранном персонаже. Выводится вся доступная информация.
![project_rickandmorty_info_ver'](https://user-images.githubusercontent.com/15383481/85541061-b764b900-b628-11ea-9339-ce6525083258.png)
![project_rickandmorty_info_hor'](https://user-images.githubusercontent.com/15383481/85541072-b9c71300-b628-11ea-8948-2193c93dec3f.png)
### Можно сразу удалить персонажа. Персонаж будет удален из базы данных и из списка. Будет выведено диалоговое окно о подтверждении удаления.
### Можно обновить информацию о персонаже, перейдя на экран с "редактором".

### 3] Экран, на котором можно обновить информацию о персонаже. Будет сделан запрос в базу данных для обновления информации.
![project_rickandmorty_update_ver'](https://user-images.githubusercontent.com/15383481/85541306-f0049280-b628-11ea-926a-27c41300af80.png)
![project_rickandmorty_update_hor'](https://user-images.githubusercontent.com/15383481/85541316-f1ce5600-b628-11ea-8594-9e8b8a2ed444.png)

### Примечание:

#### 1] Это лишь пример использования Retrofit/RxJava/RxAndroid/Sqlite/Picasso/прочее в одной связке друг с другом.  
#### 2] Почти все окна содержат фрагменты.
#### 3] Для облегчения подстройки под разные разрешения экрана, были использованы единицы sdp/ssp.

#### sdp: https://github.com/intuit/sdp
#### ssp: https://github.com/intuit/ssp 

### Device: Samsung galaxy note 5.
