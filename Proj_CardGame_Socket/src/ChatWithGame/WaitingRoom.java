package ChatWithGame;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;

public class WaitingRoom extends JFrame implements ActionListener, Runnable {
	private final String ServerIP;
	private final int ServerPort;
	
    private static String HOST;
    private static int PORT;
    private String myID;
    private String myMoney;
    private String otherID;
    private String otherMoney;
    private String source=null;
    
    private boolean ready1;
    private boolean ready2;
    	
    private PrintWriter out;
    private Scanner in;
    private Thread thread;
    private Kind kind;
    
    //GUI Panels...
    private JPanel WaitingRoomPanel;

	private JPanel User1DataPanel;
	private JPanel User2DataPanel;
	private JPanel GamingPanel;
	
	private JLabel User1ImageLabel;
	private JLabel User2ImageLabel;
	private String source1;
	private String source2;
	
	private JTextField User1IDField;
	private JTextField User1MoneyField;
	private JTextField User2IDField;
	private JTextField User2MoneyField;
	private JTextField inputField;
	private JTextArea GamingArea;
	private JButton User1ReadyButton;
	private JButton User2ReadyButton;

	private Socket socket;
    public static enum Kind {
        Client(100, "name1"), Server(500, "name2");
        private int offset;
        private String activity;

        private Kind(int offset, String activity) {
            this.offset = offset;
            this.activity = activity;
        }
    }

