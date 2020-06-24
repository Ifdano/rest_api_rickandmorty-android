//Класс с фрагментом для макета current_fragment

package com.example.myapplication_rickandmorty.fragments;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.support.v4.app.Fragment;

import com.example.myapplication_rickandmorty.R;

public class CurrentFragment extends Fragment{
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.current_fragment, container, false);		
		return view;
	}
}