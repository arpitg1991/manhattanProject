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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class AsyncWebPostMaster extends AsyncTask {

	static String tag = "WebPostMaster";
	static String userId;
	static String create_post_url = "http://shrouded-retreat-3846.herokuapp.com/createPost";
	//static String get_post_url = "http://shrouded-retreat-3846.herokuapp.com/getPost?lat=40.808&long=-73.96";
	static String create_comment_url = "http://???/";
    static String get_data_url = "http://shrouded-retreat-3846.herokuapp.com/getPost?";
    static Context context;
	

    public static String new_post = "NEW_POST";
    public static String new_comment = "NEW_COMMENT";
    public static String get_data = "GET_DATA";
    public static String get_comments_url = "GET_COMMENTS";
    private static Gson gson = new Gson();
    private ThreadArrayAdapter t_adapter;
    private ImageManager img_mgr;
    private Context myContext;
    //private static AsyncWebPostMaster instance = null;
     
    
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
	
	public void setImageManager(ImageManager tempAdapter)
	{
		this.img_mgr = tempAdapter;
		Log.i(tag, "ImageAdapter instantiated");
	}
	

	
    public HttpResponse sendGetRequest(Map<String,String> params, String url)
    {
    	HttpClient client = new DefaultHttpClient();
		Uri.Builder uriBuilder = Uri.parse(url).buildUpon();
		
		for (String key : params.keySet())
		{
			uriBuilder.appendQueryParameter(key, params.get(key));
		}
				
		Uri uri = uriBuilder.build();
		
		Log.i(tag, "Constructed get Request : " + uri.toString());
		
		HttpGet request = new HttpGet(uri.toString());
		HttpResponse response = null;
		try{
		response = client.execute(request);
		}
		catch (ClientProtocolException E) { Log.i(tag, "Caught ClientSideException");}
		catch (IOException E) {Log.i(tag, "Caught IOException");}
		finally{
			client.getConnectionManager().shutdown();
		}
		
		return response;
    }


	public HttpResponse postJSON(String jsonString, String url)
	{
		int statusCode= 0;
		
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpResponse response = null;
		try{
			
		HttpPost postRequest = new HttpPost(url);
		StringEntity input = new StringEntity(jsonString);
		input.setContentType("application/json");
		postRequest.setEntity(input);
		response = httpClient.execute(postRequest);
		statusCode = response.getStatusLine().getStatusCode();
		Log.i(tag,response.getStatusLine().toString());
		Log.i(tag,response.getEntity().getContent().toString());
		}
		catch (Exception ex){
			ex.printStackTrace();
			Log.e(tag, "Response code"+ new Integer(statusCode));//Error Handling goes here
		}
		finally
		{
			httpClient.getConnectionManager().shutdown();
		}
		
		return response;
		
	}
	
	//New Event
	
	class PostPOJO 	{
		
		HashMap<String, String> post = new HashMap<String,String>();
		
		public PostPOJO(String comment, String lat, String lon, String Expiry){
			
			post.put("userId", userId);			post.put("text", comment);
	    	post.put("catId","E");				post.put("lat", lat);
	    	post.put("lon",lon);				post.put("exp",Expiry);
	    	
		}
	}
	
	public HttpResponse createPost(String comment, String lat, String lon, String expiry) throws Exception
    {
    	Log.i(tag,"Creating Post") ; 
    	
    	Map<String, String> post = new HashMap<String, String>();
    	String jsonString = gson.toJson(new PostPOJO(comment, lat.toString(), lon.toString(), expiry.toString()));

    	Log.i(tag,"Created JSON String - \n" + jsonString);
    	
    	return postJSON(jsonString, create_post_url);
    	
       }
	
	
	public class CommentPOJO{
		HashMap<String, String> post = new HashMap<String,String>();
		public CommentPOJO(String postId, String comment) 		{
			post.put("postId",postId);
			post.put("userId", userId);
			post.put("text", comment);	    	
		}
	}

	
	//Generate New Comment
    public HttpResponse createComment(String comment, String postId) throws Exception
    {
    	System.out.println("in Create") ; 
    	
    	Map<String, String> post = new HashMap<String, String>();
    	String jsonString = gson.toJson(new CommentPOJO(postId,comment));
    	
    	return postJSON(jsonString, create_comment_url);
    	
    }
        
    
    public HttpResponse getComments(String postId) {
    	
    	if (this.t_adapter==null){
    		//The adapter for comments list is not initialized.
    		Log.e(tag, "ThreadArrayAdapter not set: Not executing");
    		return null;
    	}
    	
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("postId", postId);
    	HttpResponse response = sendGetRequest(params, get_comments_url);
    	return response;
    }
    
    public HttpResponse getData (String lat, String lon) throws Exception{
    	
    	Log.i(tag, "Entered getData Call");
    	//Get data to populate the list
    	if (this.img_mgr==null){
    		
    		Log.e(tag, "ImageAdapter not set: Not executing");
    		return null;
    	}
    	
    	
    	System.out.println("in getpost") ;
    	
    	
    	
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("lat", lat.toString());
    	params.put("lon",lon.toString());
    	params.put("dist","500");
    	
    	HttpResponse response = sendGetRequest(params, get_data_url);

    	return response;

    }
   
    
    public class customResponse{
    	public String responseType;
    	public HttpResponse responseBody;
    	
    	public customResponse(String responseType, HttpResponse responseBody)
    	{
    		this.responseType = responseType;
    		this.responseBody = responseBody;
    	}
    }
    
	@Override
	protected Object doInBackground(Object... params) {

	    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		customResponse response = null;
		String query = (String)params[0];
		Log.i(tag, "Initiating doInBackground");
		try {
			
			if 	(query.equals(new_post)){	
				
				Log.i(tag, "Extracting variables");

				String text = (String) params[1];
				String lat = (String)params[2];
				String lon = (String)params[3];
				String expiry = (String) params[4];
				Log.i(tag, "Variables received: \nText:"+text + "\nlat:"+lat+"\nlong:"+lon+"\nexpiry:"+expiry);
				response = new customResponse(query, createPost(text,lat,lon,expiry));
			}
			
			
			else if (query.equals(new_comment)) {
				
				String comment = (String) params[1];
				String postId = (String)  params[2];
				response = new customResponse(query, createComment(postId, comment));
				
			}
			
			
			else if (query.equals(get_data)){
				
				String lat = params[1].toString();
				String lon = params[2].toString();
				Log.i(tag, "Lat: "+lat + " Long:"+ lon);
				response = new customResponse(query, getData(lat,lon));
			}
			
		}
		catch(Exception shit)
		{
			shit.printStackTrace();
			return null;
		}
		
	return response;	
	}

	@Override
	protected void onPostExecute(Object response)
	{
		customResponse cres = (customResponse) response;
		
		 if (cres.responseType.equals(this.new_comment)){
			//Do something
		}
		if (cres.responseType.equals(this.new_post)){
			//Do second thing
		}
		 if (cres.responseType.equals(this.get_data)){
			//Do third thing;
			 customResponse cR = (customResponse)response;
		try {	
				JSONObject json = transformResponseToJSON(cR);
				JSONArray jarray = (JSONArray)json.get("post");
				//json = (JSONObject) jarray.get(0);
				//Log.i(tag, "Individual JSONObject:" + json.toString());
				
				for (int i=0; i<jarray.length(); i++)
				{
					json = jarray.getJSONObject(i);
					Log.i(tag, "JSONObject:"+json.toString());
					EventItem new_comment = new EventItem (json);
					Log.i(tag, "Trying to add a view to EventList");
					PanoramioItem item = new_comment.getPanoramioItem();
					this.img_mgr.add(item);
					Log.i(tag, "View added");
				}
				
			} 
		catch (JSONException | IOException e) {
				
				e.printStackTrace();
			}
			 		 
		}
		
	}
	
	 public JSONObject transformResponseToJSON(customResponse cR) throws JSONException, IOException {
		 HttpResponse response = cR.responseBody;
		 if (response!=null){
		 BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		 StringBuilder builder = new StringBuilder();
		 for (String line = null; (line = reader.readLine()) != null;) {
		     builder.append(line).append("\n");
		 } 		 
		 JSONTokener tokener = new JSONTokener(builder.toString());
		 JSONObject finalResult = new JSONObject(tokener);
		 Log.i(tag,"JSON Response"+finalResult.toString());
		 
		 return finalResult;}
		 else
			 return null;
	 }
}
