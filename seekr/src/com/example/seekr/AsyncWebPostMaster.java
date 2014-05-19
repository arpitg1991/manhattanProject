package com.example.seekr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;








import org.json.JSONTokener;

















//import org.json.JSONObject;
import com.google.gson.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;


public class AsyncWebPostMaster extends AsyncTask {

	static String tag = "WebPostMaster";
	
    private String userId = UserInfoProvider.getInstance().getUserId();
    private String userName = UserInfoProvider.getInstance().getUserName();

	static String create_comment_url	= 	"http://shrouded-retreat-3846.herokuapp.com/createComment";
	static String create_post_url 		= 	"http://shrouded-retreat-3846.herokuapp.com/createPost";
	static String get_data_url 			= 	"http://shrouded-retreat-3846.herokuapp.com/getPost?";
	static String search_url 			= 	"http://shrouded-retreat-3846.herokuapp.com/searchPosts?";
	static String get_comments_url 		= 	"http://shrouded-retreat-3846.herokuapp.com/getComments?";
	
	
//	static String create_post_url 			= "http://160.39.179.36:9000/createPost";    
//    static String get_data_url 				= "http://160.39.179.36:8/getPost?";
//	static String get_comments_url 			= "http://160.39.179.36:9000/getComments?";
//	static String search_url 				= "http://160.39.179.36:8080/searchPosts?";
//	static String create_comment_url		= "http://160.39.179.36:9000/createComment?";
	
    private static Context context;
    public static String new_post 		= "NEW_POST";
    public static String new_comment 	= "NEW_COMMENT";
    public static String get_data 		= "GET_DATA";
    public static String search_data 	= "SEARCH_DATA";
    public static String get_comments 	= "GET_COMMENTS";
    
    
    private static Gson gson = new Gson();
    private ThreadArrayAdapter t_adapter;
    private ImageManager img_mgr;
    private Context myContext;
    
    
	public AsyncWebPostMaster(String userId, Context context) {
	    this.userId = userId;
	    this.myContext= context;
	   }
	
	public AsyncWebPostMaster() {
		// TODO Auto-generated constructor stub
	}

	public void setThreadArrayAdapter(ThreadArrayAdapter adapter)
	{
		this.t_adapter = adapter;
		Log.i(tag, "ThreadArrayAdapter instantiated");
	}
	
	public void setImageManager(ImageManager tempAdapter) {
		this.img_mgr = tempAdapter;
		Log.i(tag, "ImageAdapter instantiated");
	}
	

	
    public String sendGetRequest(Map<String,String> params, String url) {

    	HttpClient client = new DefaultHttpClient();
		Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
		
		for (String key : params.keySet())
		{
			uriBuilder.appendQueryParameter(key, params.get(key));
		}
				
		Uri uri = uriBuilder.build();		
		Log.i(tag, "Constructed get Request : " + uri.toString());		
		HttpGet request = new HttpGet(uri.toString());
		String response = null;
        ResponseHandler<String> responseHandler=new BasicResponseHandler();
        
		try	{			 
				response = client.execute(request, responseHandler);
				Log.i(tag, "Response body=\n"+response);
			}
		
		catch (ClientProtocolException E) {			
				Log.i(tag, "Caught ClientSideException");		
			}
		
		catch (IOException E) {			
				Log.i(tag, "Caught IOException");
			}
		finally	{			
				client.getConnectionManager().shutdown();				
			}
		return response;
    }


	public String postJSON(String jsonString, String url)
	{
		int statusCode= 0;
	    final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
	
		HttpResponse response = null;
		String responseBody = null;
		
		try	{
		
				HttpPost postRequest = new HttpPost(url);
				
				StringEntity input = new StringEntity(jsonString);
				input.setContentType("application/json");
				postRequest.setEntity(input);
				response = httpClient.execute(postRequest);
				responseBody = new BasicResponseHandler().handleResponse(response);
				statusCode = response.getStatusLine().getStatusCode();
				Log.i(tag,response.getStatusLine().toString());
				Log.i(tag,response.getEntity().getContent().toString());
			}
		
		catch (Exception ex) {
			ex.printStackTrace();
			Log.e(tag, "Response code"+ new Integer(statusCode));//Error Handling goes here
		}
		finally {
			httpClient.getConnectionManager().shutdown(); 
		}
		
		return responseBody;		
	}
	
