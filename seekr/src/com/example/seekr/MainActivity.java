package com.example.seekr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class MainActivity extends FragmentActivity {

	private static final int SETTINGS = 2;
	private static final int SPLASH = 0;
	private static final int SELECTION = 1;
	private static final int FRAGMENT_COUNT = SETTINGS +1;
	//	private static final int FRAGMENT_COUNT = SELECTION +1;
	private MenuItem settings;
	private Session session;

	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.example.seekr", 
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {

				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("MainActivity", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		FragmentManager fm = getSupportFragmentManager();
		fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
		fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
		fragments[SETTINGS] = fm.findFragmentById(R.id.userSettingsFragment);

		FragmentTransaction transaction = fm.beginTransaction();
		for(int i = 0; i < fragments.length; i++) {
			transaction.hide(fragments[i]);
		}
		transaction.commit();
	}


	private void showFragment(int fragmentIndex, boolean addToBackStack) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			if (i == fragmentIndex) {
				transaction.show(fragments[i]);
			} else {
				transaction.hide(fragments[i]);
			}
		}
		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	private boolean isResumed = false;
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		// Only make changes if the activity is visible
		if (isResumed) {
			FragmentManager manager = getSupportFragmentManager();
			// Get the number of entries in the back stack
			int backStackSize = manager.getBackStackEntryCount();
			// Clear the back stack
			for (int i = 0; i < backStackSize; i++) {
				manager.popBackStack();
			}

			System.out.println("ON SESSION STATE CHANGE OPEN CHOICE.......................");

			System.out.println("SESSION STATE");
			System.out.println(state.name());

			if (state.isOpened()) {
				// If the session state is open:
				// Show the authenticated fragment
				System.out.println("SELECTION OPENED.... REDIRECTING TO PANORAMIO");
				Intent intent = new Intent(this, Panoramio.class);

				// GET USER INFO
				// make request to the /me API				
				System.out.println("gETTING UID 1");
				Request request = Request.newMeRequest(this.getSession(),
						new Request.GraphUserCallback() {
					// callback after Graph API response with user object

					@Override
					public void onCompleted(GraphUser user,
							Response response) {
						if (user != null) {
							System.out.println(user.getName() + ","
									+ user.getUsername() + ","
									+ user.getId() + "," + user.getLink()
									+ "," + user.getFirstName()+ user.asMap().get("email"));						
						}
					}
				});

				Request.executeBatchAsync(request);

				startActivity(intent);

				//	            showFragment(SELECTION, false);
			} else if (state.isClosed()) {
				// If the session state is closed:
				// Show the login fragment
				System.out.println("SPLASH OPENED");
				showFragment(SPLASH, false);
			}
		}
	}

	public final Session getSession() {
		return this.session;
	}


	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Session session = Session.getActiveSession();

		System.out.println("ON RESUME FRAGMENTS SESSION STATE.......................");
		System.out.println(session.getState());

		System.out.println("RESUME CHOICE.......................");

		if (session != null && session.isOpened()) {
			// if the session is already open,
			// try to show the selection fragment
			System.out.println("SELECTION RESUMED");
			System.out.println(session.isOpened());
			//	        showFragment(SELECTION, false);
			Intent intent = new Intent(this, Panoramio.class);

			// GET USER INFO
			// make request to the /me API			
			System.out.println("gETTING UID 2");
			Request request = Request.newMeRequest(session,
					new Request.GraphUserCallback() {
				// callback after Graph API response with user object

				@Override
				public void onCompleted(GraphUser user,
						Response response) {
					System.out.println("gETTING is null?");
					System.out.println(user == null);
					if (user != null) {
						System.out.println(user.getName() + ","
								+ user.getUsername() + ","
								+ user.getId() + "," + user.getLink()
								+ "," + user.getFirstName()+ user.asMap().get("email"));
						
						UserInfoProvider info = UserInfoProvider.getInstance();
						// USER ID
						String USER_ID = user.getId();
						info.setUserId(USER_ID);

						// USER NAME
						String NAME_OF_USER = user.getName();
						info.setUserName(NAME_OF_USER);
						// DISPLAY IMAGE
						URL fbAvatarUrl = null;
						Bitmap fbAvatarBitmap = null;
//						try {
//							fbAvatarUrl = new URL("http://graph.facebook.com/" + USER_ID + "/picture");
//							fbAvatarBitmap = BitmapFactory.decodeStream(fbAvatarUrl.openConnection().getInputStream());
//						} catch (MalformedURLException e) {
//							e.printStackTrace();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
						
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						info.setImageBitmap(fbAvatarBitmap);
//						fbAvatarBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//						byte[] byteArray = stream.toByteArray();
					}
				}
			});

			Request.executeBatchAsync(request);

			startActivity(intent);
		} else {
			// otherwise present the splash screen
			// and ask the person to login.
			System.out.println("SPLASH RESUMED");
			showFragment(SPLASH, false);
		}
	}

	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = 
			new Session.StatusCallback() {
		@Override
		public void call(Session session, 
				SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	//	@Override
	//	protected void onCreate(Bundle savedInstanceState) {
	//		super.onCreate(savedInstanceState);
	//		setContentView(R.layout.activity_main);
	//
	//		if (savedInstanceState == null) {
	//			getSupportFragmentManager().beginTransaction()
	//					.add(R.id.container, new PlaceholderFragment()).commit();
	//		}
	//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// only add the menu when the selection fragment is showing
		if (fragments[SELECTION].isVisible()) {
			if (menu.size() == 0) {
				settings = menu.add(R.string.settings);
			}
			return true;
		} else {
			menu.clear();
			settings = null;
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println("-----------HERE------------");
		System.out.println(item.equals(settings));
		System.out.println(item.getClass().toString());
		if (item.equals(settings)) {
			showFragment(SETTINGS, true);
			return true;
		}
		return false;
	}

	//	@Override
	//	public boolean onOptionsItemSelected(MenuItem item) {
	//		// Handle action bar item clicks here. The action bar will
	//		// automatically handle clicks on the Home/Up button, so long
	//		// as you specify a parent activity in AndroidManifest.xml.
	//		int id = item.getItemId();
	//		if (id == R.id.action_settings) {
	//			return true;
	//		}
	//		return super.onOptionsItemSelected(item);
	//	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
