package com.example.seekr;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.util.Log;

public class UserInfoProvider {

	//Singleton class to provide information to all activities.
	static String tag = "UserInfoProvider";
	private String userName;
	private String userId;
	private String imageURL;
	private Bitmap fbAvatarBitmap;
	
	int  LatE6;
	int  LonE6;
	
	static UserInfoProvider sInstance = null;
	
	private UserInfoProvider() {
	}
	
	
	public static UserInfoProvider getInstance() {
		if (sInstance!=null)
			return sInstance;
		else {
			sInstance = new UserInfoProvider();
			Log.i(tag, "Creating new instance");
			return sInstance;
			}
		}
	
	public void setUserName(String userName) {
		this.userName = userName;
		Log.i(tag, "Setting new instance userName: "+ userName);
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
		Log.i(tag, "Setting new userId: "+ userId);
	}
	
	public String getUserName(){
		return userName;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setImageBitmap(Bitmap bmap){
		fbAvatarBitmap = bmap;
	}
	
	public byte[] getImageAsByteArray()
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        fbAvatarBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
		return byteArray;
	}
	
	public Bitmap getImageBitmap(){
		return fbAvatarBitmap;
	}
	
	public void setLatitude(Double Latitude){
		this.LatE6 = (int) (Latitude * 1000000);
	}
	
	public void setLongitude(Double Latitude){
		this.LonE6 = (int) (Latitude * 1000000);
	}
	
	public Double getLatitude(){
		return LatE6/1000000.0;
	}
	public Double getLongitude(){
		return LonE6/1000000.0;
	}


	public String getImageURL() {
		return imageURL;
	}


	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
}
	

