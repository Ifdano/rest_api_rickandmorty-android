/*Диалоговое окно с подтверждением удаления персонажа из базы данных,
Удаление на главном экране, при долгом нажатии на персонажа из списка*/

package com.example.myapplication_rickandmorty.dialogs;

import android.support.v4.app.DialogFragment;

import android.os.Bundle;

import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;

import com.example.myapplication_rickandmorty.Main;

public class DeleteDialog extends DialogFragment{
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder
			.setTitle("ВНИМАНИЕ!")
			.setMessage("Вы подтверждаете удаление персонажа из базы данных?")
			.setCancelable(true)
			.setPositiveButton(
					"НЕТ",
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							dialog.cancel();
						}
					}
				)
			.setNegativeButton(
					"ДА",
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							/*если подтвердили удаление, то вызываем метод deleteCharacter из класса Main,
							 чтобы удалить персонажа из базы данных */
							((Main)getActivity()).deleteCharacter();
						}
					}
				);
				
		return builder.create();
	}
}