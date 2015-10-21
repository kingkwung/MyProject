package Simulation;

class Building {
	protected Elevator elevator;
	protected Floor[] floor;
	private int countFloors;

	public Building(int cntFloor) {
		elevator = new Elevator(cntFloor);
		floor = new Floor[cntFloor];
		floor[0] = new Floor(1, true);
		for (int i = 1; i < cntFloor; i++)
			floor[i] = new Floor(i + 1, false);
		countFloors = cntFloor;
	}

	public int getCountPersons() {
		int sum = elevator.getCountPersons();
		for (int i = 0; i < countFloors; i++)
			sum = sum + floor[i].getCountPersons();
		return sum;
	}

	public int getCountFloors() {
		return countFloors;
	}
}