	//New Event
	
	class PostPOJO 	{
		
		HashMap<String, String> post = new HashMap<String,String>();
		
		public PostPOJO(String comment, String lat, String lon, String Expiry, String imgText){
			
			post.put("userId", userId);			
			post.put("text", comment);
			post.put("userName", userName);		
			post.put("userPic", imgText);
	    	post.put("catId","E");				
	    	post.put("lat", lat);
	    	post.put("lon",lon);				
	    	post.put("exp",Expiry);
	    	
	    	Log.i(tag, "Name of user: " + userName);
	    	
		}
	}
	
	public String createPost(String comment, String lat, String lon,  String expiry, String imgText) throws Exception {
    	Log.i(tag,"Creating Post"); 
    	Map<String, String> post = new HashMap<String, String>();
    	String jsonString = gson.toJson(new PostPOJO(comment, lat.toString(), lon.toString(), expiry.toString(), imgText));
    	Log.i(tag,"Created JSON String - \n" + jsonString);    	
    	return postJSON(jsonString, create_post_url);
    }
	
	
	public class CommentPOJO{
		HashMap<String, String> comment = new HashMap<String,String>();
		public CommentPOJO(String postId, String text) 		{
			
			comment.put("postId",postId); 						comment.put("text", text);
			comment.put("userName", userName);					comment.put("userId", userId); 
			
			
		}
	}

	
	//Generate New Comment
    public String createComment(String comment, String postId) throws Exception
    {
    	Log.i(tag,"Creating comment:"+userId) ; 
    	Map<String, String> post = new HashMap<String, String>();
    	String jsonString = gson.toJson(new CommentPOJO(postId,comment));
    	
    	Log.i(tag, "JSONSTRING:"+jsonString);
    	return postJSON(jsonString, create_comment_url);	
    }
    
    public String getComments(String postId) {
    	
    	if (this.t_adapter==null){
    		//The adapter for comments list is not initialized.
    		Log.e(tag, "ThreadArrayAdapter not set: Not executing");
    		return null;
    	}
    	
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("postId", postId);
    	return sendGetRequest(params, get_comments_url);
    	
    }
    
    public String getData (String lat, String lon) throws Exception {
    	
    	Log.i(tag, "Entered getData Call");
    	//Get data to populate the list
    	if (this.img_mgr==null){    		
    		Log.e(tag, "ImageAdapter not set: Not executing");
    		return null;
    	}
    	    	
	    	
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("lat", lat.toString());
    	params.put("lon",lon.toString());
    	params.put("dist","20000");
    	String response = sendGetRequest(params, get_data_url);
    	Log.i(tag, "Response loaded" + response);
    	return response;

    }
    
    public String searchData (String searchText, String lat, String lon) throws Exception {
    	
    	Log.i(tag, "Entered searchData Call");
    	//Get data to populate the list
    	if (this.img_mgr==null){    		
    		Log.e(tag, "ImageAdapter not set: Not executing");
    		return null;
    	}
    	    	    	    	    	
    	Map<String, String> params = new HashMap<String, String>();
    	
//    	params.put("lat", lat.toString());
//    	params.put("lon",lon.toString());
//    	params.put("dist","20000");
    	params.put("searchText", searchText);
    	
    	String response = sendGetRequest(params, search_url);
    	Log.i(tag, "Response loaded" + response);
    	return response;

    }
   
    
    public class customResponse{
    	public String responseType;
    	public String responseBody;
    	
    	public customResponse(String responseType, String responseBody)
    	{
    		this.responseType = responseType;
    		this.responseBody = responseBody;
    	}
    }
    
