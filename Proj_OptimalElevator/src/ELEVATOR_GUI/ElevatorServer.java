package ELEVATOR_GUI;

import java.util.LinkedList;

public class ElevatorServer{
	private ElevatorGUI[] elevatorClients;
	private LinkedList<PersonGUI> SpecialQueue = new LinkedList<PersonGUI>();
	
	public ElevatorServer(ElevatorGUI[] eles){
		elevatorClients = eles;
	}
	
	public void addSpecialQ(PersonGUI p){
		//Error handling
		if(SpecialQueue.contains(p)) return;
		
		SpecialQueue.add(p);
	}
	public boolean checkIfInSpecialQ(PersonGUI p){
		if(SpecialQueue.contains(p)) return true;
		return false;
	}
	public void removeSpecialQ(PersonGUI p){SpecialQueue.remove(p);}
	public LinkedList<PersonGUI> getSpecialQueue(){return SpecialQueue;}
	public ElevatorGUI getAvailableElevator(PersonGUI person){
		ElevatorGUI suitableE = null;
		int distance=Integer.MAX_VALUE,tmpDistance;
		
		if(person.isSOS()){
			System.out.println();
		}
		//First idle elevator
		boolean ThereAreIdle=false;
		for(int i=0;i<elevatorClients.length;i++){
			tmpDistance = Math.abs(person.getCurrentFloor()-elevatorClients[i].getCurrentFloor());
			if(elevatorClients[i].getDestSize()>1) continue;	//It has work...
			else if(elevatorClients[i].getDestSize()==1 && elevatorClients[i].getFinalDest()!=elevatorClients[i].getCurrentFloor()) continue;	//아직 최종목적지로 가고있는 중이니까 pass
			
			//Find idle elevator!
			if(tmpDistance<distance){//Find closest elevaotr.
				ThereAreIdle = true;
				suitableE = elevatorClients[i];
				distance = tmpDistance;
			}
		}if(ThereAreIdle){
			return suitableE;
		}
		
		suitableE = null;
		distance=Integer.MAX_VALUE;
		for(int i=0;i<elevatorClients.length;i++){
			int Cur = person.getCurrentFloor();
			int Dest = person.getDestFloor();
			
			//Pass idle elevator
			if(elevatorClients[i].isFull()) continue;
			int finalDestination = elevatorClients[i].getFinalDest();			
			if(elevatorClients[i].getCurrentFloor()==elevatorClients[i].getFinalDest() && elevatorClients[i].getDestSize()>1)
				finalDestination = elevatorClients[i].getLastDest();
				
			if(elevatorClients[i].getCurrentFloor() - finalDestination < 0){		//elevator is up flowing.
				//Check the person is touchable
				if(Cur < elevatorClients[i].getCurrentFloor()) continue;
				//Check flow!!		
				//Do not match flow!!
				if(Cur!=10 && Cur-Dest>0) continue;
				
				tmpDistance = person.getCurrentFloor()-elevatorClients[i].getCurrentFloor();
				if(tmpDistance==0 && elevatorClients[i].isItHasDestination()) continue;
				
				if(distance>tmpDistance){
					suitableE = elevatorClients[i];
					distance = tmpDistance;
				}
			}else if(elevatorClients[i].getCurrentFloor() - finalDestination > 0){	//elevator is down flowing.
				//Check the person is touchable
				if(Cur > elevatorClients[i].getCurrentFloor()) continue;
				///Check flow!!		
				//Do not match flow!!
				if(Cur!=1 && Cur-Dest<0) continue;
								
				tmpDistance = elevatorClients[i].getCurrentFloor()-person.getCurrentFloor();
				if(tmpDistance==0 && elevatorClients[i].isItHasDestination()) continue;
				
				if(distance>tmpDistance){
					suitableE = elevatorClients[i];
					distance = tmpDistance;
				}
			}
		}
		
		return suitableE;
	}
	public ElevatorGUI getIdleElevatorOnSameFloor(int fNum){
		for(int i=0;i<elevatorClients.length;i++){
			if(elevatorClients[i].getCurrentFloor()==fNum && !elevatorClients[i].isItHasDestination()
					&& elevatorClients[i].getImageIndex() == 0)
				return elevatorClients[i];
		}		
		return null;
	}
	public ElevatorGUI getIdleElevatorOnOtherFloor(int fNum){
		int distance = Integer.MAX_VALUE;
		ElevatorGUI returnVal = null;
		
		for(int i=0;i<elevatorClients.length;i++){
			if(elevatorClients[i].getCurrentFloor()!=fNum && !elevatorClients[i].isItHasDestination()
					&& elevatorClients[i].getImageIndex()==0 && Math.abs(elevatorClients[i].getCurrentFloor()-fNum)<distance)
				returnVal = elevatorClients[i];
		}		
		return returnVal;
	}
}