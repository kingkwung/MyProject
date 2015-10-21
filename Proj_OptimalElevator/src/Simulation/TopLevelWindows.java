package Simulation;

/*
 *	This Class is made by 홍정구
 *	Date : 2001.7.9
 */

/**
 * 전체적인 view와 이벤트 처리등을 해놓은 클래스이다.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class TopLevelWindows {
	// Implementation
	protected SimulBox simBox;
	private JFrame frm = new JFrame("Elevator Simulator");
	private JPanel pnWest = new JPanel();
	private JPanel pnNorth = new JPanel();
	private JPanel pnSouth = new JPanel();
	private JButton[] btnFloor;
	private JComboBox[] cmbFloor;
	private static int countFloor;
	private static int capacityElevator;
	private JTextField txtCntFloor, txtCptElevator, txtCntElevator;
	private JLabel lblCntFloor, lblCptElevator, lblCntElevator;
	private JButton btnStart, btnClose;
	private Container contentF;
	private static boolean simOn = false;
	private JPanel pnTemp = new JPanel();

	// Selector
	public boolean getSimOn() {
		return simOn;
	}

	// Operator
	private Object makeComboItem(final int item) {
		return new Integer(item);  
	}

	/**
	 * 화면에 보여지는 버튼, 콤보박스 등을 생서하고 초기값등을 정해준다.
	 */
	public void init() {
		txtCntFloor = new JTextField("         ");
		txtCptElevator = new JTextField("          ");
		txtCntElevator = new JTextField("          ");
		lblCntFloor = new JLabel("건물의 총층수");
		lblCptElevator = new JLabel("엘리베이터 정원");
		lblCntElevator = new JLabel("엘리베이터 현재인원");
		btnStart = new JButton("START");
		btnClose = new JButton("CLOSE");
		txtCntElevator.setEnabled(false);

		btnStart.addActionListener(new StartSimulationListener());
		btnClose.addActionListener(new CloseSimulationListener());

		frm.setSize(800, 700);
		frm.setLocation(100, 10);

	}

	/**
	 * 레이아웃을 정하고 버튼등을 판넬에 추가하고 보여준다.
	 */
	public void operation() {
		contentF = frm.getContentPane();
		contentF.setLayout(new BorderLayout());
		pnNorth.setLayout(new FlowLayout());
		pnSouth.setLayout(new GridLayout(1, 2));
		pnNorth.add(lblCntFloor);
		pnNorth.add(txtCntFloor);
		pnNorth.add(lblCptElevator);
		pnNorth.add(txtCptElevator);

		pnSouth.add(btnStart);
		pnSouth.add(btnClose);

		contentF.add(pnNorth, BorderLayout.NORTH);
		contentF.add(pnSouth, BorderLayout.SOUTH);
		frm.setVisible(true);
	}

	/**
	 * START버튼을 클릭하면 실행된다.
	 */
	private void startButtonClick() {
		if (Integer.parseInt(txtCntFloor.getText().trim()) >= 2
				&& Integer.parseInt(txtCptElevator.getText().trim()) >= 1) {
			try {
				countFloor = Integer.parseInt(txtCntFloor.getText().trim());
				simBox = new SimulBox(countFloor,
						Integer.parseInt(txtCptElevator.getText().trim()));
				contentF.add(simBox, BorderLayout.CENTER);
				simBox.setSize(new Dimension(670, 610));
			} catch (NumberFormatException pe) {
			}

			simBox.fixFloor();
			simBox.setElevatorSize(
					simBox.floor[0].getPx() + simBox.floor[0].getWidth() + 35,
					simBox.floor[0].getPy(), simBox.getWidth() / 5,
					simBox.floor[0].getHeight());
			btnFloor = new JButton[countFloor];
			cmbFloor = new JComboBox[countFloor];

			for (int i = 0; i < countFloor; i++) {
				btnFloor[i] = new JButton((countFloor - i) + " 층");
				cmbFloor[i] = new JComboBox();
			}

			for (int i = 0; i < countFloor; i++){
				for (int j = 0; j < countFloor; j++)
					if (i != j) 
						cmbFloor[countFloor - (i + 1)].addItem((makeComboItem(j + 1)));
			}
			for (int i = 0; i < countFloor; i++)
				btnFloor[i].addActionListener(new CreatePersonButtonListener(
						countFloor - i, i));

			pnWest.setLayout(new GridLayout(countFloor, 2));
			for (int i = 0; i < countFloor; i++) {
				pnWest.add(btnFloor[i]);
				pnWest.add(cmbFloor[i]);
			}
			contentF.add(pnWest, BorderLayout.WEST);

			contentF.validate();
			simOn = true;
			simBox.revalidate();
			frm.repaint();
			btnStart.setEnabled(false);
			txtCntFloor.setEnabled(false);
			txtCptElevator.setEnabled(false);

		}
	}

	/**
	 * 사람생성버튼을 클릭할때의 리스너 내부 클래스
	 */
	private class CreatePersonButtonListener implements ActionListener {
		private int cFloor, index;

		public CreatePersonButtonListener(int c, int d) {
			cFloor = c;
			index = d;
		}

		public void actionPerformed(ActionEvent e) {
			simBox.createPerson(cFloor,
					((Integer) cmbFloor[index].getSelectedItem()).intValue());
		}
	}

	/**
	 * START버튼을 클릭할때의 리스너 내부 클래스
	 */
	private class StartSimulationListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			startButtonClick();
		}
	}

	/**
	 * CLOSE버튼을 클릭하면때의 리스너 내부 클래스
	 */
	private class CloseSimulationListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}