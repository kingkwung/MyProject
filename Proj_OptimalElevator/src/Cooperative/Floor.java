package Cooperative;

import java.util.*;

public class Floor {
	private int FloorNum;
	private int waitingPersonNum;
	private boolean IsElevatorArrive;
	protected LinkedList<Person> WaitingPersons;
	protected ElevatorButton button;
	
	public Floor(int FloorIndex, boolean OnOff){
		FloorNum = FloorIndex;
		waitingPersonNum = 0;
		IsElevatorArrive = OnOff;
		WaitingPersons = new LinkedList<Person>();
		button = new ElevatorButton(true,true,true);
	}
	public int getFloorNum(){return FloorNum;}
	public int getWaitingPersonNum(){return waitingPersonNum;}
	public boolean IsElevatorArrive(){return IsElevatorArrive;}
	//public void setFloorNum(int FloorIndex){FloorNum = FloorIndex;} 필요없을듯...
	public void elevatorArrived(boolean IsArrived){IsElevatorArrive = IsArrived;}
	public void addPerson(Object objPerson){
		if(objPerson instanceof Person)
			WaitingPersons.add((Person)objPerson);
		waitingPersonNum = WaitingPersons.size(); //Is will work?		
	}
	public void removePerson(){
		WaitingPersons.remove(0);
		waitingPersonNum = WaitingPersons.size();
	}
	public Person getPersonToBeGetOn(){
		return WaitingPersons.getFirst();
	}
}