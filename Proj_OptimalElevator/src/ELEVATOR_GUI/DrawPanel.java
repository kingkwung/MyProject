package ELEVATOR_GUI;

import java.awt.*;
import javax.swing.*;

public class DrawPanel extends JPanel{
	private static int timer = 0;
	private ElevatorGUI[] e;
	private PersonGUI[] p;
	private Motion moving;
	private int[] SData;
	
	public DrawPanel(ElevatorGUI[] eSet,PersonGUI[] pSet, int day, int time){
		setLayout(null);
		setBounds(0,0,1000,600);
		
		SData = RunElevator.scheduleData.get(day).getTotalPercentageForSchedule(time);
		
		e = eSet;
		p = pSet;
		moving = new Motion(e,p,this,day,time);
		moving.start();
	}
	
	public void paintComponent(Graphics G){//automatically called
		int x=60;
		//draw background.
		Toolkit tkline = Toolkit.getDefaultToolkit();
		G.setColor(Color.white);
		G.fillRect(0, 0, 1000, 600);
		G.drawImage(tkline.getImage("./img/etc/background.png"),0,0,this);
		
		G.setColor(Color.black);
		G.fillRect(1000, 0, 100, 600);

		G.drawImage(tkline.getImage("./img/etc/eleBG.png"),350,0,this);
		
		//draw elevator line
		//ele1_380	ele2_450	ele3_520	ele4_590
		
		//draw floor line and elevator line.
		for(int i=1;i*60<600;i++)
			G.drawImage(tkline.getImage("./img/etc/fLine.png"), 0, x*i-1,this);
		for(int i=0;i*60<600;i++){
			G.setColor(Color.BLACK);
			if(SData[9-i]<10) G.drawString(" "+SData[9-i]+" %", 974, x*i+55);
			else G.drawString(SData[9-i]+" %", 970, x*i+55);
		}
		G.drawImage(tkline.getImage("./img/etc/fLine.png"), 0, 598,this);
		G.drawImage(tkline.getImage("./img/etc/eLine.png"),340,0,this);
		G.drawImage(tkline.getImage("./img/etc/eLine.png"),410,0,this);
		G.drawImage(tkline.getImage("./img/etc/eLine.png"),480,0,this);
		G.drawImage(tkline.getImage("./img/etc/eLine.png"),550,0,this);
		G.drawImage(tkline.getImage("./img/etc/eLine.png"),620,0,this);

		Toolkit tk = Toolkit.getDefaultToolkit();
		Image serverImage1 = tk.getImage("./img/server/IServer1.gif");
		Image serverImage2 = tk.getImage("./img/server/IServer2.gif");
		for(int i=0;i<10;i++){
			G.drawImage(serverImage1, 312+7, 19+60*i, this);
			G.drawImage(serverImage2, 632, 19+60*i, this);
		}
		
		for(int i=0;i<e.length;i++){	//draw elevator
			if(e[i].isSOS()){
				if(e[i].getSOSFactor()%3==0) G.drawImage(e[i].geteImage(e[i].getImageIndex()), e[i].getCurX(), e[i].getCurY(), this);
				else G.drawImage(e[i].geteSOSImage(e[i].getImageIndex()), e[i].getCurX(), e[i].getCurY(), this);
				
				e[i].setSOSFactor((e[i].getSOSFactor()+1)%100);
				if(e[i].getSOSFactor()==0) e[i].setSOS(false);				
			}else G.drawImage(e[i].geteImage(e[i].getImageIndex()), e[i].getCurX(), e[i].getCurY(), this);
			
			G.setColor(Color.RED);
			G.drawString(""+e[i].getNumOfBordedPerson(), e[i].getCurX()+45, e[i].getCurY()+20);
		}
		for(int i=0;i<p.length;i++){	//draw people
			if(p[i].IsVisible()){
				G.setColor(Color.BLUE);
				G.drawString(""+p[i].getDestFloor(), p[i].getCurX()+10, p[i].getCurY());
				if(p[i].getCurX() == p[i].getDestX()){
					G.drawImage(p[i].getIm(0), p[i].getCurX(), p[i].getCurY(), this);
					continue;
				}
				
				if((timer % 6) < 2)
					G.drawImage(p[i].getIm(0), p[i].getCurX(), p[i].getCurY(),this);// 1.이미지 2.그리는 위치 x,y(왼쪽 위기 0,0) 4.
				else
					G.drawImage(p[i].getIm(1), p[i].getCurX(), p[i].getCurY(),this);// 1.이미지 2.그리는 위치 x,y(왼쪽 위기 0,0) 4.
			}
		}timer++;
	}
	public void setMovingSpeed(int speed){moving.setTimeFactor(speed);}
	public void Stop(){
		moving.stop();
	}
}