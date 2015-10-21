package zoipower;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintWriter;

public class WindowDestroyer extends WindowAdapter {
	
	PrintWriter out;
	String name;
	WindowDestroyer(PrintWriter out,String name){
		this.out=out;
		this.name=name;
	}
	public void windowClosing(WindowEvent e){
		out.println("EXIT "+name);
		
		System.exit(0);
	}
}
