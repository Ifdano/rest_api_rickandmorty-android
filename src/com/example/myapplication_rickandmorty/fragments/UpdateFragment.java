//Класс с фрагментом для макета update_fragment

package com.example.myapplication_rickandmorty.fragments;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.example.myapplication_rickandmorty.R;

public class UpdateFragment extends Fragment{
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.update_fragment, container, false);
		return view;
	}
}