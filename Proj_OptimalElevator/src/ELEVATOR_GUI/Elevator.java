package ELEVATOR_GUI;

import java.util.*;

public class Elevator{
	private LinkedList<Integer> destFloor = new LinkedList<Integer>();
	private LinkedList<PersonGUI> boardPeople = new LinkedList<PersonGUI>();
	private int currentFloor;
	private int FinalDest;
	private int accomodation;
	private boolean up;
	private boolean down;
	protected boolean SOS;
	
	//Constructor.
	public Elevator(Integer InitialDest, int acc){
		if(InitialDest!=0){
			destFloor.add(InitialDest+1);
			setToUp();
		}
		else setToStop();

		accomodation = acc;
		SOS = false;
		currentFloor = FinalDest = InitialDest+1;
	}
	
	public int getAccomodation(){return accomodation;}
	public boolean isSOS(){return SOS;}
	public LinkedList<PersonGUI> getBoardedPersons(){return boardPeople;}
	public int getCurrentFloor(){return currentFloor;}
	public int getFirstDest(){return destFloor.getFirst();}
	public int getLastDest(){return destFloor.getLast();}
	public int getFinalDest(){return FinalDest;}
	public void setCurrentFloor(int cf){currentFloor = cf;}
	public void setFinalDest(int fd){FinalDest = fd;}
	public void setSOS(boolean sosState){SOS = sosState;}	
	public boolean isItHasDestination(){return !destFloor.isEmpty();}
	public boolean isIdle(){return destFloor.isEmpty() || (destFloor.size()==1 && FinalDest==currentFloor);};
	public int getDestSize(){return destFloor.size();}
	
