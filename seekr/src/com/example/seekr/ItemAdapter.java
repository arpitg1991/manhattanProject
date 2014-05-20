package com.example.seekr;

import java.security.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
 
public class ItemAdapter extends ArrayAdapter {
	
      List   data;
      Context context;
      int layoutResID;
      Object temp;
      
      Drawable defaultImage = null;
      
      String tag = "ItemAdapter";
      
public ItemAdapter(Context context, int layoutResourceId,List data) {
      super(context, layoutResourceId, data);
 
      this.data=data;
      this.context=context;
      this.layoutResID=layoutResourceId;
      this.defaultImage = context.getResources().getDrawable(R.drawable.ic_launcher);
      // TODO Auto-generated constructor stub
}

	
	
	public void addItemRow(ItemRow newRow)
	{
		data.add(newRow);
		this.notifyDataSetChanged();
	}


@Override
public View getView(int position, View convertView, ViewGroup parent) {
 
      NewsHolder holder = null;
         View row = convertView;
          holder = null;
 
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResID, parent, false);
 
            holder = new NewsHolder();
 
            holder.itemName = (TextView) row.findViewById(R.id.example_username);
            holder.text 	= (TextView) row.findViewById(R.id.example_text);
            holder.icon=(ImageView)row.findViewById(R.id.example_image);
            holder.button1=(Button)row.findViewById(R.id.swipe_button1);
            holder.button2=(Button)row.findViewById(R.id.swipe_button2);
            holder.button3=(Button)row.findViewById(R.id.swipe_button3);
            row.setTag(holder);
        }
        else
        {
            holder = (NewsHolder)row.getTag();
        }
 
        final ItemRow itemdata =  (ItemRow) data.get(position);
        
        holder.itemName.setText(itemdata.getItemName());
        holder.text.setText(itemdata.getText());
        
        
        if (itemdata.getUserPic()!=null) {
        	BitmapDrawable bdraw = new BitmapDrawable(context.getResources(), itemdata.getBitmapUserPic());
        	holder.icon.setImageDrawable(bdraw);
        } else { 
        	holder.icon.setImageDrawable(defaultImage);
        }
        
        holder.button1.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.drawable.ic_action_place), null, null);
        holder.button1.setText("Map");
        holder.button1.setOnClickListener(new View.OnClickListener() {
 
                  @Override
                  public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Toast.makeText(context, "Loading map",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, MapDisplayActivity.class);
                		i.putExtra("poster", itemdata.getItemName());
                		i.putExtra("message", itemdata.getText());
                		i.putExtra("lat", itemdata.getLat().floatValue());
                		i.putExtra("long", itemdata.getLong().floatValue());
                		Log.i(tag, "lat" +itemdata.getLat()+ "long"+itemdata.getLong());
                		context.startActivity(i);

                  }
            });
        
//        class mapHandler {
//        	public void handleMaps() 
//        	{
//        		
//        		return;
//        	}
//        }
        
//itemdata.getExpiryTime();
        
        
        

        String timeLeft = itemdata.getExpiryTime();
        Long timeSeconds = Long.parseLong(timeLeft);
        Date now = new Date();
        Long diffInMins = (timeSeconds-(now.getTime()))/(1000*60);
        String timeDisplayString = "";
        
        if (diffInMins<0)
        {
        	timeDisplayString = "Expired";
        }
        else if (diffInMins<60) {
        	timeDisplayString = timeDisplayString + diffInMins + " min";
        }
        
        else if (diffInMins/60 > 24)
        {
        	timeDisplayString = timeDisplayString+ Math.round(diffInMins/(60*24)) + "d";
        	
        }
        else if (diffInMins>60){
        	timeDisplayString = "Expires: "+ Math.round(diffInMins/60) + " hr";
        }
        
        
        
        holder.button2.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.drawable.ic_action_time), null, null);
        holder.button2.setText(timeDisplayString);
        holder.button2.setOnClickListener(new View.OnClickListener() {
 
                              @Override
                              public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    Toast.makeText(context, "Loading comment thread...",Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(context, ThreadActivity.class);
                                    i.putExtra("userId", itemdata.getUserId());
                                    i.putExtra("lat", itemdata.getLat());
                                    i.putExtra("long", itemdata.getLong());
                                    i.putExtra("postId", itemdata.getPostId());
                                    i.putExtra("message", itemdata.getText());
                                    i.putExtra("poster", itemdata.getItemName());
                                    //i.putExtra("location", temp.getLocation; //For when it is created.
                                    context.startActivity(i);
                              }
                        });
             
        
        
        
        holder.button3.setCompoundDrawablesWithIntrinsicBounds(null,context.getResources().getDrawable(R.drawable.ic_action_reply_all), null, null);
        holder.button3.setText("Reply");
        holder.button3.setOnClickListener(new  View.OnClickListener() {
 
                        @Override
                        public void onClick(View v) {
                              // TODO Auto-generated method stub
                        	  Toast.makeText(context, "Loading comment thread",Toast.LENGTH_SHORT).show();
                        	  Intent i = new Intent(context, ThreadActivity.class);
                              i.putExtra("userId", itemdata.getUserId());
                              i.putExtra("lat", itemdata.getLat());
                               i.putExtra("long", itemdata.getLong());
                              i.putExtra("postId", itemdata.getPostId());
                              i.putExtra("message", itemdata.getText());
                              i.putExtra("poster", itemdata.getItemName());
                              //i.putExtra("location", temp.getLocation; //For when it is created.
                              context.startActivity(i);

                        }
                  });
 
        return row;
 
}
 
static class NewsHolder{
 
      TextView itemName;
      TextView text;
      
      ImageView icon;      
      Button button1;
      Button button2;
      Button button3;
      }
 
}