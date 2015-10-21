package Cooperative;

//Thread??
public class Person{
	private int createFloor;
	private int currentFloor;
	private int DestFloor;
	private boolean InElevator;
	
	public Person(int created, int destination, Building building){
		createFloor=currentFloor=created;
		DestFloor = destination;
		InElevator=false;
		building.floor[createFloor].addPerson(this);
	}
	public int getCreateFloor(){return createFloor;}
	public int getCurrentFloor(){return currentFloor;}
	public int getDestinationFloor(){return DestFloor;}
	public boolean isHeInElevator(){return InElevator;}
	public void setCurrentFloor(int current){currentFloor=current;}
	public void setDestinationFloor(int dest){DestFloor=dest;}
	public void setInElevator(boolean IsInElevator){InElevator=IsInElevator;}
	public int getDirection(){
		int UpAndDown=DestFloor-currentFloor;
		
		if(UpAndDown > 0) return 1;
		else if(UpAndDown < 0) return -1;
		
		return 0;
	}
	//public void run() {	// TODO Auto-generated method stub }
}

