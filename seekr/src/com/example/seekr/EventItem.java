package com.example.seekr;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.gson.JsonObject;

public class EventItem {

	String tag = "EventItem";
	String userId;
	String userName;
	String expireTime;
	String catId;
	String text;
	String _id;
	String postTime;
	String location_type;
	Double Lat;
	Double Long;
	String PostTime;
	String userPic;
	Integer likes;
	JSONObject json;
	
	public EventItem()	{
		
	}
	public EventItem (JSONObject json)
	{
		this.json = json;
		
		try{
			
			try {
			Long lexpireTime=(Long) json.get("expireTime");
			expireTime = lexpireTime.toString();
			} catch(Exception e) {
			expireTime = "0";
			}
			
			 
					
			catId = (String) json.get("catId");
			text = (String) json.get("text");
			userId = json.getString("userId");
			_id  = (String) json.get("_id");
			
			try{
			userName = json.getString("userName");
			} catch(Exception e) { userName = null; } 
			
			try{
			userPic = json.getString("userPic");
			} 
			catch(Exception e) { userPic = null; }
			
			
			JSONObject location = (JSONObject) json.get("location");
			location_type = location.getString("type");
			JSONArray coordArray = (JSONArray) location.get("coordinates");
			
			Lat = coordArray.getDouble(1);
			Long = coordArray.getDouble(0);
			
			postTime = json.getString("postTime");
			likes = (Integer) json.get("likes");
		
		}
		
		catch(Exception e) { 
		Log.e(tag, "Error in translating JSONObject"); 
	}
}
	
	public String getJSONString() {
		return json.toString();
	}
	
	public JSONObject getJSON() {
		return json;
	}
	
	public View getView(Context context){
		
		View view;
        // Make up a new view
        LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.image_item, null);
        ImageView i = (ImageView) view.findViewById(R.id.image);
        i.setBackground(new ColorDrawable(Color.BLACK));
        i.setPaddingRelative(10, 20, 5, 20);
        
        
        TextView t = (TextView) view.findViewById(R.id.title);
        t.setText(this.text);
        t.setTextColor(Color.WHITE);
        t = (TextView) view.findViewById(R.id.owner);
        t.setText(this.userName);
        return view;

	}
	
	public PanoramioItem getPanoramioItem()
	{
		int E6 = 1000000;
		PanoramioItem pItem = new PanoramioItem(100, null, null, Lat.intValue(), Long.intValue(), text, userId, _id, userName);
		return pItem;
	}
	
	public ItemRow getItemRow() {
		ItemRow newRow = new ItemRow(this.userName, null);
		
		newRow.setExpiryTime(this.expireTime);
		newRow.setLat(this.Lat);
		newRow.setLong(this.Long);
		newRow.setPostId(this._id);
		newRow.setUserId(this.userId);
		newRow.setText(this.text);
		newRow.setUserPic(this.userPic);
		
		return newRow;
	
	}

}
