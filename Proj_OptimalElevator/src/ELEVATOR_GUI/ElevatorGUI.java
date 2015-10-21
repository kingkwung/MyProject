package ELEVATOR_GUI;

import java.awt.*;
import java.util.Random;

public class ElevatorGUI extends Elevator{	//PIXEL -> width: 60px, height: 60px
	//Elevator GUI variables
	public static final int eleP = 29;
	private int index;
	private Toolkit tk;
	private Image[] eImage = new Image[42];
	private Image[] SOSeImage = new Image[42];
	private int imageIndex=0;
	private int[] floor = new int[10];
	private int CurX; //position of elevator.
	private int CurY;
	//data variables.
	private int DestY;
	private boolean arrived;
	private boolean Open = false;
	private int openCount = 0;
	private int SOSFactor = 0;

	//constructor
	public ElevatorGUI(int x,int dest,int in){
		super(dest,10);//board limit: 10
		
		tk = Toolkit.getDefaultToolkit();
		for(int i=9;i>=0;i--)
			floor[i] = 60*(9-i) + 1;
		
		for(int i=0;i<42;i++){
			eImage[i] = tk.createImage("img/ele/ele" + (i+1) + ".png");
			SOSeImage[i] = tk.createImage("img/ele/SOSele" + (i+1) + ".png");
		}
		
		CurX = x;
		CurY = floor[0];
		DestY = floor[dest];
		index = in;
		
		arrived = true;
	}
	//getter and setter
	public Toolkit getTk(){return tk;}
	public Image geteImage(int index){return eImage[index];}
	public Image geteSOSImage(int index){return SOSeImage[index];}
	public int getCurX(){return CurX;}
	public int getCurY(){return CurY;}
	public int getDestY(){return DestY;}
	public int getElevatorIndex(){return index;}
	public boolean isOpen(){return Open;}
	public void setOpen(boolean oc){Open = oc;}
	public int getOpenCount(){return openCount;}
	public void setOpenCount(int c){openCount=c;}
	public int getFloorY_value(int fNum){return floor[fNum-1];}
	public int getSOSFactor(){return SOSFactor;}
	public void setSOSFactor(int sOSFactor){SOSFactor = sOSFactor;}
	public void setTk(Toolkit Thetk){tk = Thetk;}
	public void setCurX(int curX){CurX = curX;}
	public void setCurY(int curY){CurY = curY;}
	public void setDest(int destInfo){DestY = floor[destInfo-1];}
	public void setDestY(int destY){DestY = destY;}	
	public boolean IsArrived(){return arrived;}
	public void setArrived(boolean arr){arrived=arr;}
	public int getImageIndex(){return imageIndex;}
	public void setImageIndex(int idx){imageIndex=idx%42;}
	public int getElevatorPosition(){return CurX+5+(new Random()).nextInt(25);}
	public int getElevatorMidPosition(){return CurX+eleP;}
	public int getCurrentFloorNum(){
		for(int i=0;i<floor.length;i++)
			if(floor[i]==CurY) return i+1;
		return 0;
	}
	
	//Update flow methods.
	public void updateCurrentStateByElevatorPosition(){
		for(int i=0;i<floor.length;i++){
			if(CurY == floor[i]){
				super.setCurrentFloor(i+1);
				return;
			}
		}
	}
	public void updateDestStateByElevatorPosition(){
		if(isItHasDestination()){
			balancingDirection();
			DestY = floor[super.getFirstDest()-1];
		}
		else DestY = floor[getCurrentFloor()-1];
	}
}