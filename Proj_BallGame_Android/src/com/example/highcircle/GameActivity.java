package com.example.highcircle;


import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.os.PowerManager.WakeLock;
import android.view.*;

/*
 * GameActivity이다.
 * 즉, 게임 화면
 * 
 * 그런데, 여기서 그리지는 않고,
 * GameView(extends SurfaceView)를 만들어서
 * 이 Activity를 통으로 채울 것이다.
 * 
 * 즉, 실제로 그려지는 부분은 이 엑티비티가 아닌,
 * GameView이다.
 * 
 * */
public class GameActivity extends Activity {

	static float density;
	static int unitSpeed;

	private GameView gv;

	PowerManager powerManager;
	WakeLock wakeLock;

	// 다른 클래스에서 GameActivity.measuredWidth / Height 가져다 씀
	static int measuredWidth = 0;
	static int measuredHeight = 0;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 밀도 구하기
		density = getResources().getDisplayMetrics().density;
		// 기본 단위 속도 구하기 (화면 크기에 맞게)
		unitSpeed = (int)(density * 2) / 3;

		// 앱 이름 없애기
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// width, (height) 구하기 (가로 길이의 n으로 나누어서 반지름을 구하려고)
		getWidthHeightOfTheScreen();

		// GameView 생성
		// SurfaceView 이며, GameActivity를 채운다.
		gv = new GameView(this, this);
		setContentView(gv);
		
		powerManager = (PowerManager) this
				.getSystemService(Context.POWER_SERVICE);
		
		wakeLock = powerManager.newWakeLock(
				PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "TAG");
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
//		gv.cThread.isPaused = true;
//		gv.mThread.isPaused = true;
//		gv.dThread.isPaused = true;
		
	}
	
	

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
//		AlertDialog dialBox = createDialogBoxOnPlaying();
//		dialBox.show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		wakeLock.acquire();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (wakeLock.isHeld())
			wakeLock.release();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}
	
	private void endThread() {
		// 쓰레드 정리하고나서 //////////////////////////////////////////////
		gv.cThread.isEnd = true; // 더 이상 생성 안되게 생성 쓰레드 end
		gv.mThread.isEnd = true; // 더 이상 안움직이게
		gv.dThread.isEnd = true; // 더이상 안그리게

		while (true) {
			try {
				gv.dThread.join();
				gv.mThread.join();
				gv.cThread.join();
				break;
			} catch (Exception e) {
			}
		}
		// /////////////////////////////////////////////////////////////////////
	}
	
//	public void goToMainActivity(){
//		// 쓰레드 정리하고나서 //////////////////////////////////////////////
//		endThread();
//		///////////////////////////////////////////////////////////////////////
//		
//		finish();
//		
//		Intent i = new Intent(this, MainActivity.class);
//		startActivity(i);
//	}
	
	
	// 게임중일 때 백버튼 (홈버튼은..?)
	public void onBackPressed() {

		// TODO Auto-generated method stub
		// super.onBackPressed(); //지워야 실행됨
		
		if (gv.mThread.isEnd){
			// 종료에 관한 다이얼로그
//			super.onBackPressed();
			AlertDialog dialBox = createDialogBoxOnPlaying();
			dialBox.show();
			
		} else{
			// 게임 중에 눌렀다면
			gv.cThread.isPaused = true;
			gv.mThread.isPaused = true;
			gv.dThread.isPaused = true;
			
			AlertDialog dialBox = createDialogBoxOnPlaying();
			dialBox.show();
		}
		
	}
	
	// 종료, 메뉴로가기, 계속
	public AlertDialog createDialogBoxOnPlaying() {
		AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
				// set message, title, and icon
				.setTitle("Really?")
				.setMessage("메뉴로 이동합니다.")
				// set three option buttons
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								
								// 쓰레드 정리하고나서 //////////////////////////////////////////////
								endThread();
								///////////////////////////////////////////////////////////////////////
								android.os.Process.killProcess(android.os.Process.myPid());
							}
						})// setPositiveButton
//				.setNeutralButton("Menu", new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int whichButton) {
//								
//								// 쓰레드 정리하고나서 //////////////////////////////////////////////
//								endThread();
//								///////////////////////////////////////////////////////////////////////
//								// 메뉴 인텐트
////								goToMainActivity();
//								// 왜 이게 없어도되는거지..
//								
//								// 이걸 안하면 오류가뜸.
//								android.os.Process.killProcess(android.os.Process.myPid());
//							}
//						}) // setNeutralButton
				.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								gv.cThread.isPaused = false;
								gv.mThread.isPaused = false;
								gv.dThread.isPaused = false;
							}
						})// setNegativeButton
				.create();
		return myQuittingDialogBox;
	}// createDialogBox
	
	

	// 핸드폰의 크기를 구한다 (구글에서 복붙한 코드이다 - 이해 안해도 됨)
	// 위에 선언해 놓은 measuredwidth, measuredheight 를 초기화 해준다.
	@SuppressWarnings("deprecation")
	private void getWidthHeightOfTheScreen() {
		Point size = new Point();
		// WindowManager w = getWindowManager();
		//
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
		// w.getDefaultDisplay().getSize(size);
		//
		// measuredWidth = size.x;
		// measuredHeight = size.y;
		// } else {
		// Display d = w.getDefaultDisplay();
		// measuredWidth = d.getWidth();
		// measuredHeight = d.getHeight();
		// }

		Display display = getWindowManager().getDefaultDisplay();
		measuredWidth = display.getWidth();
		measuredHeight = display.getHeight();

	}

}
