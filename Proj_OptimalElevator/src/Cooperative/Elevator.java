package Cooperative;

import java.util.LinkedList;

public class Elevator implements Runnable{
	private static double currentFloor;
	private static boolean[] upperDestination;
	private static boolean[] lowerDestination;
	private int accomodatedPerson;
	private int currentNumOfPerson;
	//private int countFloors;
	private static int direction;
	private static int directing;
	private Door door;
	private LinkedList<Person> persons;
	private int endDestination = 0;
	
	public Elevator(int cntFloors){
		
	}
	public void fullDestination(){}
	public double getCurrentFloor(){return currentFloor;}
	//public int getCountFloors(){return countFloors;}
	public synchronized int getDestinationFloor(){return 0;}
	public int getAccomodatedPerson(){return accomodatedPerson;}
	public synchronized int getCurrentNumOfPerson(){return currentNumOfPerson;}
	public int getDirection(){return direction;}
	public int getDirecting(){return directing;}
	public int getEndDestination(){return endDestination;}
	public void setCurrentFloor(double cFloor){}
	public void addDestinationFloor(int dFloor, int drt){}
	public void setCapacityPersons(int cptPersons){}
	public synchronized void setDirection(){}
	public void removeDest(int dFloor){}
	public synchronized void addPerson(Object objPerson){}
	public synchronized void removePerson(Object objPerson){}
	public Person getPriorityPerson(){return persons.getFirst();}

	public void run(){
		// TODO Auto-generated method stub
	}
}