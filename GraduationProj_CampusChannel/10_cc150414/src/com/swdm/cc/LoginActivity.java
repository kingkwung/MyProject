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

			// 이거 뺏더니 페북로그인버튼 1개됨
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

		// 랜덤으로 메인글씨 뿌려주기
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

	// 로그인 여부 확인
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
				Toast.makeText(getApplicationContext(), "로그인하세요.",
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
											// 로그인 성공!
											Toast.makeText(
													getApplication(),
													"Welcome, "
															+ user.getName(),
													Toast.LENGTH_SHORT).show();
											Log.i("fb",
													"fb user: "
															+ user.toString());

											// mainActivity를 불러주면 된다.
											Intent intent = new Intent(
													LoginActivity.this,
													MainActivity.class);

											// 부를 때 내 정보 같이 넘김
											intent.putExtra("user_id", user
													.getId().toString());
											intent.putExtra("user_name", user
													.getName().toString());
											intent.putExtra("user_gender", user
													.asMap().get("gender")
													.toString());
											// intent.putExtra("age", "21");
											// age 원래는 이거 ->
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
			// 비회원 전용
			Toast.makeText(
					getApplication(),
					"비회원으로 구경합니다. ",
					Toast.LENGTH_SHORT).show();
			
			// mainActivity를 불러주면 된다.
			Intent intent = new Intent(
					LoginActivity.this,
					MainActivity.class);

			// 부를 때 내 정보 같이 넘김
			intent.putExtra("user_id", "000");
			intent.putExtra("user_name", "손님");
			intent.putExtra("user_gender", "손님sex");
			// intent.putExtra("age", "21");
			// age 원래는 이거 ->
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
