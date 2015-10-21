package Simulation;

import java.util.Vector;

class Elevator extends Thread {
	private static double currentFloor;
	protected static boolean[] destinationUpOn; // -1:Down 0:Stop , 1:Up ,
	protected static boolean[] destinationDownOn; // -1:Down 0:Stop , 1:Up ,
	private int capacityPersons;
	private int countFloors;
	private int countPersons;
	protected static int direction; // -1:Down 0:Stop , 1:Up ,
	protected static int directing;
	protected Door door;
	protected Vector persons;
	protected int endDestination = 0;

	public Elevator(int cntFloors) {
		currentFloor = 1;
		destinationUpOn = new boolean[cntFloors];
		destinationDownOn = new boolean[cntFloors];
		for (int i = 0; i < cntFloors; i++) {
			destinationUpOn[i] = false;
			destinationDownOn[i] = false;
		}
		capacityPersons = 1;
		countPersons = 0;
		countFloors = cntFloors;
		direction = 0;
		door = new Door();
		persons = new Vector();
		directing = 1;
	}

	public void fullDestination() {
		System.out.print("UP   : ");
		for (int i = 0; i < countFloors; i++)
			System.out.print(destinationUpOn[i] + ", ");
		System.out.println();
		System.out.print("DWON : ");
		for (int i = 0; i < countFloors; i++)
			System.out.print(destinationDownOn[i] + ", ");
		System.out.println();
	}

	public double getCurrentFloor() {
		return currentFloor;
	}

	public int getCountFloors() {
		return countFloors;
	}

	public synchronized int getDestinationFloor() {
		if (directing == 1) { // Up
			for (int i = (int) Math.round(getCurrentFloor()) - 1; i < getCountFloors(); i++) {
				if (destinationUpOn[i])
					return i + 1;
			}
			for (int i = getCountFloors() - 1; i >= getCurrentFloor() - 1; i--) {
				endDestination = i + 1;
				if (destinationDownOn[i])
					return i + 1;
			}
		} else if (directing == -1) { // Down
			for (int i = (int) getCurrentFloor() - 1; i >= 0; i--) {
				if (destinationDownOn[i])
					return i + 1;
			}
			for (int i = 0; i < getCurrentFloor() - 1; i++) {
				endDestination = i + 1;
				if (destinationUpOn[i])
					return i + 1;
			}
		} else {
			for (int i = 0; i < getCountFloors(); i++) {
				if (destinationUpOn[i] || destinationDownOn[i])
					return i + 1;
			}
		}
		return 0; // 정지하고있고 아무도 엘리베이터의 요청이 없을때.
	}

	public int getCapacityPersons() {
		return capacityPersons;
	}

	public synchronized int getCountPersons() {
		return countPersons;
	}

	public int getDirection() {
		return direction;
	}

	public int getDirecting() {
		return directing;
	}

	public int getEndDestination() {
		return endDestination;
	}

	public void setCurrentFloor(double cFloor) {
		currentFloor = cFloor;
	}

	public void addDestinationFloor(int dFloor, int drt) {
		if (drt == 1)
			destinationUpOn[dFloor - 1] = true;
		else
			destinationDownOn[dFloor - 1] = true;
	}

	public void setCapacityPersons(int cptPersons) {
		capacityPersons = cptPersons;
	}

	public synchronized void setDirection() {
		if (getDestinationFloor() != 0) {
			if (getCurrentFloor() - getDestinationFloor() < 0)
				direction = 1; // up
			else if (getCurrentFloor() - getDestinationFloor() > 0)
				direction = -1; // down
			else
				direction = 0; // stop
		} else
			direction = 0; // stop
	}

	public void removeDest(int dFloor) {
		destinationUpOn[dFloor] = false;
		destinationDownOn[dFloor] = false;
	}

	public synchronized void addPerson(Object objPerson) {
		if (door.isOpen() == true) {
			if ((objPerson instanceof Person))
				persons.add(objPerson);
			((Person) persons.firstElement()).setInElevator(true);
			countPersons = persons.size();
		}
	}

	public synchronized void removePerson(Object objPerson) {
		if (door.isOpen() == true) {
			if ((objPerson instanceof Person))
				persons.remove(objPerson);
			countPersons = persons.size();
		}
	}

	public Person getPriorityPerson() {
		return (Person) persons.firstElement();
	}

}
