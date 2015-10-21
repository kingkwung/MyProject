package ELEVATOR_GUI;

public class Person{
	//person data.
	private int BuildFloor;
	private int CurrentFloor;
	private int DestFloor;
	private boolean hasDestination=true;
	private boolean pressButton;
	private boolean SOS;
	private int indexTobeboard;
	private int waitingTime;

	public Person(int bf, int df){
		BuildFloor = CurrentFloor = bf;
		DestFloor = df;
		pressButton = SOS = false;
		
		if(DestFloor == CurrentFloor) hasDestination = false;		
		indexTobeboard = -1;	//initially no one has elevator to board.
		waitingTime = 0;
	}
	
	//getter & setter
	public int getBuildFloor(){return BuildFloor;}
	public int getCurrentFloor(){return CurrentFloor;}
	public int getDestFloor(){return DestFloor;}
	public boolean isPressButton(){return pressButton;}
	public boolean isItHasDestination(){return hasDestination;}
	public int getIndexOfEleToBoard(){return indexTobeboard;}
	public boolean isSOS(){return SOS;}
	public int getWaitingTime(){return waitingTime;}
	public void addWaitingTime(){waitingTime++;}
	public void setWaitingTime(int waitingTime){this.waitingTime = waitingTime;}
	public void setBuildFloor(int buildFloor){BuildFloor = buildFloor;}
	public void setCurrentFloor(int curFloor){CurrentFloor = curFloor;}
	public void setDestFloor(int destFloor){DestFloor = destFloor;}
	public void setPressButton(boolean pressButton){this.pressButton = pressButton;}
	public void setHasDestination(boolean hd){hasDestination = hd;}
	public void setIndexOfEleToBoard(int index){indexTobeboard = index;}
	public void setSOS(boolean sos){SOS = sos;}
	public void setIndexOfEleToBoardByPixel(int pixel){
		if(350<=pixel && pixel<=410) indexTobeboard = 0;
		else if(420<=pixel && pixel<=480) indexTobeboard = 1;
		else if(490<=pixel && pixel<=550) indexTobeboard = 2;
		else if(560<=pixel && pixel<=620) indexTobeboard = 3;
	}
}