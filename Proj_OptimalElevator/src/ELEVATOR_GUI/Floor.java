package ELEVATOR_GUI;

import java.util.LinkedList;

public class Floor {
	public static final int initial = 20;
	private int height;
	private LinkedList<PersonGUI> waitingQueue = new LinkedList<PersonGUI>();
	
	//constructor
	public Floor(int h){height = h;}
	//getter & setter
	public int getFloorNumByPixel(int px){return 10 - (px-20)/height;}
	public int getFloorPixelByNum(int fNum){return initial+(height*(10-fNum));}//Total 10 floors
	public LinkedList<PersonGUI> getWaitingQueue(){return waitingQueue;}
	public void AddWaitingPerson(PersonGUI p){waitingQueue.add(p);}
	public void DeleteWaitingPerson(PersonGUI p){waitingQueue.remove(p);}
	public boolean IsFloorHasWaitingPerson(){
		if(waitingQueue.isEmpty()) 
			return false;		
		return true;
	}	
}