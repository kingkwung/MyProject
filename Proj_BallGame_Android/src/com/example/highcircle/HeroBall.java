package com.example.highcircle;

import com.example.highcircle.GameView.*;

import android.graphics.*;
import android.graphics.Shader.*;
import android.hardware.*;
import android.widget.*;

public class HeroBall extends ObsBall {

	public static final int NONE = 0;
	public static final int RIGHT = 1;
	public static final int LEFT = 2;
	public static final int TOP = 3;
	public static final int BOTTOM = 4;

	int currentDirction_rl = NONE;
	int currentDirction_tb = BOTTOM;

	boolean isPoisoned = false;
	long startTime_poisoned;
	
	int normalColor, poisonedColor;
	
	boolean isCrashedWithSide = false;
	int times_VxForCrahsed = 3;
	static int maxPoisonedTime = 2000;


	Paint attackPnt = new Paint(Paint.ANTI_ALIAS_FLAG);
	Paint attackRangePnt = new Paint(Paint.ANTI_ALIAS_FLAG);
	boolean isAttack = false;
	int attackRad;
	int attackMaxRad[] = new int[4];
	int idx_attack = 0;
	
	int attackSpeed; // 노바가 퍼질 때 증가량
	
	int energy = 0;
	public static final int MAX_ENERGY = 2;

	
	DrawThread dt = null;
	
	int basic_speed;
	
	int ori_x, ori_y;
	
	
	
	public void reset(){
		x = ori_x;
		y = ori_y;
		energy = 0;
		isAttack = false;
		idx_attack = 0;
		isCrashedWithSide = false;
		isPoisoned = false;
		
		
		currentDirction_rl = NONE;
		currentDirction_tb = BOTTOM;

	}
	
	public HeroBall(int x, int y, int Rad, int sideDiff) {
		super(x, y, Rad, sideDiff, 0);
		
		ori_x = x;
		ori_y = y;

		basic_speed =  GameActivity.unitSpeed * 7;
		vx = 0;
		vy = basic_speed;

		attackRad = Rad;
		attackMaxRad[0] = 0;
		attackMaxRad[1] = 7 * Rad;
		attackMaxRad[2] = 10 * Rad;
		attackMaxRad[3] = 10 * Rad; 	// 죽었을 때 효과용
		
		attackSpeed = GameActivity.unitSpeed * 5;

		normalColor = Color.rgb(183, 0, 0);
		poisonedColor = Color.rgb(128, 0, 128);
		
		inCirclePnt.setColor(normalColor);
		attackPnt.setStyle(Paint.Style.STROKE);
		attackPnt.setStrokeWidth(Rad / 5);
		attackPnt.setPathEffect(new DashPathEffect(new float[]{Rad, Rad}, 0));
		attackPnt.setColor(Color.RED);
		
		attackRangePnt.setStyle(Paint.Style.STROKE);
		attackRangePnt.setStrokeWidth(Rad / 7);
		attackRangePnt.setPathEffect(new DashPathEffect(new float[]{Rad, Rad}, 0));
		attackRangePnt.setColor(Color.rgb(243, 218, 150));
		
		outCirclePnt.setColor(Color.RED);
	}

	boolean isCrashedWithTB(int width, int height){
		if (y < rad + sideDiff + sideDiff/2)
			return true;
		else if (y > height - rad - sideDiff/2) 
			return true;
		else
			return false;
	}
	
	@Override
	void move(int width, int height) {
		

		// 충돌 체크 //////////////////////////////////////////////
		
		// 오른쪽에 부딪힘
		if (x > width - rad - sideDiff - sideDiff/2)
			currentDirction_rl = LEFT;
		// 왼쪽에 부딪힘
		else if (x < rad + sideDiff + sideDiff/2)
			currentDirction_rl = RIGHT;
		
		///////////////////////////////////////////////////////////
		
		switch (currentDirction_rl) {
		case RIGHT:
			vx = (basic_speed);
			break;
		case LEFT:
			vx = - (basic_speed);
			break;
		case NONE:
			vx = 0;
		}
		switch (currentDirction_tb) {
		case BOTTOM:
			vy = (basic_speed);
			break;
		case TOP:
			vy = - (basic_speed);
			break;
		case NONE:
			vy = 0;
		}

		// Move
		x += vx;
		y += vy;

	}
	
	// 공격시 GameView에서 불린다. 
	void doAttack(DrawThread dt){
		this.dt = dt;
		isAttack = true;
		dt.curruntControlBitmap = dt.attack;
	}
	
	@Override
	void draw(Canvas canvas) {

		// 노바 발동
		if (isAttack) {
			if (attackRad < attackMaxRad[energy]) {
				// 공격 원 그리기
				canvas.drawCircle(x, y, attackRad, attackPnt);
				attackRad += attackSpeed; // vr 만큼씩 노바가 퍼진다
				if (attackRad >= attackMaxRad[energy]) {
					attackRad = rad;
					
					energy -- ;	// 모두 사라지는 것이 아니라.
					
					if (energy < 0) 	energy = 0;
					
					isAttack = false;
					dt.curruntControlBitmap = dt.none;
				}
			}
		}

		// 기본 주인공 그리기
		canvas.drawCircle(x, y, rad, inCirclePnt);
		canvas.drawCircle(x, y, rad, outCirclePnt);
		// 공격 범위 표시
		canvas.drawCircle(x, y, attackMaxRad[energy%3/*이것 한 이유는 죽을 때*/], attackRangePnt);
	}

	void setDirection(int d) {
		currentDirction_rl = d;
	}

}
