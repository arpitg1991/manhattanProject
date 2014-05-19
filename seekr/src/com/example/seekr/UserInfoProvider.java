package com.example.seekr;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.util.Log;

public class UserInfoProvider {

	//Singleton class to provide information to all activities.
	static String tag = "UserInfoProvider";
	String userName;
	String userId;
	Bitmap fbAvatarBitmap;
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
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
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
	
	public Double getLatitude(){
		return LatE6/1000000.0;
	}
	public Double getLongitude(){
		return LonE6/1000000.0;
	}
}
	

