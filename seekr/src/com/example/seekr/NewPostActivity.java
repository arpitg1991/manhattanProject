package com.example.seekr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewPostActivity extends FragmentActivity {

	String tag = "NewPostActivity";
	AsyncWebPostMaster postman;
	EditText edit_text;
	EditText expiresIn;
	EditText eventAddress;
	Button postButton;
	String userId; 
	int LatE6;
	int LonE6;
	float E6 = ( float ) 1000000.0;
	int requestCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);        
		setContentView(R.layout.new_post);      
		Intent intent = getIntent();

		userId = intent.getStringExtra("userId");
		LatE6 = intent.getIntExtra("LatitudeE6", 0);
		LonE6 = intent.getIntExtra("LongitudeE6",0);
		postman = new AsyncWebPostMaster(userId, getApplicationContext());
		edit_text = (EditText) findViewById(R.id.edit_message);
		expiresIn = (EditText) findViewById(R.id.expires_in);

		eventAddress = (EditText) findViewById(R.id.event_location);
		eventAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//				String url = "http://maps.google.com/maps";
				//				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
				//				intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
				Intent currintent = new Intent(NewPostActivity.this, EventAddressActivity.class);
				startActivity(currintent);
			}
		});

		postButton = (Button) findViewById(R.id.button_send);
		postButton.setOnClickListener( 
				new OnClickListener()
				{
					@Override
					public void onClick(View v) {				
						try{
							Integer.parseInt(expiresIn.getText().toString());
							Log.i(tag, "expiryTime can be parsed to Integer: "+expiresIn.getText().toString());
							sendPost();
						}
						catch (Exception e){
							Toast.makeText(getApplicationContext(), "Entered value is not an integer: "+ expiresIn.getText().toString() +"\nPlease send again" , Toast.LENGTH_SHORT).show();
							Log.e(tag, "Value entered in expiresIn is not a string");
						}
					}	

				});
	}

	public void sendPost()
	{
		String lat = new Float(LatE6/E6).toString();
		String lon = new Float(LonE6/E6).toString();
		String exp = expiresIn.getText().toString();

		Log.i(tag, "Sending post " + edit_text.getText().toString() + " " + lat + " " + lon + " "+exp);
		postman.execute(postman.new_post, edit_text.getText().toString(), lat, lon, exp);
		Toast.makeText(getApplicationContext(), "Request sent to generate new event" , Toast.LENGTH_LONG).show();

		finish();
	}
}
