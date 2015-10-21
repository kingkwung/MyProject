package ELEVATOR_GUI;

import java.awt.*;
import java.util.Random;

public class PersonGUI extends Person{
	//person GUI variable
	private Toolkit tk;
	private Image[] im = new Image[2];
	private String imgName;
	private int CurX; //position of person.
	private int CurY;
	private int DestX;
	//person's formal data.
	private int speed;
	private int hideHere;
	public final int index;
	private boolean stop;
	private boolean board;
	private boolean visible;
	private boolean fast;
	
	public PersonGUI(PersonInfo pData,int in){
		//set person GUI
		super((new Floor(60)).getFloorNumByPixel(pData.startFloor), (new Floor(60)).getFloorNumByPixel(pData.destFloor));
		
		tk = Toolkit.getDefaultToolkit();
		imgName = RunElevator.imgNames[(new Random().nextInt(10))];
		if(pData.direction.equals("LEFT")){
			im[0] = tk.createImage("img/person/"+ imgName +"1.png");
			im[1] = tk.createImage("img/person/"+ imgName +"2.png");
		}else{
			im[0] = tk.createImage("img/person/"+ imgName +"3.png");
			im[1] = tk.createImage("img/person/"+ imgName +"4.png");
		}
		if(new Random().nextInt(2)<1)
			hideHere = -100;
		else hideHere = 1200;
	
		CurX = pData.x;
		CurY = pData.y;
		DestX = pData.dest;
		if(pData.startFloor==pData.destFloor) DestX = pData.x;//exception handling.
		speed = (new Random()).nextInt(5)+2;
		
		index = in;
		
		board = false;
		visible = true;
		
		if(CurX == DestX) stop = true;
		else stop = false;
		
		fast=false;
	}
	
	//setter & getter
	public Toolkit getTk(){return tk;}
	public Image getIm(int index){return im[index];}
	public int getCurX(){return CurX;}
	public int getCurY(){return CurY;}
	public int getDestX(){return DestX;}
	public int getHideHere(){return hideHere;}
	public boolean IsBoard(){return board;}
	public boolean IsVisible(){return visible;}
	public boolean IsHeStop(){return stop;}
	public void setTk(Toolkit thetk){tk = thetk;}
	public void setX(int theCurX){CurX = theCurX;}
	public void setY(int theCurY){CurY = theCurY;}
	public void setDestX(int theDestX){DestX = theDestX;}
	public void setSpeed(int s){speed = s;}
	public void setBoard(boolean b){board = b;}
	public void setVisible(boolean vi){visible = vi;}
	public void setStop(boolean s){stop = s;}
	public void setFast(boolean f){fast = f;}
	public boolean ItTouchesServer(){
		if(CurX==280 || CurX==665)
			return true;
		return false;
	}
	public int getSpeed(){
		if(fast) return 10;
		
		return speed;
	}
	//image change methods
	public void setImageLeftToRight(){
		im[0] = Toolkit.getDefaultToolkit().getImage("img/person/"+ imgName +"1.png");
		im[1] = Toolkit.getDefaultToolkit().getImage("img/person/"+ imgName +"2.png");		
	}
	public void setImageRightToLeft(){
		im[0] = Toolkit.getDefaultToolkit().getImage("img/person/"+ imgName +"3.png");
		im[1] = Toolkit.getDefaultToolkit().getImage("img/person/"+ imgName +"4.png");
	}
}