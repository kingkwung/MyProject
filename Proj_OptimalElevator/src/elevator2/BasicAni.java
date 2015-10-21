package elevator2;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BasicAni extends JFrame {

	private static final int max_num_of_elve = 2;
	private static final int max_floors = 10;

	private JPanel floorPanel = new JPanel();
	private JButton[] floorBtn = new JButton[max_floors];	

	//private JPanel[] elevator;
	private OneElev[] elevCtrl = new OneElev[max_num_of_elve];	// elevator control

	public BasicAni() {
		this.getContentPane().setLayout(new GridLayout(1, 4));

		floorPanel.setLayout(new GridLayout(max_floors, 1));
		
		for (int i = max_floors - 1; i >= 0; i--) {
			floorBtn[i] = new JButton(Integer.toString(i) + "F");
			floorBtn[i].setSize(50, 50);
			floorPanel.add(floorBtn[i]);
		}
		add(floorPanel);	//
		//this.getContentPane().add(floorLabel);

		for (int i = 0; i < max_num_of_elve; i++) {
			elevCtrl[i] = new OneElev((i + 1) * 25 , i);
			add(elevCtrl[i]);
			new Thread(elevCtrl[i]).start();
		}

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});

		/*
		 * setBounds(int x, int y, int width, int height) Moves and resizes this
		 * component. x,y is top left, and top right
		 */
		setBounds(0, 0, 400, 600); 
		setVisible(true);
	}

	class OneElev extends JPanel implements Runnable {
		private Toolkit tk;
		private Image img;
		
		private int posOfElev_y;
		private int posOfElev_x;

		public OneElev(int startPos_X, int id) {
			tk = Toolkit.getDefaultToolkit();
			img = tk.createImage("swdm.png");

			posOfElev_x = startPos_X;
			posOfElev_y = 500; 
		}

		public void run() {
			int destFloor = 5;	
			int destPos = 500 - 50*(destFloor + 1);

			try {
				while(posOfElev_y != destPos){
					Thread.sleep(10); 
					posOfElev_y -= 2; 
					repaint(); 
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		public void paint(Graphics g) {
			g.clearRect(0, 0, 600, 600); 
			g.drawImage(img, posOfElev_x, posOfElev_y, this);
		}
		
	}

	public static void main(String[] args) {
		BasicAni myProg = new BasicAni();
	}
}
