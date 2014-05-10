package com.example.seekr;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ThreadArrayAdapter extends ArrayAdapter<OneComment> {

	private TextView singleView;
	private List<OneComment> listOfViews = new ArrayList<OneComment>();
	private LinearLayout wrapper;

	@Override
	public void add(OneComment object) {
		listOfViews.add(object);
		super.add(object);
	}

	public ThreadArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public int getCount() {
		return this.listOfViews.size();
	}

	public OneComment getItem(int index) {
		return this.listOfViews.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.thread_item, parent, false);
		}

		wrapper = (LinearLayout) row.findViewById(R.id.wrapper);
		//wrapper.setOrientation(wrapper);

		OneComment coment = getItem(position);

		singleView = (TextView) row.findViewById(R.id.comment);
		singleView.setTextColor(coment.left ? Color.WHITE : Color.BLACK);
		singleView.setText("  "+coment.comment+"  ");
		singleView.setAlpha((float) 0.9);;
		
		//singleView.setBackgroundResource(coment.left ? R.drawable.bubble_yellow : R.drawable.bubble_green);	
		singleView.setBackgroundResource(coment.left ? R.drawable.black : R.drawable.white);
		TextView posterView = (TextView) row.findViewById(R.id.poster);
		posterView.setText(coment.userName);
		posterView.setTextColor(Color.WHITE);
		wrapper.setGravity(coment.left ? Gravity.LEFT : Gravity.RIGHT);
		return row;
	}

	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

}