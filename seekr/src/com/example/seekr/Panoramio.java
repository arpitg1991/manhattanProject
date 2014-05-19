/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.seekr;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;



// README
// Notes about project
//
//>> Adding Background with Image Downloaded from Panoramio
//	
//	//LOCATION: ImageAdapter.MyDataObserver
//	
//	Change code in ImageAdapter.MyDataObserver to keep sampling images from 
//	the list in adapter until you find an image of sufficient darkness 
//	then set that as the background.
//	
//

/**
 * Activity which lets the user select a search area
 *
 */

public class Panoramio extends MapActivity implements OnClickListener {
    private MapView mMapView;
    private MyLocationOverlay mMyLocationOverlay;
    private ImageManager mImageManager;
    
    
    public static final int MILLION = 1000000;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
    	super.onCreate(savedInstanceState);      
        setContentView(R.layout.panoramio_main);
        
        mImageManager = ImageManager.getInstance(this);
        
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        
        FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
        frame.setBackgroundColor(Color.BLACK);
        Button goButton = (Button) findViewById(R.id.go);
        goButton.setText("Find nearby events");
        goButton.setOnClickListener(this);
        
        // Add the map view to the frame
        mMapView = new MapView(this, "Panoramio_DummyAPIKey");
        frame.addView(mMapView, 
                new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        frame.setBackgroundColor(Color.BLACK);
        
        // Create an overlay to show current location
        mMyLocationOverlay = new MyLocationOverlay(this, mMapView);
        mMyLocationOverlay.runOnFirstFix(new Runnable() { public void run() {
            mMapView.getController().animateTo(mMyLocationOverlay.getMyLocation());
        }});

        mMapView.getOverlays().add(mMyLocationOverlay);
        mMapView.getController().setZoom(15);
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setSatellite(true);
        
        addZoomControls(frame);
        



    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyLocationOverlay.enableMyLocation();
    }

    @Override
    protected void onStop() {
        mMyLocationOverlay.disableMyLocation();
        super.onStop();
    }

    /**
     * Add zoom controls to our frame layout
     */
    private void addZoomControls(FrameLayout frame) {
        // Get the zoom controls and add them to the bottom of the map
        View zoomControls = mMapView.getZoomControls();

        FrameLayout.LayoutParams p = 
            new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL);
        frame.addView(zoomControls, p);
    }
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    /**
     * Starts a new search when the user clicks the search button.
     */
    public void onClick(View view) {
        // Get the search area
        int latHalfSpan = mMapView.getLatitudeSpan() >> 1;
        int longHalfSpan = mMapView.getLongitudeSpan() >> 1;
        
        // Remember how the map was displayed so we can show it the same way later
        GeoPoint center = mMapView.getMapCenter();
        int zoom = mMapView.getZoomLevel();
        int latitudeE6 = center.getLatitudeE6();
        int longitudeE6 = center.getLongitudeE6();
        
        Log.i(new Integer(latitudeE6).toString(), new Integer(longitudeE6).toString());
        
        Intent i = new Intent(this, ImageList.class);
        //Intent i = new Intent(this, MapDisplayActivity.class);
        i.putExtra(ImageManager.ZOOM_EXTRA, zoom);
        i.putExtra(ImageManager.LATITUDE_E6_EXTRA, latitudeE6);
        i.putExtra(ImageManager.LONGITUDE_E6_EXTRA, longitudeE6);

        float minLong = ((float) (longitudeE6 - longHalfSpan)) / MILLION;
        float maxLong = ((float) (longitudeE6 + longHalfSpan)) / MILLION;
        float minLat = ((float) (latitudeE6 - latHalfSpan)) / MILLION;
        float maxLat = ((float) (latitudeE6 + latHalfSpan)) / MILLION;
        
        mImageManager.clear();
        
        // Start downloading
        //mImageManager.load(minLong, maxLong, minLat, maxLat);
        
        // Show results
        startActivity(i);
    }
}
