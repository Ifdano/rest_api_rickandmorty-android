//Диалоговое окно для подтверждения возврата на предыдущее окно

package com.example.myapplication_rickandmorty.dialogs;

import android.os.Bundle;

import android.support.v4.app.DialogFragment;

import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;

import com.example.myapplication_rickandmorty.UpdateData;

public class BackDialog extends DialogFragment{
	public Dialog onCreateDialog(Bundle savedInstanceState){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		builder
			.setTitle("ВНИМАНИЕ!")
			.setMessage("Данные будут потеряны. Выйти из редактора?")
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
							/*если подтвердили возврат, то вызываем метод setBack из класса UpdateData,
							 чтобы вернуться на предыдущий экран*/
							((UpdateData)getActivity()).setBack();
						}
					}
				);
		
		return builder.create();
	}
}