package Cooperative;

public class Building {
	//protected Elevator elevator;
	protected Floor[] floor;
	private int numOfFloors;
	
	public Building(int cntFloor){
		//elevator = new Elevator(cntFloor);
		numOfFloors = cntFloor;
		floor = new Floor[numOfFloors+1];	//setting floor one base(not zero base!!)
		for(int i=1;i<numOfFloors+1;i++)
			floor[i] = new Floor(i, false);		
	}
	public int getCountFloors(){return numOfFloors;}
	public int getNumberOfPersonWaiting(){
		int sum = 0;//For all 3-4 elevator clients get the number of people in.
		for(int i=0;i<numOfFloors;i++)
			sum += floor[i].getWaitingPersonNum();
		return sum;
	}
}
