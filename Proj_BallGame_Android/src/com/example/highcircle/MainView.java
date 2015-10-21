package com.example.highcircle;

import java.util.*;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.media.*;
import android.util.*;
import android.view.*;
import android.widget.*;

class MainView extends SurfaceView implements SurfaceHolder.Callback {

	static final int HERO_RADIUS_DIV = 65; // 화면의 너비를 이걸로 나눠서 반지름을 구할 것이다.
	int radiusOfHero;
	
	static final int OBSTACLE_RADIUS_DIV = 65;
	int radiusOfObstacle;

	ArrayList<ObsBall> obsBallArrayList = new ArrayList<ObsBall>();
	HeroBall hero;

	CreateBallThread cThread;
	final static int CREATE_DELAY = 1000;

	DrawThread dThread;
	final static int DRAW_DELAY = 1;

	MoveThread mThread;
	final static int MOVE_DELAY = 25; // 이거 바꿀 때, ObsBall들의 속도도 신경써야됨.

	SurfaceHolder sfHolder;

	long startTime, endTime;


	int width, height;
	int btnSetHeight;
	int btnHeight;
	int miniBtnLength;
	int barSize;

	int btnWidth;

	int buttonSetStartPoint_y;
	int buttonSetEndPoint_y;

	SoundPool pool;
	int nova_sound_id;
	
	MainActivity ma;

	public MainView(Context context, MainActivity ma) {
		super(context);
		
		this.ma = ma;
		
		pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0); // 사운드풀 등록
		nova_sound_id = pool.load(getContext(), R.raw.nova, 1); // 노바 효과음 로드

		width = MainActivity.measuredWidth;
		height = MainActivity.measuredHeight;

		// 버튼의 크기와, 화면의 높이 너비를 구한다.
		btnSetHeight = MainActivity.measuredHeight / 8;
		
		btnHeight = btnSetHeight / 2;
		btnWidth = width / 6;

		miniBtnLength = btnHeight / 2;


		barSize = width / 80;
		
		// 그냥 버튼의 시작 y좌표와 끝 y좌표를 구해놨다. (힘들게) 그냥 이걸 쓰면된다 밑에서
		buttonSetStartPoint_y = height - (int) ((btnSetHeight / 2) * 3.5);
		buttonSetEndPoint_y = buttonSetStartPoint_y + btnHeight * 2;

		// surfaceView를 총괄?하는 holder 생성
		sfHolder = getHolder();
		sfHolder.addCallback(this);

