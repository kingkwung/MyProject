package Simulation;

import java.awt.*;
import javax.swing.*;

class GFloor extends Floor {
	// Implementation
	protected Rectangle floorRect;
	protected Rectangle btnBox;
	protected int[] upBtnX = new int[3], upBtnY = new int[3];
	protected Rectangle stopBtn;
	protected int[] downBtnX = new int[3], downBtnY = new int[3];
	// protected double dx, dy; // Motion vector
	public final Color FLOORCOLOR = Color.black;
	public final Color BTNBOXCOLOR = Color.blue;
	public final Color UPCOLOR = Color.red, STOPCOLOR = Color.green,
			DOWNCOLOR = Color.red;

	// Creator
	public GFloor(int px, int py, int width, int height, int floor,
			boolean isEle) {
		super(floor, isEle);
		int bX, bY, bWidth, bHeight;
		floorRect = new Rectangle(px, py, width, height);
		bX = px + 5;
		bY = py + height / 20;
		bWidth = width / 10;
		bHeight = height * 9 / 10;
		btnBox = new Rectangle(bX, bY, bWidth, bHeight);

		upBtnX[0] = bX + bWidth / 2;
		upBtnY[0] = bY + bHeight / 16; // 업버튼의 꼭지점.
		upBtnX[1] = bX + bWidth / 4;
		upBtnY[1] = bY + bHeight / 4; // 업버튼의 왼쪽점.
		upBtnX[2] = bX + bWidth * 3 / 4;
		upBtnY[2] = bY + bHeight / 4; // 업버튼의 오른쪽점.

		downBtnX[0] = bX + bWidth / 2;
		downBtnY[0] = bY + bHeight * 15 / 16; // 다운버튼의 아래꼭지점.
		downBtnX[1] = bX + bWidth / 4;
		downBtnY[1] = bY + bHeight * 3 / 4; // 다운버튼의 왼쪽점.
		downBtnX[2] = bX + bWidth * 3 / 4;
		downBtnY[2] = bY + bHeight * 3 / 4; // 다운버튼의 오른쪽점.

		stopBtn = new Rectangle(bX + bWidth / 4, bY + bHeight * 3 / 8,
				bWidth / 2, bHeight / 4);
	}

	public int getTopFloor() {
		return floorRect.y;
	}

	// Modifier
	public int getWidth() {
		return floorRect.width;
	}

	public int getHeight() {
		return floorRect.height;
	}

	public int getPx() {
		return floorRect.x;
	}

	public int getPy() {
		return floorRect.y;
	}

	// Operator
	public void paint(Graphics g) {
		g.setColor(FLOORCOLOR); // draw floor
		g.drawRect(floorRect.x, floorRect.y, floorRect.width, floorRect.height);

		if (getIsElevator()) {
			g.setColor(Color.cyan); // button on in elevator
			g.fillRect(floorRect.x + floorRect.width + 2, floorRect.y + 3, 30,
					floorRect.height - 6);
			g.setColor(Color.black);
			g.drawString((new Integer(getIntFloor())).toString(), floorRect.x
					+ floorRect.width + 2 + 10, floorRect.y + 3
					+ floorRect.height / 2);
		}

		g.setColor(FLOORCOLOR); // draw buttonBox
		g.drawRect(btnBox.x, btnBox.y, btnBox.width, btnBox.height);

		g.setColor(UPCOLOR); // draw upButton
		if (this.button.isUp())
			g.fillPolygon(upBtnX, upBtnY, 3);
		else
			g.drawPolygon(upBtnX, upBtnY, 3);

		g.setColor(DOWNCOLOR); // draw downButton
		if (this.button.isDown())
			g.fillPolygon(downBtnX, downBtnY, 3);
		else
			g.drawPolygon(downBtnX, downBtnY, 3);

		g.setColor(STOPCOLOR);
		if (this.button.isStop())
			g.fillOval(stopBtn.x, stopBtn.y, stopBtn.width, stopBtn.height);
		else
			g.drawOval(stopBtn.x, stopBtn.y, stopBtn.width, stopBtn.height);

	}

}