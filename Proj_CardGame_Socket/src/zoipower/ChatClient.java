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
	
	//?¬ì©?ê? ??ë¥??ì²­?ì?? ?í??ë ì°½ì?ë¤.
	//?´ê³³???µí´ ?¬ì©?ë ?í¸ê°ì ??ë¥??ê² ?©ë?? 
	
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
		
		//requester???ì ???ì²­?ì¬?ì´ ?ì²­ë°ì??¬ë?¸ì? ?í??´ì´ì¤ë??
		//true?¼ê²½???ì ???ì²­???¬ë?ë??
		
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

			//ì²ì?ì???ì ??ì±í?ê³ ?¶ë¤??ê²ì ?ë¦½?ë¤.
			
			out.println("CHAT");
			
			while (true) {
				//ì²ì?ì???ì²­ë°ì??¬ë???ì²­???¬ë?ê²
				//?´ë¦ ?ë³´ë¥??»ì´?¤ë ê³¼ì ?ë??
				
				if(!requester){
					out.println("YOURNAME ");
				}
				
				String line = in.readLine();
				System.out.println(line);
			
				if(line.startsWith("MESSAGE")){
					messageArea.append("recive:"+line.substring(8)+"\n");
				}else if(line.startsWith("MYNAME")){
					requesterInfo=line.substring(7);
					//?´ë¦??ê°? ¸?ì¼ë©?requesterë¥?trueë¡ë°ê¾¸ì´ ë°ë³µ??ë°©ì??©ë??
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
		
		//?¬ì©?ê? ?°ì´???ì¡?ì??SENDë²í¼???ë??ë ?ë?? 
		
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("SEND")){
			if(jfc.showOpenDialog(frame.getContentPane())==JFileChooser.APPROVE_OPTION){
				String fileName=jfc.getSelectedFile().toString();
				Socket fileSocket;
				try {
					if(requesterInfo!=null){
						
						//?´ê²½???¬ì©?ê? ?ì²­ ë°ì??¬ë??ê²½ì°?ë?? 
						//ì´ê¸°????¥?´ë??address??portë²í¸???ì ???¬í¸ë²í¸?´ê¸°?ë¬¸??
						//?°ì´?°ë? ?ì¡??ê³³ì¼ë¡?ë³?²½?´ì£¼?´ì¼?©ë??
						//ê·¸ë???ë²?ì REQUESTë¥?ë¿ë ¤ ?ë?ë°©ì ipì£¼ìë¥??»ì´?ë??
						//ê·¸í?ë ì§??ì¼ë¡?address??portë¥???¥?ê¸°?ë¬¸??
						//?ë²???ì ?ì ?ì ê¸°ì??requesterInfo ë¥¼ë¤??nullë¡ì??í©?ë¤.
						
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
