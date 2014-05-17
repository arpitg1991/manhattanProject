package com.example.seekr;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;



public class CustomWebActivity extends Activity{

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView webview = new WebView(this);
		
		
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		webview.getSettings().setJavaScriptEnabled(true);
		
		final Activity activity = this;
		webview.setWebChromeClient(new WebChromeClient() {
		public void onProgressChanged(WebView view, int progress) {
		     // Activities and WebViews measure progress with different scales.
		     // The progress meter will automatically disappear when we reach 100%
		     activity.setProgress(progress * 1000);
		   }
		 });
		 webview.setWebViewClient(new WebViewClient() {
		   public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		     Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
		   }
		 });

		 webview.loadUrl("https://dl.dropboxusercontent.com/u/7359751/seekr/index.html");
		 setContentView(webview);
		 
		 
		
	}

	
}
