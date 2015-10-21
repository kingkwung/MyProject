package Simulation;

/*
 *	This Class is made by 홍정구
 *	Date : 2001.7.9
 */

/**
 * 사람, 엘리베이터 그리고 각층들을 모아서 그래픽으로 구현하는 클래스
 */

import java.util.*;
import java.awt.*;
import javax.swing.*;

class SimulBox extends JComponent {
	// Implementation
	private int heightFloor;
	protected Building building;
	protected GFloor[] floor;
	Vector person = new Vector();
	protected SimGElevator elevator;

	// Creator
	public SimulBox(int cFloor, int cPerson) {
		building = new Building(cFloor);
		int width = this.getWidth();
		int height = this.getHeight();
		floor = new GFloor[cFloor];
		elevator = new SimGElevator(0, 0, 0, 0, cFloor);
		elevator.setCapacityPersons(cPerson);
		floor = new GFloor[cFloor];
		for (int i = 0; i < building.getCountFloors(); i++)
			floor[i] = new GFloor(0, 0, 0, 0, 0, false);
		elevator.start();
	}

	// Operator
	/**
	 * 각층의 좌표와 높이 넒이 등을 정함.
	 */
	public void fixFloor() {
		heightFloor = getHeight() / building.getCountFloors(); // 한층의 높이
		for (int i = building.getCountFloors() - 1; i >= 0; i--)
			floor[building.getCountFloors() - i - 1] = new GFloor(getX(),
					getY() + heightFloor * i, getWidth() * 4 / 5, heightFloor,
					building.getCountFloors() - i, false);
	}

	/**
	 * 사람을 생성.
	 */
	public void createPerson(int cFloor, int dFloor) {
		SimGPerson cPerson = new SimGPerson(50, (int) (heightFloor
				* (building.getCountFloors() - cFloor) + heightFloor / 10),
				2 * heightFloor / 3, 4 * heightFloor / 5, cFloor, dFloor,
				building);
		person.add(cPerson);
		movePersons(cPerson);
	}

	/**
	 * 엘리베이터의 초기 좌표와 높이 넒이를 정함.
	 */
	protected void setElevatorSize(int px, int py, int width, int height) {
		elevator.setRect(px, py, width, height);
	}

	/**
	 * 사람의 쓰래드를 시작함.
	 */
	private void movePersons(SimGPerson mPerson) {
		mPerson.start();
	}

	// public
	// SimGElevator getElevator() {
	// return elevator;
	// }

