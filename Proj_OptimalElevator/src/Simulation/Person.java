package Simulation;

class Person extends Thread {
	private int createFloor;
	private int currentFloor;
	private int destinationFloor;
	private boolean inElevator;

	public Person(int crtFloor, int dstFloor, Building building) {
		createFloor = crtFloor;
		currentFloor = crtFloor;
		destinationFloor = dstFloor;
		inElevator = false;
		building.floor[crtFloor - 1].addPerson(this);
	}

	public int getCreateFloor() {
		return createFloor;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public int getDestinationFloor() {
		return destinationFloor;
	}

	public boolean isInElevator() {
		return inElevator;
	}

	public void setCreateFloor(int cFloor) {
		createFloor = cFloor;
	}

	public void setCurrentFloor(int cFloor) {
		currentFloor = cFloor;
	}

	public void setDestinationFloor(int dFloor) {
		destinationFloor = dFloor;
	}

	public int getDirection() {
		if (destinationFloor - currentFloor > 0)
			return 1;
		else if (destinationFloor - currentFloor < 0)
			return -1;
		else
			return 0;
	}

	public void setInElevator(boolean ox) {
		inElevator = ox;
	}
}
