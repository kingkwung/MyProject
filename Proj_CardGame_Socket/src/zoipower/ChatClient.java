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
	
	//?�용?��? ??���??�청?�을?? ?��??�는 창입?�다.
	//?�곳???�해 ?�용?�는 ?�호간에 ??���??�게 ?�니?? 
	
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
		
		//requester???�신???�청?�사?�이 ?�청받�??�람?��? ?��??�어줍니??
		//true?�경???�신???�청???�람?�니??
		
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

			//처음?�속???�신??채팅?�고?�다??것을 ?�립?�다.
			
			out.println("CHAT");
			
			while (true) {
				//처음?�속???�청받�??�람???�청???�람?�게
				//?�름 ?�보�??�어?�는 과정?�니??
				
				if(!requester){
					out.println("YOURNAME ");
				}
				
				String line = in.readLine();
				System.out.println(line);
			
				if(line.startsWith("MESSAGE")){
					messageArea.append("recive:"+line.substring(8)+"\n");
				}else if(line.startsWith("MYNAME")){
					requesterInfo=line.substring(7);
					//?�름??�?��?�으�?requester�?true로바꾸어 반복??방�??�니??
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
		
		//?�용?��? ?�이???�송?�위??SEND버튼???��??�때 ?�니?? 
		
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("SEND")){
			if(jfc.showOpenDialog(frame.getContentPane())==JFileChooser.APPROVE_OPTION){
				String fileName=jfc.getSelectedFile().toString();
				Socket fileSocket;
				try {
					if(requesterInfo!=null){
						
						//?�경???�용?��? ?�청 받�??�람??경우?�니?? 
						//초기????��?�놓??address??port번호???�신???�트번호?�기?�문??
						//?�이?��? ?�송??곳으�?�?��?�주?�야?�니??
						//그래???�버?�에 REQUEST�?뿌려 ?��?방의 ip주소�??�어?�니??
						//그후?�는 �?��?�으�?address??port�???��?�기?�문??
						//?�버???�접?�을 ?�애기위??requesterInfo 를다??null로�??�합?�다.
						
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
