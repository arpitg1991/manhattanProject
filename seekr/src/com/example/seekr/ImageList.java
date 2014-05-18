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

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

/**
 * Activity which displays the list of images.
 */
public class ImageList extends ListActivity {
    
    ImageManager mImageManager;
    SearchActivity searchActvity = new SearchActivity();
    private MyDataSetObserver mObserver = new MyDataSetObserver();

    private String userId = "Akuma";
    
    /**
     * 	UserId for the current user: Dummy variable for my app.  
     */
    
    /**
     * The zoom level the user chose when picking the search area
     */
    private int mZoom;

    /**
     * The latitude of the center of the search area chosen by the user
     */
    private int mLatitudeE6;

    /**
     * The longitude of the center of the search area chosen by the user
     */
    private int mLongitudeE6;

    /**
     * Observer used to turn the progress indicator off when the {@link ImageManager} is
     * done downloading.
     */

    public class MyDataSetObserver extends DataSetObserver {        
    	@Override
        public void onChanged() {
            if (!mImageManager.isLoading()) {
                getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
                        Window.PROGRESS_VISIBILITY_OFF);
            }
        }
        @Override
        public void onInvalidated() {
        }
    }
    

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        mImageManager = ImageManager.getInstance(this);
        
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);      
        actionBar.setTitle("Seekr");
        actionBar.setSubtitle("Your ad-hoc social network.");
                
        SearchView searchview = new SearchView(this);
        ListView listView = getListView();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View footer = inflater.inflate(R.layout.list_footer, listView, false);
        
        listView.setScrollingCacheEnabled(false);
        
        int[] colors = {0, 0xFFFFFFFF, 0}; // Transparent to white to transparent
        
        listView.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(2);
        
        ImageAdapter tempAdapter = new ImageAdapter(this);
        tempAdapter.listView = listView;
        
        
        setListAdapter(tempAdapter);
                
        
        if (mImageManager.isLoading()) {
            getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
                    Window.PROGRESS_VISIBILITY_ON);
            mImageManager.addObserver(mObserver);
        }
        listView.setBackgroundColor(Color.BLACK);
        //l.setBackgroundColor(Color.parseColor("#000000"));
        
        // Read the user's search area from the intent
        Intent i = getIntent();
   
        mZoom = i.getIntExtra(ImageManager.ZOOM_EXTRA, Integer.MIN_VALUE);
        mLatitudeE6 = i.getIntExtra(ImageManager.LATITUDE_E6_EXTRA, Integer.MIN_VALUE);
        mLongitudeE6 = i.getIntExtra(ImageManager.LONGITUDE_E6_EXTRA, Integer.MIN_VALUE);
        
        AsyncWebPostMaster awpm = new AsyncWebPostMaster("1234", getApplicationContext());
        awpm.setImageManager(mImageManager);
        awpm.execute(awpm.get_data, mLatitudeE6/1000000.0, mLongitudeE6/1000000.0);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

    	PanoramioItem item = mImageManager.get(position);   
        
//        	Create an intent to show a particular item.
//        	Pass the user's search area along so the next activity can use it
//        Intent i = new Intent(this, ViewImage.class);
//        i.putExtra(ImageManager.PANORAMIO_ITEM_EXTRA, item);
//        i.putExtra(ImageManager.ZOOM_EXTRA, mZoom);
//        i.putExtra(ImageManager.LATITUDE_E6_EXTRA, mLatitudeE6);
//        i.putExtra(ImageManager.LONGITUDE_E6_EXTRA, mLongitudeE6);
        
        //Convert background to black instead of starting new activity
        //l.setBackgroundColor(Color.parseColor("#000000"));
        //Intent i = new Intent(this, MapDisplayActivity.class);
        
         Intent i = new Intent(this, ThreadActivity.class);
        
        i.putExtra("userId", userId);
        i.putExtra("lat", mLatitudeE6);
        i.putExtra("long", mLongitudeE6);
        i.putExtra("postId", item.getPostId());
        
        startActivity(i);
        
        

        //Intent intent = new Intent(this , NewPostActivity.class);
        //startActivity(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
         SearchView searchView =
                 (SearchView) menu.findItem(R.id.search).getActionView();
         searchView.setSearchableInfo(
                 searchManager.getSearchableInfo(getComponentName()));
         searchView.setSubmitButtonEnabled(true);
         
         OnQueryTextListener queryListener = new OnQueryTextListener() {
        	 
        	 @Override
        	 public boolean onQueryTextSubmit(String query)
        	 {
        		 
        		 try
        		 {
        		 Toast.makeText(getApplicationContext(), "Seeking...", Toast.LENGTH_LONG).show();
        		 Log.i("ImageList", "textSubmit");
        		 
        		 onSearchRequested();
        		 return true;        		 
        		 } 
        		 
        		 
        		 catch(Exception e) { e.printStackTrace(); return false ; } 
        		 
        		 
        	 }

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Absolutely nothing;
				//Can't do anything 
				return false;
			}
         };

         searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), SearchActivity.class)));
         searchView.setQueryHint("Start Seeking...");
         //searchView.setOnQueryTextListener(queryListener);
         
        return true;
    
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{         
		switch (item.getItemId())
		{
		case R.id.new_post_button:
			//Toast.makeText(MainActivity.this, "DebugScreen is selected", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, NewPostActivity.class);
			intent.putExtra("LatitudeE6", mLatitudeE6);
			intent.putExtra("LongitudeE6", mLongitudeE6);
			intent.putExtra("userId", userId);
			startActivity(intent);
			
			return true;



		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
}