package com.example.highcircle;

import java.lang.Character.*;
import java.util.*;

import android.graphics.*;
import android.nfc.cardemulation.*;

/*
 * 부딪힐 때마다 색이 바뀌게? 5번 부딪히면 사라지게..
 * 점점 연해지게?
 * 부딪힐때마다 rgb 각각을 - 20씩해줘도..혹은 alpha값을..
 * 
 * 주의할 것
 * 크기 결정은 화면 크기에 비례하게..
 * 
 * 점프 메소드 = jumpCnt 둬서! 
 * 
 * 속도 마저도 화면크기로부터 정보를 얻어야함.
 * */
public class ObsBall {

	public static final int TYPE_ENERGY = 1;
	public static final int TYPE_OBSTACLE = 2;
	public static final int TYPE_POISON = 3;
	public static final int ICE_OBSTACLE = 4;
	
	int idx_speed = 0;
	int speed[] = new int[3];	//slow(0) , midium(1), fast (2)
	
	int red[] = new int[3];
	int green[] = new int[3];
	int blue[] = new int[3];
	int idx_color = 0;
	
	long infectionStartTime;

	int type;
	int x, y; // 좌표

	int rad; // 반지름
	int vx, vy; // 증가량

	boolean isBumped = false;
	boolean isStateChagedByCrash = false;
	
	Paint inCirclePnt;
	Paint outCirclePnt;

	int sideDiff;

	static Random Rnd = new Random();
	static int obsCnt = 0;

	static int offsetFromHero; // hero가 있는데서 장애물이 생성되지 않게 할 때 이용.
	
	public ObsBall(int x, int y, int Rad, int sideDiff, int type) {
		this.x = x;
		this.y = y;
		this.rad = Rad;
		this.sideDiff = sideDiff;
		this.type = type;

		// hero 위치에 이만큼을 더해서, 그 범위 내에서는 장애물 생성 안되게 막으려고
		offsetFromHero = Rad * 25;	
		
		// 속도 조절
		speed[0] = MainActivity.unitSpeed * 4;
		speed[1] = (int)(MainActivity.unitSpeed * 8);
		speed[2] = (int)(MainActivity.unitSpeed *  12);
		
		idx_speed = 0;
		
		
		// vx 정하기. 
		vx = speed[idx_speed];
		vy = speed[idx_speed];
		
		if (Rnd.nextInt(1) == 0)
			vx *= -1;
		if (Rnd.nextInt(1) == 0)
			vy *= -1;
		//
		
		
		inCirclePnt = new Paint();
		inCirclePnt.setAntiAlias(true);
		if (type == TYPE_OBSTACLE){ // 장애물 볼
			red[0] = 135;green[0] = 206;blue[0] = 235;
			red[1] = 28;green[1] = 160;	blue[1] = 217;
			red[2] = 16;	green[2] = 94;	blue[2] = 126;
			inCirclePnt.setColor(Color.rgb(red[idx_color], green[idx_color], blue[idx_color]));
			
		} else if (type == TYPE_ENERGY){ // 노란색 볼
			inCirclePnt.setColor(Color.rgb(243, 218, 150));
			
		} else if (type == ICE_OBSTACLE){	// 얼음 볼
			inCirclePnt.setColor(Color.WHITE);
			
		}
		
		outCirclePnt = new Paint();
		outCirclePnt.setAntiAlias(true);
		outCirclePnt.setColor(Color.BLACK);
		outCirclePnt.setStyle(Paint.Style.STROKE);
		outCirclePnt.setStrokeWidth(rad/5);

	}
	
	static ObsBall create(int Rad, int parentWidth, int buttonStartPoint_y,
			int barSize, HeroBall hero) {
		int x, y;

		do {
		if (Rnd.nextInt(10) % 2 == 0) {
			// 절반 확률로 왼쪽에서 생성
			x = 0 + barSize + Rad;

		} else {
			// 절반 확률로 오른쪽에서 생성
			x = parentWidth - barSize - Rad;
		}
		y = Rad + Rnd.nextInt(buttonStartPoint_y - barSize - Rad - Rad);

		// 만약 x, y가 hero 주변이라면 다시생성
		} while( (hero.x - offsetFromHero) < x 
				&& x < (hero.x + offsetFromHero) 
				&& (hero.y - offsetFromHero) < y
				&& y < (hero.y + offsetFromHero) );

		ObsBall ob;

		// 에너지공과 장애물이 1:1비율로 나온다
		// 그런데 에너지 공과 장애물이 만나면 독으로 변한다.
		if (obsCnt % 2 == 0) {
			ob = new ObsBall(x, y, Rad, barSize, TYPE_ENERGY);

		} else {
			ob = new ObsBall(x, y, Rad, barSize, TYPE_OBSTACLE);
			
		}

		// obsball이 생성될 때마다 하나씩 증가 시킨다.
		obsCnt++;

		return ob;
	}


	// 충돌(장애물끼리 혹은 장애물 + 에너지볼)시 불러서 방향을 바꿔준다. 가속도도 약간 더해준다.
	// TYPE_OBSTACLE 일 때만 불려야 된다.
	public void afterCrashed(int type){
		
		// 부딪혔을 때 중복으로 체크되서(불린 변수를 뒀음 -> 저에게 물어봐주세요)
		if (!isStateChagedByCrash ){
			//방향 변경
			vx *= -1;
			vy *= -1;
			
			if (type == 0/*장애물 끼리 충돌이라면*/ && idx_color < 2){
				// 색 변경
				idx_color ++;
				inCirclePnt.setColor(Color.rgb(red[idx_color], green[idx_color], blue[idx_color]));
	
				// 속도 상승
				idx_speed++;
				vx = speed[idx_speed];
				vx = speed[idx_speed];
			}
			
			// 모든 상태 변화에 대해서 . 최초 충돌 시에만 적용 되기 위해서.
			isStateChagedByCrash = true;
		}
	}
	
	// 감염 됨
	public void poisoned(){
		inCirclePnt.setColor(0xBBAB1297);
		type = TYPE_POISON;
	}
	
	// 회복 됨
	public void dePoisoned() {
		inCirclePnt.setColor(Color.rgb(243, 218, 150));
		type = TYPE_ENERGY;
	}

	void move(int width, int height) {

		x += vx;
		y += vy;

		if (x < rad + sideDiff + sideDiff/2) {
			// 오른쪽 충돌
			if (vx < 0)
				vx *= -1;
			
		} else if (x > width - (rad + sideDiff ) - sideDiff/2){
			// 왼쪽 충돌
			if (vx > 0)
				vx *= -1;
		
		}
		
		// 맨 위에는 bar가 없으니까 barSize를 더하지 않는다.
		if (y < rad + sideDiff + sideDiff/2 ) {
			if (vy < 0)
				vy *= -1;
		} else if (y > height - rad - sideDiff/2){
			if (vy > 0)
				vy *= -1;
		}
	}

	void draw(Canvas canvas) {
		canvas.drawCircle(x, y, rad, inCirclePnt);
		canvas.drawCircle(x, y, rad, outCirclePnt);
	}
}