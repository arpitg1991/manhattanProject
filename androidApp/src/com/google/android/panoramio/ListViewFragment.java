package com.google.android.panoramio;

import android.app.ListFragment;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

public class ListViewFragment extends ListFragment {
	
	ImageManager mImageManager;
    
	public class MyDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            if (!mImageManager.isLoading()) {
               getActivity().getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
                        Window.PROGRESS_VISIBILITY_OFF);
            }
        }

        @Override
        public void onInvalidated() {
        }
    }
	
	
	MyDataSetObserver mObserver = new MyDataSetObserver();



    
    public void onCreate(Bundle savedInstanceState)
    {	
    	getActivity().requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
    	
    	ListView listView = getListView();
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View footer = inflater.inflate(R.layout.list_footer, listView, false);
        //listView.addFooterView(footer, null, false);
        listView.setScrollingCacheEnabled(false);
        
        //Fancy colors effect  in the listView divider
        int[] colors = {0, 0xFF7F00FF, 0}; // Transparent to purple to transparent
        listView.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(2);
        
        ImageAdapter tempAdapter = new ImageAdapter(getActivity());
        tempAdapter.listView = listView;
        setListAdapter(tempAdapter);
        
        // Theme.Light sets a background on our list.
        
        listView.setBackgroundDrawable(null);
        if (mImageManager.isLoading()) {
            getActivity().getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS,
                    Window.PROGRESS_VISIBILITY_ON);
            
        	mImageManager.addObserver(mObserver);
        }
        listView.setBackgroundColor(Color.BLACK);
        
        // Read the user's search area from the intent

    }

    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        PanoramioItem item = mImageManager.get(position);   
        
        // Create an intent to show a particular item.
        // Pass the user's search area along so the next activity can use it
//        Intent i = new Intent(ImageList.class, ViewImage.class);
//        i.putExtra(ImageManager.PANORAMIO_ITEM_EXTRA, item);
//        i.putExtra(ImageManager.ZOOM_EXTRA, mZoom);
//        i.putExtra(ImageManager.LATITUDE_E6_EXTRA, mLatitudeE6);
//        i.putExtra(ImageManager.LONGITUDE_E6_EXTRA, mLongitudeE6);
        
        //Convert background to black instead of starting new activity
        l.setBackgroundColor(Color.parseColor("#000000"));
        //startActivity(i);
    }   
    
    
    
    /**
     * Observer used to turn the progress indicator off when the {@link ImageManager} is
     * done downloading.
     */


}
