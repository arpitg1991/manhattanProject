package com.example.seekr;

import com.google.android.gms.location.LocationClient;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class CustomWebActivity extends Activity{

	int latitude = 0;
	int longitude = 0;
	static Location currentLocation; 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webview = new WebView(this);

		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		webview.getSettings().setJavaScriptEnabled(true);

		final Activity activity = this;
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different scales.
				// The progress meter will automatically disappear when we reach 100%
				activity.setProgress(progress * 1000);
			}
		});
		webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
			}
		});

		webview.loadUrl("https://dl.dropboxusercontent.com/u/13118548/seekr/index.html");
		setContentView(webview);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.webview, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.start_seeking:
		{			
			Intent i = new Intent(this, ImageList.class);
			
			Intent currIntent = getIntent();
	        latitude = currIntent.getIntExtra(ImageManager.LATITUDE_E6_EXTRA, Integer.MIN_VALUE);
	        longitude = currIntent.getIntExtra(ImageManager.LONGITUDE_E6_EXTRA, Integer.MIN_VALUE);
			
			i.putExtra(ImageManager.LATITUDE_E6_EXTRA, latitude);
			i.putExtra(ImageManager.LONGITUDE_E6_EXTRA, longitude);
			startActivity(i);
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void makeUseOfNewLocation(Location location) {
		System.out.println("HERE!");

		double lat = location.getLatitude();
		double lon = location.getLongitude();

		latitude =  (int) (lat*1000000);
		longitude = (int) (lon*1000000);
		
		System.out.println(latitude);
		System.out.println(longitude);
	}
}