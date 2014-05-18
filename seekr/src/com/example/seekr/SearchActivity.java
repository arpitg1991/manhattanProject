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
            
            
            finish();
            //use the query to search your data somehow
        }
    }



	
}
