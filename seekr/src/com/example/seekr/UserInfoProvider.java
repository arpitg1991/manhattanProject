package com.example.seekr;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

public class UserInfoProvider {

	//Singleton class to provide information to all activities.
	static String tag = "UserInfoProvider";
	private Context context;
	private String userName;
	private String userId;
	private String imageURL;
	private Bitmap fbAvatarBitmap;
	
	Integer  LatE6;
	Integer  LonE6;
	
	private static UserInfoProvider sInstance = new UserInfoProvider();
	
	private UserInfoProvider() {
	}
	
	
	
	public static UserInfoProvider getInstance() {

			return sInstance;
		}
	
	public void setContext(Context context) {
		this.context = context;
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
	
	public void setLatE6(Integer LatE6){
		this.LatE6=LatE6;
		Log.i(tag, "Setting new LatE6: "+ LatE6);
	}
	
	public void setLonE6(Integer LonE6){
		this.LonE6 = LonE6;
		Log.i(tag, "Setting new instance LonE6: "+ LonE6);
	}
	
	public int getLatE6(){
		return LatE6;
	}
	
	public int getLonE6(){
		return LonE6;
	}


	public String getImageURL() {
		return imageURL;
	}


	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public boolean checkInit(){
		
		if (userName!=null){
		 	if(userId!=null){
		 		if  (LatE6!=null){
		 			if(LonE6!=null){
		 				Toast.makeText(this.context, "Credentials validated for user:\n"+userName, Toast.LENGTH_SHORT).show();
		 				return true;
		 			}
		 		}	
		 		}	
		 	}
			Toast.makeText(this.context, "Error - credentials not validated correctly", Toast.LENGTH_SHORT).show();
			return false;
	}
}
	

