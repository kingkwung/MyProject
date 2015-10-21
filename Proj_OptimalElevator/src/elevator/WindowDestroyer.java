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
	                "집중?�꺼�?yes�??�력??",
	                "?�뭐??!! ?�른거하�?!",
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
		//?�용?��? ?�로?�스�??�때,?�버?�게 EXIT?�는 명령??보낸??
		out.println("EXIT "+name);
		
		System.exit(0);
	}
}