	//Logic
	public void addDestQ(Integer NewDest){
		if(destFloor.isEmpty()){
			FinalDest = NewDest;
			destFloor.add(NewDest);			
			return;
		}
		
		for(Integer dests : destFloor){	//don't enqueue already exist destination.
			if(dests.intValue() == NewDest.intValue()) return;			
			if(dests.intValue() == FinalDest) break;
		}
		
		if(currentFloor < FinalDest){		/*UP*/
			if(!destFloor.getLast().equals(FinalDest) && currentFloor>=NewDest){//different flow~!
				if(checkDestRepetitionAt2ndFlow(NewDest)) return;	//pass if repetition.
				int secondFinal = destFloor.getLast();
				if(NewDest < secondFinal)
					destFloor.addLast(NewDest);
				else{
					boolean findFinal=false;
					for(Integer dest : destFloor){
						if(dest.equals(FinalDest)){
							findFinal = true;
							continue;
						}
						
						if(findFinal && NewDest.intValue() > dest.intValue()){	//down flow!
							destFloor.add(destFloor.indexOf(dest), NewDest);	//Enqueuing floor.
							break;
						}
					}
				}
				return;
			}
			
			if(NewDest > FinalDest){	//Update final destination
				if(destFloor.getLast()==FinalDest){	//one flow
					FinalDest = NewDest;
					destFloor.addLast(NewDest);
				}else{								//two flow
					destFloor.add(destFloor.indexOf(FinalDest)+1, NewDest);
					FinalDest = NewDest;
				}
			}else if(currentFloor>=NewDest){	//in normal queue
				destFloor.addLast(NewDest);		//in abnormal queue
			}else{
				int index=0;
				for(Integer dest : destFloor){
					if(NewDest.intValue() < dest.intValue()){
						destFloor.add(index, NewDest);	//Enqueue floor
						break;
					}index++;
				}
			}
		}else if(currentFloor > FinalDest){	/*DOWN*/
			if(!destFloor.getLast().equals(FinalDest) && currentFloor<=NewDest){//different flow~!
				if(checkDestRepetitionAt2ndFlow(NewDest)) return;	//pass if repetition.
				int secondFinal = destFloor.getLast();
				if(NewDest > secondFinal)
					destFloor.addLast(NewDest);
				else{
					boolean findFinal=false;
					for(Integer dest : destFloor){
						if(dest.equals(FinalDest)){
							findFinal = true;
							continue;
						}

						if(findFinal && NewDest.intValue() < dest.intValue()){
							destFloor.add(destFloor.indexOf(dest), NewDest);	//Enqueue floor
							break;
						}
					}
				}
				return;
			}

			if(NewDest < FinalDest){	//update final destination
				if(destFloor.getLast()==FinalDest){	//one flow
					FinalDest = NewDest;
					destFloor.addLast(NewDest);
				}else{								//two flow
					destFloor.add(destFloor.indexOf(FinalDest)+1, NewDest);
					FinalDest = NewDest;
				}
			}else if(currentFloor<=NewDest){	//in normal queue
				destFloor.addLast(NewDest);		//in abnormal queue
			}else{
				int index=0;
				for(Integer dest : destFloor){
					if(NewDest.intValue() > dest.intValue()){
						destFloor.add(index, NewDest);	//enqueue floors
						break;
					}index++;
				}
			}
		}else{	//If elevator is at final destination
			FinalDest = NewDest;
			destFloor.addLast(NewDest);
		}
	}
	public void addDestQForSecondFlow(Integer NewDest, String FirstFlow){
		if(destFloor.isEmpty()){
			FinalDest = NewDest;
			destFloor.add(NewDest);
			return;
		}else if(checkDestRepetitionAt2ndFlow(NewDest))	//pass repetition
			return;
		else if(destFloor.getLast() == FinalDest){
			destFloor.addLast(NewDest);
			return;
		}
		
		int secondFinal = destFloor.getLast();
		if(FirstFlow.equals("upstream")){
			if(NewDest < secondFinal)
				destFloor.addLast(NewDest);
			else{
				boolean findFinal=false;
				for(Integer dest : destFloor){
					if(dest.equals(FinalDest)){
						findFinal = true;
						continue;
					}

					if(findFinal && NewDest.intValue() > dest.intValue()){
						destFloor.add(destFloor.indexOf(dest), NewDest);	//Enqueue floors
						break;
					}
				}
			}			
		}else{
			if(NewDest > secondFinal)
				destFloor.addLast(NewDest);
			else{
				boolean findFinal=false;
				for(Integer dest : destFloor){
					if(dest.equals(FinalDest)){
						findFinal = true;
						continue;
					}

					if(findFinal && NewDest.intValue() < dest.intValue()){
						destFloor.add(destFloor.indexOf(dest), NewDest);	//Enqueue floors
						break;
					}
				}
			}
		}
	}
	public boolean checkDestRepetitionAt2ndFlow(int NewDest){
		int flag=0, finalIndex = destFloor.indexOf(FinalDest);
		for(Integer dest : destFloor){
			if(flag<finalIndex){
				flag++;
				continue;
			}
			if(dest.equals(NewDest)) return true;
		}
		return false;
	}
	public void deleteDestQ(Integer arrived){
		destFloor.remove(arrived);
	}
	public boolean requestedDeleteDestQ(Floor fData,int floorNum,int deleteEleIndex){
		LinkedList<PersonGUI> waitingQ = fData.getWaitingQueue();
		
		//error handling!
		if(!destFloor.contains(floorNum)) return false;	//error...
		if(destFloor.size()<=1) return false;
		
		for(PersonGUI p : waitingQ){
			if(p.getIndexOfEleToBoard() == deleteEleIndex) return false;	//don't mention it~!
		}		
		for(PersonGUI p : boardPeople){
			if(p.getDestFloor() == floorNum) return false;	//neglect request to delete queue!
		}
		
		if(destFloor.getFirst()==FinalDest && FinalDest==floorNum)
			FinalDest = destFloor.getLast();
		else if(FinalDest==floorNum)
			FinalDest = destFloor.get(destFloor.indexOf(FinalDest)-1);
		
		deleteDestQ(floorNum);
		
		return true;
	}
	public void addPassenger(PersonGUI p){boardPeople.add(p);}
	public void removePassenger(PersonGUI P){boardPeople.remove(P);}
	public void modifyDestPriority(Integer MDInfo){//will be modified destination info.
		destFloor.remove(MDInfo);
		destFloor.addFirst(MDInfo);
	}
	public void balancingDirection(){
		if(currentFloor==FinalDest && destFloor.size()>1){
			FinalDest = destFloor.getLast();
		}
	}
	public int getNumOfBordedPerson(){
		return boardPeople.size();
	}
	public boolean isFull(){
		if(boardPeople.size() == accomodation)
			return true;
		return false;
	}
	
	public void setToUp(){
		up=true;
		down=false;
	}
	public void setToDown(){
		up=false;
		down=true;
	}
	public void setToStop(){
		up=down=false;
	}
}