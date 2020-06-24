//Диалоговое окно с подтверждением обновление персонажа в базе данных

package com.example.myapplication_rickandmorty.dialogs;

import android.os.Bundle;

import android.support.v4.app.DialogFragment;

import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;

import com.example.myapplication_rickandmorty.UpdateData;

public class UpdateDialog extends DialogFragment{
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder
			.setTitle("ВНИМАНИЕ!")
			.setMessage("Вы подтверждаете обновление данных персонажа?")
			.setCancelable(true)
			.setPositiveButton(
					"Нет",
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							dialog.cancel();
						}
					}
				)
			.setNegativeButton(
					"Да",
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							/*если подтвердили обновление, то вызываем метод updateCharacter из класса UpdateMain,
							 чтобы обновить данные персонажа в базе данных */
							((UpdateData)getActivity()).updateCharacter();
						}
					}
				);
		
		return builder.create();
	}
}