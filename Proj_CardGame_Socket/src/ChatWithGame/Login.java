package ChatWithGame;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Random;

import javax.swing.*;

public class Login extends JFrame implements ActionListener {
	private final String ServerIPAddress;
	private final int ServerPortNumber;
	private BufferedReader inFromServer;
	private PrintStream outToServer;
	private Socket clientSocket;

	private String ID;
	private String PassWord;

	private JPanel loginPanel;
	private JButton loginButton;
	private JButton JoinButton;
	private JTextField IDField;
	private JPasswordField PWField;

	public Login(){
		setTitle("Login");
		ServerIPAddress = getServerAddress();
		ServerPortNumber = 4343;	//포트넘버는 미리 알고있는 것으로 가정...

		//Setting outer panel
		loginPanel = new JPanel(){
			public void paintComponent(Graphics g) {
				ImageIcon BgIcon = new ImageIcon("./ChattingData/img/lgbg.png");
				g.drawImage(BgIcon.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		loginPanel.setLayout(null);
		loginPanel.setBounds(0, 0, 446, 360);

		//Setting panel for login input's background
		JPanel lgbgPanel = new JPanel();
		lgbgPanel.setBounds(22+70, 180, 240, 100);
		float[] hsb = new float[3];  
		hsb = Color.RGBtoHSB(75, 77, 77, hsb);
		lgbgPanel.setBackground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));

		//Setting for login labels.
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(2, 1));
		JLabel lab1 = new JLabel("ID: ",JTextField.RIGHT);
		lab1.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
		lab1.setForeground(Color.white);
		labelPanel.add(lab1);
		JLabel lab2 = new JLabel("Password: ",JTextField.RIGHT);
		lab2.setFont(new Font("Microsoft JhengHei", Font.BOLD, 13));
		lab2.setForeground(Color.white);
		labelPanel.add(lab2);
		labelPanel.setBounds(40+70, 195, 70, 70);
		labelPanel.setBackground(new Color(0f,0f,0f,.0f));

		//Setting for login text fields.
		JPanel FieldPanel = new JPanel();
		FieldPanel.setLayout(new GridLayout(2,1,0,5));
		IDField = new JTextField();
		PWField = new JPasswordField();
		FieldPanel.add(IDField);
		FieldPanel.add(PWField);
		FieldPanel.setBounds(120+70, 195, 120, 70);
		FieldPanel.setBackground(new Color(0f,0f,0f,.0f));

		//Setting for login and join buttons
		loginButton = new JButton("Login");
		JoinButton = new JButton("Join");
		loginButton.setBounds(120, 285, 70, 30);
		JoinButton.setBounds(240, 285, 70, 30);
		loginButton.addActionListener(this);
		JoinButton.addActionListener(this);
		loginButton.setBackground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
		loginButton.setBorderPainted(false);
		JoinButton.setBorderPainted(false);
		loginButton.setForeground(Color.white);
		loginButton.setFocusable(false);
		JoinButton.setFocusable(false);
		JoinButton.setForeground(Color.white);
		JoinButton.setBackground(Color.getHSBColor(hsb[0], hsb[1], hsb[2]));
		getRootPane().setDefaultButton(loginButton);

		//Add all components
		loginPanel.add(labelPanel);
		loginPanel.add(FieldPanel);
		loginPanel.add(lgbgPanel);
		loginPanel.add(loginButton);
		loginPanel.add(JoinButton);

		//Add panel's to outer frame.
		add(loginPanel);

		//frame setting.
		setBounds(10, 38, 446, 360);
		//setPreferredSize(new Dimension(300,250));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	private String getServerAddress(){
		return JOptionPane.showInputDialog(this,
				"Enter IP Address of the Server:",
				"Welcome to the Chatter",
				JOptionPane.QUESTION_MESSAGE);
	}
	public void actionPerformed(ActionEvent ev){
		if(ev.getActionCommand().equals("Login")){
			String ID = IDField.getText();
			if(ID.equals("") || PWField.getText().equals("")){
				//show dialog. 값을 입력하지 않았소!!!
				JOptionPane.showMessageDialog(this,"You didn't write anything!.","ERROR!!!",1);
			}
			try{
				if(LoginProcess(ID, PWField.getText())){
					//(String ID,BufferedReader in,PrintStream out,Socket socket
					(new Thread(new ChatClient(ServerIPAddress,ServerPortNumber,ID))).start();
					dispose();
				}
			} catch (IOException e){e.printStackTrace();}
		}else if(ev.getActionCommand().equals("Join")){
			(new Thread(new Enrollment(ServerIPAddress, ServerPortNumber))).start();
		}
	}
	public boolean LoginProcess(final String id, final String pw) throws IOException{
		boolean LoginOK=false;
		clientSocket = new Socket(ServerIPAddress, ServerPortNumber);
		try{
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer = new PrintStream(clientSocket.getOutputStream());

			for(;;){
				String line = inFromServer.readLine();
				if(line.startsWith("SUBMITINFO"))
					outToServer.println(id+"//"+pw);
				else if(line.startsWith("REJECTED")){
					//다시 아이디랑 비번 입력받게해야함...
					if(line.substring(8).equals("WRONGID")){
						//show dialog
						JOptionPane.showMessageDialog(this,"There are no ID you entered!","ERROR!!!",1);
						outToServer.println("SORRY");
						break;
					}else if(line.substring(8).equals("WRONGPW")){
						//show dialog
						JOptionPane.showMessageDialog(this,"The wrong password!!","ERROR!!!",1);
						outToServer.println("SORRY");
						break;
					}        			
				}else if(line.startsWith("ACCEPTED")){
					LoginOK=true;
					int FilePort = (new Random()).nextInt(20000)+35000;  //range to 36000~56000
            		new Thread(new Reciever(FilePort)).start();
            		
            		//	port//server directory//ID
            		String dest = (new File("./ChattingData/userimages/")).getAbsolutePath()+"\\";
            		outToServer.println("LOGINFILEOKAY"+FilePort+"<!!>"+dest+"<!!>"+"D:/SUTTA/UserImg/"+id+".png<!!>"+id);
            		String imageReply = inFromServer.readLine();
            		if(imageReply.equals("OK")){
            			try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
            		}
					break;
				}
			}
		}catch(IOException e){}
		finally{
			inFromServer.close();
			outToServer.close();
			clientSocket.close();
		}
		return LoginOK;
	}
	
	public static void main(String[] args){
		new Login();
	}
}
