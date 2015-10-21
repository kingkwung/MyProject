package ELEVATOR_GUI;

import java.util.*;

public class Motion extends Thread{
	//Motion variables
	public static final int RunningSpeed = 30;
	private int timefactor = 0;
	
	//Person Queue data
	public static final Floor[] floors = new Floor[10];
	public final Schedule scheduleData;
	private ElevatorServer elevatorServer;
	private ElevatorGUI[] elevatorClients;
	private PersonGUI[] person;
	
	//GUI variables
	private DrawPanel panel;
	private int timer = 0;
	
	//Date manage ment variables
	private int Day;
	private int Time;
	
	
	//constructor.
	public Motion(ElevatorGUI[] e, PersonGUI[] p, DrawPanel pan,int day, int time){
		elevatorServer = new ElevatorServer(e);
		elevatorClients = e;
		person = p;
		panel = pan;
		for(int i=0;i<floors.length;i++)
			floors[i] = new Floor(60);
		
		for(int i=0;i<person.length;i++){
			int floorValue = person[i].getCurrentFloor();
			floors[floorValue-1].AddWaitingPerson(person[i]);
		}
		
		scheduleData = RunElevator.scheduleData.get(day);
		//eleSOS = new PlaySound("./sound/SOS.wav");
		
		Day = day;
		Time = time;
	}
	
