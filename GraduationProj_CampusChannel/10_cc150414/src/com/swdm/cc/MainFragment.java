package com.swdm.cc;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;

public class MainFragment extends Fragment{
	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.main, container, false);

	    LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setFragment(this);
	    
	    authButton.setReadPermissions(Arrays.asList("user_friends", "public_profile", "read_friendlists"));


	    return view;
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	        
	        if (session.getState().isOpened()) {
				
				Log.i("session",
						"session is open " + session.getPermissions());
				
//				new Request(session, "/me/taggable_friends", null,
//						HttpMethod.GET, new Request.Callback() {
//					public void onCompleted(Response response) {
//						/* handle the result */
//						Log.i("INFO4", response.toString());
//
//						GraphObject graphObject = response.getGraphObject();
//
//						if (graphObject == null) {
//							return;
//						}
//
//						JSONObject jsonObject = graphObject
//								.getInnerJSONObject();
//						JSONArray array;
//						ArrayList<String> name_list = new ArrayList<String>();
//
//						try {
//							array = jsonObject.getJSONArray("data");
//
//							JSONObject o;
//
//							for (int i = 0; i < array.length(); i++) {
//								
//								o = array.getJSONObject(i);
//								
//								// Log.i("INFO friend",
//								// o.getString("name")+o.getString("id"));
//								name_list.add(o.getString("name"));
//							}
//							// Log.i("IDLIST", response.toString());
//							// Log.i("LIST", list.toString());
//							String friendsList = name_list.toString();
//							Log.i("LIST", friendsList);
//
//							
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}).executeAsync();
	        }
	    }
	};
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
	}
//	
//	private void onClickLogin() {
//	    Session session = Session.getActiveSession();
//	    if (!session.isOpened() && !session.isClosed()) {
//	    	
//	        session.openForRead(new Session.OpenRequest(this)
//	            .setPermissions(Arrays.asList("public_profile"))
//	            .setCallback(statusCallback));
//	        
//	    } else {
//	        Session.openActiveSession(getActivity(), this, true, statusCallback);
//	    }
//	}

	private class SessionStatusCallback implements Session.StatusCallback {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	            // Respond to session state changes, ex: updating the view
	    }
	}
	
	
	
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	    
	 // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }

	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
}
