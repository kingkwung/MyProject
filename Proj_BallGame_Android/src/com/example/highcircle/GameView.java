package com.example.highcircle;

import java.util.*;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

/*
 * GameView 클래스는 SurfaceView를 상속 받으며
 * 
 * 세 개의 쓰레드를 가지고 있다.
 * - 장애물을 생성하는 쓰레드
 * - 모든 객체들을 이동 시켜주는 쓰레드
 * - 그려주는 쓰레드
 * 
 * 세 개로 분리한 이유는, 한 개로 합쳤을 경우
 * 반응성이 떨어 질 수 있기 때문이다.
 * 
 * 컨스트럭터에는 부모 activity의 너비와, hero를 전달 받는다.
 * hero를 바깥에서 생성한 이유는, hero는 센서에 의해 움직이는데,
 * 센서를 바깥(activity)에서 등록할 수 밖에 없기 때문이다.
 * 
 * 너비는 왜 전달 받았냐면,
 * 폰마다 해상도가 다르기 때문에
 * 절대 크기를 이용해서 공을 그릴 수 없어서
 * (Gpro2에서 손톱만한게, 갤럭시2에서는 화면크기의 반을 차지함)
 * 
 * 그래서 상대적으로 
 * (폰의 너비 / N) 을 공의 반지름으로 삼기 위해서이다.
 * 
 * 살기 위해서 애들을 죽여야하는데
 * 에너지볼이나 포이즌볼(오염된 에너지볼)을 먹으면 공격 가능
 * */
class GameView extends SurfaceView implements SurfaceHolder.Callback {

	static final int HERO_RADIUS_DIV = 65; // 화면의 너비를 이걸로 나눠서 반지름을 구할 것이다.
	int radiusOfHero;

	static final int OBSTACLE_RADIUS_DIV = 65;
	int radiusOfObstacle;

	ArrayList<ObsBall> obsBallArrayList = new ArrayList<ObsBall>();
	HeroBall hero;

	CreateBallThread cThread;
	final static int CREATE_DELAY = 777;

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
	int ending_sound_id;
	int nyam_sound_id;
	int die_sound_id;

	MediaPlayer player;
	
	GameActivity ga;

	public GameView(Context context, GameActivity ga) {
		super(context);
		
		this.ga = ga;

		player = MediaPlayer.create(getContext(), R.raw.game);
		// .start(), .stop(), .pause(), setLooping(true)
		player.setLooping(true);

		player.start();

		pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0); // 사운드풀 등록
		nova_sound_id = pool.load(getContext(), R.raw.nova, 1); // 노바 효과음 로드
		ending_sound_id = pool.load(getContext(), R.raw.zzz, 1); // 기분나쁜 웃음소리
		nyam_sound_id = pool.load(getContext(), R.raw.nyam, 1); //냠냠냠
		die_sound_id = pool.load(getContext(), R.raw.goodbye, 1); //푸슉

		width = GameActivity.measuredWidth;
		height = GameActivity.measuredHeight;
		
		// 버튼의 크기와, 화면의 높이 너비를 구한다.
		btnSetHeight = GameActivity.measuredHeight / 8;
		
		btnHeight = btnSetHeight / 2;
		btnWidth = width / 6;

		miniBtnLength = btnHeight / 2;


		barSize = width / 80;

		// 그냥 버튼의 시작 y좌표와 끝 y좌표를 구해놨다. (힘들게) 그냥 이걸 쓰면된다 밑에서
		buttonSetStartPoint_y = height - (int)((btnSetHeight / 2) * 3.5);
		buttonSetEndPoint_y = buttonSetStartPoint_y + btnHeight * 2;


		// surfaceView를 총괄?하는 holder 생성
		sfHolder = getHolder();
		sfHolder.addCallback(this);

		// 화면의 너비를 이용해(부모Activity의 너비를 전달 받음) 장애물 공의 크기 초기화
		radiusOfObstacle = width / OBSTACLE_RADIUS_DIV;

