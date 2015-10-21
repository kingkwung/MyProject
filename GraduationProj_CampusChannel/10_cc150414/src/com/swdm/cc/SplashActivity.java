package com.swdm.cc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SplashActivity extends Activity implements OnClickListener {
	/*@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				finish();
			}
		};
		
		handler.sendEmptyMessageDelayed(0,4000);
	}*/
	
private ImageView mIvAnimation;	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		mIvAnimation = (ImageView)findViewById(R.id.main_iv_animation);
		mIvAnimation.setOnClickListener(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		
		// 뷰가 화면에 뿌려지기 전에는 애니메이션이 시작하지 못한다. 
		// 약간의 딜레이를 주기 위하여 핸들러를 이용한다.
		
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				mIvAnimation.setBackgroundResource(R.drawable.splash);
				AnimationDrawable frameAnimation = (AnimationDrawable) mIvAnimation.getBackground();
				frameAnimation.start();
			}
		}, 1000);
	}
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
		// 이미지뷰가 클릭되었을 때도 같은 방식으로 애니메이션을 시작할 수 있도록 한다.
		case R.id.main_iv_animation:

			// loading 화면 띄워주기..
			startActivity(new Intent(this, LoginActivity.class));
			break;
		}
	}
	/*private ImageView mIvAnimation;	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		mIvAnimation = (ImageView)findViewById(R.id.main_iv_animation);
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				mIvAnimation.setBackgroundResource(R.drawable.splash);
				AnimationDrawable frameAnimation = (AnimationDrawable) mIvAnimation.getBackground();
				frameAnimation.start();
			}
		}, 1000);
		// mIvAnimation.setOnClickListener(this);
	}*/
}