		// 화면의 너비를 이용해(부모Activity의 너비를 전달 받음) 장애물 공의 크기 초기화
		radiusOfObstacle = width / OBSTACLE_RADIUS_DIV;

		
		radiusOfHero = width / HERO_RADIUS_DIV;
		// hero의 컨스트럭터에는 x, y, radius를 전달한다.
		// hero는 화면 맨 위에 중앙 에서 생성된다.
		// 그래서 너비/2와 radius를 전달했다
		// -> radius 대신 0을 전달하면 원의 위쪽 절반이 잘려서 나온다(원의중심이라)
		hero = new HeroBall(width / 2, 0 + radiusOfHero, radiusOfHero, barSize);

	}
	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// do nothing
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		dThread = new DrawThread(sfHolder);
		dThread.start();
		mThread = new MoveThread();
		mThread.start();
		cThread = new CreateBallThread();
		cThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		cThread.isEnd = true; // 더 이상 생성 안되게 생성 쓰레드 end
		mThread.isEnd = true; // 더 이상 안움직이게
		dThread.isEnd = true; // 더이상 안그리게
		
		while (true) {
			try {
				dThread.join();
				mThread.join();
				cThread.join();
				break;
			} catch (Exception e) {
			}
		}
		
	}

	int x = -10, y = -10;
	
	public boolean onTouchEvent(MotionEvent event) {
		x = (int) event.getX();
		y = (int) event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			if (x > dThread.startPointOfMenu_x
					&& x < dThread.startPointOfMenu_x + dThread.itemWidthOfMenu){
				
				if (y > dThread.startPointOfMenu_y 
						&& y < dThread.startPointOfMenu_y + dThread.itemHeightOfMenu){
					// start 버튼
					dThread.currentBackgroundBitmap = dThread.start;
					
				}else if (y > dThread.startPointOfMenu_y + dThread.itemHeightOfMenu
						&& y < dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 2){
					// ranking 버튼
					dThread.currentBackgroundBitmap = dThread.ranking;
					
				} else if (y > dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 2
						&& y < dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 3){
					// help 버튼
					dThread.currentBackgroundBitmap = dThread.help;
					
				} else if (y > dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 3
						&& y < dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 4){
					// end 버튼
					dThread.currentBackgroundBitmap = dThread.exit;
				}
			}
			
			pool.play(nova_sound_id, 1, 1, 0, 0, 1);
			
			return true;

		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			
//			dThread.currentBackgroundBitmap = dThread.main;
			
			if (x > dThread.startPointOfMenu_x
					&& x < dThread.startPointOfMenu_x + dThread.itemWidthOfMenu){
				
				if (y > dThread.startPointOfMenu_y 
						&& y < dThread.startPointOfMenu_y + dThread.itemHeightOfMenu){
					// start 버튼
					cThread.isEnd = true; // 더 이상 생성 안되게 생성 쓰레드 end
					mThread.isEnd = true; // 더 이상 안움직이게
					dThread.isEnd = true; // 더이상 안그리게
					
					while (true) {
						try {
							dThread.join();
							mThread.join();
							cThread.join();
							break;
						} catch (Exception e) {
						}
					}
					
					ma.goToGameActivity();
					
				}else if (y > dThread.startPointOfMenu_y + dThread.itemHeightOfMenu
						&& y < dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 2){
					// ranking 버튼
					Toast.makeText(getContext(), "아직 구현 안함", 0).show();
					
				} else if (y > dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 2
						&& y < dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 3){
					// help 버튼
					Toast.makeText(getContext(), "아직 구현 안함", 0).show();
					
				} else if (y > dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 3
						&& y < dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 4){
					// end 버튼
					ma.exitActivity();
				}
			}
			return true;
		}
		return false;

	}

	// 장애물 생성 쓰레드
	int cnt_create = 0;
	class CreateBallThread extends Thread {
		int mWidth, mHeight;
		boolean isEnd = false;
		boolean isPaused = false;

		public CreateBallThread() {
			mWidth = width;
			mHeight = height;

		}

		public void run() {
			Random Rnd = new Random();

			while (isEnd == false) {
				if (!isPaused){
					// 장애물들이 벽에서 나오게 하는 것은 create함수에 구현 해놓음
					// 가끔 에너지 볼로 나오는 것도 create함수에 구현 해놓음
					ObsBall ob = ObsBall.create(radiusOfObstacle, mWidth,
							buttonSetStartPoint_y, barSize * 2, hero);
					obsBallArrayList.add(ob);
					
					cnt_create ++;
					if (cnt_create > 7)
						isEnd = true;
					try {
						Thread.sleep(GameView.CREATE_DELAY);
					} catch (Exception e) {
					}
				}
			}

		}
	}
	
	// 이동 쓰레드
	// 여기서 충돌 체크도 한다.
	class MoveThread extends Thread {
		int mWidth, mHeight;
		boolean isEnd = false;
		boolean isPaused = false;
		boolean isGameOver = false;
		
		public MoveThread() {
			mWidth = width;
			mHeight = buttonSetEndPoint_y;
		}

		public void run() {
			ObsBall ob1, ob2;
			double dist;

			while (!isEnd) {
				
				if (!isPaused){
					// 장애물 쭉 훑으면서 충돌 체크
					for (int idx = 0; idx < obsBallArrayList.size(); idx++) {
	
						ob1 = obsBallArrayList.get(idx);
						ob1.isBumped = false;
	
						// 장애물 이동
						ob1.move(mWidth, mHeight);
						
						// 다른 장애물들이랑 비교
						for (int i = 0; i < obsBallArrayList.size(); i++) {
							ob2 = obsBallArrayList.get(i);
	
							if (getDist(ob1.x, ob1.y, ob2.x, ob2.y) <= ob1.rad * 2
									&& idx != i/* 자기 자신 제외 */) {
	
								// 포이즌 공의 탄생 (노란공과 파란공의 충돌 - 오염됨)
								if (ob1.type == ObsBall.TYPE_ENERGY/* 노란공 기준 */
										&& ob2.type == ObsBall.TYPE_OBSTACLE/* 장애물 공 */)
									ob1.poisoned(); // 에너지공이 오염됨
	
								// 파란공 끼리의 충돌 (색 바뀌고, 가속도 더해짐)
								if (ob1.type == ObsBall.TYPE_OBSTACLE
										&& ob2.type == ObsBall.TYPE_OBSTACLE) {
	
									ob1.afterCrashed(0/* type */);
									ob1.isBumped = true;
								}
								// 그 이외의 공과의 충돌
								else {
	
									ob1.afterCrashed(1/* type */);
								}
	
							}
						}
	
						/*
						 * 원 끼리 겹쳐있다면 계속해서 충돌이 난다. 깔끔하게 원끼리 접하면 상관없는데, 겹쳐있으면
						 * Delay(5ms)마다 계속 충돌이 감지될 수도 있다. 그래서 최초로 충돌 했을 때, isBumped를
						 * true로 바꿔주고 다른 애들로 부터 완전히 벗어났을 때, false로 바꿔준다.
						 */
						if (!ob1.isBumped) {
							ob1.isStateChagedByCrash = false;
						}
	

					}
					// 장애물 이동 끝
	
	
					try {
						Thread.sleep(GameView.MOVE_DELAY);
					} catch (Exception e) {
					}
				}
			}

		}
	}

	// 거리 측정 매서드
	public static double getDist(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	// 그리기 쓰레드
	class DrawThread extends Thread {

		int mWidth, mHeight;
		boolean isEnd = false;
		boolean isPaused = false;

		Bitmap bgBitmap[] = new Bitmap[2];
		
		Bitmap main, start, help, ranking, exit;
		Bitmap currentBackgroundBitmap;
		
		Bitmap title ;
		
		Rect menuRect;
		Rect titleRect;

		int bgColorNormal, bgColorPoison;
		
		int unit;
		int itemHeightOfMenu;
		int itemWidthOfMenu;
		int startPointOfMenu_x;
		int startPointOfMenu_y;

		Paint barPnt = new Paint();

		Paint tracePnt = new Paint();

		Rect bottomBarRect_down;
		Rect topBarRect;
		Rect leftBarRect;
		Rect rightBarRect;


		SurfaceHolder mHolder;
		
		DrawThread(SurfaceHolder holder) {
			mWidth = width;
			mHeight = height;

			mHolder = holder;

			// 바탕
			bgColorNormal = Color.WHITE; // rgb(189, 174, 158)
			bgColorPoison = 0xBBAB1297;

			barPnt.setColor(Color.rgb(85, 35, 41));

			// bar 사각형
			bottomBarRect_down = new Rect(0, buttonSetEndPoint_y, mWidth,
					buttonSetEndPoint_y + barSize);
			leftBarRect = new Rect(barSize * 2, 0, barSize * 2 + barSize,
					mHeight);
			rightBarRect = new Rect(mWidth - barSize * 2, 0, mWidth - barSize
					* 2 - barSize, mHeight);
			topBarRect = new Rect(0, mWidth / 40, mWidth, mWidth / 40 + barSize);

			// 비트맵으로 만들어 놓기
			bgBitmap[0] = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(bgBitmap[0]);
			// 배경
			c.drawColor(bgColorNormal); // rgb(189, 174, 158)
			// 화면 상단 bar
			c.drawRect(topBarRect, barPnt);
			// 컨트롤버튼 밑에 경계 bottom bar
			c.drawRect(bottomBarRect_down, barPnt);
			// 왼쪽 bar
			c.drawRect(leftBarRect, barPnt);
			// 오른쪽 bar
			c.drawRect(rightBarRect, barPnt);
			// 1번 배경 (독을 먹지 않았을 때) 끝

			// main, start, help, ranking, exit;
			Resources res = getResources();
			main = BitmapFactory.decodeResource(res, R.drawable.main);
			start = BitmapFactory.decodeResource(res, R.drawable.start);
			ranking = BitmapFactory.decodeResource(res, R.drawable.ranking);
			help = BitmapFactory.decodeResource(res, R.drawable.help);
			exit = BitmapFactory.decodeResource(res, R.drawable.exit);
			
			currentBackgroundBitmap = main;

			unit = mWidth/8;
			startPointOfMenu_y = mHeight/2 - unit;
			startPointOfMenu_x = unit + unit/10;
			itemWidthOfMenu = (unit * 6);
			itemHeightOfMenu = (unit * 6) / 4;
			menuRect = new Rect(startPointOfMenu_x, startPointOfMenu_y
					, startPointOfMenu_x + itemWidthOfMenu
					, startPointOfMenu_y + itemHeightOfMenu * 4);
			
			
			unit = mWidth / 10;
			title = BitmapFactory.decodeResource(res, R.drawable.title2);
			titleRect = new Rect(unit , unit * 2, mWidth - unit , unit * 5);


		}

		public void run() {
			Canvas canvas;

			while (isEnd == false) {

				synchronized (mHolder) {

					// ※※※Drawing start : 락을 걸고 그린다. (화면이 아닌 메모리에)
					canvas = mHolder.lockCanvas();
					if (canvas == null)
						break;

					// 기본 배경 그리기 - 컨스트럭터에서 비트맵에 담아놨음
					// 우연한 효과 ㅡ,ㅡ
					canvas.drawBitmap(bgBitmap[0], 0, 0, tracePnt);

					// 장애물( Obstacle ball ) 그리기
					for (int idx = 0; idx < obsBallArrayList.size(); idx++)
						obsBallArrayList.get(idx).draw(canvas);
					
					canvas.drawBitmap(currentBackgroundBitmap, null, menuRect, null);
					
					canvas.drawBitmap(title, null, titleRect, null);

					mHolder.unlockCanvasAndPost(canvas);
					// ※※※Drawing end : 락을 해제하면 그려진다.

				}
				try {

					Thread.sleep(GameView.DRAW_DELAY);
				} catch (Exception e) {
				}

			}
			// 무한 루프 종료

		}

	}
	
}
