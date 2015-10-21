package Simulation;

public class ElevatorRun {
	public static 
	void main(String[] args) {
		TopLevelWindows tlw = new TopLevelWindows();

		tlw.init();		
		tlw.operation();
		
		while(true) {
			if(tlw.getSimOn())
				tlw.simBox.repaint();
			try {
				Thread.sleep(50);
			} catch(InterruptedException e) {}
		}
	}
}