	/**
	 * 사람들과 엘리베이터 각층들의 paint()메쏘드를 호출.
	 */
	public void paintComponent(Graphics g) {
		elevator.paint(g);
		for (int i = 0; i < person.size(); i++) {
			if (((GPerson) person.elementAt(i)).visible == true) {
				((GPerson) person.elementAt(i)).paint(g);
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
				}
			}
		}
		for (int i = 0; i < building.getCountFloors(); i++)
			floor[i].paint(g);
	}

	/**
	 * 사람이 생성되고 소멸될때까지의 행동절차를 run()메쏘드에 표현한 내부 클래스
	 */
	public class SimGPerson extends GPerson {
		private boolean effAnimation = false;

		public SimGPerson(int px, int py, int width, int height, int cFloor,
				int dFloor, Building building) {
			super(px, py, width, height, cFloor, dFloor, building);
			this.visible = true;
		}

		public void run() {
			while (true) {
				if (getCurrentFloor() == getCreateFloor()) {
					// 사람이 생성되어서 엘리베이터 앞까지 갈때.
					if (this.getPx() + this.getWidth() < floor[0].getPx()
							+ floor[0].getWidth()) {
						setMotion((int) (Math.random() * 10 + 1), 0);
						this.move();
						setWalk(true);
					}
					// 엘리베이터 앞까지 왔을때의 행동 절차
					else {
						setWalk(false);
						// 엘리베이터 버튼을 누른다.
						while (true) {
							// 엘리베이터 버튼을 누른다.
							if (this.getDirection() == 1)
								floor[this.getCurrentFloor() - 1].button
										.setUp(2);
							else
								floor[this.getCurrentFloor() - 1].button
										.setDown(2);
							// 기다리고 있는 층을 엘리베이터에 알린다.
							elevator.addDestinationFloor(
									this.getCurrentFloor(), this.getDirection());
							// 엘리베이터의 층과 자기가 있는 층과 같고 갈려는 방향도 같고 엘리베이터가
							// 멈출때까지 기다린다. 그러면서 자기가 있는 층을 계속 엘리베이터에 알린다.
							try {
								sleep(200);
							} catch (InterruptedException e) {
							}
							while (elevator.getCurrentFloor() != this
									.getCreateFloor()
									|| elevator.getDirection() != 0
									|| elevator.directing != this
											.getDirection()) {
								if (elevator.getDirection() == 0
										&& elevator.getCurrentFloor() == this
												.getCreateFloor())
									elevator.addDestinationFloor(
											this.getDestinationFloor(),
											this.getDirection());
								else
									elevator.addDestinationFloor(
											this.getCurrentFloor(),
											this.getDirection());
								try {
									sleep(20);
								} catch (InterruptedException e) {
								}
							}
							if (elevator.getCountPersons() < elevator
									.getCapacityPersons()
									&& elevator.door.isOpen())
								break;
						}

						// 엘리베이터가 와서 탄다.
						this.visible = false;
						// 엘리베이터에 자기가 갈려는 층을 알린다.
						elevator.addDestinationFloor(
								this.getDestinationFloor(), this.getDirection());
						elevator.setDirection();

						floor[this.getDestinationFloor() - 1]
								.setIsElevator(true);
						if (this.getDirection() == 1)
							floor[this.getCurrentFloor() - 1].button.setUp(1);
						else
							floor[this.getCurrentFloor() - 1].button.setDown(1);
						elevator.addPerson(this);
						try {
							sleep(500);
						} catch (InterruptedException e) {
						}
						// 엘리베이터가 목적자기의 목적층과 같아질때까지 기다린다.
						// synchronized
						// (elevator.destinationFloorOn[this.getDestinationFloor()-1])
						// {
						while (elevator.getCurrentFloor() != this
								.getDestinationFloor()
								|| elevator.getDirection() != 0) {
							elevator.addDestinationFloor(
									this.getDestinationFloor(),
									this.getDirection());
							elevator.setDirection();
							try {
								sleep(5);
							} catch (InterruptedException e) {
							}
						}
						// }
						try {
							sleep(200);
						} catch (InterruptedException e) {
						}

						// 엘리베이터가 도착해서 내린다.
						elevator.removePerson(this);
						// 자기의 위치를 목적층의 위치로 맞춘다.
						this.setRectLocationY((int) (heightFloor * (building
								.getCountFloors() - this.getDestinationFloor() + 0.15)));
						setCurrentFloor(getDestinationFloor());
						floor[this.getDestinationFloor() - 1]
								.setIsElevator(false);
						this.visible = true;
					}
				} else if (getCurrentFloor() == getDestinationFloor()) {
					if (this.getPx() > getX()) {
						setMotion(-(int) (Math.random() * 10 + 1), 0);
						this.move();
						setWalk(true);
					} else {
						this.visible = false;
						break;
					}
				}
				try {
					this.sleep(60);
				} catch (InterruptedException i) {
				}
			}
		}
	}

	// private
	// int getFloorTop(int gFloor) {
	// return getHeight()-(getHeight() / building.getCountFloors() * gFloor);
	// }

	/**
	 * 엘리베이터의 스케줄링을 run()메쏘드에 구현한 내부 클래스
	 */
	private class SimGElevator extends GElevator {
		public SimGElevator(int px, int py, int width, int height, int cFloor) {
			super(px, py, width, height, cFloor);
		}

		public void run() {
			while (true) {
				System.out.println("Directing : " + this.directing);
				System.out.println("(D) : " + getDestinationFloor()
						+ "  (C) : " + getCurrentFloor());
				switch (this.getDirection()) {
				// 올라갈때
				case 1:
					// 올라가고 있을때
					if (this.getPy() > // 엘리베이터의 윗면
					floor[getDestinationFloor() - 1].getPy()) { // 목적층의 윗면
						if (this.getCurrentFloor()
								- (int) this.getCurrentFloor() == 0) {
							floor[(int) this.getCurrentFloor() - 1].button
									.setStop(1);
						}
						// 현재의 층보다 한층윗층을 지나가면 현재층에 1을 더한다.
						// 멈추지 않고 지나 갈때는 X.5층이 된다.
						if (this.getPy() < floor[(int) Math.round(this
								.getCurrentFloor()) - 1].getTopFloor())
							setCurrentFloor(Math.round(getCurrentFloor()) + 0.5);
						this.setMotion(0, -3.0);
						this.move();
						elevator.directing = 1;
					}
					// 목적층에 도달했을때
					else {
						this.door.setDoor(true);
						// 엘리베이터를 멈추게 한다.
						setCurrentFloor(getDestinationFloor());
						setDirection();
					}
					break;
				// 내려갈때
				case -1:
					// 내려가고 있을때
					if (this.getPy() < // 엘리베이터의 윗면
					floor[getDestinationFloor() - 1].getPy()) {
						if (this.getCurrentFloor()
								- (int) this.getCurrentFloor() == 0) {
							floor[(int) this.getCurrentFloor() - 1].button
									.setStop(1);
						}
						// 현재의 층보다 한층아래층을 지나가면 현재층에 1을 뺀다.
						// 멈추지 않고 지나 갈때는 X.5층이 된다.
						if (this.getPy() > floor[(int) this.getCurrentFloor() - 1]
								.getTopFloor())
							setCurrentFloor((int) getCurrentFloor() - 0.5);
						this.setMotion(0, 3.0);
						this.move();
						elevator.directing = -1;
					}
					// 목적층에 도달 했을때
					else {
						this.door.setDoor(true);
						// 엘리베이터를 멈추게 한다.
						setCurrentFloor(getDestinationFloor());
						setDirection();
					}
					break;
				// 멈추었을때
				default:
					this.setDirection();
					if (getDestinationFloor() == 0)
						this.directing = this.directing * -1;
					floor[(int) this.getCurrentFloor() - 1].button.setStop(2);
					try {
						sleep(800);
					} catch (InterruptedException e) {
					}
					this.removeDest((int) this.getCurrentFloor() - 1);
					this.door.setDoor(false);
					break;
				}

				try {
					this.sleep(30);
				} catch (InterruptedException e) {
				}
				fullDestination();

			}
		}
	}
}