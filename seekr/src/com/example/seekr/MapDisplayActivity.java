package com.example.seekr;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;


public class MapDisplayActivity extends Activity {

	private String className = "MapDisplayActivity";
	private int latitudeE6;		private Float latitude = new Float(0);
	private int longitudeE6;	private Float longitude = new Float(0);
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(className, "Creating MapDisplayActivity...");
        
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);
        
        Log.i("MapDisplayActivity","REACHED");
        
        // Get a handle to the Map Fragment
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        
        LatLng sydney = new LatLng(-33.867, 151.206);
//
        map.setMyLocationEnabled(true);
       //LocationClient local = new LocationClient (getApplicationContext(), null, null);
       //Location location = local.getLastLocation();
       Location location = map.getMyLocation();
       
       Intent i = getIntent();
       latitudeE6 	= i.getIntExtra("lat", Integer.MIN_VALUE);
       longitudeE6  = i.getIntExtra("long", Integer.MIN_VALUE);
       
       latitude 	+=	latitudeE6;		
       latitude/=1000000;
       longitude	+=	longitudeE6; 	
       longitude/=1000000;
       
       Log.i(className, "Latitude :" + latitude.toString() + " Longitude :" + longitude.toString());
       sydney = new LatLng(latitude, longitude);
       map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
       String poster  = i.getStringExtra("poster");
       String message = i.getStringExtra("message");
       Log.i("Map", "poster: "+poster + " message: "+message);
        map.addMarker(new MarkerOptions()
                .title(poster)
                .snippet(message)
                .position(sydney));
        
    }
}
