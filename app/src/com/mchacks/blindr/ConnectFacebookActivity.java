package com.mchacks.blindr;

import java.util.Arrays;
import java.util.List;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.mchacks.blindr.controllers.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class ConnectFacebookActivity extends Activity {

	private UiLifecycleHelper uiHelper;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	}; 
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		Controller.getInstance().setSession(session);
		if(state.isOpened()) {
			List<String> permissions = session.getPermissions();
			Log.i("FACEBOOK_CONNECTION", "Logged in..." + permissions.toString());
			Intent i = new Intent(ConnectFacebookActivity.this, PublicChatActivity.class);
			startActivity(i);
			finish();
		} else if(state.isClosed()) {
			Log.i("FACEBOOK_CONNECTION", "Logged out...");
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.connect_facebook);
		
		LoginButton authButton = (LoginButton)findViewById(R.id.authButton);
		authButton.setReadPermissions(Arrays.asList("public_profile"));
		
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if(session != null && 
				(session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
	
	
}
