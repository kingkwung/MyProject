package zoipower;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatList implements ActionListener {

	//ê¸°ë³¸?ì¸ ?¬ìš©??UI?…ë‹ˆ?? ?´ê³³???µí•´ ?¬ëŒ?¤ê³¼ ?°ê²°?œì¼œì£¼ëŠ”??™œ?„í•˜ë©?
	//?´í´?˜ìŠ¤???ì²´???œë²„ê°?ì¡´ì¬?©ë‹ˆ?? ê·¸ê²ƒ???ë‹¤ë¥??¬ìš©?ê? ?‘ì†?„ì›?˜ë©´
	//ê·¸ì ‘?ì„ ?±ì‚¬?œì¼œì£¼ê¸° ?„í•¨?…ë‹ˆ??
	//?í•œ ë²„íŠ¼???´ë¦­???¤ë¥¸ ?´ë¼?´ì–¸?¸ì—ê²??‘ì†???”ì²­?˜ì—¬ ??™”ì°½ì„ ?´ì–´ì¤ë‹ˆ??
	
	BufferedReader in;
	PrintWriter out;
	JFrame frame = new JFrame("Chatter");
	JPanel contentPane=new JPanel();
	int allPort;
	boolean flag=true;
	
	JPanel userList=new JPanel();
	LinkedList<JButton> myButton=new LinkedList<JButton>();


	private static String name=null;
	
	
	private Socket socket;
	int requesterName;
	
	//?”ì²­ë°›ì? ?¬ëŒ???”ì²­?œì‚¬?Œì˜ ip???¬íŠ¸ë²ˆí˜¸ë¥??Œê¸¸?´ì—†ê¸°ë•Œë¬¸ì—,
	//?”ì²­ë°›ì? ?¬ëŒ???”ì²­???¬ëŒ?ê²Œ ?Œì¼???„ì†¡?˜ê¸°?„í•´ ?°ê²°? ë–„ 
	//?”ì²­???¬ëŒ??ip???¬íŠ¸ë²ˆí˜¸ë¥??Œì•„?´ê¸°?„í•´ ?„ìš”?©ë‹ˆ??
	
	public ChatList() {
		
		while(true){
			try {
		
				//?¬íŠ¸ë²ˆí˜¸???œë¤?¼ë¡œ ë°›ë˜, ê°™ì?ê²ƒì´ ?‡ì„ê²½ìš° ë°˜ë³µ?©ë‹ˆ??
				
				allPort=((int)(Math.random()*10000))+9000;
				new InputHandler(allPort).start();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		userList.setLayout(new BoxLayout(userList,BoxLayout.Y_AXIS));
		JLabel title=new JLabel("userList");

		userList.add(title);
		frame.add(new JScrollPane(userList));

		contentPane.add(userList);

		frame.add(contentPane);
		frame.setBounds(0, 0, 200, 500);


		// Add Listeners

	}

	private String getServerAddress() {
		return JOptionPane.showInputDialog(
				frame,
				"Enter IP Address of the Server:",
				"Welcome to the Chatter",
				JOptionPane.QUESTION_MESSAGE);
	}


	private String getName() {
	
		//?´ë¦„??ì§?†?ìœ¼ë¡???¥? ìˆ˜ ?ˆê²Œ ë³?ˆ˜ ?¤ì •?˜ì???
		
		name=JOptionPane.showInputDialog(
				frame,
				"Choose a screen name:",
				"Screen name selection",
				JOptionPane.PLAIN_MESSAGE);
		return name;
	}

	/**
	 * Connects to the server then enters the processing loop.
	 * @throws ClassNotFoundException 
	 */
	private void run() throws IOException, ClassNotFoundException {

		String serverAddress = getServerAddress();
		
		socket = new Socket(serverAddress, 9008);

		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(),true);


		while (true) {

			String line = in.readLine();
			
			if(line.startsWith("LIST")){
		
				//LIST??ì¶”ê??˜ë„ë¡??œë‹¤.
				
				String userName=line.substring(5);
				myButton.add(new JButton(userName));

				for(int i=0;i<myButton.size();i++){
					userList.add(myButton.get(i));
					myButton.get(i).addActionListener(this);
				}
				
				this.frame.setVisible(true);
				
			}else if(line.startsWith("ADDRESS")){
			
				//ì£¼ì†Œë¥?ë°›ì•„?µë‹ˆ?? ?œë²„?ì„œ REQUEST ë¥?ë°›ì•„??
				//ADDRESSë¥?ë³´ëƒ„?¼ë¡œ?? ?´ë¼?´ì–¸?¸ëŠ” ?ë‹¤ë¥??´ë¼?´ì–¸?¸ë¡œ ?‘ì†???©ë‹ˆ??
				
				flag=true;
				String[] message=line.split(" ");				
				System.out.println(message[1]+"  "+message[2]);
			
				Socket connectSocket=new Socket(message[1],Integer.parseInt(message[2]));	
				System.out.println("my name is "+name);
				System.out.println("connect the "+message[1]+" "+message[2]);
				
				new ChatClient(connectSocket,name,true).start();
				
				
			}else if(line.startsWith("GETPORT")){
		
				//ì²˜ìŒ?‘ì†???ì‹ ??ipì£¼ì†Œë°??¬íŠ¸ë²ˆí˜¸ë¥??œë²„???Œë¦½?ˆë‹¤. 
				
				out.println(InetAddress.getLocalHost().getHostAddress()+" "+(allPort));
				System.out.println(InetAddress.getLocalHost().getHostAddress());
			}
			else if (line.startsWith("SUBMITNAME")) {
				out.println(getName());
				frame.setTitle(name);
			
				//?´ë¦„????‹¹?˜ê²Œ ë°›ì•„?¤ì—¬ì¡Œìœ¼ë©? 
				//frame??Destroyer???¤ì •?œë‹¤.
				
				frame.addWindowListener(new WindowDestroyer(out,name));
			}else if (line.startsWith("NAMEACCEPTED")) {
			}else if (line.startsWith("DELETE")){
			
				//?¬ìš©?ê? ì¢…ë£Œ? ë•Œ ë°œìƒ?œë‹¤.
				//?¤ë¥¸ ?´ë¼?´ì–¸?¸ì˜ comboBox??listë¥??˜ì •?œë‹¤.
				
				String deleteName=line.substring(7).trim();
				for(int i=0;i<myButton.size();i++){
					if(myButton.get(i).getActionCommand().equals(deleteName)){
						myButton.get(i).setVisible(false);
						myButton.remove(i);
					}else{
						userList.add(myButton.get(i));
					}
				}
			}
		}
	}


	/**
	 * Runs the client as an application with a closeable frame.
	 */
	public static void main(String[] args) throws Exception {
		ChatList client = new ChatList();

		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		client.frame.setVisible(true);
		client.run();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String command=e.getActionCommand();
		
		for(int i=0;i<myButton.size();i++){
			if(flag&&command.equals(myButton.get(i).getActionCommand())){
				//?¬ìš©?ê? ?°ê²°ë²„íŠ¼???„ë¥¸?¤ë©´, ?œë²„?ê²Œ REQUESTë¥??µí•´ ?”ì²­?„í•©?ˆë‹¤.
				//ê·¸ëŸ¬ë©??œë²„???¤ì‹œ ADDRESS?¼ëŠ” ê²ƒì„ ë³´ë‚´ê²Œë˜ê³?
				//?„ì—??ê·¸ê²ƒ??ë°›ì•„ ?°ê²°?©ë‹ˆ?? 
				System.out.println("REQUEST "+name+" "+command+"\n");
				out.println("REQUEST "+name+" "+command);
				flag=false;
				break;
			}
		}
	}


	private static class InputHandler extends Thread{
		//?´ë¼?´ì–¸?¸ëŠ” ?ì‹ ???œë²„ê°??„ìš”?©ë‹ˆ?? 
		//?´ë??„í•´ inputHandlerë¥¼ë‘????‹œ ì¼œë†“?„ë¡ ?´ë†“?˜ìŠµ?ˆë‹¤.
		
		private ServerSocket mySocket;
		//?¹ì •ì¸µì—???¤ëŠ” ?”êµ¬ë¥?ë°›ì•„?¤ì´ê¸°ìœ„??ê³³ì´??
		int port;
		public InputHandler(int port) throws IOException{
			this.port=port;
			mySocket=new ServerSocket(port);
		}
		public void run(){
			while(true){
				try {
					System.out.println("just waiting");
					Socket inSocket=mySocket.accept();
					Socket temp=inSocket;
					
					System.out.println("my name is :"+name);
					
					BufferedReader inRead = new BufferedReader(new InputStreamReader(inSocket.getInputStream()));
					String input=inRead.readLine();
					System.out.println(input);
					
					if(input.equals("CHAT")){
						//ì±„íŒ…?¼ë¡œ ?”ì²­ ?˜ëŠ” ê²½ìš°?…ë‹ˆ??
						//ì²˜ìŒ?‘ì†???´ê³³?¼ë¡œ ?”ì²­?©ë‹ˆ??
						inRead=null;
						new ChatClient(inSocket,name,false).start();
					}else{
						//?Œì¼?„ì†¡?¼ë¡œ ?”ì²­?˜ëŠ” ê²½ìš°?…ë‹ˆ??
						//?˜ì?ë§??¤íŠ¸ë¦?PrintWiriterë¡œì´ë¯??Œìº£???¤ì •?´ë†“?˜ê¸°??
						//ê¸°ì¡´???Œì¼“???ˆì–´ë²„ë¦¬ê³??ˆë¡œ???Œìº£?„ë§Œ?¤ì–´
						//ObjectStream?„ìœ„?˜ì—¬ ?¬ìš©?©ë‹ˆ??
						inSocket.close();
						inSocket=mySocket.accept();
						System.out.println("ACCEPT the OK");
						new FileDown(inSocket).start();
						//êµìˆ˜?˜ê»˜??ì£¼ì‹  fileDown???œì‘?©ë‹ˆ??
					}
					//?Œì¼?¸ì? ??™” ?œì‘?¸ì? ê²?ƒ‰?˜ê¸° 
					
					System.out.println(port);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
