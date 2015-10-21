package Simulation;

import java.awt.*;
import javax.swing.*;

class GPerson extends Person {
	protected boolean visible = false;
	protected int myPriority = 0;
	protected static int priority = 1;
	protected boolean isWalk = false; // 1: Walking -1:Stoping
	protected boolean walkAni;
	protected Rectangle boundedRect;
	protected Rectangle faceRect;
	protected int[] armPx = new int[5], armPy = new int[5];
	protected int[] bodyPx = new int[2], bodyPy = new int[2];
	protected int[] legPx = new int[5], legPy = new int[5];
	protected double dx, dy; // Motion vector
	protected Color personColor;
	protected static Color[] color = { Color.blue, Color.cyan, Color.darkGray,
			Color.green, Color.magenta, Color.orange, Color.pink, Color.red,
			Color.white, Color.yellow };

	public GPerson(int px, int py, int width, int height, int cFloor,
			int dFloor, Building building) {
		super(cFloor, dFloor, building);
		int randomColor;
		boundedRect = new Rectangle(px, py, width, height);
		faceRect = new Rectangle(px + width / 4, py, width / 2, height / 3);
		for (int i = 0; i < 5; i++) { // ´Ù¸®¿Í ÆÈÀÇ xÁÂÇ¥
			armPx[i] = px + (width / 4) * i;
			legPx[i] = (px + width / 6) + (width / 6) * i;
		}
		armPy[0] = py + (height / 12) * 8;
		armPy[1] = py + (height / 12) * 6;
		armPy[2] = py + (height / 12) * 5;
		armPy[3] = py + (height / 12) * 7;
		armPy[4] = py + (height / 12) * 5;

		legPy[0] = py + height;
		legPy[1] = (py + height) - (height / 6);
		legPy[2] = py + (height / 3) * 2;
		legPy[3] = (py + height) - ((height / 12) * 3);
		legPy[4] = py + height;

		bodyPx[0] = px + (width / 2);
		bodyPx[1] = px + (width / 2);
		bodyPy[0] = py + (height / 3);
		bodyPy[1] = py + (height / 3) * 2;

		dx = 5;
		dy = 0;
		randomColor = (int) (Math.random() * 10);
		personColor = color[randomColor];
	}

	public void setMotion(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void setWalk(boolean ox) {
		isWalk = ox;
	}

	public synchronized void move() {
		boundedRect.translate((int) dx, (int) dy);
	}

	private void personWalking() {
		int px = boundedRect.x;
		int py = boundedRect.y;
		int width = boundedRect.width;
		int height = boundedRect.height;
		faceRect = new Rectangle(px + width / 4, py, width / 2, height / 3);
		for (int i = 0; i < 5; i++) { // ´Ù¸®¿Í ÆÈÀÇ xÁÂÇ¥
			armPx[i] = px + (width / 4) * i;
			legPx[i] = (px + width / 6) + (width / 6) * i;
		}
		armPy[0] = py + (height / 12) * 8;
		armPy[1] = py + (height / 12) * 6;
		armPy[2] = py + (height / 12) * 5;
		armPy[3] = py + (height / 12) * 7;
		armPy[4] = py + (height / 12) * 5;

		legPy[0] = py + height;
		legPy[1] = (py + height) - (height / 6);
		legPy[2] = py + (height / 3) * 2;
		legPy[3] = (py + height) - ((height / 12) * 3);
		legPy[4] = py + height;

		bodyPx[0] = px + (width / 2);
		bodyPx[1] = px + (width / 2);
		bodyPy[0] = py + (height / 3);
		bodyPy[1] = py + (height / 3) * 2;
	}

	private void personStoping() {
		int px = boundedRect.x;
		int py = boundedRect.y;
		int width = boundedRect.width;
		int height = boundedRect.height;
		faceRect = new Rectangle(px + width / 4, py, width / 2, height / 3);
		for (int i = 0; i < 5; i++) { // ´Ù¸®¿Í ÆÈÀÇ xÁÂÇ¥
			armPx[i] = (px + width / 6) + (width / 6) * i;
			legPx[i] = (px + 3 * width / 10) + (width / 10) * i;
		}
		armPy[0] = py + (height / 12) * 10;
		armPy[1] = py + (height / 12) * 7;
		armPy[2] = py + (height / 12) * 5;
		armPy[3] = py + (height / 12) * 7;
		armPy[4] = py + (height / 12) * 10;

		legPy[0] = py + height;
		legPy[1] = (py + height) - (height / 6);
		legPy[2] = py + (height / 3) * 2;
		legPy[3] = (py + height) - (height / 6);
		legPy[4] = py + height;

		bodyPx[0] = px + (width / 2);
		bodyPx[1] = px + (width / 2);
		bodyPy[0] = py + (height / 3);
		bodyPy[1] = py + (height / 3) * 2;
	}

	public int getWidth() {
		return boundedRect.width;
	}

	public int getPx() {
		return boundedRect.x;
	}

	public int getPy() {
		return boundedRect.y;
	}

	public Rectangle getBoundedRect() {
		return this.boundedRect;
	}

	public void setRectLocationY(int y) {
		this.boundedRect.setLocation(boundedRect.x, y);
	}

	public synchronized void paint(Graphics g) {
		walkAni = !walkAni;
		if (isWalk) {
			if (walkAni)
				personWalking();
			else
				personStoping();
		} else
			personStoping();
		g.setColor(personColor);
		g.fillOval(faceRect.x, faceRect.y, faceRect.width, faceRect.height);
		g.drawPolyline(bodyPx, bodyPy, 2);
		g.drawPolyline(armPx, armPy, 5);
		g.drawPolyline(legPx, legPy, 5);
		g.setColor(Color.black);
		g.drawString((new Integer(this.getDestinationFloor())).toString(),
				faceRect.x + faceRect.width / 4, faceRect.y + faceRect.height
						* 2 / 3);
	}
}
