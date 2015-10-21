package ChatWithGame;
import java.util.ArrayList;

public class cardMove extends Thread{

	private int x1;
	private int y1;
	private int x2;
	private int y2;
	private int x3;
	private int y3;
	private int x4;
	private int y4;
	GamePanel panel;
	public cardMove(GamePanel panel){
		this.panel = panel;
		x1 = 200;
		x3 = 300;
		y1 = y3 =55;
		x2 = x4 = 220;
		y2 = y4 = 35;
	}
	public void run(){
		while(true){
			try {
				for(int i=0; i<panel.getList().size(); i++)
				{
					double tmp = panel.getList().get(i).getX() - panel.getList().get(i).getDestX();
					if(tmp > 2 || tmp < -2)
					{
						panel.getList().get(i).setX();
					}
					double tmp2 = panel.getList().get(i).getY() - panel.getList().get(i).getDestY();
					if(tmp2 > 2 || tmp2 < -2)
					{
						panel.getList().get(i).setY();
					}
				}
				Thread.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			panel.repaint();
		}
	}
	public int getX1(){
		return this.x1;
	}
	public int getY1(){
		return this.y1;
	}
	public int getX2(){
		return this.x2;
	}
	public int getY2(){
		return this.y2;
	}
	public int getX3(){
		return this.x3;
	}
	public int getY3(){
		return this.y3;
	}
	public int getX4(){
		return this.x4;
	}
	public int getY4(){
		return this.y4;
	}
	public void plusEachLocation(){
		if(this.x2<900){
			this.x2 = x2+3;}
		if(this.y1<500){
			this.y1 = y1+3;}
	}
	public void plusEachLocation2(){
		if(this.x4<800){
			this.x4 = x4+3;}
		if(this.y3<500){
			this.y3 = y3+3;}
	}
}

