package zoipower;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ChatClient extends Thread implements ActionListener {
	
	//?¬ìš©?ê? ??™”ë¥??”ì²­?ˆì„?? ?˜í??˜ëŠ” ì°½ì…?ˆë‹¤.
	//?´ê³³???µí•´ ?¬ìš©?ëŠ” ?í˜¸ê°„ì— ??™”ë¥??˜ê²Œ ?©ë‹ˆ?? 
	
	JFileChooser jfc=new JFileChooser();
	BufferedReader in;
	PrintWriter out;
	JFrame frame = new JFrame("Chatter");
	JTextField textField = new JTextField(40);
	JTextArea messageArea = new JTextArea(8, 40);
	JButton fileSend=new JButton("SEND");
	Socket socket;
	InetAddress address;
	ObjectInputStream inputStream = null;
	String myName;
	int port;
	boolean requester;
	String requesterInfo;
	public ChatClient(Socket socket,String title,boolean requester) {
		
		//requester???ì‹ ???”ì²­?œì‚¬?Œì´ ?”ì²­ë°›ì??¬ëŒ?¸ì? ?˜í??´ì–´ì¤ë‹ˆ??
		//true?¼ê²½???ì‹ ???”ì²­???¬ëŒ?…ë‹ˆ??
		
		this.requester=requester;
		
		if(requester){
			requesterInfo=null;
		}
		this.socket=socket;
		port=socket.getPort();
		address=socket.getInetAddress();
		myName=title;
		
		// Layout GUI
		frame.setTitle(title);
		frame.getContentPane().add(textField, "North");
		frame.getContentPane().add(new JScrollPane(messageArea), "Center");
		
		fileSend.addActionListener(this);
		
		frame.getContentPane().add(fileSend,"South");
		
		frame.pack();
		
		// Add Listeners
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("MESSAGE "+textField.getText());
				messageArea.append("send:"+textField.getText()+"\n");
				textField.setText("");
			}
		});
		frame.setVisible(true);
	}



	public void run() {

		// Make connection and initialize streams
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			//ì²˜ìŒ?‘ì†???ì‹ ??ì±„íŒ…?˜ê³ ?¶ë‹¤??ê²ƒì„ ?Œë¦½?ˆë‹¤.
			
			out.println("CHAT");
			
			while (true) {
				//ì²˜ìŒ?‘ì†???”ì²­ë°›ì??¬ëŒ???”ì²­???¬ëŒ?ê²Œ
				//?´ë¦„ ?•ë³´ë¥??»ì–´?¤ëŠ” ê³¼ì •?…ë‹ˆ??
				
				if(!requester){
					out.println("YOURNAME ");
				}
				
				String line = in.readLine();
				System.out.println(line);
			
				if(line.startsWith("MESSAGE")){
					messageArea.append("recive:"+line.substring(8)+"\n");
				}else if(line.startsWith("MYNAME")){
					requesterInfo=line.substring(7);
					//?´ë¦„??ê°? ¸?“ìœ¼ë©?requesterë¥?trueë¡œë°”ê¾¸ì–´ ë°˜ë³µ??ë°©ì??©ë‹ˆ??
					requester=true;
				}else if(line.startsWith("YOURNAME")){
					out.println("MYNAME "+myName);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		//?¬ìš©?ê? ?°ì´???„ì†¡?„ìœ„??SENDë²„íŠ¼???Œë??„ë•Œ ?…ë‹ˆ?? 
		
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("SEND")){
			if(jfc.showOpenDialog(frame.getContentPane())==JFileChooser.APPROVE_OPTION){
				String fileName=jfc.getSelectedFile().toString();
				Socket fileSocket;
				try {
					if(requesterInfo!=null){
						
						//?´ê²½???¬ìš©?ê? ?”ì²­ ë°›ì??¬ëŒ??ê²½ìš°?…ë‹ˆ?? 
						//ì´ˆê¸°????¥?´ë†“??address??portë²ˆí˜¸???ì‹ ???¬íŠ¸ë²ˆí˜¸?´ê¸°?„ë¬¸??
						//?°ì´?°ë? ?„ì†¡??ê³³ìœ¼ë¡?ë³?²½?´ì£¼?´ì•¼?©ë‹ˆ??
						//ê·¸ë˜???œë²„?ì— REQUESTë¥?ë¿Œë ¤ ?ë?ë°©ì˜ ipì£¼ì†Œë¥??»ì–´?…ë‹ˆ??
						//ê·¸í›„?ëŠ” ì§?†?ìœ¼ë¡?address??portë¥???¥?˜ê¸°?Œë¬¸??
						//?œë²„???œì ‘?ì„ ?†ì• ê¸°ìœ„??requesterInfo ë¥¼ë‹¤??nullë¡œì??•í•©?ˆë‹¤.
						
						Socket serverSocket=new Socket("127.0.0.1",9008);
						PrintWriter pw=new PrintWriter(serverSocket.getOutputStream(),true);
						BufferedReader br=new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
						String inString=br.readLine();
						System.out.println(inString+" "+myName+" "+requesterInfo);
						while(inString.equals("SUBMITNAME")){
							pw.println("REQUEST "+myName+" "+requesterInfo);
							inString=br.readLine();
							System.out.println(inString);
						}
						System.out.println(inString);
						String[] input1=inString.split(" ");
						System.out.println(input1[1]+" "+input1[2]);
						address=InetAddress.getByName(input1[1]);
						port=Integer.parseInt(input1[2]);
						serverSocket.close();
						requesterInfo=null;
					}	
					
					fileSocket = new Socket(address,port);
					FileSend send=new FileSend(fileSocket,fileName);
					System.out.println("file accept");
					send.start();
	
					
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		}
	}
}
