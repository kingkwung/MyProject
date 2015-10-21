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

	//기본?�인 ?�용??UI?�니?? ?�곳???�해 ?�람?�과 ?�결?�켜주는??��?�하�?
	//?�클?�스???�체???�버�?존재?�니?? 그것???�다�??�용?��? ?�속?�원?�면
	//그접?�을 ?�사?�켜주기 ?�함?�니??
	//?�한 버튼???�릭???�른 ?�라?�언?�에�??�속???�청?�여 ??��창을 ?�어줍니??
	
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
	
	//?�청받�? ?�람???�청?�사?�의 ip???�트번호�??�길?�없기때문에,
	//?�청받�? ?�람???�청???�람?�게 ?�일???�송?�기?�해 ?�결?�떄 
	//?�청???�람??ip???�트번호�??�아?�기?�해 ?�요?�니??
	
	public ChatList() {
		
		while(true){
			try {
		
				//?�트번호???�덤?�로 받되, 같�?것이 ?�을경우 반복?�니??
				
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
	
		//?�름??�?��?�으�???��?�수 ?�게 �?�� ?�정?��???
		
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
		
				//LIST??추�??�도�??�다.
				
				String userName=line.substring(5);
				myButton.add(new JButton(userName));

				for(int i=0;i<myButton.size();i++){
					userList.add(myButton.get(i));
					myButton.get(i).addActionListener(this);
				}
				
				this.frame.setVisible(true);
				
			}else if(line.startsWith("ADDRESS")){
			
				//주소�?받아?�니?? ?�버?�서 REQUEST �?받아??
				//ADDRESS�?보냄?�로?? ?�라?�언?�는 ?�다�??�라?�언?�로 ?�속???�니??
				
				flag=true;
				String[] message=line.split(" ");				
				System.out.println(message[1]+"  "+message[2]);
			
				Socket connectSocket=new Socket(message[1],Integer.parseInt(message[2]));	
				System.out.println("my name is "+name);
				System.out.println("connect the "+message[1]+" "+message[2]);
				
				new ChatClient(connectSocket,name,true).start();
				
				
			}else if(line.startsWith("GETPORT")){
		
				//처음?�속???�신??ip주소�??�트번호�??�버???�립?�다. 
				
				out.println(InetAddress.getLocalHost().getHostAddress()+" "+(allPort));
				System.out.println(InetAddress.getLocalHost().getHostAddress());
			}
			else if (line.startsWith("SUBMITNAME")) {
				out.println(getName());
				frame.setTitle(name);
			
				//?�름????��?�게 받아?�여졌으�? 
				//frame??Destroyer???�정?�다.
				
				frame.addWindowListener(new WindowDestroyer(out,name));
			}else if (line.startsWith("NAMEACCEPTED")) {
			}else if (line.startsWith("DELETE")){
			
				//?�용?��? 종료?�때 발생?�다.
				//?�른 ?�라?�언?�의 comboBox??list�??�정?�다.
				
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
				//?�용?��? ?�결버튼???�른?�면, ?�버?�게 REQUEST�??�해 ?�청?�합?�다.
				//그러�??�버???�시 ADDRESS?�는 것을 보내게되�?
				//?�에??그것??받아 ?�결?�니?? 
				System.out.println("REQUEST "+name+" "+command+"\n");
				out.println("REQUEST "+name+" "+command);
				flag=false;
				break;
			}
		}
	}


	private static class InputHandler extends Thread{
		//?�라?�언?�는 ?�신???�버�??�요?�니?? 
		//?��??�해 inputHandler를두????�� 켜놓?�록 ?�놓?�습?�다.
		
		private ServerSocket mySocket;
		//?�정층에???�는 ?�구�?받아?�이기위??곳이??
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
						//채팅?�로 ?�청 ?�는 경우?�니??
						//처음?�속???�곳?�로 ?�청?�니??
						inRead=null;
						new ChatClient(inSocket,name,false).start();
					}else{
						//?�일?�송?�로 ?�청?�는 경우?�니??
						//?��?�??�트�?PrintWiriter로이�??�캣???�정?�놓?�기??
						//기존???�켓???�어버리�??�로???�캣?�만?�어
						//ObjectStream?�위?�여 ?�용?�니??
						inSocket.close();
						inSocket=mySocket.accept();
						System.out.println("ACCEPT the OK");
						new FileDown(inSocket).start();
						//교수?�께??주신 fileDown???�작?�니??
					}
					//?�일?��? ??�� ?�작?��? �?��?�기 
					
					System.out.println(port);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
