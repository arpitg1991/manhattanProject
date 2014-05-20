package com.example.seekr;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
 
public class ItemRow {
 
      private String itemName;
      private String text;
      private String userId;
      private Double Lat;
      private Double Long;
      private String postTime;
      private String expiryTime;
      private String postId;
      private String userPic;
      
      Drawable icon;
 
      public ItemRow(String itemName, Drawable icon) {
            super();
            this.itemName = itemName;
            this.icon = icon;
      }
      public String getItemName() {
            return itemName;
      }
      public void setItemName(String itemName) {
            this.itemName = itemName;
      }
      public Drawable getIcon() {
            return icon;
      }
      public void setIcon(Drawable icon) {
            this.icon = icon;
      }
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Double getLat() {
		return Lat;
	}
	public void setLat(Double lat) {
		Lat = lat;
	}
	public Double getLong() {
		return Long;
	}
	public void setLong(Double l) {
		Long = l;
	}
	public String getPostTime() {
		return postTime;
	}
	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}
	public String getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getUserPic() {
		return userPic;
	}
	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}
 
	public Bitmap getBitmapUserPic(){
		byte[] bytearray = Base64.decode(userPic, Base64.DEFAULT);
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
		bitmap = getRoundedCornerBitmap(bitmap, 50);
		return bitmap;
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
	    
		try{
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
	            .getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);
	    final float roundPx = pixels;

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);

	    return output;
		} catch (Exception e) {
			//Log.i(tag, "Bitmap rounding failed");
			return bitmap;
		}
	}
}