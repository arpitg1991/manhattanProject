package com.example.seekr;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SearchActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        Log.i(this.getClass().getSimpleName(), "SearchActivity Instantiated");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(this.getClass().getSimpleName(), "NewIntent SearchActivity");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        Log.i(this.getClass().getSimpleName(), "Trying something new");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            
            Log.i(this.getClass().getSimpleName(), "Reached SearchActivity - insert processing here");
            Toast.makeText(getApplicationContext(), "Search activated", Toast.LENGTH_LONG).show();
            
            Intent searchIntent = new Intent(this, ImageList.class);
            
            Intent i = new Intent(this, ImageList.class);
            //Intent i = new Intent(this, MapDisplayActivity.class);
            i.putExtra(ImageList.SEARCH_SERVICE, query);
            //i.putExtra(ImageManager.LATITUDE_E6_EXTRA, latitudeE6);
            //i.putExtra(ImageManager.LONGITUDE_E6_EXTRA, longitudeE6);

            
            ImageManager mImageManager = ImageManager.getInstance(getApplicationContext());
            mImageManager.clear();
            // Start downloading
            //mImageManager.load(minLong, maxLong, minLat, maxLat);
            
            // Show results
             startActivity(i);
            
            
            
            finish();
            //use the query to search your data somehow
        }
    }



	
}
