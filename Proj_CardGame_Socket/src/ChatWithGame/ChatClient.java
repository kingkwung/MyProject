package ChatWithGame;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.util.Random;
import javax.swing.*;

import ChatWithGame.WaitingRoom.Kind;

public class ChatClient extends JFrame implements Runnable{
	//Member variable for Server Data
	public final String ServerIP;
	public final int ServerPort;
	
	//Member variable for chatting model.
	private String UserID="";
	private int WinNum;
	private int LooseNum;
	private int GameMoney;
	private String Major;
	private String Source;
	
    BufferedReader inFromServer;
    PrintStream outToServer;
    static String users[];
    //Member variable for chatting runner.
    JTabbedPane tabbedPane;
    JScrollPane totalListScrollPane;
    JScrollPane majorListScrollPane;
	private JList TotalList;
	private JList MajorList;
	private DefaultListModel<String> totalListModel;
	private DefaultListModel<String> majorListModel;
	private MouseListener mouseListener;
    JTextField inputText;
    JTextArea messageArea; 
    
    JLabel userImagelabel;
    JTextField IDfield;
    JTextField WinRatefield;
    JTextField Gamemoneyfield;
    JTextField historyfield;
    JTextField Majorfield;
    
    JPanel ChatPanel;

    private final JFrame frame=this;
    
