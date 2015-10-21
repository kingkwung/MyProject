package ELEVATOR_GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StartElevatorSimulation extends JFrame implements ActionListener{
	//GUI variables.
	private JPanel mainPanel;
	private JScrollPane scrollPane;
	private JButton StartButton;
	private JButton ClearButton;
	private JList list;
	private DefaultListModel<String> listModel;
	
	public StartElevatorSimulation(){
		setTitle("COOPERATIVE ELEVATOR");
		
		//Set Frame variables.
		mainPanel = new JPanel(){
			public void paintComponent(Graphics g) {
				ImageIcon BgIcon = new ImageIcon("./img/etc/MainBG.png");
				g.drawImage(BgIcon.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		mainPanel.setLayout(null);
		StartButton = new JButton(new ImageIcon("./img/etc/RunButton.png"));
		StartButton.setFocusable(false);
		StartButton.setBounds(15+180, 345, 90, 90);
		StartButton.setBorderPainted(false);
		StartButton.setBackground(new Color(0f,0f,0f,.0f));
		StartButton.setAutoscrolls(false);
		StartButton.addActionListener(this);
		StartButton.setRolloverEnabled(false);
		StartButton.setContentAreaFilled(false);
		ClearButton = new JButton(new ImageIcon("./img/etc/ExitButton.png"));
		ClearButton.setFocusable(false);
		ClearButton.setBounds(135+180, 345, 90, 90);
		ClearButton.setBorderPainted(false);
		ClearButton.setBackground(new Color(0f,0f,0f,.0f));
		ClearButton.addActionListener(this);
		ClearButton.setRolloverEnabled(false);
		ClearButton.setContentAreaFilled(false);
		
		//set List.
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBounds(2, 120, 190, 300);
		list.setOpaque(false);
		list.setSelectionForeground(Color.white);

		list.setFont(new Font("sansserif", Font.BOLD, 15));
		list.setBackground(new Color(0f,0f,0f,.0f));
		list.setForeground(Color.white);
		mainPanel.add(list);
		list.validate();
		
		listModel.addElement("CLUSTER _UPPER(HIGH)");
		listModel.addElement("CLUSTER _LOWER(HIGH)");
		listModel.addElement("PERIODICAL CLUSTER");
		listModel.addElement("NORMAL");
		listModel.addElement("PUNCH IN");
		listModel.addElement("PUNCH OUT");
		listModel.addElement("IDEAL CASE");
		listModel.addElement("GET ON(VICINITY)");
		listModel.addElement("GET OFF(NON_VICINITY)");
		
		//add component to panel.
		mainPanel.add(StartButton);
		mainPanel.add(ClearButton);		
		
		scrollPane = new JScrollPane(mainPanel);
        setContentPane(scrollPane);

        //set frame and view frame.
		setSize(600,500);
		setLocation(400, 80);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == StartButton){
			if(list.getSelectedValue()==null){
				JOptionPane.showMessageDialog(this,"You didn't click anyone here.","ERROR!!!",1);
			}else new RunElevator(list.getSelectedValue()+"");
		}else System.exit(0);
	}
	
	//Elevator start.
	public static void main(String[] args) {
		new StartElevatorSimulation();
	}
}
