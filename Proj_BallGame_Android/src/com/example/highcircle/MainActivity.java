package com.example.highcircle;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;

public class MainActivity extends Activity {

	static int measuredWidth = 0;
	static int measuredHeight = 0;
	static float density;
	static int unitSpeed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// 밀도 구하기
		density = getResources().getDisplayMetrics().density;
		// 기본 단위 속도 구하기 (화면 크기에 맞게)
		unitSpeed = (int) (density * 2) / 3;

		// 앱 이름 없애기
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// width, (height) 구하기 (가로 길이의 n으로 나누어서 반지름을 구하려고)
		getWidthHeightOfTheScreen();
				
		super.onCreate(savedInstanceState);
		setContentView(new MainView(this, this));
	}
	
	public void goToGameActivity(){
		Intent i = new Intent(this, GameActivity.class);
		startActivity(i);
	}
	
	
	// 메인에서 백버튼 
	public void onBackPressed() {
		  android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public void exitActivity(){
		  android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@SuppressWarnings("deprecation")
	private void getWidthHeightOfTheScreen() {
		Point size = new Point();

		Display display = getWindowManager().getDefaultDisplay();
		measuredWidth = display.getWidth();
		measuredHeight = display.getHeight();

	}

}
