package com.example.seekr;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AndroidListViewActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Retrieve List of Locations
		Intent intent = getIntent();
		ArrayList<String> addresses = intent.getStringArrayListExtra("listofevents");
		
		// storing string resources into Array
		String[] event_locations = new String[addresses.size()];
		event_locations = addresses.toArray(event_locations);

		// Binding Array to ListAdapter
		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, event_locations));

		ListView lv = getListView();

		// listening to single list item on click
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// selected item 
				String selectedLocation = ((TextView) view).getText().toString();

				// Return selected location to parent activity
				Intent resultIntent = new Intent();
				resultIntent.putExtra("selectedLocation", selectedLocation);
				setResult(AndroidListViewActivity.RESULT_OK, resultIntent);
				finish();

				// Launching new Activity on selecting single List Item
				//        	  Intent i = new Intent(getApplicationContext(), SingleListItem.class);
				// sending data to new activity
				//      	  i.putExtra("product", product);
				//      	  startActivity(i);
			}
		});
	}
}