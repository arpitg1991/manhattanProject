package com.example.seekr;

import java.util.ArrayList;
import java.util.Random;





import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
//import de.svenjacobs.loremipsum.LoremIpsum;
import android.widget.SearchView.OnQueryTextListener;

public class ThreadActivity extends Activity {
	
	private String tag = "ThreadActivity";
    private String userId = UserInfoProvider.getInstance().getUserId();
    private String userName = UserInfoProvider.getInstance().getUserName();
    private String postId;
    private String message;
    private String location;
    private String poster;
	private ThreadArrayAdapter adapter;
	private ListView lv;
	private View sepView;
	private EditText editText1;
	private static Random random;
	private String className = "ThreadActivity";
	private Integer latitudeE6;
	private Integer longitudeE6;
	private PanoramioItem myItem;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thread_activity);
		random = new Random();
		
        Intent i = getIntent();
        //userId = i.getStringExtra("userId");
        poster= i.getStringExtra("poster");
        postId = i.getStringExtra("postId");
        message = i.getStringExtra("message");
        Log.i(tag, "Message:"+message);
        location = i.getStringExtra("location");
        
        latitudeE6 = i.getIntExtra("lat", Integer.MIN_VALUE);
        longitudeE6 = i.getIntExtra("long", Integer.MIN_VALUE);
		lv = (ListView) findViewById(R.id.listView1);
		adapter = new ThreadArrayAdapter(getApplicationContext(), R.layout.thread_item);

        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Seekr");
        actionBar.setSubtitle("Your ad-hoc social network.");		
		
		//-----------ListView Stuff----------------------//
		lv.setAdapter(adapter);
		lv.setBackgroundColor(Color.BLACK);
		
		//-----------------------------------------------//
		//------------EDIT TEXT STUFF -------------------------//
		editText1 = (EditText) findViewById(R.id.editText1);
		editText1.setCursorVisible(true);
		editText1.setHint("Add your text here-");
		editText1.setHintTextColor(Color.GRAY);
		editText1.setHighlightColor(Color.CYAN);
		editText1.setTextColor(Color.WHITE);
		editText1.setBackgroundColor(Color.BLACK);
		editText1.setBackgroundResource(R.drawable.three_tier);
		editText1.setAlpha((float)1.5);
		editText1.setClickable(true);
		editText1.requestFocus();

		
		loadComments();
		
		editText1.setOnHoverListener(
		new OnHoverListener(){
					@Override
					public boolean onHover(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						v.setAlpha((float)0.5);
						return false;
					}			
		});
		

		
		editText1.setOnKeyListener(new OnKeyListener() {
		public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				
			if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press
					//
					
					if (editText1.getText().toString().equals(""))
					{
						Toast.makeText(getApplicationContext(), "Cannot post empty string", Toast.LENGTH_LONG).show();
						return false;
					}
					postComment();
					adapter.add(new OneComment(false, editText1.getText().toString(), userId, userName));
					editText1.setText("");
					return true;
				}
				return false;
			}
		});
		//------------EDIT TEXT STUFF -------------------------//

		addItems();
	}

	public void postComment()
	{
		Log.i(tag, userId);
		AsyncWebPostMaster postman = new AsyncWebPostMaster(userId, getApplicationContext());
		String comment = editText1.getText().toString();
		//OneComment newComment = new OneComment(false, editText1.getText().toString());
		postman.execute(postman.new_comment,comment, postId);
		
	}
	
	public void loadComments()
	{
		
		AsyncWebPostMaster postman = new AsyncWebPostMaster (userId, getApplicationContext());
		postman.setThreadArrayAdapter(this.adapter);
		Log.i(tag, "Retrieving posts for postId: "+postId);
		postman.execute(postman.get_comments, postId);
		
	}
	
	public void populate(ArrayList<OneComment> commentary){
		
		for (OneComment comment : commentary)
			adapter.add(comment);
		
		Log.i(className, "Populating comments complete");
		
	}
	private void addItems() {

		adapter.add(new OneComment(true, message, postId, poster));

//		for (int i = 0; i < 4; i++) {
//			boolean left = getRandomInteger(0, 1) == 0 ? true : false;
//			int word = getRandomInteger(1, 10);
//			int start = getRandomInteger(1, 40);
//			String words = "LOL";
//			adapter.add(new OneComment(left, words));
//		}
	}

	private static int getRandomInteger(int aStart, int aEnd) {
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		long range = (long) aEnd - (long) aStart + 1;
		long fraction = (long) (range * random.nextDouble());
		int randomNumber = (int) (fraction + aStart);
		return randomNumber;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comments_menu, menu);
		
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_search:	        	
	        	Intent intent = new Intent(this, Panoramio.class);
	        	startActivity(intent);
	            return true;
	            
	        case R.id.action_settings:
	        	AsyncWebPostMaster postman = new AsyncWebPostMaster("1234", getApplicationContext());
	    		Toast.makeText(getApplicationContext(), "Sending post request - test", Toast.LENGTH_SHORT).show();
	    		postman.execute();
	            return true;
	        
	        case R.id.mapbutton:
	        	Intent i = new Intent(getApplicationContext(), MapDisplayActivity.class);
	        	i.putExtra("poster", poster);
	        	i.putExtra("message", message);
	        	i.putExtra("lat", latitudeE6);
	        	i.putExtra("long", longitudeE6);
	        	startActivity(i);
	        
	        default:
	        	finish();
	            return super.onOptionsItemSelected(item);
	    }

	}

}