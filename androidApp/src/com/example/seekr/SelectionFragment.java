package com.example.seekr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SelectionFragment extends Fragment {
	
	private static final String TAG = "SelectionFragment";

//	public SelectionFragment() {
//		// TODO Auto-generated constructor stub
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
		System.out.println("IN SELECtiON FRAGMENT");
	    super.onCreateView(inflater, container, savedInstanceState);
	    View view = inflater.inflate(R.layout.selection, 
	            container, false);
	    return view;
	}
}
