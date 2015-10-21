package ELEVATOR_GUI;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class RunElevator extends JFrame implements ChangeListener{
	public static ElevatorGUI[] ELEVATORS;
	public static PersonGUI[] PERSONS;
	public static LinkedList<Schedule> scheduleData;
	public static final int[] elePixel = {351,421,491,561};	//351 is absolute value for elevator's x value.
	public static String[] imgNames = {"p1","p2","p3","p4","p5","p6","p7","p8","p9","p10"};
	private DrawPanel dp;
	private JPanel FloorPanel;
	private JPanel spinPanel;
	private JSpinner SpeedSpinner;
	
	public RunElevator(String InputFile){
		setTitle("Elevator Simulation(" + InputFile + ")");
		//Setting Schedule.
		FileReadWriter scheduleFile = new FileReadWriter("./schedule/Schedule.dat");
		scheduleData = scheduleFile.LoadSchedule();
		Calendar cal = Calendar.getInstance();
		int dayIndex = cal.getTime().getDay();
		int timeIndex = cal.getTime().getHours();
		
		//Setting People.
		FileReadWriter file = new FileReadWriter("./DataSample/"+InputFile + ".dat");
		PERSONS = file.LoadPersonGUI();
		
		//Setting Elevators.
		ELEVATORS = new ElevatorGUI[4];
		ELEVATORS[0] = new ElevatorGUI(elePixel[0], 0, 0);
		ELEVATORS[1] = new ElevatorGUI(elePixel[1], 0, 1);
		ELEVATORS[2] = new ElevatorGUI(elePixel[2], 0, 2);
		ELEVATORS[3] = new ElevatorGUI(elePixel[3], 0, 3);
		
		//Setting panel.
			//Drawing panel
		dp = new DrawPanel(ELEVATORS, PERSONS, dayIndex, timeIndex);	//elevator input, person input
			//Floor button panel
		FloorPanel = new JPanel(new GridLayout(10, 1));
		FloorPanel.setSize(100, 600);
		FloorPanel.setBounds(1000, 0, 100, 600);
		for(int i=10;i>0;i--)
			FloorPanel.add(new JButton(new ImageIcon("./img/floor/F" + (i) +".png")));
		
		spinPanel = new JPanel();
		spinPanel.setLayout(new GridLayout(0, 3));
		add(spinPanel, BorderLayout.CENTER);

		SpeedSpinner = new JSpinner();
		SpeedSpinner.addChangeListener(this);
	
		spinPanel.add(new JLabel("Time Factor: "));
		spinPanel.add(SpeedSpinner);
		final JLabel valueLabel = new JLabel();
		spinPanel.add(valueLabel);
		spinPanel.setSize(250, 25);
		spinPanel.setBounds(918, 600, 250, 25);
		
		//Setting date panel.
		JPanel datePanel = new JPanel();
		datePanel.add(new JLabel(getDate(dayIndex, timeIndex)));
		datePanel.setBounds(0, 600, 150, 25);
		
		//Setting GUI Frame.
		getContentPane().add(dp);
		getContentPane().add(FloorPanel);
		getContentPane().add(spinPanel);
		getContentPane().add(datePanel);
		setLayout(null);
		setBounds(0,0,1100 +10, 625 +38);
		setLocation(100, 20);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dp.Stop();
				setVisible(false);
				dispose();
			}
		});
		
		//Background music.
		(new PlaySound("./sound/s1.wav")).startMusic();
	}
	public static void storeSchedule(){
		(new FileReadWriter("./schedule/Schedule.dat")).storeSchedule(scheduleData);
	}
	public void stateChanged(ChangeEvent evt){
		dp.setMovingSpeed((int)SpeedSpinner.getValue());
	}
	public String getDate(int day, int time){
		String returnvalue="";
		
		switch(day){
		case 0:		
			returnvalue += "Sunday "; 
			break;
		case 1:
			returnvalue += "Monday ";
			break;
		case 2:			
			returnvalue += "Tuesday ";
			break;
		case 3:			
			returnvalue += "wednesday ";
			break;
		case 4:			
			returnvalue += "Thursday ";
			break;
		case 5:			
			returnvalue += "Friday ";
			break;
		case 6:		
			returnvalue += "Saturday ";
			break;
		default:
			break;
		}
		return returnvalue + ": " + time + ":00 - " + (time+1) + ":00";
	}
}