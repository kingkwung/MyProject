package Simulation;

/*
 *	This Class is made by ȫ����
 *	Date : 2001.7.9
 */

/**
 * ���, ���������� �׸��� �������� ��Ƽ� �׷������� �����ϴ� Ŭ����
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
	 * ������ ��ǥ�� ���� ���� ���� ����.
	 */
	public void fixFloor() {
		heightFloor = getHeight() / building.getCountFloors(); // ������ ����
		for (int i = building.getCountFloors() - 1; i >= 0; i--)
			floor[building.getCountFloors() - i - 1] = new GFloor(getX(),
					getY() + heightFloor * i, getWidth() * 4 / 5, heightFloor,
					building.getCountFloors() - i, false);
	}

	/**
	 * ����� ����.
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
	 * ������������ �ʱ� ��ǥ�� ���� ���̸� ����.
	 */
	protected void setElevatorSize(int px, int py, int width, int height) {
		elevator.setRect(px, py, width, height);
	}

	/**
	 * ����� �����带 ������.
	 */
	private void movePersons(SimGPerson mPerson) {
		mPerson.start();
	}

	// public
	// SimGElevator getElevator() {
	// return elevator;
	// }

	/**
	 * ������ ���������� �������� paint()�޽�带 ȣ��.
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
	 * ����� �����ǰ� �Ҹ�ɶ������� �ൿ������ run()�޽�忡 ǥ���� ���� Ŭ����
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
					// ����� �����Ǿ ���������� �ձ��� ����.
					if (this.getPx() + this.getWidth() < floor[0].getPx()
							+ floor[0].getWidth()) {
						setMotion((int) (Math.random() * 10 + 1), 0);
						this.move();
						setWalk(true);
					}
					// ���������� �ձ��� �������� �ൿ ����
					else {
						setWalk(false);
						// ���������� ��ư�� ������.
						while (true) {
							// ���������� ��ư�� ������.
							if (this.getDirection() == 1)
								floor[this.getCurrentFloor() - 1].button
										.setUp(2);
							else
								floor[this.getCurrentFloor() - 1].button
										.setDown(2);
							// ��ٸ��� �ִ� ���� ���������Ϳ� �˸���.
							elevator.addDestinationFloor(
									this.getCurrentFloor(), this.getDirection());
							// ������������ ���� �ڱⰡ �ִ� ���� ���� ������ ���⵵ ���� ���������Ͱ�
							// ���⶧���� ��ٸ���. �׷��鼭 �ڱⰡ �ִ� ���� ��� ���������Ϳ� �˸���.
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

						// ���������Ͱ� �ͼ� ź��.
						this.visible = false;
						// ���������Ϳ� �ڱⰡ ������ ���� �˸���.
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
						// ���������Ͱ� �����ڱ��� �������� ������������ ��ٸ���.
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

						// ���������Ͱ� �����ؼ� ������.
						elevator.removePerson(this);
						// �ڱ��� ��ġ�� �������� ��ġ�� �����.
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
	 * ������������ �����ٸ��� run()�޽�忡 ������ ���� Ŭ����
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
				// �ö󰥶�
				case 1:
					// �ö󰡰� ������
					if (this.getPy() > // ������������ ����
					floor[getDestinationFloor() - 1].getPy()) { // �������� ����
						if (this.getCurrentFloor()
								- (int) this.getCurrentFloor() == 0) {
							floor[(int) this.getCurrentFloor() - 1].button
									.setStop(1);
						}
						// ������ ������ ���������� �������� �������� 1�� ���Ѵ�.
						// ������ �ʰ� ���� ������ X.5���� �ȴ�.
						if (this.getPy() < floor[(int) Math.round(this
								.getCurrentFloor()) - 1].getTopFloor())
							setCurrentFloor(Math.round(getCurrentFloor()) + 0.5);
						this.setMotion(0, -3.0);
						this.move();
						elevator.directing = 1;
					}
					// �������� ����������
					else {
						this.door.setDoor(true);
						// ���������͸� ���߰� �Ѵ�.
						setCurrentFloor(getDestinationFloor());
						setDirection();
					}
					break;
				// ��������
				case -1:
					// �������� ������
					if (this.getPy() < // ������������ ����
					floor[getDestinationFloor() - 1].getPy()) {
						if (this.getCurrentFloor()
								- (int) this.getCurrentFloor() == 0) {
							floor[(int) this.getCurrentFloor() - 1].button
									.setStop(1);
						}
						// ������ ������ �����Ʒ����� �������� �������� 1�� ����.
						// ������ �ʰ� ���� ������ X.5���� �ȴ�.
						if (this.getPy() > floor[(int) this.getCurrentFloor() - 1]
								.getTopFloor())
							setCurrentFloor((int) getCurrentFloor() - 0.5);
						this.setMotion(0, 3.0);
						this.move();
						elevator.directing = -1;
					}
					// �������� ���� ������
					else {
						this.door.setDoor(true);
						// ���������͸� ���߰� �Ѵ�.
						setCurrentFloor(getDestinationFloor());
						setDirection();
					}
					break;
				// ���߾�����
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