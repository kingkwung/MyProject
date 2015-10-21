import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WhisperWindow extends JDialog implements ActionListener{
    	private static WhisperWindow choosedName;
    	private static String value = "";
    	private JList list;
		static String[] possibleValues;

    	public static String showUsers(Component frameComp,
    			Component locationComp,
    			String labelText,
    			String title,
    			String[] Users,
    			String initialValue,
    			String longValue) {
    		Frame frame = JOptionPane.getFrameForComponent(frameComp);
    		
    		possibleValues = new String[Users.length+1];
    		possibleValues[0]="[To all]";
    		for(int i=0;i<Users.length;i++)
    			possibleValues[i+1]=Users[i];
    		
    		choosedName = new WhisperWindow(frame,
    				locationComp,
    				labelText,
    				title,
    				possibleValues,
    				initialValue,
    				longValue);
    		choosedName.setVisible(true);
    		
    		return value;
    	}

    	private void setValue(String newValue) {
    		value = newValue;
    		list.setSelectedValue(value, true);
    	}

    	private WhisperWindow(Frame frame,
    			Component locationComp,
    			String labelText,
    			String title,
    			Object[] data,
    			String initialValue,
    			String longValue) {
    		super(frame, title, true);

    		//Create and initialize the buttons.
    		JButton cancelButton = new JButton("Cancel");
    		cancelButton.addActionListener(this);
    		
    		final JButton setButton = new JButton("Select");
    		setButton.setActionCommand("Select");
    		setButton.addActionListener(this);
    		getRootPane().setDefaultButton(setButton);

    		list = new JList(data) {
    			public int getScrollableUnitIncrement(Rectangle visibleRect,int orientation,int direction) {
    				int row;
    				if (orientation == SwingConstants.VERTICAL &&
    						direction < 0 && (row = getFirstVisibleIndex()) != -1) {
    					Rectangle r = getCellBounds(row, row);
    					if ((r.y == visibleRect.y) && (row != 0))  {
    						Point loc = r.getLocation();
    						loc.y--;
    						int prevIndex = locationToIndex(loc);
    						Rectangle prevR = getCellBounds(prevIndex, prevIndex);

    						if (prevR == null || prevR.y >= r.y) {
    							return 0;
    						}
    						return prevR.height;
    					}
    				}
    				return super.getScrollableUnitIncrement(
    						visibleRect, orientation, direction);
    			}
    		};

    		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    		if (longValue != null) {
    			list.setPrototypeCellValue(longValue); //get extra space
    		}
    		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    		list.setVisibleRowCount(-1);
    		list.addMouseListener(new MouseAdapter() {
    			public void mouseClicked(MouseEvent e) {
    				if (e.getClickCount() == 2) {
    					setButton.doClick(); //emulate button click
    				}
    			}
    		});
    		JScrollPane listScroller = new JScrollPane(list);
    		listScroller.setPreferredSize(new Dimension(250, 80));
    		listScroller.setAlignmentX(LEFT_ALIGNMENT);

    		JPanel listPane = new JPanel();
    		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
    		JLabel label = new JLabel(labelText);
    		label.setLabelFor(list);
    		listPane.add(label);
    		listPane.add(Box.createRigidArea(new Dimension(0,5)));
    		listPane.add(listScroller);
    		listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

    		//Lay out the buttons from left to right.
    		JPanel buttonPane = new JPanel();
    		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
    		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    		buttonPane.add(Box.createHorizontalGlue());
    		buttonPane.add(cancelButton);
    		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
    		buttonPane.add(setButton);

    		//Put everything together, using the content pane's BorderLayout.
    		Container contentPane = getContentPane();
    		contentPane.add(listPane, BorderLayout.CENTER);
    		contentPane.add(buttonPane, BorderLayout.PAGE_END);

    		//Initialize values.
    		setValue(initialValue);
    		pack();
    		setLocationRelativeTo(locationComp);
    	}
    	public void actionPerformed(ActionEvent e) {
    		if ("Select".equals(e.getActionCommand())) {
    			WhisperWindow.value = (String)(list.getSelectedValue());
    		}
    		WhisperWindow.choosedName.setVisible(false);
    	}
}