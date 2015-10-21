import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class ManagerClient {
	private Font fo = new Font("Consolas", Font.BOLD, 28);

	private static final int width = 510;
	private static final int height = 600;

	private static final int MAIN_SERVER_PORT = 9001;

	private String client_ID;

	private String clientIPaddress;
	private String target_ID;

	// ������ ���ϴ� ��ǲ �ƿ�ǲ ��Ʈ��
	private BufferedReader in;
	private PrintWriter out;

	private JFrame frame = new JFrame("Comic book rental system");

	private JPanel searchPanelForMemeber = new JPanel();
	private JTextField userTextForMember = new JTextField();
	private JButton searchEnterBtnForMember = new JButton();
	private JTextField memberSearchTxt = new JTextField();

	private JPanel searchPanelForBook = new JPanel();
	private JTextField userTextForBook = new JTextField();
	private JButton searchEnterBtnForBook = new JButton();
	private JTextField bookSearchTxt = new JTextField();

	private JPanel staticPanel = new JPanel();
	private JTextField staticTxt = new JTextField();
	private JButton genreBtn = new JButton(); // �帣��
	private JButton genderBtn = new JButton(); // ����
	private JButton ageBtn = new JButton(); // ���̺�
	private JButton writerBtn = new JButton(); // �۰���

	private JTextField listTextField = new JTextField(40);
	private JPanel searchResultListPanel = new JPanel();
	private JList<String> searchList;

	// �α��� â
	private JFrame loginFrame;

	private JTextField idField;
	private JPasswordField pwField;

	// ȸ�� ���� â
	private JFrame registerFrame;

	private JTextField register_idField;
	private JTextField register_nameField;
	private JTextField register_pwField;

	public ManagerClient() {
		frame.setLayout(null);
		frame.setBounds(300, 100, width - 10, height - 10 + 23);
		frame.setResizable(false);

		// x��ư ������ ��
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// ������ ���� �ÿ��� MainSever���� ���Ḧ �˸���.
				out.println("LOGOUT" + client_ID);
				System.exit(1);
			}
		});

		// ���� : �޴� ����
		JMenu menu = new JMenu("Menu");
		JMenuItem m[] = new JMenuItem[3];

		// å �߰� / ���� �޴�
		m[0] = new JMenuItem("Add/Delete book");
		m[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//
				out.println("CHANGE_CHAR " + client_ID);
			}
		});
		menu.add(m[0]);

		// ȸ�� �߰� �޴�
		m[1] = new JMenuItem("Add customer");
		m[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//
				// out.println("");
			}
		});
		menu.add(m[1]);

		// �α׾ƿ� �޴�
		m[2] = new JMenuItem("Logout");
		m[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ������ ���� �ÿ��� MainSever���� ���� ���Ḧ �˸���.
				out.println("LOGOUT," + client_ID);
				System.exit(1);
			}
		});
		menu.add(m[2]);

		JMenuBar mBar = new JMenuBar();
		mBar.add(menu);
		frame.setJMenuBar(mBar);
		// �� : �޴� ����


		// �� ���� ���δ� ����ȭ�鿡�� ���� 3���� �г�(��� �˻�, å �˻�, ���)
		int inputPanelHeight = 100;
		int inputPanelWidth = 500;

		// ȸ�� �˻� �г� ����
		// /////////////////////////////////////////////////////////////
		searchPanelForMemeber
				.setBounds(0, 0, inputPanelWidth, inputPanelHeight);
		searchPanelForMemeber.setLayout(null);
		searchPanelForMemeber.setBackground(new Color(230, 230, 230));

		// �ؽ�Ʈ : Book search
		memberSearchTxt.setEditable(false);
		memberSearchTxt.setText("Member search");
		memberSearchTxt.setForeground(Color.black);
		memberSearchTxt.setFont(new Font(memberSearchTxt.getFont().getName(),
				Font.BOLD, 16));
		memberSearchTxt.setBounds(0, 0, width, 25);
		searchPanelForMemeber.add(memberSearchTxt);

		// �˻� ��ǲ â
		userTextForMember.setBounds(5 + 15, 50 + 7, 428 - 15 - 15, 25);
		userTextForMember.setBackground(Color.white);
		// userText.setBorder(null);
		searchPanelForMemeber.add(userTextForMember);

		searchEnterBtnForMember.setBounds(438 - 15, 50 + 7, 40, 25);
		//searchEnterBtnForMember.setBorder(null);
		searchEnterBtnForMember.setBackground(new Color(219, 252, 182));
		// �˻� ���� ��ư
		// frame.getRootPane().setDefaultButton(searchEnterBtnForMember);
		searchEnterBtnForMember.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String s = userTextForMember.getText();
				if (s.length() != 0) {
					if (out != null) {
						out.println("ALLMESSAGE " + client_ID + ": " + s);
					}
				}
				// display(clientName + ": " + s);
				userTextForMember.setText("");
			}
		});
		searchPanelForMemeber.add(searchEnterBtnForMember);

		frame.add(searchPanelForMemeber);
		// ȸ�� �˻� �г� ��
		// /////////////////////////////////////////////////////////////

		// å �˻� �г� ����
		// /////////////////////////////////////////////////////////////
		searchPanelForBook.setBounds(0, inputPanelHeight/* �� �г� ���̸�ŭ �̵� */,
				inputPanelWidth, inputPanelHeight);
		searchPanelForBook.setLayout(null);
		searchPanelForBook.setBackground(Color.white);

		// �ؽ�Ʈ : Book search
		bookSearchTxt.setEditable(false);
		bookSearchTxt.setText("Book search");
		bookSearchTxt.setForeground(Color.black);
		bookSearchTxt.setFont(new Font(bookSearchTxt.getFont().getName(),
				Font.BOLD, 16));
		bookSearchTxt.setBounds(0, 0, width, 25);
		searchPanelForBook.add(bookSearchTxt);

		// �˻� ��ǲ â
		userTextForBook.setBounds(5 + 15, 50 + 7, 428 - 15 - 15, 25);
		userTextForBook.setBackground(new Color(230, 230, 230));
		// userText.setBorder(null);
		searchPanelForBook.add(userTextForBook);

		searchEnterBtnForBook.setBounds(438 - 15, 50 + 7, 40, 25);
		//searchEnterBtnForBook.setBorder(null);
		searchEnterBtnForBook.setBackground(new Color(193, 249, 132));
		// �˻� ���� ��ư
		searchEnterBtnForBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String s = userTextForBook.getText();
				if (s.length() != 0) {
					if (out != null) {
						out.println("ALLMESSAGE " + client_ID + ": " + s);
					}
				}
				// display(clientName + ": " + s);
				userTextForBook.setText("");
			}
		});
		searchPanelForBook.add(searchEnterBtnForBook);

		frame.add(searchPanelForBook);
		// å �˻� �г� ��
		// /////////////////////////////////////////////////////////////

		// ��� �г� ����
		// /////////////////////////////////////////////////////////////
		staticPanel.setBounds(0, inputPanelHeight * 2, inputPanelWidth,
				inputPanelHeight);
		staticPanel.setLayout(null);
		staticPanel.setBackground(new Color(230, 230, 230));

		// �ؽ�Ʈ : static
		staticTxt.setEditable(false);
		staticTxt.setText("Statics");
		staticTxt.setForeground(Color.black);
		staticTxt
				.setFont(new Font(staticTxt.getFont().getName(), Font.BOLD, 16));
		staticTxt.setBounds(0, 0, width, 25);
		staticPanel.add(staticTxt);

		ageBtn.setBounds(35 , 40, 80, 45);
		//ageBtn.setBorder(null);
		ageBtn.setBackground(new Color(219, 252, 182));
		ageBtn.setText("Age");
		ageBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (out != null) {
					out.println("ALLMESSAGE " + client_ID + ": ");
				}
			}
		});
		staticPanel.add(ageBtn);
		
		genderBtn.setBounds(35 + (35 + 80)  , 40, 80, 45);
		//genderBtn.setBorder(null);
		genderBtn.setBackground(new Color(193, 249, 132));
		genderBtn.setText("Gender");
		genderBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (out != null) {
					out.println("ALLMESSAGE " + client_ID + ": ");
				}
			}
		});
		staticPanel.add(genderBtn);
		

		writerBtn.setBounds(35 + (35 + 80)  + (35 + 80) , 40, 80, 45);
		//writerBtn.setBorder(null);
		writerBtn.setBackground(new Color(219, 252, 182));
		writerBtn.setText("Writer");
		writerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (out != null) {
					out.println("ALLMESSAGE " + client_ID + ": ");
				}
			}
		});
		staticPanel.add(writerBtn);

		genreBtn.setBounds(35 + (35 + 80)  + (35 + 80)   + (35 + 80), 40, 80, 45);
		//genreBtn.setBorder(null);
		genreBtn.setBackground(new Color(193, 249, 132));
		genreBtn.setText("Genre");
		genreBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (out != null) {
					out.println("ALLMESSAGE " + client_ID + ": ");
				}
			}
		});
		staticPanel.add(genreBtn);

		frame.add(staticPanel);
		// ��� �г� ��
		// /////////////////////////////////////////////////////////////

		// �˻� ��� ����Ʈ �г� ����
		// /////////////////////////////////////////////////////////////
		searchResultListPanel.setLayout(null);
		searchResultListPanel.setBounds(0, inputPanelHeight * 3,
				inputPanelWidth, height - inputPanelHeight * 2/* �˻� �г� �ΰ� */);
		searchResultListPanel.setBorder(null);

		listTextField.setEditable(false);
		listTextField.setText("Searched result");
		listTextField.setForeground(Color.black);
		listTextField.setFont(new Font(listTextField.getFont().getName(),
				Font.BOLD, 16));
		listTextField.setBounds(0, 0, width, 30);
		searchResultListPanel.add(listTextField);
		//

		// ����Ʈ�� ������ ��
		searchList = new JList<String>();
		searchList.setBackground(Color.white);
		searchList.setBorder(null);
		searchList.addMouseListener(new MouseAdapter() {
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

						// [ "GET_INFO" protocol : ���� ����Ʈ�� �ִ� �� Ŭ�� ���� ��
						// �������� �� ������ �����ֱ� ��û�Ѵ�.]
						out.println("GET_INFO " + target_ID);

						// �����ʿ����� SEND_INFO ���������� ���ؼ� ������ �����ش�.
					}
				}
			}
		});
		JScrollPane scrollPane1 = new JScrollPane(searchList);
		scrollPane1.setBounds(-1, 30 - 1, width, 230);
		searchResultListPanel.add(scrollPane1);

		frame.add(searchResultListPanel);
		// �˻� ��� ����Ʈ �г� ��
		// /////////////////////////////////////////////////////////////

		// IP�ּ� ��� ���� (��� �ʿ� ����)
		try {
			clientIPaddress = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Local Host:");
			System.out.println("\t" + clientIPaddress);
		} catch (UnknownHostException e) {
			System.out.println("Unable to determine this host's address");
		}
	}

	/*
	 * �����κ��� ������� �޽����� ���� ó���� �ϴ� �߿��� �κ��̴�. ������ Ŭ���̾�Ʈ�� �޽����θ� ��ȭ�� �Ѵ�. ������ ����,
	 * in.readLine()���� � ���������� �޾Ҵٸ�, ���������� �䱸�ϴ� �ٸ� out.println()�� �̿�, ������ �ٽ�
	 * �������ָ� �ȴ�.
	 */
	private void run() throws IOException {
		// [ connect to 'Main server' ]
		// ���� ������ �����ϰ� processing loop�� ����.
		String serverAddress = getServerAddress();
		Socket socket = new Socket(serverAddress, MAIN_SERVER_PORT);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		// [ processing loop ]
		while (true) {
			String inputLine = in.readLine();
			System.out.println(inputLine);

			if (inputLine.startsWith("SUBMIT_ID_PW")) {
				// [ "SUBMIT_ID_PW" protocol..]

				// login frame �� ����.
				// login �޼ҵ�� �α���â�� �������
				// ��ư�� ������ Main server ������ ���̵�� �н����带 ������(TRY_LOGIN)
				login();

			} else if (inputLine.startsWith("LOGIN_OK")) {
				// [ "LOGIN_OK" protocol.. : �α��� ������ �޾Ҵٴ� ��. ������ ���� �Ƿ���.]

				frame.setVisible(true);

				// �α� â�� â�� �ݴ´�.
				idField.setText("");
				pwField.setText("");
				loginFrame.dispose();

			} else if (inputLine.equals("AlREADY_LOGON")) {
				alert("�̹� ���� �� �Դϴ�.", loginFrame);

			} else if (inputLine.equals("LOGIN_CANCEL")) {
				alert("���̵�� ��й�ȣ�� �ٽ� �Է��ϼ���.", loginFrame);

			} else if (inputLine.equals("REGISTER_OK")) {
				alert("ȸ�������� ���������� �̷�� �����ϴ�.", registerFrame);

			} else if (inputLine.startsWith("UPDATE_LIST")) {
				// [ "UPDATE LIST" protocol : when a client who is connected
				// with Main Server
				// in or out this protocol send to all clients. ]
				String tmp = inputLine.substring(12);
				String t[] = tmp.split(",");
				ArrayList<String> tmpList = new ArrayList<>();
				for (int i = 0; i < t.length; i++)
					tmpList.add(t[i]);

				Vector<String> labels = new Vector<String>();
				for (int i = 0; i < tmpList.size(); i++) {
					if (tmpList.get(i).equals(client_ID))
						labels.addElement(tmpList.get(i) + " < Me >");
					else
						labels.addElement(tmpList.get(i));
				}
				searchList.setListData(labels);

			} else if (inputLine.startsWith("SEND_INFO")) {
				// [ "SEND_INFO" protocol : ���� ����Ʈ�� �ִ� ����� �ѹ� Ŭ��������,
				// ������ �װ��� ������ �����ش�]

				// ���� ���� ������ �����κ��� �޾Ƽ� �����ش�. (setText�̿�)
			}

		}
	}

	// yes = 0, no = 1
	private int yesOrNo() {
		return JOptionPane.showConfirmDialog(frame, "�ʴ뿡 ���Ͻðڽ��ϱ�?", "confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	}

	private int alert(String inform, JFrame parent) {
		return JOptionPane.showConfirmDialog(parent, inform, "warning",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
	}

	private String getServerAddress() {
		return JOptionPane.showInputDialog(frame,
				"Enter IP Address of the Server:", "Welcome to the Chatter",
				JOptionPane.QUESTION_MESSAGE);
	}

	private void login() {
		loginFrame = new JFrame("Login");
		loginFrame.setBounds(450, 200, 350 + 8, 260 + 30);
		loginFrame.setResizable(false);
		loginFrame.setLayout(null);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton loginBtn;
		JButton registerBtn;
		JLabel idLabel;
		JLabel pwLabel;

		JPanel outerPanel = new JPanel();
		outerPanel.setBackground(Color.white);
		outerPanel.setLayout(null);
		outerPanel.setBounds(0, 0, 350, 260);

		// �Ķ��� ����� �г�
		JPanel innerPanel = new JPanel();

		idLabel = new JLabel("ID   ", JLabel.RIGHT);
		idLabel.setBounds(40, 40, 30, 25);
		innerPanel.add(idLabel);

		pwLabel = new JLabel("PW  ", JLabel.RIGHT);
		pwLabel.setBounds(40, 75, 30, 25);
		innerPanel.add(pwLabel);

		innerPanel.setBounds(10, 120, 330, 130);
		innerPanel.setLayout(null);
		innerPanel.setBackground(new Color(93, 128, 155));

		idField = new JTextField();
		idField.setBounds(75, 40, 100, 25);
		innerPanel.add(idField);

		pwField = new JPasswordField();
		pwField.setBounds(75, 75, 100, 25);
		innerPanel.add(pwField);

		loginFrame.add(innerPanel);

		// login button
		loginBtn = new JButton("Login");
		loginBtn.setFocusable(false);
		loginFrame.getRootPane().setDefaultButton(loginBtn);
		loginBtn.setBackground(new Color(253, 169, 51));
		loginBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// ���� ���̵�� ��й�ȣ�� ������ ���ؼ� ������ ������. (out �̿�)
				if (!idField.getText().equals("")
						&& !pwField.getText().equals(""))
					out.println("TRY_LOGIN " + idField.getText() + " "
							+ pwField.getText());
				else if (idField.getText().equals(""))
					alert("id�� �Է��ϼ���!", loginFrame);
				else if (pwField.getText().equals(""))
					alert("pw�� �Է��ϼ���!", loginFrame);
			}
		});
		loginBtn.setBounds(185, 40, 100, 26);
		innerPanel.add(loginBtn);

		// register button
		registerBtn = new JButton("Register");
		registerBtn.setFocusable(false);
		registerBtn.setBackground(new Color(253, 169, 51));
		registerBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// register â�� ����ִ� �޼ҵ� �����Ѵ�.
				register();
			}
		});
		registerBtn.setBounds(185, 75, 100, 26);
		innerPanel.add(registerBtn);

		outerPanel.add(innerPanel);

		// add Image
		JPanel loginImg = new LoginImg();
		outerPanel.add(loginImg);

		loginFrame.add(outerPanel);
		loginFrame.setVisible(true);
	}

	public void register() {

		registerFrame = new JFrame("ȸ�� ����");

		registerFrame.setLayout(null);
		registerFrame.setBounds(loginFrame.getLocationOnScreen().x,
				loginFrame.getLocationOnScreen().y, 280 + 8, 250 + 30);

		register_idField = new JTextField();
		register_nameField = new JTextField();
		register_pwField = new JTextField();

		JLabel idLabel = new JLabel("I D     ", JLabel.RIGHT);
		JLabel nameLabel = new JLabel("NAME     ", JLabel.RIGHT);
		JLabel pwLabel = new JLabel("PW   ", JLabel.RIGHT);

		JButton submitBtn = new JButton("submit");
		submitBtn.setBackground(new Color(253, 169, 51));
		submitBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// ������ �˷��ش�. �����ϱ�
				out.println("TRY_REGISTER " + register_idField.getText().trim()
						+ " " + register_nameField.getText().trim() + " "
						+ register_pwField.getText().trim());

				register_idField.setText("");
				register_nameField.setText("");
				register_pwField.setText("");
				registerFrame.dispose();
			}
		});

		JButton cancelBtn = new JButton("cancel");
		cancelBtn.setBackground(new Color(253, 169, 51));
		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				register_idField.setText("");
				register_nameField.setText("");
				register_pwField.setText("");
				registerFrame.dispose();
			}
		});

		//
		JPanel regPanel = new JPanel();
		regPanel.setBounds(0, 0, 280, 40);
		regPanel.setLayout(null);
		regPanel.setBackground(new Color(253, 169, 51));

		JLabel regLabel = new JLabel("REGISTER", JLabel.RIGHT);
		regLabel.setBounds(55, 10, 100, 15);

		regPanel.add(regLabel);
		registerFrame.add(regPanel);
		//

		//
		JPanel innerPanel = new JPanel();
		innerPanel.setBounds(0, 40, 280, 220);
		innerPanel.setLayout(null);
		innerPanel.setBackground(Color.white);

		idLabel.setBounds(20, 40, 30, 25);
		nameLabel.setBounds(19, 75, 50, 25);
		pwLabel.setBounds(20, 110, 30, 25);

		register_idField.setBounds(80, 40, 100, 25);
		register_nameField.setBounds(80, 75, 100, 25);
		register_pwField.setBounds(80, 110, 100, 25);

		submitBtn.setBounds(30, 155, 100, 26);
		cancelBtn.setBounds(150, 155, 100, 26);

		innerPanel.add(idLabel);
		innerPanel.add(nameLabel);
		innerPanel.add(pwLabel);

		innerPanel.add(register_idField);
		innerPanel.add(register_nameField);
		innerPanel.add(register_pwField);

		innerPanel.add(submitBtn);
		innerPanel.add(cancelBtn);

		registerFrame.add(innerPanel);

		registerFrame.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		ManagerClient client = new ManagerClient();
		client.run();
	}

}