	@Override
	protected Object doInBackground(Object... params) {

	    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
	    
	    UserInfoProvider creds = UserInfoProvider.getInstance();
	    Log.i(tag, "UserName: " + creds.getUserName() );
		customResponse response = null;
		String query = (String)params[0];
		Log.i(tag, "Initiating doInBackground"+query);
		try {
			
			if 	(query.equals(new_post)){	
				
				Log.i(tag, "Extracting variables");

				String text = (String) params[1];
				String lat = (String)params[2];
				String lon = (String)params[3];
				String expiry = (String) params[4];
				String imgText;
				try{
				 imgText = (String) params[5];
				}
				catch(Exception e) {
					imgText = null;
				}
				
				Log.i(tag, "Variables received: \nText:"+text + "\nlat:"+lat+"\nlong:"+lon+"\nexpiry:"+expiry);
				response = new customResponse(query, createPost(text,lat,lon,expiry, imgText));
			}
			
			
			else if (query.equals(new_comment)) {
				
				Log.i(tag, "Trying to process "+new_comment);
				
				String comment = (String) params[1];
				String postId = (String)  params[2];
				Log.i(tag, "Creating new variables "+"comment:"+comment + " postId"+postId);
				response = new customResponse(query, createComment(comment, postId));
				
			}
			
			
			else if (query.equals(get_data)){
				
				String lat = params[1].toString();
				String lon = params[2].toString();
				Log.i(tag, "Lat: "+lat + " Long:"+ lon);
				response = new customResponse(query, getData(lat,lon));
				Log.i(tag, "loaded new response" + response.responseBody);
			}
			
			else if (query.equals(search_data)){
				
				String searchText = params[1].toString();
				Log.i(tag, "Searching for text "+searchText);
				response = new customResponse(query, searchData(searchText, null, null));
				Log.i(tag, "loaded new response" + response.responseBody);
			}
			
			else if (query.equals(get_comments)){
				
				String postId = params[1].toString();
				response = new customResponse(query, getComments(postId));
			}
			
		}
		catch(Exception shit)
		{
			Log.e(tag, "Error:" + shit.getMessage());
			return null;
		}
		
	Log.i(tag, "DoInBackground Returning response:\n"+response.responseBody);
	return response;	
	}

	@Override
	protected void onPostExecute(Object response)
	{
		
		//Testbed
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		customResponse cres = (customResponse) response;
		Log.i(tag,"onPostExecute: "+cres.responseType.equals(this.get_data));
		 
		if (cres.responseType.equals(this.new_comment)){	 //Do something
		}
		
		if (cres.responseType.equals(this.new_post)){
			//Do second thing
		}
		
		if (cres.responseType.equals(this.get_comments)){
			//Do second thing
			try {
				
				JSONObject json = new JSONObject(cres.responseBody);
				Log.i(tag, "JSON received: " + json.toString());
				JSONArray commentsList = json.getJSONArray("comment");
				
				String lastUser = "";
				boolean bool = false;				
				for (int i=0; i< commentsList.length(); i++) {
					
					JSONObject singleComment = commentsList.getJSONObject(i);
					String commentUser = singleComment.getString("userId");
					String commentUserName = null;
					try{
					commentUserName = singleComment.getString("userName");
					} catch(Exception e) {};
					//if (!commentUser.equals(userId))
					if (!commentUser.equals(lastUser))	{
						
								bool = !bool ;
								lastUser = commentUser ;
					}
					if (commentUserName!=null){
						
						t_adapter.add(new OneComment(bool, singleComment.getString("text"), commentUser, commentUserName));						
					} else {
						t_adapter.add(new OneComment(bool, singleComment.getString("text"), commentUser));
					}
				}
			} catch (JSONException e) {
				
				// TODO Auto-generated catch block
				
				Log.e(tag, "JSONParsingfailed"+ e.getMessage());			
			}
		}

		if (cres.responseType.equals(this.get_data) || cres.responseType.equals(this.search_data)){
			
			//Do third thing;
			
			Log.i(tag, "Entering responsetype " + this.get_data);

			try {	
				Log.i(tag, cres.responseBody);
				JSONObject json = new JSONObject(cres.responseBody);
				JSONArray jarray = (JSONArray)json.get("post");
				

				
				for (int i=0; i<jarray.length(); i++)
				{
					json = jarray.getJSONObject(i);
					Log.i(tag, "JSONObject:"+json.toString());
					EventItem new_comment = new EventItem (json);
					Log.i(tag, "Trying to add a view to EventList");
					PanoramioItem item = new_comment.getPanoramioItem();
					Log.i(tag, "item generated"+item);
					this.img_mgr.add(item);
					Log.i(tag, "View added");
					}
				} 				
				catch (JSONException e) {	
					Log.e(tag, "JSONException" + e.getMessage()); 	
				} catch (Exception e) {
					Log.e(tag, "Somecrap" + e.getMessage());
				}
			}
		} 
	}