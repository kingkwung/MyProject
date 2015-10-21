import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class LoginImg extends JPanel { //jw
	private Toolkit tk;
	private Image img;

	public LoginImg()
	{
		tk = Toolkit.getDefaultToolkit();
		img = tk.createImage("login.png");
		
		this.setBounds(25, 40, 300, 64);
	}
	
	public void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.drawImage(img, 0, 0, this);
	}
}
