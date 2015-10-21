package elevator;

import java.util.LinkedList;
import javax.swing.JOptionPane;

public class ElevatorInfo extends Thread {
	private int presentFloor;
	private LinkedList<Integer> floorQueue;
	private boolean waiting;
	private int direct;
	private int num;
	SwingHandler myUI;
		
	public ElevatorInfo(){	
		floorQueue=new LinkedList<Integer>();
		waiting=false;
	}

	public ElevatorInfo(int num){
		this.num=num;
		
		floorQueue=new LinkedList<Integer>();
	}
	public ElevatorInfo(ElevatorInfo d){
		this.presentFloor=d.getPresentFloor();
		this.floorQueue=d.getFloorQueue();
		this.waiting=d.isWaiting();
		this.direct=d.getDirect();
		this.myUI=d.getMyUI();
	}
	
	public LinkedList<Integer> getFloorQueue() {
		return floorQueue;
	}

	public void setFloorQueue(LinkedList<Integer> floorQueue) {
		this.floorQueue = floorQueue;
	}

	public SwingHandler getMyUI() {
		return myUI;
	}

	public boolean isWaiting() {
		return waiting;
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

	public void run(){		
		while(true){			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(waiting==true){
				String input=getFloor();
				if(!input.equals("")&&input!=null){
					addQueue(Integer.parseInt(input));	
				}
				waiting=false;	
			}
		}
	}
	
	public void setMyUI(SwingHandler myUI){
		this.myUI=myUI;
	}
	
	private String getFloor() {
		String floor=JOptionPane.showInputDialog(
				myUI,
				"Choose a screen name:",
				"Screen name selection",
				JOptionPane.PLAIN_MESSAGE);
		return floor;
		
	}

	public int getFirstQueue(){
		if(floorQueue.isEmpty()){
			return -1;
		}
		return floorQueue.getFirst();
	}
	public void addQueue(int num){
		
		if(direct==0){
			floorQueue.addLast(num);
			if(presentFloor>num){
				direct=2;
			}else if(presentFloor<num){
				direct=1;
			}else{

				
			}
			return;
		}
		else if(direct==1){
			if(presentFloor<num){		
				for(int i=0;i<floorQueue.size();i++){
					if(floorQueue.get(i)>num){
						floorQueue.add(i, num);
						return;
					}
				}
			}else if(presentFloor>num){
				for(int i=0;i<floorQueue.size();i++){
					if(floorQueue.get(i)<num){
						floorQueue.add(i,num);
						return;
					}
				}
			}
			floorQueue.addLast(num);
		}
		else if(direct==2){
			if(presentFloor>num){
				for(int i=0;i<floorQueue.size();i++){
					if(floorQueue.get(i)<num){
						floorQueue.add(i,num);
						return;
					}
				}
			}else if(presentFloor<num){
				for(int i=0;i<floorQueue.size();i++){
					if(floorQueue.get(i)>num){
						floorQueue.add(i,num);
						return ;
					}
				}
			}

			floorQueue.addLast(num);
		}
	}
		
	public int removeFloorQueue(){
		int returnValue;
		
		if(floorQueue.isEmpty()){
			direct=0;
			return -1;
		}
		else {
			returnValue=floorQueue.removeFirst();
			
			if(floorQueue.isEmpty()){
				direct=0;
			}else if(returnValue==floorQueue.getFirst()){
				removeFloorQueue();
			}
			
			if(direct==1){
				if(returnValue>floorQueue.getFirst()){
					direct=2;
				}
			}else if(direct==2){
				if(returnValue<floorQueue.getFirst()){
					direct=1;
				}
			}
		}
		
		return returnValue;
	}
	public boolean isQueueEmpty(){
		return floorQueue.isEmpty();
	}	
	public int getDirect() {		
		return direct;
	}
	public void setDirect(int direct) {
		this.direct = direct;
	}
	public int getPresentFloor() {
		return presentFloor;
	}
	public void upperFloor(){
		presentFloor++;
	}
	public void lowerFloor(){
		presentFloor--;
	}	
	public void setPresentFloor(int presentFloor) {
		this.presentFloor = presentFloor;
	}
}
