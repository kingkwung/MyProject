package elevator;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SwingHandler extends JFrame 
{
	MyPanel[] elevator=new MyPanel[3];
	JButton[] floor=new JButton[10];
	ArrayList<ElevatorInfo> elevatorInfo;
		
	JPanel labelPanel=new JPanel();
	
	public SwingHandler(ArrayList<ElevatorInfo> elevatorInfo){
		this.elevatorInfo=elevatorInfo;
		
		labelPanel.setLayout(new GridLayout(10,1));
		
		this.setLayout(new GridLayout(1,4));
		
		for(int i=10;i>0;i--){
			floor[0]=new JButton("   "+Integer.toString(i));
			floor[0].setSize(50,300);
			labelPanel.add(floor[0]);
		}
		
		add(labelPanel);
		for(int i=0;i<3;i++){
			elevator[i] = new MyPanel((i+1)*20,i);
			elevator[i].setLayout(new GridLayout());
			
			add(elevator[i]);
			new Thread(elevator[i]).start();	
		}
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				System.exit(0);
			}
		});
		setBounds(200,200,600,600);
		setVisible(true);
	}

	class  MyPanel extends JPanel implements Runnable{
		int start;
		int elevatorNumber;
		Toolkit tk;
		Image img;
		
		int y1;
		int y2;
		public MyPanel(int start,int idx){
			tk = Toolkit.getDefaultToolkit();
			img = tk.createImage("save.jpg");
			y1= 520;
			elevatorNumber=idx;
			this.start=start;
		}
		
		public void run(){			
			//	y1 = 0;
			int destination ;
			
			while(true){
				destination =elevatorInfo.get(elevatorNumber).getFirstQueue();
				
				try{
					if(elevatorInfo.get(elevatorNumber).isWaiting()){
						img=tk.createImage("test2.jpg");
						Thread.sleep(50);
						
						continue;
					}else if(destination==-1){
						continue;
					}


					int presentFloor=elevatorInfo.get(elevatorNumber).getPresentFloor();
					y1 =(10-(presentFloor-1))*50;

					img=tk.createImage("save.jpg");
					
					if(presentFloor>destination){
						
						for(int i=presentFloor*100;i>destination*100;i--){

							if(elevatorInfo.get(elevatorNumber).isWaiting()){
								break;
							}
							Thread.sleep(10);
							y1 +=5.2;
							repaint();
						}
					}
					else {	

						for(int i=presentFloor*100;i<destination*100;i++){
							if(elevatorInfo.get(elevatorNumber).isWaiting()){
								break;
							}

							Thread.sleep(10);

							y1 -=0.52;
							repaint();
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		public void paint(Graphics g){
			g.clearRect(0,0,600,600); //?„ì²´ ?”ë©´ ì§?š°ê¸?			
			g.drawImage(img, start, y1, this);
		}
	}
}
