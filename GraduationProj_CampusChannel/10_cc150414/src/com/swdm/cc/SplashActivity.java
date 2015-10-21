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
		
		// �䰡 ȭ�鿡 �ѷ����� ������ �ִϸ��̼��� �������� ���Ѵ�. 
		// �ణ�� �����̸� �ֱ� ���Ͽ� �ڵ鷯�� �̿��Ѵ�.
		
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
		// �̹����䰡 Ŭ���Ǿ��� ���� ���� ������� �ִϸ��̼��� ������ �� �ֵ��� �Ѵ�.
		case R.id.main_iv_animation:

			// loading ȭ�� ����ֱ�..
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