    public WaitingRoom(final Kind theKind,String data,String otherData,String ipaddress,int port, String sIP, int sPort){
    	ServerIP = sIP;
    	ServerPort = sPort;
    	
    	//Info protocol format: ID//Game_Money
    	String[] UserInfo = data.split("//");
    	String[] OtherInfo = otherData.split("//");
    	
    	ready1 = ready2 = false;
    	
        this.kind = theKind;
        theKind.activity = myID = UserInfo[0];
        myMoney = UserInfo[1];
        otherID = OtherInfo[0];
        otherMoney = OtherInfo[1];
        HOST = ipaddress;
        PORT = port;
        
        setTitle(myID);
        initiateGUI();
        pack();
        
        //DefaultCaret caret = (DefaultCaret) GamingArea.getCaret();
        //caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        //display(kind.activity + " " + HOST + " on port " + PORT);
        thread = new Thread(this, theKind.toString());
        
        addWindowListener(new WindowAdapter() {// 창을 끄면 반대쪽에 bye 프로토콜 보내주고 쓰레드 멈춤
			public void windowClosing(WindowEvent e) {
				if(theKind == Kind.Server){
					out.println("DIECLIENT");
				}else{
					out.println("EXIT");
					new Thread(new AlamToServer(ServerIP, ServerPort, myID)).start();
					thread.stop();
					dispose();
				}
			}
		});

    }
    public void initiateGUI(){
    	source1 = "./ChattingData/userimages/"+ myID +".png";//"D:/SUTTA/UserImg/"+ myID +".png";
		source2 = "./ChattingData/userimages/"+ otherID +".png";//"D:/SUTTA/UserImg/"+ otherID +".png";
		
		WaitingRoomPanel = new JPanel(){
			public void paintComponent(Graphics g) {
				ImageIcon BgIcon = new ImageIcon("./ChattingData/img/bg(1vs1).png");
				g.drawImage(BgIcon.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		WaitingRoomPanel.setLayout(null);
		WaitingRoomPanel.setBounds(0, 0, 600, 360);
		//WaitingRoomPanel.setBackground(Color.red);
		
		User1DataPanel = new JPanel();
		ImageIcon userImage = new ImageIcon(source1);
		Image tmpImg = userImage.getImage();
        Image newImg = tmpImg.getScaledInstance(150, 180, java.awt.Image.SCALE_SMOOTH);
        userImage = new ImageIcon(newImg);
		User1ImageLabel = new JLabel(userImage);
		User1DataPanel.setBounds(0, -6, 150, 180);
		User1DataPanel.add(User1ImageLabel);
		
		User1IDField = new JTextField(myID);
		User1IDField.setBounds(0, 174, 150, 20);
		User1IDField.setEditable(false);
		User1MoneyField = new JTextField(getStringMoney(Integer.parseInt(myMoney)));
		User1MoneyField.setBounds(0, 194, 150, 20);
		User1MoneyField.setEditable(false);
		
		User2DataPanel = new JPanel();
		ImageIcon userImage2 = new ImageIcon(source2);
		Image tmpImg2 = userImage2.getImage();
        Image newImg2 = tmpImg2.getScaledInstance(150, 180, java.awt.Image.SCALE_SMOOTH);
        userImage2 = new ImageIcon(newImg2);
        User2ImageLabel = new JLabel(userImage2);
		User2DataPanel.setBounds(434, -6, 150, 180);
		User2DataPanel.add(User2ImageLabel);
		
		User2IDField = new JTextField(otherID);
		User2IDField.setBounds(434, 174, 150, 20);
		User2IDField.setEditable(false);
		User2MoneyField = new JTextField(getStringMoney(Integer.parseInt(otherMoney)));
		User2MoneyField.setBounds(434, 194, 150, 20);
		User2MoneyField.setEditable(false);
		
		GamingPanel = new JPanel();
		GamingPanel.setLayout(null);
		GamingPanel.setBounds(165, 0, 253, 200);
		GamingPanel.setBackground(Color.white);
		GamingArea = new JTextArea();
		GamingArea.setBounds(0, 0, 253, 170);
		GamingArea.setBackground(Color.lightGray);
		GamingArea.setEditable(false);
		GamingArea.setLineWrap(true);
		GamingArea.setWrapStyleWord(true);
		JScrollPane mPane = new JScrollPane(GamingArea);
    	mPane.setBounds(0, 0, 253, 170);
    	mPane.setBackground(Color.LIGHT_GRAY);
		inputField = new JTextField();
		inputField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	display(myID + ": " + inputField.getText());
            	out.println(inputField.getText());
            	inputField.setText("");
            }
        });
		inputField.setBounds(0, 170, 253, 30);
		GamingPanel.add(mPane);
		GamingPanel.add(inputField);
		
		User1ReadyButton = new JButton(new ImageIcon("./ChattingData/img/r1.png"));
		User1ReadyButton.setActionCommand("READY1");
		User1ReadyButton.setBorderPainted(false);
		User2ReadyButton = new JButton(new ImageIcon("./ChattingData/img/r2.png"));
		User2ReadyButton.setActionCommand("READY2");
		User2ReadyButton.setBorderPainted(false);
		User1ReadyButton.setBounds(90, 230, 180, 83);
		User2ReadyButton.setBounds(310, 230, 180, 83);
		User1ReadyButton.addActionListener(this);
		User2ReadyButton.addActionListener(this);
				
		WaitingRoomPanel.add(User1DataPanel);
		WaitingRoomPanel.add(User2DataPanel);
		WaitingRoomPanel.add(GamingPanel);
		WaitingRoomPanel.add(User1ReadyButton);
		WaitingRoomPanel.add(User1IDField);
		WaitingRoomPanel.add(User1MoneyField);
		WaitingRoomPanel.add(User2ReadyButton);
		WaitingRoomPanel.add(User2IDField);
		WaitingRoomPanel.add(User2MoneyField);
		
		//getContentPane().add(WaitingRoomPanel);
		JScrollPane scrollPane = new JScrollPane(WaitingRoomPanel);
        setContentPane(scrollPane);
		setBounds(10, 38, 600, 360);
		setPreferredSize(new Dimension(600,360));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    public void start() {
        setVisible(true);
        thread.start();
    }

    public void actionPerformed(ActionEvent ae) {
    	if(ae.getActionCommand().equals("READY1")){
    		ready1 = true;
    		User1ReadyButton.setIcon(new ImageIcon("./ChattingData/img/c.png"));
    		out.println("READY1TRUE");
    		User1ReadyButton.setEnabled(false);
    	}else if(ae.getActionCommand().equals("READY2")){
    		ready2 = true;
    		User2ReadyButton.setIcon(new ImageIcon("./ChattingData/img/c.png"));
    		out.println("READY2TRUE");
    		User2ReadyButton.setEnabled(false);
    	}
    }

    public void run(){
    	String otherIP=null, myIP=null;
    	int otherPort=0, myPort=0;
    	
        try{
			if(kind == Kind.Client){
				socket = new Socket(HOST, PORT);
				try{					
					in = new Scanner(socket.getInputStream());
					out = new PrintWriter(socket.getOutputStream(), true);

					myIP = socket.getInetAddress().toString().substring(1);
					otherIP = socket.getLocalAddress().toString().substring(1);
					otherPort = socket.getPort();
					myPort = socket.getLocalPort();
					User1ReadyButton.setEnabled(true);
					User2ReadyButton.setEnabled(false);
				}catch(ConnectException ce){socket.close();}
			}else{
				ServerSocket serverSocket = new ServerSocket(PORT);
				try{
					socket = serverSocket.accept();		
					in = new Scanner(socket.getInputStream());
					out = new PrintWriter(socket.getOutputStream(), true);
				
					myIP = socket.getInetAddress().toString().substring(1);
					otherIP = socket.getLocalAddress().toString().substring(1);
					otherPort = socket.getPort();
					myPort = socket.getLocalPort();
					User1ReadyButton.setEnabled(false);
					User2ReadyButton.setEnabled(true);
				}catch(ConnectException ce){serverSocket.close();}
			}
			
            display("Connected");
            while (true) {
            	String input = in.nextLine();
            	if(input==null) break;	//communication link broken...
            	
            	System.out.println(input);
            	if(input.equals("EXIT")){
            		new Thread(new AlamToServer(ServerIP, ServerPort, myID)).start();
            		thread.stop();
            		dispose();
            		return;	//exit...
            	}else if(input.equals("DIECLIENT")){
            		out.println("EXIT");
					new Thread(new AlamToServer(ServerIP, ServerPort, myID)).start();
					thread.stop();
					dispose();
					return;
            	}else if(input.equals("READY1TRUE")){
            		User1ReadyButton.setIcon(new ImageIcon("./ChattingData/img/c.png"));
            		User1ReadyButton.setEnabled(false);
            		ready1=true;
            		if(ready1&&ready2){
                		ready1 = ready2 = false;
                		int portNum = (new Random().nextInt(3000)+1000);
                		//JOptionPane.showMessageDialog(this, "Game start~!");
                		
                		String myip = socket.getLocalAddress().toString().substring(1);
                		if(kind == kind.Client){
                			out.println("CLIENTSTART "+ portNum);
                		}
                		else{
                			try {
                				String userdata = myID+"//"+myMoney+"//"+otherID+"//"+otherMoney;
                				new Thread( new Gaming(ServerIP,ServerPort,userdata, false, portNum,myip)).start();
                				new PlaySound("./ChattingData/sound/enter.wav").startMusic();
                				out.println("SEVERSTART "+ myip +" "+portNum);
                				dispose();
                			} catch (IOException e) {
                				// TODO Auto-generated catch block
                				e.printStackTrace();
                			}	
                		}
                	}
            	}else if(input.equals("READY2TRUE")){
            		User2ReadyButton.setIcon(new ImageIcon("./ChattingData/img/c.png"));
            		User2ReadyButton.setEnabled(false);
            		ready2=true;
            		if(ready1&&ready2){
                		ready1 = ready2 = false;
                		int portNum = (new Random().nextInt(3000)+1000);
                		//JOptionPane.showMessageDialog(this, "Game start~!");
                		
                		String myip = socket.getLocalAddress().toString().substring(1);
                		if(kind == kind.Client){
                			out.println("CLIENTSTART "+ portNum);
                		}
                		else{
                			try {
                				String userdata = myID+"//"+myMoney+"//"+otherID+"//"+otherMoney;
                				new Thread(new Gaming(ServerIP,ServerPort,userdata,false, portNum,myip)).start();
                				new PlaySound("./ChattingData/sound/enter.wav").startMusic();
                				out.println("SEVERSTART "+ myip +" "+portNum);
                				dispose();
                			} catch (IOException e) {
                				// TODO Auto-generated catch block
                				e.printStackTrace();
                			}	
                		}
                	}
            	}else if(input.startsWith("SEVERSTART")){
            		String []tmp = input.split(" ");
            		System.out.println("클라이언트야 켜져라");
            		String userdata = myID+"//"+myMoney+"//"+otherID+"//"+otherMoney;
            		Gaming game = new Gaming(ServerIP,ServerPort,userdata,true, Integer.parseInt(tmp[2]),tmp[1]);
            		game.start();
            		new PlaySound("./ChattingData/sound/enter.wav").startMusic();
            		dispose();
            	}else if(input.startsWith("CLIENTSTART")){
            		String []tmp = input.split(" ");
            		String myip = socket.getLocalAddress().toString().substring(1);
            		String userdata = myID+"//"+myMoney+"//"+otherID+"//"+otherMoney;
            		new Thread(new Gaming(ServerIP,ServerPort,userdata,false, Integer.parseInt(tmp[1]),myip)).start();
            		new PlaySound("./ChattingData/sound/enter.wav").startMusic();
            		out.println("COMEWITHME "+myip+" "+tmp[1]);
            		dispose();
            	}else if(input.startsWith("COMEWITHME")){
            		String []tmp = input.split(" ");
            		String userdata = myID+"//"+myMoney+"//"+otherID+"//"+otherMoney;
            		Gaming game = new Gaming(ServerIP,ServerPort,userdata,true, Integer.parseInt(tmp[2]),tmp[1]);
            		game.start();
            		new PlaySound("./ChattingData/sound/enter.wav").startMusic();
            		dispose();
            	}
            	else display(otherID + ": " + input);
            	
            }
        }catch(Exception e){
            display(e.getMessage());
            e.printStackTrace(System.err);
        }finally{
        	try {
        		in.close();
        		out.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    private void display(final String s) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                GamingArea.append(s + "\u23CE\n");
            }
        });
    }
    public String getStringMoney(int input) {
		int intMoney = input;
		String stringMoney = "";

		if (intMoney >= 1000000) {
			stringMoney += Integer.toString(intMoney / 1000000);
			stringMoney += "억 ";
			intMoney = intMoney % 1000000;
		}

		if (intMoney >= 100000) {
			stringMoney += Integer.toString(intMoney / 100000);
			stringMoney += "천 ";
			intMoney = intMoney % 100000;
		}

		if (intMoney >= 10000) {
			stringMoney += Integer.toString(intMoney / 10000);
			stringMoney += "백 ";
			intMoney = intMoney % 10000;
		}

		return stringMoney+"만원";
	}
    
    private class AlamToServer implements Runnable{
    	private String ServerIP;
    	private int ServerPort;
    	private String userid;
    	private BufferedReader inFromServer;
    	private PrintStream outToServer;
    	private Socket clientSocket=null;
    	
    	public AlamToServer(String IP, int Port, String ID){
    		ServerIP = IP;
    		ServerPort = Port;
    		userid = ID;
    	}
    	public void run(){
    		try{
    			clientSocket = new Socket(ServerIP, ServerPort);
    			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    			outToServer = new PrintStream(clientSocket.getOutputStream());

    			for(;;){
    				String line = inFromServer.readLine();
    				if(line==null) break;
    				
    				if(line.startsWith("SUBMITINFO")){
    					outToServer.println("REVIVE"+userid);
    				}
    			}
    		}catch(IOException e){}
    		finally{
    			try {
					inFromServer.close();
					outToServer.close();
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	}
    }
}