	//Main logic(It change elevator's and people's State)
	public void run(){
		reSetting(Time);
		do{
			try{
				Thread.sleep(getSpeed());
				if(AllElevatorHasDone()){
					boolean workNomally=true;
					for(int i=0;i<floors.length;i++){
						LinkedList<PersonGUI> waitingQ = floors[i].getWaitingQueue();
						if(waitingQ.size()==0) continue;
						
						for(PersonGUI p : waitingQ){
							if(p.isPressButton()){
								workNomally = false;
								elevatorServer.addSpecialQ(p);
							}
						}
					}
					if(workNomally) reSetting(Time);
				}
				
				updateWaitingTimePerPerson();				
				CheckElevatorArrived();				
				
				//Pick up people first who can get in more fast elevator than waiting one.  
				CheckThereAreMoreFastWayOnSameFloor();
				AssignElevatorToWaitingPeople();				
				CheckPressingButton();
				
				//Move people and elevator.
				movePeople();
				moveElevator();
				
				timer++;
				//repaint changed image in panel and show it.
				panel.repaint();
			}catch(InterruptedException IE){}
		}while(!AllPeopleArrived() || !AllElevatorHasDone());
		
		//Stop algorithm and show result value.
		(new PlaySound("./sound/graph.wav")).startMusic();
		ResultWindow.ShowResult(person, getAccendingOrder(), getDecendingOrder());
		RunElevator.storeSchedule();
	}
	public boolean doNotHavetoStop(int eleIndex, int floorIndex){
		LinkedList<PersonGUI> waitQ = floors[floorIndex-1].getWaitingQueue();
		for(PersonGUI p : waitQ){
			if(p.isPressButton() && !p.IsBoard() && (p.getCurrentFloor()!=p.getDestFloor()) && p.getIndexOfEleToBoard()==eleIndex)
				return false;
		}
		
		LinkedList<PersonGUI> boardQ = elevatorClients[eleIndex].getBoardedPersons();
		for(PersonGUI p : boardQ){
			if(p.getDestFloor() == floorIndex) return false;
		}
		
		return true;
	}
	public void CheckElevatorArrived(){	//Algorithm of open and close ELEVATOR.
		for(int i=0;i<elevatorClients.length;i++){
			if(elevatorClients[i].getCurY()==elevatorClients[i].getDestY()){//for stopped elevator~
				if(elevatorClients[i].IsArrived() && !elevatorClients[i].isOpen()) continue;	//Don't change state if elevator is stop or the door is opening.

				if(!elevatorClients[i].IsArrived()){	//if elevator is arrived
					//Defensive code!!!
					if(elevatorClients[i].getImageIndex()>21){	//if elevator door is opened, quickly close it!
						elevatorClients[i].setImageIndex(42-elevatorClients[i].getImageIndex());
						continue;
					}
					
					if(doNotHavetoStop(i,elevatorClients[i].getFirstDest())){
						elevatorClients[i].deleteDestQ(elevatorClients[i].getCurrentFloor());	//Elevator done the work then update destination!
						if(elevatorClients[i].getImageIndex()==0)
							elevatorClients[i].updateDestStateByElevatorPosition();
						
						if(elevatorClients[i].isItHasDestination())	//Elevator has destination but it don't take people.
							elevatorClients[i].setArrived(false);
						else elevatorClients[i].setArrived(true);	//Elevator doesn't has destination.
						
						continue;
					}

					elevatorClients[i].setImageIndex(elevatorClients[i].getImageIndex()+1);
					elevatorClients[i].setArrived(true);
					elevatorClients[i].setOpen(true);
					elevatorClients[i].balancingDirection();	//Flow change!
				}

				if(elevatorClients[i].getImageIndex()==0){	//Arrived at destination and door is opened and then closed.
					//If there are other people to get in elevator reopen the door.
					LinkedList<PersonGUI> waitQ = floors[elevatorClients[i].getCurrentFloor()-1].getWaitingQueue();

					boolean hasMore = false;
					for(PersonGUI p : waitQ){
						if(p.isSOS()) continue;
						if(p.getIndexOfEleToBoard() == i){
							hasMore = true;
							p.setFast(true);
						}
					}if(hasMore){//reopening the door~
						elevatorClients[i].setImageIndex(elevatorClients[i].getImageIndex()+1);
						elevatorClients[i].setOpen(true);
						continue;
					}
					
					LinkedList<PersonGUI> BP = elevatorClients[i].getBoardedPersons();
					
					if(!BP.isEmpty()){	//There are people who want to get in this elevator.
						for(PersonGUI bp : BP){
							elevatorClients[i].addDestQ(bp.getDestFloor());	 //Enqueue the destination of people.
							elevatorClients[i].setArrived(false);	//Change state to go another destination~!						
						}
					}

					elevatorClients[i].setOpen(false);	//Door is closed
					elevatorClients[i].deleteDestQ(elevatorClients[i].getCurrentFloor());	//Elevator has done at the stop, then update state.
					
					elevatorClients[i].updateDestStateByElevatorPosition();

					if(elevatorClients[i].isItHasDestination())	//Doesn't take people but it has destinations.
						elevatorClients[i].setArrived(false);
					else elevatorClients[i].setArrived(true);	//Doesn't take people and it also doesn't has destinations.
				}else if(elevatorClients[i].getImageIndex() == 21){ //Door is opened. Then get in and off people.
					elevatorClients[i].setImageIndex(elevatorClients[i].getImageIndex()+1);
					
					LinkedList<PersonGUI> BP = elevatorClients[i].getBoardedPersons();
					
					//Get OFF
					for(Iterator<PersonGUI> ItBP = BP.iterator();ItBP.hasNext();){
						PersonGUI bp = ItBP.next();
						if(bp.IsBoard() && bp.getDestFloor()==elevatorClients[i].getCurrentFloor()){
							bp.setY((new Floor(60)).getFloorPixelByNum(bp.getDestFloor()));
							bp.setCurrentFloor(bp.getDestFloor());
							bp.setVisible(true);
							bp.setX(elevatorClients[bp.getIndexOfEleToBoard()].getElevatorPosition());
							bp.setDestX(bp.getHideHere());
							bp.setStop(false);
							bp.setBoard(false);
							
							ItBP.remove();
							if(BP.isEmpty()) break;
						}
					}
					
					LinkedList<PersonGUI> waitingP =  floors[elevatorClients[i].getCurrentFloor()-1].getWaitingQueue(); //People waiting particular floors
					//Get In
					for(Iterator<PersonGUI> personIt = waitingP.iterator();personIt.hasNext();){
						PersonGUI person = personIt.next();
						if(person.getCurrentFloor()!=person.getDestFloor() && 
								person.getIndexOfEleToBoard()==i){	//Waiting particular elevator
							int flowFactor = elevatorClients[i].getFinalDest()-elevatorClients[i].getCurrentFloor();
							int personFactor = person.getDestFloor()-person.getCurrentFloor();
							if(flowFactor>0 && personFactor<0){	//up flow elevator but down flow people!
								//SOS!!! and enqueue at the second queue.
								elevatorClients[i].addDestQForSecondFlow(person.getCurrentFloor(), "upstream");
								elevatorClients[i].setSOS(true);

								person.setSOS(true);
								elevatorServer.addSpecialQ(person);
								continue;
							}else if(flowFactor<0 && personFactor>0){ //down flow elevator but up flow people!
								//SOS!!! and enqueue at the second queue.
								elevatorClients[i].addDestQForSecondFlow(person.getCurrentFloor(), "downstream");
								elevatorClients[i].setSOS(true);

								person.setSOS(true);
								elevatorServer.addSpecialQ(person);
								continue;
							}else if(flowFactor>0 && personFactor>0 && elevatorClients[i].isFull()){
								//If elevator is full...
								//SOS!!! and enqueue at the second queue.
								elevatorClients[i].addDestQForSecondFlow(person.getCurrentFloor(), "upstream");
								elevatorClients[i].setSOS(true);
								
								person.setSOS(true);
								elevatorServer.addSpecialQ(person);
								continue;
							}else if(flowFactor<0 && personFactor<0 && elevatorClients[i].isFull()){
								//If elevator is full...
								//SOS!!! and enqueue at the second queue.
								elevatorClients[i].addDestQForSecondFlow(person.getCurrentFloor(), "downstream");
								elevatorClients[i].setSOS(true);
								
								person.setSOS(true);
								elevatorServer.addSpecialQ(person);
								continue;
							}
							
							//Board people!
							//Dequeuing people from waiting queue. 
							if(elevatorServer.checkIfInSpecialQ(person))
								elevatorServer.removeSpecialQ(person);

							if(elevatorClients[i].isIdle())	//Set elevator Flow.
								elevatorClients[i].addDestQ(person.getDestFloor());	 //Enqueue destination at first flow.  
							
							BP.add(person);
							person.setBoard(true);
							person.setFast(false);
							person.setVisible(false);
							personIt.remove();
						}
					}
				}
				
				//Door is open and wait some liitle.
				if(elevatorClients[i].getOpenCount()==0){
					if(timer%2 == 0) elevatorClients[i].setImageIndex(elevatorClients[i].getImageIndex()+1);
				}else elevatorClients[i].setOpenCount((elevatorClients[i].getOpenCount()+1)%20);
			}
		}
	}
	public void CheckThereAreMoreFastWayOnSameFloor(){
		for(int i=0;i<floors.length;i++){
			ElevatorGUI idleELE = elevatorServer.getIdleElevatorOnSameFloor(i+1);
			if(idleELE==null) continue;
			
			LinkedList<PersonGUI> waitingPersons = floors[i].getWaitingQueue();
			for(Iterator<PersonGUI> personIt = waitingPersons.iterator();personIt.hasNext();){
				PersonGUI P = personIt.next();
				if(!P.isPressButton()) continue;
				
				int oldIndex = P.getIndexOfEleToBoard();
				
				if(oldIndex<0){
					P.setDestX(idleELE.getElevatorPosition());
					P.setIndexOfEleToBoardByPixel(idleELE.getElevatorMidPosition());
					P.setStop(false);
					P.setFast(true);
					//Get Idle elevator and give job!
					idleELE.addDestQ(i+1);
					idleELE.setArrived(false);
					if(idleELE.getCurrentFloor()==P.getCurrentFloor() && idleELE.getImageIndex()>21)
						idleELE.setImageIndex(42-idleELE.getImageIndex());
				}else{
					ElevatorGUI waitingELE = elevatorClients[oldIndex];

					//Error handling.
					if(idleELE.equals(elevatorClients[oldIndex])) continue;
					else if(elevatorClients[oldIndex].getCurrentFloor() == P.getCurrentFloor()) continue;

					//Don't wait slow elevator.
					P.setDestX(idleELE.getElevatorPosition());
					P.setIndexOfEleToBoardByPixel(idleELE.getElevatorMidPosition());
					P.setStop(false);
					P.setFast(true);//Run!!!
					//Get Idle elevator and give job!
					idleELE.addDestQ(i+1);
					idleELE.setArrived(false);
					if(idleELE.getCurrentFloor()==P.getCurrentFloor() && idleELE.getImageIndex()>21)
						idleELE.setImageIndex(43-idleELE.getImageIndex());

					//dequeue destination to elevator.
					waitingELE.requestedDeleteDestQ(floors[i], i+1, oldIndex);
				}
			}
		}
	}
	public void AssignElevatorToWaitingPeople(){
		LinkedList<PersonGUI> people = elevatorServer.getSpecialQueue();
		
		//Cut in funding to waiting people whose destination flow is same.
		for(Iterator<PersonGUI> ItPerson = people.iterator();ItPerson.hasNext();){
			PersonGUI p = ItPerson.next();
			for(int i=0;i<person.length;i++){
				if(!person[i].equals(p) && person[i].isPressButton() &&	
						person[i].getIndexOfEleToBoard()>=0 && person[i].getCurrentFloor()==p.getCurrentFloor()){ 	//People who can be cutted in funding~
					if(!person[i].IsVisible()) continue;
					if(person[i].getIndexOfEleToBoard() == p.getIndexOfEleToBoard()) continue;
					
					if(p.getDestFloor()-p.getCurrentFloor()>0 && person[i].getDestFloor()-person[i].getCurrentFloor()>0){//Same in up flow~!!
						if(person[i].getDestFloor()>=p.getDestFloor()){//Touchable flow!
							p.setDestX(elevatorClients[person[i].getIndexOfEleToBoard()].getElevatorPosition());
							p.setIndexOfEleToBoard(person[i].getIndexOfEleToBoard());
							p.setStop(false);
							p.setFast(true);
							elevatorClients[person[i].getIndexOfEleToBoard()].addDestQ(p.getCurrentFloor());
							if(elevatorClients[person[i].getIndexOfEleToBoard()].getImageIndex()==0)
								elevatorClients[person[i].getIndexOfEleToBoard()].updateDestStateByElevatorPosition();
							elevatorClients[person[i].getIndexOfEleToBoard()].setArrived(false);
							
							if(elevatorClients[person[i].getIndexOfEleToBoard()].getCurrentFloor()==p.getCurrentFloor() && elevatorClients[person[i].getIndexOfEleToBoard()].getImageIndex() > 21)
								elevatorClients[person[i].getIndexOfEleToBoard()].setImageIndex(43-elevatorClients[person[i].getIndexOfEleToBoard()].getImageIndex());
							//Dequeue
							ItPerson.remove();
							break;
						}
					}else if(p.getDestFloor()-p.getCurrentFloor()<0 && person[i].getDestFloor()-person[i].getCurrentFloor()<0){//Same in up flow~!!
						if(person[i].getDestFloor()<=p.getDestFloor()){//Touchable flow!
							p.setDestX(elevatorClients[person[i].getIndexOfEleToBoard()].getElevatorPosition());
							p.setIndexOfEleToBoard(person[i].getIndexOfEleToBoard());
							p.setStop(false);
							p.setFast(true);
							elevatorClients[person[i].getIndexOfEleToBoard()].addDestQ(p.getCurrentFloor());
							if(elevatorClients[person[i].getIndexOfEleToBoard()].getImageIndex()==0)
								elevatorClients[person[i].getIndexOfEleToBoard()].updateDestStateByElevatorPosition();
							elevatorClients[person[i].getIndexOfEleToBoard()].setArrived(false);
							
							if(elevatorClients[person[i].getIndexOfEleToBoard()].getCurrentFloor()==p.getCurrentFloor() && elevatorClients[person[i].getIndexOfEleToBoard()].getImageIndex() > 21)
								elevatorClients[person[i].getIndexOfEleToBoard()].setImageIndex(43-elevatorClients[person[i].getIndexOfEleToBoard()].getImageIndex());
							//Dequeue
							ItPerson.remove();
							break;
						}
					}
				}
			}
		}
		for(Iterator<PersonGUI> ItPerson = people.iterator();ItPerson.hasNext();){
			PersonGUI p = ItPerson.next();
			ElevatorGUI tmpE = elevatorServer.getAvailableElevator(p);
			
			//Error handling
			if(tmpE == null) continue;
			if(p.getIndexOfEleToBoard() == tmpE.getElevatorIndex())
				continue;
			
			if(tmpE.getCurrentFloor()==p.getCurrentFloor() && tmpE.getImageIndex()>21)
				tmpE.setImageIndex(43-tmpE.getImageIndex());
				
			p.setDestX(tmpE.getElevatorPosition());
			p.setIndexOfEleToBoard(tmpE.getElevatorIndex());
			p.setStop(false);
			p.setFast(true);
			
			tmpE.addDestQ(p.getCurrentFloor());
			if(tmpE.getImageIndex()==0)
				tmpE.updateDestStateByElevatorPosition();
			tmpE.setArrived(false);
			
			ItPerson.remove();
			if(people.isEmpty()) break;	 
		}
	}
	public void CheckPressingButton(){	//Press Elevator server Button
		for(int i=0;i<floors.length;i++){
			//Check some person press server button
			for(PersonGUI people : floors[i].getWaitingQueue()){
				if(people.ItTouchesServer() && !people.isPressButton()){
					people.setPressButton(true);
					scheduleData.addInput(Time, people.getCurrentFloor()-1);
					
					//Server Scheduling part.
					ElevatorGUI tmpE = elevatorServer.getAvailableElevator(people);
					
					if(tmpE == null){	//Server do not allocate elevator
						elevatorServer.addSpecialQ(people);
						continue;
					}else{				//Server allocate the elevator
						tmpE.balancingDirection();
						tmpE.addDestQ(people.getCurrentFloor());
						if(tmpE.getImageIndex()==0)
							tmpE.updateDestStateByElevatorPosition();
						tmpE.setArrived(false);
						people.setDestX(tmpE.getElevatorPosition());
						people.setIndexOfEleToBoardByPixel(tmpE.getElevatorMidPosition());
						people.setStop(false);
						if(tmpE.getCurrentFloor() == people.getCurrentFloor())
							people.setFast(true);
					}
				}
			}
		}
	}
	public void movePeople(){
		for(int i=0;i<person.length;i++){	//algorithm of Moving people.
			if(person[i].IsHeStop()) continue;
			
			if(person[i].getCurX() < person[i].getDestX()){//go to right.
				person[i].setImageLeftToRight();
				person[i].setX(person[i].getCurX() + person[i].getSpeed());
				if(person[i].getCurX() > person[i].getDestX()) person[i].setX(person[i].getDestX());
			}else if(person[i].getCurX() > person[i].getDestX()){//go to left.
				person[i].setImageRightToLeft();
				person[i].setX(person[i].getCurX() - person[i].getSpeed());
				if(person[i].getCurX() < person[i].getDestX()) person[i].setX(person[i].getDestX());
			}
			if(!person[i].IsHeStop() && person[i].getCurX() == person[i].getDestX())
				person[i].setStop(true);
		}		
	}
	public void moveElevator(){
		for(int i=0;i<elevatorClients.length;i++){//(Algorithm of moving elevator)
			if(elevatorClients[i].getCurY() < elevatorClients[i].getDestY()){	//up flow
				elevatorClients[i].setToUp();
				elevatorClients[i].setCurY(elevatorClients[i].getCurY() + 3);
				if(elevatorClients[i].getCurY() > elevatorClients[i].getDestY()) elevatorClients[i].setCurY(elevatorClients[i].getDestY());
			}else if(elevatorClients[i].getCurY() > elevatorClients[i].getDestY()){	//down flow
				elevatorClients[i].setToDown();
				elevatorClients[i].setCurY(elevatorClients[i].getCurY() - 3);
				if(elevatorClients[i].getCurY() < elevatorClients[i].getDestY()) elevatorClients[i].setCurX(elevatorClients[i].getDestY());
			}			
			elevatorClients[i].updateCurrentStateByElevatorPosition();	//Update elevator flow
		}		
	}
	public boolean AllElevatorHasDone(){
		for(int i=0;i<elevatorClients.length;i++){
			if(elevatorClients[i].isItHasDestination())
				return false;
		}return true;
	}
	public boolean AllPeopleArrived(){
		for(PersonGUI p : person){
			if(p.getCurrentFloor()!=p.getDestFloor()) return false;
			if(p.getCurX()!=p.getDestX()) return false;
		}
		return true;
	}
	public void updateWaitingTimePerPerson(){
		for(PersonGUI p : person){
			if(p.isPressButton() && (p.getCurrentFloor()!=p.getDestFloor()))
				p.addWaitingTime();
		}
	}
	public void reSetting(int time){
		int[] rank = scheduleData.getLankForSchedule(time);	//0 -> (0H ~ 1H)
		//error handling!
		if(rank.length!=4){
			System.err.println("Index ERROR");
		}for(int i=0;i<rank.length;i++){
			if(rank[i]<0 || rank[i]>9)
				System.err.println("Floor ERROR");
		}
		for(int i=0;i<elevatorClients.length;i++){
			if(elevatorClients[i].getCurrentFloor() == rank[i]+1) continue;
			
			elevatorClients[i].addDestQ(rank[i]+1);
			elevatorClients[i].setDestY(elevatorClients[i].getFloorY_value(rank[i]+1));
			elevatorClients[i].setArrived(false);
		}	
	}
	public int getSpeed(){
		return RunningSpeed-timefactor;
	}
	public void setTimeFactor(int TF){
		if(-30 < TF && TF < RunningSpeed) timefactor = TF;
	}
	public PersonGUI[] getAccendingOrder(){
		PersonGUI[] people = person.clone();
		PersonGUI tmp;
		
		for(int i=0;i<people.length;i++){
			for(int j=0;j<people.length-1;j++){
				if(people[j].getWaitingTime() > people[j+1].getWaitingTime()){
					tmp = people[j];
					people[j] = people[j+1];
					people[j+1] = tmp;
				}
			}
		}
		
		return people;
	}
	public PersonGUI[] getDecendingOrder(){
		PersonGUI[] people = person.clone();
		PersonGUI tmp;
		
		for(int i=0;i<people.length;i++){
			for(int j=0;j<people.length-1;j++){
				if(people[j].getWaitingTime() < people[j+1].getWaitingTime()){
					tmp = people[j];
					people[j] = people[j+1];
					people[j+1] = tmp;
				}
			}
		}
		
		return people;
	}
}