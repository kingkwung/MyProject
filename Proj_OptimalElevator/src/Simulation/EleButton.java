package Simulation;

class EleButton {
	private int upButton = 0, stopButton = 0, downButton = 0; // 0: 없다, 1: 있다(불이
																// 꺼져있다.) 2:
																// 있다(불이 켜져있다.)

	public EleButton(boolean up, boolean stop, boolean down) {
		if (up)
			upButton = 1;
		if (stop)
			stopButton = 1;
		if (down)
			downButton = 1;
	}

	public void setCondition(int up, int stop, int down) {
		upButton = up;
		stopButton = stop;
		downButton = down;
	}

	public void setUp(int up) {
		upButton = up;
	}

	public void setDown(int down) {
		downButton = down;
	}

	public void setStop(int stop) {
		stopButton = stop;
	}

	public boolean isWait() {
		return (upButton == 2 || downButton == 2);
	}

	public boolean isUp() {
		return upButton == 2;
	}

	public boolean isDown() {
		return downButton == 2;
	}

	public boolean isStop() {
		return stopButton == 2;
	}
}
