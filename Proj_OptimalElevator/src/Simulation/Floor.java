package Simulation;

import java.util.Vector;

class Floor {
	private int intFloor; // floor number
	private int countPersons;
	private boolean isElevator;
	protected Vector persons;
	protected EleButton button;

	public Floor(int iFloor, boolean ox) {
		intFloor = iFloor;
		countPersons = 0;
		isElevator = ox;
		persons = new Vector();
		button = new EleButton(true, true, true); // 모든(위로, 아래로, 정지)버튼생성
	}

	public int getIntFloor() {
		return intFloor;
	}

	public int getCountPersons() {
		return countPersons;
	}

	public boolean getIsElevator() {
		return isElevator;
	}

	public void setIntFloor(int iFloor) {
		intFloor = iFloor;
	}

	public void setIsElevator(boolean ox) {
		isElevator = ox;
	}

	public void addPerson(Object objPerson) {
		if (objPerson instanceof Person)
			persons.add(objPerson);
		countPersons = persons.size();
	}

	public void removePerson() {
		persons.remove(0);
		countPersons = persons.size();
	}

	public Person getPriorityPerson() {
		return (Person) persons.firstElement();
	}
}
