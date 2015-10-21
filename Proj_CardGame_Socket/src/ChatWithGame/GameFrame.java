package ChatWithGame;
import javax.swing.JFrame;


public class GameFrame extends JFrame{
	private GamePanel gamepanel;
	
	public GameFrame(String u1, String u2)
	{
		setLayout(null);
		setSize(1200,700);
		gamepanel = new GamePanel(u1,u2);
		add(gamepanel);
	}
	public GamePanel getPanel()
	{
		return gamepanel;
	}
	public void viewGame(){
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	/**
	public static void main(String[] args)
	{
		GameFrame main = new GameFrame();
		main.viewGame();
	}
	*/
}
