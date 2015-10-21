package com.swdm.cc;

import java.util.Random;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class LoginActivity extends FragmentActivity implements OnClickListener {
	private ImageView txtImageView;
	private ImageButton loginButton;
	private ImageButton nonmemberLoginButton;
	private MainFragment mainFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup

			// �̰� ������ ��Ϸα��ι�ư 1����
			// mainFragment = new MainFragment();
			// getSupportFragmentManager().beginTransaction()
			// .add(android.R.id.content, mainFragment).commit();
		} else {
			// Or set the fragment from restored state info
			mainFragment = (MainFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}

		// Load the ImageView that will host the animation and
		// set its background to our AnimationDrawable XML resource.
		ImageView img = (ImageView) findViewById(R.id.imageView1);
		img.setBackgroundResource(R.drawable.spin_animation);

		// Get the background, which has been compiled to an AnimationDrawable
		// object.
		AnimationDrawable frameAnimation = (AnimationDrawable) img
				.getBackground();

		// Start the animation (looped playback by default).
		frameAnimation.start();

		// �������� ���α۾� �ѷ��ֱ�
		txtImageView = (ImageView) findViewById(R.id.imageViewTxt);
		int randInt = new Random().nextInt(4) + 1;
		int drawImage = R.drawable.txtimage1;
		
		if (randInt == 1) {
			drawImage = R.drawable.txtimage1;
		}
		else if (randInt == 2) {
			drawImage = R.drawable.txtimage2;
		}
		else if (randInt == 3) {
			drawImage = R.drawable.txtimage3;
		}
		else if (randInt == 4) {
			drawImage = R.drawable.txtimage4;
		}
		txtImageView.setBackgroundResource(drawImage);
		
		loginButton = (ImageButton) findViewById(R.id.login_button);
		nonmemberLoginButton = (ImageButton) findViewById(R.id.nonmember_button);
		loginButton.setOnClickListener(this);
		nonmemberLoginButton.setOnClickListener(this);
	}

	// �α��� ���� Ȯ��
	boolean isLogined() {
		Session session = Session.getActiveSession();
		if (session == null)
			return false;

		if (!session.isOpened())
			return false;

		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == loginButton.getId()) {
			if (!isLogined()) {
				Toast.makeText(getApplicationContext(), "�α����ϼ���.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			// start Facebook Login
			Session.openActiveSession(this, true, new Session.StatusCallback() {

				// callback when session changes state
				@Override
				public void call(Session session, SessionState state,
						Exception exception) {
					Log.i("SESSION", session + " state" + state);
					if (session.isOpened()) {

						// make request to the /me API
						Request.newMeRequest(session,
								new Request.GraphUserCallback() {

									// callback after Graph API response with
									// user
									// object
									@Override
									public void onCompleted(GraphUser user,
											Response response) {
										if (user != null) {
											// �α��� ����!
											Toast.makeText(
													getApplication(),
													"Welcome, "
															+ user.getName(),
													Toast.LENGTH_SHORT).show();
											Log.i("fb",
													"fb user: "
															+ user.toString());

											// mainActivity�� �ҷ��ָ� �ȴ�.
											Intent intent = new Intent(
													LoginActivity.this,
													MainActivity.class);

											// �θ� �� �� ���� ���� �ѱ�
											intent.putExtra("user_id", user
													.getId().toString());
											intent.putExtra("user_name", user
													.getName().toString());
											intent.putExtra("user_gender", user
													.asMap().get("gender")
													.toString());
											// intent.putExtra("age", "21");
											// age ������ �̰� ->
											// intent.putExtra("age",
											// user.asMap().get("age").toString());

											startActivity(intent);
										}
									}
								}).executeAsync();
					}
				}
			});
		}
		
		if (v.getId() == nonmemberLoginButton.getId()) {
			// ��ȸ�� ����
			Toast.makeText(
					getApplication(),
					"��ȸ������ �����մϴ�. ",
					Toast.LENGTH_SHORT).show();
			
			// mainActivity�� �ҷ��ָ� �ȴ�.
			Intent intent = new Intent(
					LoginActivity.this,
					MainActivity.class);

			// �θ� �� �� ���� ���� �ѱ�
			intent.putExtra("user_id", "000");
			intent.putExtra("user_name", "�մ�");
			intent.putExtra("user_gender", "�մ�sex");
			// intent.putExtra("age", "21");
			// age ������ �̰� ->
			// intent.putExtra("age",
			// user.asMap().get("age").toString());

			startActivity(intent);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

}