		// 참고로 나머지 obstacle ball들은 GameView(GameActivity를 채우는 view) 안에서 생성한다.
		radiusOfHero = width / HERO_RADIUS_DIV;
		// hero의 컨스트럭터에는 x, y, radius를 전달한다.
		// hero는 화면 맨 위에 중앙 에서 생성된다.
		// 그래서 너비/2와 radius를 전달했다
		// -> radius 대신 0을 전달하면 원의 위쪽 절반이 잘려서 나온다(원의중심이라)
		hero = new HeroBall(width / 2, 0 + radiusOfHero * 4, radiusOfHero, barSize * 2);

		// 게임 시작 시간
		startTime = System.currentTimeMillis();
	}
	
	public void reGame(){
		
		startTime = System.currentTimeMillis();

		obsBallArrayList.clear();

		hero.reset();
		
		x = -10;
		y = -10;
		
		ObsBall.obsCnt = 0;
		
		mThread.isPaused = false;
		mThread.isGameOver = false;
		dThread.isPaused = false;
		cThread.isPaused = false;
		
		
		player.release();
		player = MediaPlayer.create(getContext(), R.raw.game);
		// .start(), .stop(), .pause(), setLooping(true)
		player.setLooping(true);
		player.start();
		
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
		
		player.release(); // 객체 파괴
		pool.release();
	}

	// 화면 터치 이벤트
	int x = -10, y = -10;

	
	public boolean onTouchEvent(MotionEvent event) {
		x = (int) event.getX();
		y = (int) event.getY();


		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {

			
			if (mThread.isGameOver/*주인공이 죽으면 true가 됩니다.*/){
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					pool.play(nova_sound_id, 1, 1, 0, 0, 1);
				
				if (x > dThread.startPointOfMenu_x && x < dThread.startPointOfMenu_x + dThread.itemWidthOfMenu){
					if (y > dThread.startPointOfMenu_y
							&& y < dThread.startPointOfMenu_y + dThread.itemHeightOfMenu) {
						// 리스타트 버튼
						dThread.currentMenuBitmap = dThread.endRestartBitmap;
						
					} else 	if (y > dThread.startPointOfMenu_y + dThread.itemHeightOfMenu
							&& y < dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 2) {
						// 레지스터 버튼
						dThread.currentMenuBitmap = dThread.endRegisterBitmap;
						
					} else if (y > dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 2
							&& y < dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 3) {
						// 메뉴 버튼
						dThread.currentMenuBitmap = dThread.endMenuMenuBitmap;
						
					}
				}
			} else {
				
				// 먼저 수직 수평 방향들
				if (y < buttonSetStartPoint_y  + (barSize * 3) + miniBtnLength
						&& x > btnWidth * 3 - miniBtnLength  
						&& x < btnWidth * 3 + miniBtnLength ){
					// 북쪽
					dThread.curruntControlBitmap = dThread.n;
					
					hero.currentDirction_rl = HeroBall.NONE;
					hero.currentDirction_tb = HeroBall.TOP;
					if (hero.isPoisoned){
						hero.currentDirction_rl = HeroBall.NONE;
						hero.currentDirction_tb = HeroBall.BOTTOM;
					}
					
				} else if (y > buttonSetStartPoint_y + btnHeight + miniBtnLength  
						&& x > btnWidth * 3 - miniBtnLength  
						&& x < btnWidth * 3 + miniBtnLength){
					// 남쪽
					dThread.curruntControlBitmap = dThread.s;
					
					hero.currentDirction_rl = HeroBall.NONE;
					hero.currentDirction_tb = HeroBall.BOTTOM;
					if (hero.isPoisoned){
						hero.currentDirction_rl = HeroBall.NONE;
						hero.currentDirction_tb = HeroBall.TOP;
					}
					
				} else if (x < btnWidth * 2 + (miniBtnLength * 2)  /*가로는 좀 기니까*/ 
						&& y > buttonSetStartPoint_y + (barSize * 3) + btnHeight - miniBtnLength/2 
						&& y < buttonSetStartPoint_y + (barSize * 3) + btnHeight + miniBtnLength/2){
					// 서쪽
					dThread.curruntControlBitmap = dThread.w;
					
					hero.currentDirction_rl = HeroBall.LEFT;
					hero.currentDirction_tb = HeroBall.NONE;
					if (hero.isPoisoned){
						hero.currentDirction_rl = HeroBall.RIGHT;
						hero.currentDirction_tb = HeroBall.NONE;
					}
					
				} else if(x > btnWidth * 4 - (miniBtnLength * 2)  /*가로는 좀 기니까*/ 
						&& y > buttonSetStartPoint_y + (barSize * 3) + btnHeight - miniBtnLength/2 
						&& y < buttonSetStartPoint_y + (barSize * 3) + btnHeight + miniBtnLength/2){
					// 동쪽
					dThread.curruntControlBitmap = dThread.e;
					
					hero.currentDirction_rl = HeroBall.RIGHT;
					hero.currentDirction_tb = HeroBall.NONE;
					if (hero.isPoisoned){
						hero.currentDirction_rl = HeroBall.LEFT;
						hero.currentDirction_tb = HeroBall.NONE;
					}
					
				} else {
					// 대각선 방향들
					
					if (y < buttonSetStartPoint_y + (barSize * 3) + btnHeight) {
						// 위의 버튼
						if (x > btnWidth * 3) {
							// 북동쪽
							dThread.curruntControlBitmap = dThread.ne;
							
							hero.currentDirction_rl = HeroBall.RIGHT;
							hero.currentDirction_tb = HeroBall.TOP;
							if (hero.isPoisoned){
								hero.currentDirction_rl = HeroBall.LEFT;
								hero.currentDirction_tb = HeroBall.BOTTOM;
							}
							
						} else {
							// 북서쪽
							dThread.curruntControlBitmap = dThread.nw;
							
							hero.currentDirction_rl = HeroBall.LEFT;
							hero.currentDirction_tb = HeroBall.TOP;
							if (hero.isPoisoned){
								hero.currentDirction_rl = HeroBall.RIGHT;
								hero.currentDirction_tb = HeroBall.BOTTOM;
							}
							
						}
					} else {
						// 아래 버튼
						if (x > btnWidth * 3) {
							// 남동쪽
							dThread.curruntControlBitmap = dThread.se;
							
							hero.currentDirction_rl = HeroBall.RIGHT;
							hero.currentDirction_tb = HeroBall.BOTTOM;
							if (hero.isPoisoned){
								hero.currentDirction_rl = HeroBall.LEFT;
								hero.currentDirction_tb = HeroBall.TOP;
							}
							
						} else {
							// 남서쪽
							dThread.curruntControlBitmap = dThread.sw;
							
							hero.currentDirction_rl = HeroBall.LEFT;
							hero.currentDirction_tb = HeroBall.BOTTOM;
							if (hero.isPoisoned){
								hero.currentDirction_rl = HeroBall.RIGHT;
								hero.currentDirction_tb = HeroBall.TOP;
							}
						}
					}
					
				}
			}
			
			return true;

		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			// 뗄 때는 원상태로 복구 (눌렀을 때 바꿨던 색)
			dThread.curruntControlBitmap = dThread.none;
			
			if (!mThread.isGameOver && hero.energy > 0){
				 hero.isAttack = true;
				 hero.doAttack(dThread);	// 왜 draw thread를 전달해 주냐면. 대단한건아니고 공격시 버튼부분의 이미지를 바꿔주려고.
				 pool.play(nova_sound_id, 1, 1, 0, 0, 1);
				 
			} else if (mThread.isGameOver/*hero가 죽으면 gameover*/){
				
				if (x > dThread.startPointOfMenu_x && x < dThread.startPointOfMenu_x + dThread.itemWidthOfMenu){
					if (y > dThread.startPointOfMenu_y
							&& y < dThread.startPointOfMenu_y + dThread.itemHeightOfMenu) {
						// restart 버튼
						reGame();
						dThread.currentMenuBitmap = dThread.menuBitmap;
						
					} else 	if (y > dThread.startPointOfMenu_y + dThread.itemHeightOfMenu
							&& y < dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 2) {
						//랭킹버튼
						Toast.makeText(getContext(), "아직 구현 안함", 0).show();
						
					} else if (y > dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 2
							&& y < dThread.startPointOfMenu_y + dThread.itemHeightOfMenu * 3) {
						// 메뉴 버튼
						try{
							Thread.sleep(400);
						} catch(Exception e){
						}
						 android.os.Process.killProcess(android.os.Process.myPid());

					}
					
				}
				
			}

			return true;
		}
		return false;

	}

	// 장애물 생성 쓰레드
	class CreateBallThread extends Thread {
		int mWidth, mHeight;
		boolean isEnd = false;
		boolean isPaused = false;

		public CreateBallThread() {
			mWidth = width;
			mHeight = buttonSetStartPoint_y;

		}

		public void run() {

			while (!isEnd ) {
				if (!isPaused){
					// 장애물들이 벽에서 나오게 하는 것은 create함수에 구현 해놓음
					// 가끔 에너지 볼로 나오는 것도 create함수에 구현 해놓음
					ObsBall ob = ObsBall.create(radiusOfObstacle, mWidth,
							buttonSetStartPoint_y, barSize * 2, hero);
					obsBallArrayList.add(ob);
	
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
	int idx_one, idx_another;
	class MoveThread extends Thread {
		int mWidth, mHeight;
		boolean isEnd = false;
		boolean isPaused = false;
		boolean isGameOver = false;
		
		public MoveThread() {
			mWidth = width;
			mHeight = buttonSetStartPoint_y;
		}

		public void run() {
			ObsBall one, another;
			double dist;

			while (!isEnd) {
				
				if (!isPaused){
					// 장애물 쭉 훑으면서 충돌 체크
					for (idx_one = 0; idx_one < obsBallArrayList.size(); idx_one++) {
	
						one = obsBallArrayList.get(idx_one);
						one.isBumped = false;
	
						// 장애물 이동
						one.move(mWidth, mHeight);
						
						// 독 풀어주기 3초 뒤에
						if (one.type == ObsBall.TYPE_POISON && one.infectionStartTime + 3000 < System.currentTimeMillis()){
							one.dePoisoned();
						}
	
						// 다른 장애물들이랑 비교
						for (idx_another = 0; idx_another < obsBallArrayList.size(); idx_another++) {
							another = obsBallArrayList.get(idx_another);
	
							if (getDist(one.x, one.y, another.x, another.y) <= one.rad * 2
									&& idx_one != idx_another/* 자기 자신 제외 */) {
	
								// 포이즌 공의 탄생 (노란공과 파란공의 충돌 - 오염됨)
								if (one.type == ObsBall.TYPE_ENERGY/* 노란공 기준 */
										&& another.type == ObsBall.TYPE_OBSTACLE/* 장애물 공 */){
									one.poisoned(); // 에너지공이 오염됨
									one.infectionStartTime = System.currentTimeMillis();
								}
	
								// 파란공 끼리의 충돌 (색 바뀌고, 가속도 더해짐)
								if (one.type == ObsBall.TYPE_OBSTACLE
										&& another.type == ObsBall.TYPE_OBSTACLE) {
	
									one.afterCrashed(0/* type */);
									one.isBumped = true;
								}
								// 그 이외의 공과의 충돌
								else {
									one.afterCrashed(1/* type */);
									one.isBumped = true;
								}
	
							}
						}
	
						/*
						 * 원 끼리 겹쳐있다면 계속해서 충돌이 난다. 깔끔하게 원끼리 접하면 상관없는데, 겹쳐있으면
						 * Delay(5ms)마다 계속 충돌이 감지될 수도 있다. 그래서 최초로 충돌 했을 때, isBumped를
						 * true로 바꿔주고 다른 애들로 부터 완전히 벗어났을 때, false로 바꿔준다.
						 */
						if (!one.isBumped) {
							one.isStateChagedByCrash = false;
						}
	
						dist = getDist(hero.x, hero.y, one.x, one.y);
	
						// 주인공이 노바 쓸때 isAttack = true일 때
						if (hero.isAttack) {
							if ((one.type == ObsBall.TYPE_OBSTACLE)
									&& hero.attackRad + one.rad >= dist) {
								obsBallArrayList.remove(idx_one);
								idx_one--;
	
								// vb.vibrate(50); // 100ms간 진동
							}
						}
	
						// 장애물과 주인공의 충돌 (게임 종료)
						if ((one.type == ObsBall.TYPE_OBSTACLE)
								&& (hero.rad + one.rad) >= dist + (hero.rad / 3)) {
							
							gameOver();
							
//							pool.play(ending_sound_id, 1, 1, 0, 0, 1);
							break;
						}
	
						// 에너지 볼 또는 포이즌 볼과 주인공의 충돌
						if ((hero.rad + one.rad + (hero.rad/2)/*넉넉하게*/) >= dist
								&& (one.type == ObsBall.TYPE_ENERGY || one.type == ObsBall.TYPE_POISON)) {
							// gageBar은 이것을 참조해서 그린다.

							// 냠냠냠냠냠냠냠
							pool.play(nyam_sound_id, 1, 1, 0, 0, 1);
							
							hero.energy++;
							// max 개 이상 먹어도 max개 유지 4개 먹어도 3개로 되게끔
							if (hero.energy > HeroBall.MAX_ENERGY)
								hero.energy = HeroBall.MAX_ENERGY;
							
							// 주인공과 충돌한 공 제거
							obsBallArrayList.remove(idx_one);
							idx_one--;
	
							// 이때 오염된 포이즌 공일 경우엔 주인공을 감염시킨다.
							if (one.type == ObsBall.TYPE_POISON) {
								hero.isPoisoned = true;
								// 이게 true면 onTouchEvent 리스너에서 방향을 반대로 적용..
								hero.startTime_poisoned = System
										.currentTimeMillis();
								// 독 먹은 시간 체크 (이 루프를 빠져나가고 바로 밑에서 독 해제 체크에 이용)
							}
						}
	
					}
					// 장애물 이동 끝
					
					if (hero.isCrashedWithTB(mWidth, mHeight)){
						gameOver();
					}
	
					// 독 먹은지 2초 지나면 독 해제!
					if (hero.startTime_poisoned + HeroBall.maxPoisonedTime < System
							.currentTimeMillis()) {
						hero.isPoisoned = false;
					}
	
					// 독먹었을 때 깜빡거리게
					if (hero.isPoisoned && System.currentTimeMillis() % 11 < 5)
						dThread.bg_idx = 1;
					else
						dThread.bg_idx = 0;
	
					// 주인공 이동
					hero.move(mWidth, mHeight);
	
					// 현재 시간 체크
					// (endTime - starTime)하면 플레이 시간이 나온다.
					endTime = System.currentTimeMillis();
	
					try {
						Thread.sleep(GameView.MOVE_DELAY);
					} catch (Exception e) {
					}
				}
			}

		}
		
		public void gameOver(){
			isPaused = true;
			dThread.isPaused = true;
			cThread.isPaused = true;
			
			isGameOver = true;
			
			player.stop();
			pool.stop(nova_sound_id);
			pool.play(die_sound_id, 1, 1, 0, 0, 1);
			
			hero.energy = 3;	// 에너지는 2개까지밖에 못먹고, 3을 전달하면 죽음을 표현할 수있는 효
			hero.doAttack(dThread);
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

		int bg_idx = 0;
		int bgColorNormal, bgColorPoison;

		Paint tbBarPnt = new Paint();
		Paint barPnt = new Paint();
		Paint novaBtnPnt = new Paint(); // 노바 버튼 페인트
		Paint leftBtnPnt = new Paint(); // 방향 버튼 페인트
		Paint rightBtnPnt = new Paint();
		Paint energyPnt = new Paint();

		Paint tracePnt = new Paint();

		Paint timeTxtPnt = new Paint();
		Paint dieTxtPnt = new Paint();

		Paint normalBtnPaint = new Paint();
		Paint downBtnPaint = new Paint();
		
		Paint controlCirclePnt = new Paint();

		Paint touch_point_paint = new Paint();

		// 일단 re버튼만 구현
		Paint reBtnPaint = new Paint();
		Rect reBtnRect;
		

		Rect bottomBarRect_up;
		Rect bottomBarRect_down;
		Rect topBarRect;
		Rect leftBarRect;
		Rect rightBarRect;
		
		// 컨트롤 버튼 비트맵
		Bitmap w, s, e, n , sw, se, ne, nw, attack, none;
		Bitmap curruntControlBitmap;
		Rect controlRect;
		
		SurfaceHolder mHolder;
		
		int unit;
		int itemHeightOfMenu;
		int itemWidthOfMenu;
		int startPointOfMenu_x;
		int startPointOfMenu_y;
		Rect dieRect, menuRect;
		Bitmap dieBitmap, menuBitmap, endRestartBitmap, endRegisterBitmap, endMenuMenuBitmap;
		Bitmap currentMenuBitmap;

		DrawThread(SurfaceHolder holder) {
			mWidth = width;
			mHeight = height;
			
			mHolder = holder;
			
			touch_point_paint.setColor(Color.WHITE);
			touch_point_paint.setAntiAlias(true);
			
			controlCirclePnt.setStyle(Paint.Style.STROKE);
			
			// 방향 버튼			
			e = BitmapFactory.decodeResource(getResources(), R.drawable.e);
			w = BitmapFactory.decodeResource(getResources(), R.drawable.w);
			n = BitmapFactory.decodeResource(getResources(), R.drawable.n);
			s = BitmapFactory.decodeResource(getResources(), R.drawable.s);
			sw = BitmapFactory.decodeResource(getResources(), R.drawable.sw);
			se = BitmapFactory.decodeResource(getResources(), R.drawable.se);
			nw = BitmapFactory.decodeResource(getResources(), R.drawable.nw);
			ne = BitmapFactory.decodeResource(getResources(), R.drawable.ne);
			none = BitmapFactory.decodeResource(getResources(), R.drawable.none);
			attack = BitmapFactory.decodeResource(getResources(), R.drawable.attack);
			
			curruntControlBitmap = none;
			
			// 
			controlRect = new Rect(btnWidth * 2, buttonSetStartPoint_y + ( barSize * 3)/*위에 버튼 터치 부분에 이걸 다더해줬음*/
					, btnWidth * 4, buttonSetEndPoint_y);
			
			// 바탕
			bgColorNormal = Color.WHITE; // rgb(189, 174, 158)
			bgColorPoison = 0xBBAB1297;

			
			tbBarPnt.setColor(Color.rgb(183, 0, 0));
			tbBarPnt.setStyle(Paint.Style.STROKE);
			tbBarPnt.setStrokeWidth(barSize/3);
			tbBarPnt.setPathEffect(new DashPathEffect(new float[]{barSize, barSize}, 0));
			barPnt.setColor(Color.rgb(85, 35, 41));

			// bar 사각형
			bottomBarRect_up = new Rect(0, buttonSetStartPoint_y - barSize, mWidth,
					buttonSetStartPoint_y);
//			bottomBarRect_down = new Rect(0, buttonSetEndPoint_y, mWidth,
//					buttonSetEndPoint_y + barSize);
			leftBarRect = new Rect(barSize * 2, 0, barSize * 2 + barSize, mHeight);
			rightBarRect = new Rect(mWidth - barSize * 2, 0, mWidth - barSize
					* 2 - barSize, mHeight);
			topBarRect = new Rect(0, mWidth / 40, mWidth, mWidth / 40 + barSize);						
						
			
			// 1번 배경 (독을 먹지 않았을 때)
			// 비트맵으로 만들어 놓기
			bgBitmap[0] = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(bgBitmap[0]);
			// 배경 
			c.drawColor(bgColorNormal); // rgb(189, 174, 158)
			// 화면 상단 bar 
			c.drawRect(topBarRect, tbBarPnt);
			// 컨트롤버튼 위에 경계 bottom bar
			c.drawRect(bottomBarRect_up, tbBarPnt);
			// 컨트롤버튼 밑에 경계 bottom bar
//			c.drawRect(bottomBarRect_down, barPnt);
			// 왼쪽 bar
			c.drawRect(leftBarRect, barPnt);
			// 오른쪽 bar
			c.drawRect(rightBarRect, barPnt);
			// 1번 배경 (독을 먹지 않았을 때) 끝

			
			
			// 2번 배경 (독먹었을 때)
			// 비트맵으로 만들어 놓기
			bgBitmap[1] = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			c = new Canvas(bgBitmap[1]);
			// 배경 
			c.drawColor(bgColorPoison); // rgb(189, 174, 158)
			// 화면 상단 bar 
			c.drawRect(topBarRect, tbBarPnt);
			// 컨트롤버튼 위에 경계 bottom bar
			c.drawRect(bottomBarRect_up, tbBarPnt);
			// 컨트롤버튼 밑에 경계 bottom bar
//			c.drawRect(bottomBarRect_down, barPnt);
			// 왼쪽 bar
			c.drawRect(leftBarRect, barPnt);
			// 오른쪽 bar
			c.drawRect(rightBarRect, barPnt);
			// 2번 배경 (독먹었을 때) 끝

			// 버틴 초 표시
			timeTxtPnt.setTypeface(Typeface
					.create((String) null, Typeface.BOLD));
			timeTxtPnt
					.setTextSize(30 * getResources().getDisplayMetrics().density);
			timeTxtPnt.setColor(0xCC002266);

			// 사망했을 때 나오는 글자
			dieTxtPnt
					.setTypeface(Typeface.create((String) null, Typeface.BOLD));
			dieTxtPnt
					.setTextSize(70 * getResources().getDisplayMetrics().density);
			dieTxtPnt.setColor(Color.RED);
			dieTxtPnt.setUnderlineText(true);

			tracePnt.setColor(0xBB000000);
			
			
			unit = mWidth/8;
			startPointOfMenu_y = mHeight/2 - unit - unit/2 ;
			startPointOfMenu_x = unit + unit/10;
			
			itemWidthOfMenu = (unit * 6);
			itemHeightOfMenu = (unit * 6) / 4;
			menuBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.endmenu);
			endRestartBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.endrestart);
			endRegisterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.endregister);
			endMenuMenuBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.endmenumenu);
			menuRect = new Rect(startPointOfMenu_x, startPointOfMenu_y 
					, startPointOfMenu_x + itemWidthOfMenu
					, startPointOfMenu_y + itemHeightOfMenu * 3);
			
			currentMenuBitmap = menuBitmap;
			
			unit = mWidth / 10;
			dieBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.died);
			dieRect = new Rect((int)(unit * 1.5) , (int)(unit * 1.5), mWidth - unit , unit * 6);
			
			
		}

		int idx;
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
					canvas.drawBitmap(bgBitmap[bg_idx], 0, 0, tracePnt);

					// 장애물( Obstacle ball ) 그리기
					for (idx = 0; idx < obsBallArrayList.size(); idx++)
						obsBallArrayList.get(idx).draw(canvas);

					// 주인공( Hero ) 그리기
					hero.draw(canvas);

					// 컨트롤 버튼 표시
					canvas.drawBitmap(curruntControlBitmap, null, controlRect, null);
					
					// 초 표현 : 화면에 현재 정보 표시
					canvas.drawText(" Time : "
							+ (endTime - startTime) / 1000,
							mWidth / 2 - 65 * GameActivity.density, 50 * GameActivity.density , timeTxtPnt);

					if (mThread.isGameOver){
						// 사망 텍스트 표현
						canvas.drawBitmap(dieBitmap, null,  dieRect, null);
						
						// 메뉴
						canvas.drawBitmap(currentMenuBitmap, null, menuRect, null);
					}
					
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
