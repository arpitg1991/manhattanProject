package com.example.seekr;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
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

	String popUpContents[];
	PopupWindow popupWindowLocations;
	Button buttonShowDropDown;

	List<String> addressList = new ArrayList<String>();

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
		eventAddress.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
						(keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					addressList.clear();
					String searchLoc = eventAddress.getText().toString();
					System.out.println(searchLoc);
					new HttpAsyncTask().execute("https://api.foursquare.com/v2/venues/search?client_id=NPYFXJLBUOCYQUXZOR4VUCRWQ5X3YTHVRXY5ULLPCSN1TCIE&client_secret=A0DK4XHO34SDN0WZGLV4JGYVJ3EFQSZX3ODJNPA5NWDDEQ4M&v=20130815%20&ll=40.7,-74%20&query=" + searchLoc.replaceAll("\\s+", "%20"));					
					return true;
				}
				return false;
			}
		});

		//		.setOnClickListener(new OnClickListener() {
		//			@Override
		//			public void onClick(View v) {
		//				addressList.clear();
		//				new HttpAsyncTask().execute("https://api.foursquare.com/v2/venues/search?client_id=NPYFXJLBUOCYQUXZOR4VUCRWQ5X3YTHVRXY5ULLPCSN1TCIE&client_secret=A0DK4XHO34SDN0WZGLV4JGYVJ3EFQSZX3ODJNPA5NWDDEQ4M&v=20130815%20&ll=40.7,-74%20&query=subsconscious");
		//			}			
		//		});

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

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			return GET(urls[0]);
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();

			if (result == null)
				System.out.println("Invalid Request");
			else
			{
				System.out.println(result);

				try {
					JSONObject jObject = new JSONObject(result);
					JSONObject jObject1 = (JSONObject) jObject.get("response");
					JSONArray jarray = jObject1.getJSONArray("venues");

					String placeName;
					String placeAddress;
					String placeCrossStreet;

					for (int i=0; i < jarray.length(); i++)
					{
						try {
							JSONObject oneObject = jarray.getJSONObject(i);
							JSONObject locationObject = (JSONObject) oneObject.get("location");

							// Pulling items from the array
							String locNameItem = oneObject.getString("name");							
							String locAddressItem = locationObject.getString("address");
							String locStreetItem = locationObject.getString("crossStreet");

							placeName = locNameItem;
							placeAddress = locAddressItem;
							placeCrossStreet = locStreetItem;

							String place = placeName + ", " + placeAddress + " " + placeCrossStreet;

							addressList.add(place);
						} catch (JSONException e) {
							Log.e("JSON", "JSON Error");
						}
					}
					
					eventAddress.setText(addressList.get(0));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		return result;
	}

	public static String GET(String url){
		InputStream inputStream = null;
		String result = "";
		try {
			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";
		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}
}
