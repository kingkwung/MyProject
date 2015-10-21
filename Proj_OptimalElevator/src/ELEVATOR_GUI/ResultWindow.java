package ELEVATOR_GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ResultWindow extends JPanel {
	//constructor(set frame)
    public ResultWindow(PersonGUI[] defaultOrder, PersonGUI[] accendingOrder, PersonGUI[] descendingOrder) {
        super(new GridLayout(1, 1));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JComponent Dpanel = makeTextPanel("Result");
        Dpanel.setPreferredSize(new Dimension(400, 500));
        tabbedPane.addTab("Result time", null, getTablePanel(defaultOrder), null);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        JComponent ACCpanel = makeTextPanel("ResultAccending");
        ACCpanel.setPreferredSize(new Dimension(400, 500));
        tabbedPane.addTab("Accending Order", null/*icon position*/, getTablePanel(accendingOrder),null);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        JComponent DECpanel = makeTextPanel("ResultDescending");
        DECpanel.setPreferredSize(new Dimension(400, 500));
        tabbedPane.addTab("Descending Order", null/*icon position*/, getTablePanel(descendingOrder),null);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        
        //Add the tabbed pane to this panel.
        add(tabbedPane);
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    //This is needed for table informations.
    protected JComponent makeTextPanel(String text){
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
    //Set Table information.
    public static JPanel getTablePanel(PersonGUI[] people){
    	JPanel tablePanel = new JPanel();
    	tablePanel.setLayout(new GridLayout(1, 2));
    	
    	String[] columnNames = {"Name","Departure","Destination","Waiting_Time"};

    	int sum=0;
    	Object[][] data = new Object[people.length+1][5];
    	for(int i=0;i<people.length;i++){
    		data[i][0] = "Person " + (people[i].index+1);
    		data[i][1] = people[i].getBuildFloor();
    		data[i][2] = people[i].getDestFloor();
    		data[i][3] = (people[i].getWaitingTime()/5)/60 + "min " + (people[i].getWaitingTime()/5)%60 + "sec";
    		sum += people[i].getWaitingTime()/5;
    	}
    	data[people.length][0] = "Average";
    	data[people.length][3] = (sum/people.length)/60 + "min " + (sum/people.length)%60 + "sec";
    	    	
        final JTable table = new JTable(data, columnNames);
        table.setEnabled(false);
        table.setPreferredScrollableViewportSize(new Dimension(400, 300));
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //add graph panel to table panel
        JPanel graphPanel = GraphingData.getGraphicData(people);
        
        //Add the scroll pane to this panel.
        tablePanel.add(scrollPane);
        tablePanel.add(graphPanel);
    	
    	return tablePanel;
    }
    
    public static void ShowResult(PersonGUI[] defaultOrder, PersonGUI[] AccendingOrder, PersonGUI[] descendingOrder) {
    	//Create and set up the window.
    	JFrame frame = new JFrame("Result Window");
    	
    	//Add content to the window.
    	frame.add(new ResultWindow(defaultOrder,AccendingOrder,descendingOrder), BorderLayout.CENTER);
    	frame.setLocation(100, 50);
    	//Display the window.
    	frame.pack();
    	frame.setVisible(true);
    }
}