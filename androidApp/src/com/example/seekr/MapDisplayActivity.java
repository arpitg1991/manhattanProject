package com.example.seekr;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;


public class MapDisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("MAP REACHED!");
        setContentView(R.layout.activity_map);
        
        Log.i("MapDisplayActivity","REACHED");
        
        // Get a handle to the Map Fragment
//        GoogleMap map = ((MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map)).getMap();
//        
//        LatLng sydney = new LatLng(-33.867, 151.206);
//
//        map.setMyLocationEnabled(true);
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
//
//        map.addMarker(new MarkerOptions()
//                .title("Sydney")
//                .snippet("The most populous city in Australia.")
//                .position(sydney));
        
    }
}