    public ChatClient(final String IP, final int Port, final String ID){
    	setTitle("Chatting Room");
        ServerIP = IP;
        ServerPort = Port;
        UserID = ID;
        
    	// Layout GUI
    	//setResizable(false);
    	//set member variables.
    	
    	//Set GUI
    		//Set List //Create the list and put it in a scroll pane.
    	totalListModel = new DefaultListModel();
    	TotalList = new JList(totalListModel);
    	majorListModel = new DefaultListModel();
    	MajorList = new JList(majorListModel);
    	TotalList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	TotalList.setVisibleRowCount(20);
		totalListScrollPane = new JScrollPane(TotalList);
		MajorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		MajorList.setVisibleRowCount(20);
		majorListScrollPane = new JScrollPane(MajorList);
		mouseListener = new MouseAdapter(){
			public void mouseClicked(MouseEvent mouseEvent) {
				JList theList = (JList) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2) {
					int index = theList.locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						if(GameMoney<10000){
							JOptionPane.showMessageDialog(frame,"You don't have any money!!!");
							return;
						}
						Object obj = theList.getModel().getElementAt(index);
						String targetID = obj.toString();
						targetID = targetID.substring(targetID.indexOf(")")+2);
						/*test 지울거임*/System.out.println("Double-clicked on: " + targetID);
						outToServer.println("REQUESTGAME"+targetID);
					}
				}
			}
		};
		TotalList.addMouseListener(mouseListener);
		MajorList.addMouseListener(mouseListener);
		
    		//Set Frame
    	setLayout(null);
    	setPreferredSize(new Dimension(1010,638));
        getContentPane().add(getMainPanel());
        setBounds(0,0,1000 +10, 600 +38);
		setLocation(300,80);
		pack();
		
        // Add Listeners _ This listener do for client's input.
        inputText.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	if(tabbedPane.getSelectedIndex()==0){	//전체대화방
            		outToServer.println(inputText.getText());
                    inputText.setText("");
            	}else{									//동기대화방
            		outToServer.println("[#!#?#!#]"+inputText.getText());
                    inputText.setText("");
            	}
            }
        });
        
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setVisible(true);
    }
    public void run() {
    	new PlaySound("./ChattingData/sound/in.wav").startMusic();
    	Socket clientSocket=null;
    	try {
    		clientSocket = new Socket(ServerIP, ServerPort);
    		inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    		outToServer = new PrintStream(clientSocket.getOutputStream());
    	} catch (IOException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}
    	
    	try{
    		while(true){
    			String line = inFromServer.readLine();
    			
    			/*else if(line.equals("ACCESSACCEPT")){
    				outToServer.println("GiveMeMyData");
    				continue;
    			}*/
    			if(line.startsWith("SUBMITINFO")){
    				outToServer.println("JustAccess"+UserID);
    			}else if(line.startsWith("UPDATE")){			//If server pass notation of "NAMEACCEPTED" or "UPDATE", then a client then update all user's state.
    				messageArea.append(line.substring(6)+"\n");//Cooperation with under two elseif() condition is done to update user data.
    				outToServer.println("GiveMeMyData");
    				ChatPanel.repaint();
    			}else if(line.startsWith("MyInfo:")){
    				//My Information Protocol("name//win//lose//money//major")
    				String[] data = line.substring(7).split("//");
    				
    				UserID = data[0];
    				WinNum = Integer.parseInt(data[1]);
    				LooseNum = Integer.parseInt(data[2]);
    				GameMoney = Integer.parseInt(data[3]);
    				Major = data[4];
    				Source = data[5];
    				UpdateUserPanel();
    				
    				outToServer.println("GiveMeOthersData"+UserID);
    			}else if(line.startsWith("UserInfo:")){
    				//Getting userinfo protocol("'ID/winRate'//'ID/winRate'//")
    				//각각의 유저는 "ID/승률"의 형식으로 저장되어 있음.
    				majorListModel.removeAllElements();
    				totalListModel.removeAllElements();
    				if(!line.substring(9).equals("")){
    					users = line.substring(9).split("//");
    					
    					for(int i=0;i<users.length;i++){
    						String[] data = users[i].split("/");
    						users[i] = data[1];	//just select name(win rate) and check major.
    						
    						if(data[0].equals(Major))
    							majorListModel.addElement(users[i]);
    						
    						totalListModel.addElement(users[i]);
    					}
    				}
    			}else if(line.startsWith("MESSAGE")){				//It catches Message which is come from server.
    				new PlaySound("./ChattingData/sound/message.wav").startMusic();
    				messageArea.append(line.substring(8) + "\u23CE\n");
    				messageArea.selectAll();
    				int x = messageArea.getSelectionEnd();
    				messageArea.select(x,x);
    				ChatPanel.repaint();
    			}else if(line.startsWith("REQUESTACCESS")){
    				//Protocol line: "REQUESTACCESSrequesterID//requesterGameMoney"
    				String[] requestData = line.substring(13).split("//");
    				String requestUser = requestData[0];
    				
    				if(GameMoney<10000){
    					outToServer.println("RESPONDNO"+requestUser);
    					continue;
    				}
    				
    				int reply = JOptionPane.showConfirmDialog(null, requestUser+" requested the game. Do you want to join?", "Game Acception", JOptionPane.YES_NO_OPTION);
    				
    			    if(reply == JOptionPane.YES_OPTION){
    			    	int WaitPort = (new Random()).nextInt(20000)+36000;  //range to 36000~56000
    			    	//recieve image path
    			    	int FilePort = (new Random()).nextInt(20000)+36000;  //range to 36000~56000
    			    	String myIP = clientSocket.getLocalAddress().toString().substring(1);
	            		new Thread(new Reciever(FilePort)).start();
	            		String dest = (new File("./ChattingData/userimages/")).getAbsolutePath()+"\\";
	            		outToServer.println("FILEOKAY"+FilePort+"<!!>"+dest+"<!!>"+requestUser);
	            		String imageReply = inFromServer.readLine();
	            		
	            		if(imageReply.equals("OK")){
	            			outToServer.println("RESPONDYES"+myIP+"//"+WaitPort+"//"+requestUser);
	            			try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
	            			//then wait the game.
            				//protocol sequence: IP, port, otherID//gamemoney
            			requestWaitingWindow(myIP,WaitPort,line.substring(13));

            			setVisible(false);
	            		}
    			    }
    			    else outToServer.println("RESPONDNO"+requestUser);
    			}else if(line.startsWith("RESPONSE")){
    				String reply = line.substring(8);
    				if(reply.startsWith("YES")){
    					//new game waiting window
    					//let's game하지말고 걍 게임 실행하자~~
    					//response_yes protocol: IP//PORT//TargetID//TargetGameMoney
    					reply = reply.substring(3);
    					String[] p2pData = reply.split("//");
    					String Info = p2pData[2]+"//"+p2pData[3];
    					
						//recieve other's image path
    					int FilePort = (new Random()).nextInt(20000)+36000;  //range to 36000~56000
	            		new Thread(new Reciever(FilePort)).start();
	            		
	            		String dest = (new File("./ChattingData/userimages/")).getAbsolutePath()+"\\";
	            		outToServer.println("FILEOKAY"+FilePort+"<!!>"+dest+"<!!>"+p2pData[2]);
	            		String imageReply = inFromServer.readLine();
	            		if(imageReply.equals("OK")){
	            			try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
	            			//then wait the game.
	            				//protocol sequence: IP, port, otherID//gamemoney
	            			clientWaitingWindow(p2pData[0],Integer.parseInt(p2pData[1]),Info);

	            			//JOptionPane.showMessageDialog(this,"Let's game");
	            			setVisible(false);
	            		}
    				}else{
    					JOptionPane.showMessageDialog(this,reply.substring(2)+" Denyed...ㅠㅠ");
    				}
    			}else if(line.equals("IMAGEOK")){
    				
    			}else if(line.equals("REVIVAL")){
    				UpdateUserPanel();
    				setVisible(true);
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
    public void requestWaitingWindow(final String ip, final int s_port, final String otherName){	//server		
       	new WaitingRoom(Kind.Server, UserID+"//"+GameMoney, otherName, ip, s_port,ServerIP,ServerPort).start();
	}
    public void clientWaitingWindow(final String ip, final int s_port, final String otherName){		//client
    	new WaitingRoom(Kind.Client, UserID+"//"+GameMoney, otherName, ip, s_port,ServerIP,ServerPort).start();
    }
    public String getWinRate(){
		if(WinNum==0 && (LooseNum==0||WinNum==1))
			return "0%";
		
		String returnValue = (""+(double)WinNum/(WinNum+LooseNum)*100);
		
		return returnValue.substring(0, returnValue.indexOf("."))+"%";
		
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
    public JPanel getMainPanel(){
    	JPanel mainPanel = new JPanel();
    	mainPanel.setLayout(null);
    	
    	mainPanel.setBounds(0, 0, 1000, 600);
    	
    	mainPanel.add(getUserPanel());
    	mainPanel.add(getChattingPanel());
    	mainPanel.add(getUserListPanel());
    	return mainPanel;
    }
    public void UpdateUserPanel(){
    	IDfield.setText(UserID);
    	WinRatefield.setText("승률: "+getWinRate());
    	Gamemoneyfield.setText(getStringMoney(GameMoney));
    	historyfield.setText((WinNum+LooseNum)+"전 "+WinNum+"승 "+LooseNum+"패");
    	Majorfield.setText(Major);
    	ImageIcon Userimage = new ImageIcon("./ChattingData/userimages/"+UserID+Source.substring(Source.lastIndexOf(".")));
    	
    	Image tmpImg = Userimage.getImage();
    	Image newImg = tmpImg.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
    	Userimage = new ImageIcon(newImg);
    	userImagelabel.setIcon(Userimage);
    }
    public JPanel getUserPanel(){
    	JPanel UserPanel = new JPanel(new GridLayout(3,1,0,3));
    	ImageIcon UserImage = new ImageIcon("./ChattingData/img/default.jpg");
    	
    	Image tmpImg = UserImage.getImage();
    	Image newImg = tmpImg.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
    	UserImage = new ImageIcon(newImg);
    	userImagelabel = new JLabel(UserImage);
    	
    	JPanel myInfoPanel = new JPanel(new GridLayout(5,1,5,5));
    	myInfoPanel.setBackground(Color.gray);
    	
    	IDfield = new JTextField("les");
    	WinRatefield = new JTextField("99%");
        Gamemoneyfield = new JTextField("1억 만원");
        historyfield = new JTextField("100전 99승 1패");
        Majorfield = new JTextField("Software Management&Design");
        
    	IDfield.setEditable(false);
    	WinRatefield.setEditable(false);
        Gamemoneyfield.setEditable(false);
        historyfield.setEditable(false);
        Majorfield.setEditable(false);
        IDfield.setBackground(Color.lightGray);
        WinRatefield.setBackground(Color.lightGray);
        Gamemoneyfield.setBackground(Color.lightGray);
        historyfield.setBackground(Color.lightGray);
        Majorfield.setBackground(Color.lightGray);
        myInfoPanel.add(IDfield);
        myInfoPanel.add(WinRatefield);
        myInfoPanel.add(Gamemoneyfield);
        myInfoPanel.add(historyfield);
        myInfoPanel.add(Majorfield);
    	
    	//add picture
    	UserPanel.add(userImagelabel);//,BorderLayout.NORTH);
    	//add user data
    	UserPanel.add(myInfoPanel);//, BorderLayout.LINE_START);
    	
    	UserPanel.setBounds(0, 0, 200, 600);
    	return UserPanel;
    }
    public JPanel getUserListPanel(){
    	JPanel UserListPanel = new JPanel(new GridLayout(1,1));
    	
    	UserListPanel.setBounds(800, 0, 200, 600);
    	UserListPanel.setBackground(Color.gray);
    	
    	tabbedPane = new JTabbedPane();
    	//ImageIcon icon = createImageIcon("./img/ele/ele1.png");
    	
    	JComponent Dpanel = makeTextPanel("All");
    	Dpanel.setPreferredSize(new Dimension(10000, 600));
    	tabbedPane.addTab("All", null, totalListScrollPane, null);
		TotalList.validate();
    	//tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
    	
    	JComponent ACCpanel = makeTextPanel("Major");
    	ACCpanel.setPreferredSize(new Dimension(200, 600));
    	tabbedPane.addTab("Major", null/*icon position*/, majorListScrollPane, null);
    	majorListScrollPane.validate();
    	//tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
    	
    	UserListPanel.add(tabbedPane);
    	tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    	
    	return UserListPanel;
    }
    private JComponent makeTextPanel(String text){
    	JPanel panel = new JPanel(false);
    	JLabel filler = new JLabel(text);
    	filler.setBackground(new Color(0f,0f,0f,.0f));
    	filler.setOpaque(false);
    	filler.setHorizontalAlignment(JLabel.CENTER);
    	panel.setLayout(new GridLayout(1, 1));
    	panel.add(filler);
    	panel.setBackground(new Color(0f,0f,0f,.0f));
    	panel.setOpaque(false);
    	return panel;
    }
    public JPanel getChattingPanel(){
    	ChatPanel = new JPanel(){
			public void paintComponent(Graphics g) {
				g.drawImage(Toolkit.getDefaultToolkit().getImage("./ChattingData/img/MainBG.png"), 0, 0, this);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
    	ChatPanel.setLayout(null);
    	
    	messageArea = new JTextArea();
    	messageArea.setEditable(false);
    	messageArea.setBackground(new Color(0f,0f,0f,.0f));
		messageArea.setForeground(Color.white);
		messageArea.setFont(new Font("Dialog", Font.BOLD, 14));
		//messageArea.setOpaque(false);
    	JScrollPane mPane = new JScrollPane(messageArea);
    	mPane.setBounds(0, 0, 600, 570);
    	mPane.setBackground(new Color(0f,0f,0f,.0f));
    	mPane.setOpaque(false);
    	
    	inputText = new JTextField();
    	inputText.setEditable(true);
    	inputText.setBounds(0, 570, 630, 30);
    	inputText.setBackground(new Color(0f,0f,0f,.0f));
    	inputText.setOpaque(false);
    	inputText.setForeground(Color.white);
    	ChatPanel.add(mPane);
    	ChatPanel.add(inputText);
    	
    	ChatPanel.setBounds(200, 0, 600, 600);
    	ChatPanel.setBackground(new Color(0f,0f,0f,.0f));
    	ChatPanel.setOpaque(false);
    	return ChatPanel;
    }
}