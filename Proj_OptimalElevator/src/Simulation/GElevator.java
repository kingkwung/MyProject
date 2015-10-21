package Simulation;

import java.awt.*;
import javax.swing.*;

class GElevator extends Elevator {
	// Implementation
	protected Rectangle boundedRect;
	protected double dx, dy; // Motion vector
	protected Color elevatorColor;

	// Creator
	public GElevator(int px, int py, int width, int height, int cFloor) {
		super(cFloor);
		boundedRect = new Rectangle(px, py, width, height);
		dx = 0;
		dy = -13;
		elevatorColor = Color.red;
	}

	public void setRect(int px, int py, int width, int height) {
		boundedRect.setBounds(px, py, width, height);
		System.out.println(boundedRect.x + ":" + boundedRect.y + ":"
				+ boundedRect.width + ":" + boundedRect.height);
	}

	// Modifier
	public void setBallColor(Color newColor) {
		this.elevatorColor = newColor;
	}

	public void setMotion(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void move() {
		boundedRect.translate((int) dx, (int) dy);
	}

	// Selector
	public int getWidth() {
		return boundedRect.width;
	}

	public int getHeight() {
		return boundedRect.height;
	}

	public int getPx() {
		return boundedRect.x;
	}

	public int getPy() {
		return boundedRect.y;
	}

	public double getDx() {
		return this.dx;
	}

	public double getDy() {
		return this.dy;
	}

	public int getTopY() {
		return boundedRect.y;
	}

	public int getBottomY() {
		return boundedRect.y + getHeight();
	}

	public Rectangle getBoundedRect() {
		return this.boundedRect;
	}

	// Operator
	public void paint(Graphics g) {
		g.setColor(elevatorColor);
		g.fillRect(boundedRect.x, boundedRect.y, boundedRect.width,
				boundedRect.height);
		g.setColor(Color.black);
		if (getCountPersons() < getCapacityPersons())
			g.drawString((new Integer(this.getCountPersons())).toString(),
					boundedRect.x + boundedRect.width / 2 - 3, boundedRect.y
							+ boundedRect.height / 2);
		else
			g.drawString("FULL", boundedRect.x + boundedRect.width / 2 - 20,
					boundedRect.y + boundedRect.height / 2);
	}
}
