package elevator;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class WindowDestroyer extends WindowAdapter {
	JFrame frame;
	PrintWriter out;
	String name;
	WindowDestroyer(PrintWriter out,String name,JFrame frame){
		this.frame=frame;
		this.out=out;
		this.name=name;
	}
	public void windowDeactivated(WindowEvent e){
		String yes=""; 
		while(true){
			yes=JOptionPane.showInputDialog(
	                frame,
	                "ì§‘ì¤‘? êº¼ë©?yesë¥??…ë ¥??",
	                "?ˆë­??!! ?¤ë¥¸ê±°í•˜ì§?!",
	                JOptionPane.YES_NO_CANCEL_OPTION);
			frame.setVisible(true);
			if(yes.equals(null)){
				continue;
			}
			else if(yes.equals("yes"))
				break;
		}
	}
	public void windowClosing(WindowEvent e){
		//?¬ìš©?ê? ?„ë¡œ?¸ìŠ¤ë¥??Œë•Œ,?œë²„?ê²Œ EXIT?¼ëŠ” ëª…ë ¹??ë³´ë‚¸??
		out.println("EXIT "+name);
		
		System.exit(0);
	}
}
