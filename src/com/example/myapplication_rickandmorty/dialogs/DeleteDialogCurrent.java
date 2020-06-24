/*Диалоговое окно с подтверждением удаления персонажа из базы данных,
Удаление на экране с подробной информацией*/

package com.example.myapplication_rickandmorty.dialogs;

import android.os.Bundle;

import android.support.v4.app.DialogFragment;

import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;

import com.example.myapplication_rickandmorty.CurrentData;

public class DeleteDialogCurrent extends DialogFragment{
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder
			.setTitle("ВНИМАНИЕ!")
			.setMessage("Вы подтверждаете удаление персонажа из базы данных?")
			.setCancelable(true)
			.setPositiveButton(
					"Нет",
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							dialog.cancel();
;						}
					}
				)
			.setNegativeButton(
					"Да",
					new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int id){
							/*если подтвердили удаление, то вызываем метод deleteCharacter из класса CurrentData,
							 чтобы удалить персонажа из базы данных */
							((CurrentData)getActivity()).deleteCharacter();
						}
					}
				);
		
		return builder.create();
	}
}