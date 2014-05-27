package com.example.seekr;
import com.example.seekr.ImageList.MyDataSetObserver;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListViewListener;

import java.util.ArrayList;
import java.util.List;

import com.fortysevendeg.swipelistview.SwipeListView;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.SearchManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;
 









import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
 









import android.os.Bundle;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;
 
public class ItemListActivity extends Activity {
 
	  String tag = "ItemListActivity";
      SwipeListView swipelistview;
      ItemAdapter adapter;
      List<ItemRow> itemData;
 
      private String userId = UserInfoProvider.getInstance().getUserId();
      private String userName = UserInfoProvider.getInstance().getUserName();
      
      private int mLatitudeE6;
      private int mLongitudeE6;
      
      SearchActivity searchActvity = new SearchActivity();
      //private MyDataSetObserver mObserver = new MyDataSetObserver();
      

    private void actionBarSetup()
    {
    	ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);      
        actionBar.setTitle("Seekr");
        actionBar.setSubtitle("Your ad-hoc social network.");
        SearchView searchview = new SearchView(this);
            	
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
 
        Intent intent = getIntent();
        mLatitudeE6 = intent.getIntExtra(ImageManager.LATITUDE_E6_EXTRA, Integer.MIN_VALUE);
        mLongitudeE6 = intent.getIntExtra(ImageManager.LONGITUDE_E6_EXTRA, Integer.MIN_VALUE);
        
        
        UserInfoProvider.getInstance().checkInit();
        
        actionBarSetup();
        
        swipelistview=(SwipeListView)findViewById(R.id.example_swipe_lv_list);
        itemData=new ArrayList<ItemRow>();
        adapter=new ItemAdapter(this,R.layout.custom_row,itemData);
 
        swipelistview.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }
 
            @Override
            public void onClosed(int position, boolean fromRight) {
            }
 
            @Override
            public void onListChanged() {
            }
 
            @Override
            public void onMove(int position, float x) {
            }
 
            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
            }
 
            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }
 
            @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));
 
                swipelistview.openAnimate(position); //when you touch front view it will open
 
            }
 
            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));
 
                swipelistview.closeAnimate(position);//when you touch back view it will close
            }
 
            @Override
            public void onDismiss(int[] reverseSortedPositions) {
 
            }
 
        });
 
        //These are the swipe listview settings. you can change these
        //setting as your requirement
        swipelistview.setSwipeMode(SwipeListView.SWIPE_MODE_DEFAULT); // there are five swiping modes
        swipelistview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL); //there are four swipe actions
        swipelistview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
        swipelistview.setOffsetLeft(convertDpToPixel(230f)); // left side offset
        swipelistview.setOffsetRight(convertDpToPixel(0f)); // right side offset
        swipelistview.setAnimationTime(50); // Animation time
        swipelistview.setSwipeOpenOnLongPress(true); // enable or disable SwipeOpenOnLongPress
 
        swipelistview.setAdapter(adapter);
 
        for(int i=0;i<10;i++)
        {
            itemData.add(new ItemRow("item"+i, getResources().getDrawable(R.drawable.ic_launcher) ));
 
        }
 
        adapter.notifyDataSetChanged();
    }
 
    
    
    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }
 

    
    protected void onResume()
    {
       super.onResume();   
       this.adapter.clear();
       Intent i = getIntent();
       String searchQuery = i.getStringExtra( this.SEARCH_SERVICE);

       if (searchQuery==null)
       	Log.i(tag, "is null");
       else
    	Log.i(tag, "SearQuery=="+searchQuery);
       	
       if (searchQuery == null) {
       		Log.i(tag, "Executing regular load");
       		AsyncWebPostMaster awpm = new AsyncWebPostMaster(userId, getApplicationContext());
       		//awpm.setImageManager(mImageManager);
            awpm.setMyItems(adapter);
       		awpm.execute(awpm.get_data, mLatitudeE6/1000000.0, mLongitudeE6/1000000.0);
       
       } else {
       	
    	   	Log.i(tag, "Executing search");
       		ActionBar aBar = getActionBar();
       	
       		aBar.setTitle(searchQuery);
       		aBar.setSubtitle(null);
       		AsyncWebPostMaster awpm = new AsyncWebPostMaster(userId, getApplicationContext());
       		//awpm.setImageManager(mImageManager);
       		awpm.setMyItems(adapter);
       		awpm.execute(awpm.search_data, searchQuery);
       }
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
         

         
         String username = UserInfoProvider.getInstance().getUserName();
         Log.i(tag, "name:"+username );
         menu.findItem(R.id.action_username).setTitle("<< "+username+" >>");
         
         
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
		switch (item.getItemId()) {
		
		case R.id.search:
			return false;
		
		case R.id.homeAsUp:
			finish();
		
		case R.id.home:
			Toast.makeText(getApplicationContext(), "Home clicked", Toast.LENGTH_SHORT).show();
			finish();
			return false;
		
		case R.id.new_post_button:

			Intent intent = new Intent(this, NewPostActivity.class);
			intent.putExtra("LatitudeE6", mLatitudeE6);
			intent.putExtra("LongitudeE6", mLongitudeE6);
			intent.putExtra("userId", userId);
			startActivity(intent);
			return true;
			
			
		case R.id.action_culture:
			Toast.makeText(getApplicationContext(), "culture", Toast.LENGTH_SHORT).show();
			startSearch("#Culture", false, null, false);

			return true;
			
		case R.id.action_alerts:
			Toast.makeText(getApplicationContext(), "Alerts", Toast.LENGTH_SHORT).show();
			startSearch("#Alerts", false, null, false);
			return true;
			
		case R.id.action_sports:
			startSearch("#Sports", false, null, false);
			Toast.makeText(getApplicationContext(), "Sports", Toast.LENGTH_SHORT).show();
			
			return true;
		case R.id.action_music:
			startSearch("#Music", false, null, false);
			Toast.makeText(getApplicationContext(), "Music", Toast.LENGTH_SHORT).show();
			return true;
			
		case R.id.action_food:
			startSearch("#Food", false, null, false);
			Toast.makeText(getApplicationContext(), "Food", Toast.LENGTH_SHORT).show();
			return true;
			
		case R.id.action_offers:
			startSearch("#Offers", false, null, false);
			Toast.makeText(getApplicationContext(), "Food", Toast.LENGTH_SHORT).show();
			return true;
			
		case R.id.action_username:
			String userId = UserInfoProvider.getInstance().getUserId();
			startSearch("@"+userId, false, null, false);
			Toast.makeText(getApplicationContext(), "Getting posts associated with your ID", Toast.LENGTH_SHORT).show();
			return true;
		
		case R.id.action_reco:
			userId = UserInfoProvider.getInstance().getUserId();
			//String userName = UserInfoProvider.getInstance().getUserName();
			startSearch("!"+userId, false, null, false);
			Toast.makeText(getApplicationContext(), "Would you like to find recommended posts for "+userName+ "?", Toast.LENGTH_SHORT).show();
			return true;
		
		default:
			finish();
			return super.onOptionsItemSelected(item);
		}
	}
 
}