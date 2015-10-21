import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class MemberInfoFrame {

	private Font fo = new Font("Consolas", Font.BOLD, 28);

	private static final int width = 510;
	private static final int height = 400;

	private static final int MAIN_SERVER_PORT = 9001;

	private String client_ID;

	private String clientIPaddress;
	private String target_ID;

	private BufferedReader in;
	private PrintWriter out;

	private JFrame frame = new JFrame("Member Frame");

	private JTextField searchBookByIdTxt = new JTextField();

	private JTextField infoName = new JTextField();
	private JTextField infoPhone = new JTextField();
	private JTextField infoGender = new JTextField();
	private JTextField infoAge = new JTextField();
	private JTextField infoPoint = new JTextField();
	private JTextField infoAddr = new JTextField();

	private JTextField infoNameTxt = new JTextField();
	private JTextField infoPhTxt = new JTextField();
	private JTextField infoGenderTxt = new JTextField();
	private JTextField infoAgeTxt = new JTextField();
	private JTextField infoPointTxt = new JTextField();
	private JTextField infoAddTxt = new JTextField();

	private JTextField textField = new JTextField(40);
	private JPanel bookListPanel = new JPanel();

	private JPanel infoPanel = new JPanel();
	private JTextField memberSearchTxt = new JTextField();

	private JButton searchthebooknum = new JButton();
	private JButton infoDelBtn = new JButton();
	private JButton infoModifyBtn = new JButton();
	
	JList<String> rentalList;

	int panelHeight = 400;
	int infoPanelWidth = 260;

	public MemberInfoFrame() {
		frame.setLayout(null);
		frame.setBounds(400, 100, width - 10 - 35, height - 10 + 23);
		frame.setResizable(false);

		infoPanel.setBounds(0, 0, infoPanelWidth, panelHeight);
		infoPanel.setLayout(null);
		infoPanel.setBackground(new Color(230, 230, 230));

		// 택스트
		memberSearchTxt.setEditable(false);
		memberSearchTxt.setBorder(null);
		memberSearchTxt.setText("Member Information");
		memberSearchTxt.setForeground(Color.black);
		memberSearchTxt.setFont(new Font(memberSearchTxt.getFont().getName(),
				Font.BOLD, 16));
		memberSearchTxt.setBounds(0, 0, width, 25);
		infoPanel.add(memberSearchTxt);
		frame.add(infoPanel);
		// searchPanelForMemeber.setVisible(true);

		// 정보 보여지는 것
		infoName.setEditable(false);
		infoName.setText("Name");
		// infoname.setForeground(Color.black);
		infoName.setFont(new Font(infoName.getFont().getName(), Font.BOLD, 16));
		infoName.setBounds(20, 30, 80, 25);
		infoPanel.add(infoName);

		infoPhone.setEditable(false);
		infoPhone.setText("Phone");
		// infoph.setForeground(Color.black);
		infoPhone
				.setFont(new Font(infoPhone.getFont().getName(), Font.BOLD, 16));
		infoPhone.setBounds(20, 60, 80, 25);
		infoPanel.add(infoPhone);

		infoGender.setEditable(false);
		infoGender.setText("Gender");
		// infogender.setForeground(Color.black);
		infoGender.setFont(new Font(infoGender.getFont().getName(), Font.BOLD,
				16));
		infoGender.setBounds(20, 90, 80, 25);
		infoPanel.add(infoGender);

		infoAge.setEditable(false);
		infoAge.setText("Age");
		// infoage.setForeground(Color.black);
		infoAge.setFont(new Font(infoAge.getFont().getName(), Font.BOLD, 16));
		infoAge.setBounds(20, 120, 80, 25);
		infoPanel.add(infoAge);

		infoAddr.setEditable(false);
		infoAddr.setText("Address");
		// infopoint.setForeground(Color.black);
		infoAddr.setFont(new Font(infoAddr.getFont().getName(), Font.BOLD, 16));
		infoAddr.setBounds(20, 150, 80, 25);
		infoPanel.add(infoAddr);

		infoPoint.setEditable(false);
		infoPoint.setText("Mileage");
		// infopoint.setForeground(Color.black);
		infoPoint
				.setFont(new Font(infoPoint.getFont().getName(), Font.BOLD, 16));
		infoPoint.setBounds(20, 180, 80, 25);
		infoPanel.add(infoPoint);

		// 정보 setting
		infoNameTxt.setEditable(false);
		infoNameTxt.setText("Choi min young");
		// infoname.setForeground(Color.black);
		infoNameTxt.setFont(new Font(infoNameTxt.getFont().getName(),
				Font.BOLD, 16));
		infoNameTxt.setBounds(120, 30, 120, 25);
		infoPanel.add(infoNameTxt);

		infoPhTxt.setEditable(false);
		infoPhTxt.setText("010");
		// infopht.setForeground(Color.black);
		infoPhTxt
				.setFont(new Font(infoPhTxt.getFont().getName(), Font.BOLD, 16));
		infoPhTxt.setBounds(120, 60, 120, 25);
		infoPanel.add(infoPhTxt);

		infoGenderTxt.setEditable(false);
		infoGenderTxt.setText("Woman");
		// infogendert.setForeground(Color.black);
		infoGenderTxt.setFont(new Font(infoGenderTxt.getFont().getName(),
				Font.BOLD, 16));
		infoGenderTxt.setBounds(120, 90, 120, 25);
		infoPanel.add(infoGenderTxt);

		infoAgeTxt.setEditable(false);
		infoAgeTxt.setText("23");
		// infoage.setForeground(Color.black);
		infoAgeTxt.setFont(new Font(infoAgeTxt.getFont().getName(), Font.BOLD,
				16));
		infoAgeTxt.setBounds(120, 120, 120, 25);
		infoPanel.add(infoAgeTxt);

		infoAddTxt.setEditable(false);
		infoAddTxt.setText("AAA BBB CCC");
		// infoaddt.setForeground(Color.black);
		infoAddTxt.setFont(new Font(infoAddTxt.getFont().getName(), Font.BOLD,
				16));
		infoAddTxt.setBounds(120, 150, 120, 25);
		infoPanel.add(infoAddTxt);

		infoPointTxt.setEditable(false);
		infoPointTxt.setText("0");
		// infopointt.setForeground(Color.black);
		infoPointTxt.setFont(new Font(infoPointTxt.getFont().getName(),
				Font.BOLD, 16));
		infoPointTxt.setBounds(120, 180, 120, 25);
		infoPanel.add(infoPointTxt);

		// 수정 삭제 버튼
		infoModifyBtn.setBounds(70, 180 + 25 + 10, 80, 30);
		infoModifyBtn.setBackground(new Color(219, 252, 182));
		infoModifyBtn.setText("modify");
		infoPanel.add(infoModifyBtn);

		infoDelBtn.setBounds(160, 180 + 25 + 10, 80, 30);
		infoDelBtn.setBackground(new Color(219, 252, 182));
		infoDelBtn.setText("remove");
		infoPanel.add(infoDelBtn);

		// Rental book list
		bookListPanel.setLayout(null);
		bookListPanel.setBounds(infoPanelWidth, 0, width - infoPanelWidth,
				panelHeight);
		bookListPanel.setBorder(null);

		textField.setEditable(false);
		textField.setText("Book rental list");
		textField.setBackground(new Color(230, 230, 230));
		textField.setBorder(null);
		textField.setForeground(Color.black);
		textField
				.setFont(new Font(textField.getFont().getName(), Font.BOLD, 16));
		textField.setBounds(0, 0, 200, 25);
		bookListPanel.add(textField);
		frame.add(bookListPanel);

		// 책 검색 텍스트
		searchBookByIdTxt.setBounds(10, 40, 110, 30);
		searchBookByIdTxt.setBackground(new Color(230, 230, 230));
		// searchBookByIdTxt.setBorder(null);
		bookListPanel.add(searchBookByIdTxt);

		// 책 추가 버튼
		searchthebooknum.setBounds(130, 40 - 1, 60, 30);
		searchthebooknum.setBackground(new Color(219, 252, 182));// new
																	// Color(193,
																	// 249, 132)
		searchthebooknum.setText("Add");
		// 책 검색 버튼의 리스너
		searchthebooknum.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String s = searchBookByIdTxt.getText();
				if (s.length() != 0) {
					// 책 검색해서 찾아준다.
				}
			}
		});
		bookListPanel.add(searchthebooknum);

		
		// 북 리스트 : 더블클릭할시 삭제되게 하자.
		rentalList = new JList<String>();
		rentalList.setBackground(Color.white);
		rentalList.setBorder(null);
		rentalList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				JList<String> theList = (JList<String>) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 1) {
					int index = theList.locationToIndex(mouseEvent.getPoint());
					// [ a Client who clicks the target client, should be the
					// p2p
					// server to communicate with each other ]
					if (index >= 0) {
						Object o = theList.getModel().getElementAt(index);
						target_ID = o.toString();

						// [ "GET_INFO" protocol : 내가 리스트에 있는 걸 클릭 했을 때
						// 서버에게 그 정보를 보내주길 요청한다.]
						out.println("GET_INFO " + target_ID);

						// 서버쪽에서는 SEND_INFO 프로토콜을 통해서 정보를 보내준다.
					}
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(rentalList);
		scrollPane.setBounds(10, 80, width - infoPanelWidth - 70, height - 105);
		bookListPanel.add(scrollPane);

		frame.setVisible(true);

	}

	public static void main(String args[]) {
		MemberInfoFrame mf = new MemberInfoFrame();
	}

}
