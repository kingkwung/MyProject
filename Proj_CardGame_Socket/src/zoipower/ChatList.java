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

	//๊ธฐ๋ณธ?์ธ ?ฌ์ฉ??UI?๋?? ?ด๊ณณ???ตํด ?ฌ๋?ค๊ณผ ?ฐ๊ฒฐ?์ผ์ฃผ๋???ํ๋ฉ?
	//?ดํด?์ค???์ฒด???๋ฒ๊ฐ?์กด์ฌ?ฉ๋?? ๊ทธ๊ฒ???๋ค๋ฅ??ฌ์ฉ?๊? ?์?์?๋ฉด
	//๊ทธ์ ?์ ?ฑ์ฌ?์ผ์ฃผ๊ธฐ ?ํจ?๋??
	//?ํ ๋ฒํผ???ด๋ฆญ???ค๋ฅธ ?ด๋ผ?ด์ธ?ธ์๊ฒ??์???์ฒญ?์ฌ ??์ฐฝ์ ?ด์ด์ค๋??
	
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
	
	//?์ฒญ๋ฐ์? ?ฌ๋???์ฒญ?์ฌ?์ ip???ฌํธ๋ฒํธ๋ฅ??๊ธธ?ด์๊ธฐ๋๋ฌธ์,
	//?์ฒญ๋ฐ์? ?ฌ๋???์ฒญ???ฌ๋?๊ฒ ?์ผ???์ก?๊ธฐ?ํด ?ฐ๊ฒฐ? ๋ 
	//?์ฒญ???ฌ๋??ip???ฌํธ๋ฒํธ๋ฅ??์?ด๊ธฐ?ํด ?์?ฉ๋??
	
	public ChatList() {
		
		while(true){
			try {
		
				//?ฌํธ๋ฒํธ???๋ค?ผ๋ก ๋ฐ๋, ๊ฐ์?๊ฒ์ด ?์๊ฒฝ์ฐ ๋ฐ๋ณต?ฉ๋??
				
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
	
		//?ด๋ฆ??์ง??์ผ๋ก???ฅ? ์ ?๊ฒ ๋ณ? ?ค์ ?์???
		
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
		
				//LIST??์ถ๊??๋๋ก??๋ค.
				
				String userName=line.substring(5);
				myButton.add(new JButton(userName));

				for(int i=0;i<myButton.size();i++){
					userList.add(myButton.get(i));
					myButton.get(i).addActionListener(this);
				}
				
				this.frame.setVisible(true);
				
			}else if(line.startsWith("ADDRESS")){
			
				//์ฃผ์๋ฅ?๋ฐ์?ต๋?? ?๋ฒ?์ REQUEST ๋ฅ?๋ฐ์??
				//ADDRESS๋ฅ?๋ณด๋?ผ๋ก?? ?ด๋ผ?ด์ธ?ธ๋ ?๋ค๋ฅ??ด๋ผ?ด์ธ?ธ๋ก ?์???ฉ๋??
				
				flag=true;
				String[] message=line.split(" ");				
				System.out.println(message[1]+"  "+message[2]);
			
				Socket connectSocket=new Socket(message[1],Integer.parseInt(message[2]));	
				System.out.println("my name is "+name);
				System.out.println("connect the "+message[1]+" "+message[2]);
				
				new ChatClient(connectSocket,name,true).start();
				
				
			}else if(line.startsWith("GETPORT")){
		
				//์ฒ์?์???์ ??ip์ฃผ์๋ฐ??ฌํธ๋ฒํธ๋ฅ??๋ฒ???๋ฆฝ?๋ค. 
				
				out.println(InetAddress.getLocalHost().getHostAddress()+" "+(allPort));
				System.out.println(InetAddress.getLocalHost().getHostAddress());
			}
			else if (line.startsWith("SUBMITNAME")) {
				out.println(getName());
				frame.setTitle(name);
			
				//?ด๋ฆ????น?๊ฒ ๋ฐ์?ค์ฌ์ก์ผ๋ฉ? 
				//frame??Destroyer???ค์ ?๋ค.
				
				frame.addWindowListener(new WindowDestroyer(out,name));
			}else if (line.startsWith("NAMEACCEPTED")) {
			}else if (line.startsWith("DELETE")){
			
				//?ฌ์ฉ?๊? ์ข๋ฃ? ๋ ๋ฐ์?๋ค.
				//?ค๋ฅธ ?ด๋ผ?ด์ธ?ธ์ comboBox??list๋ฅ??์ ?๋ค.
				
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
				//?ฌ์ฉ?๊? ?ฐ๊ฒฐ๋ฒํผ???๋ฅธ?ค๋ฉด, ?๋ฒ?๊ฒ REQUEST๋ฅ??ตํด ?์ฒญ?ํฉ?๋ค.
				//๊ทธ๋ฌ๋ฉ??๋ฒ???ค์ ADDRESS?ผ๋ ๊ฒ์ ๋ณด๋ด๊ฒ๋๊ณ?
				//?์??๊ทธ๊ฒ??๋ฐ์ ?ฐ๊ฒฐ?ฉ๋?? 
				System.out.println("REQUEST "+name+" "+command+"\n");
				out.println("REQUEST "+name+" "+command);
				flag=false;
				break;
			}
		}
	}


	private static class InputHandler extends Thread{
		//?ด๋ผ?ด์ธ?ธ๋ ?์ ???๋ฒ๊ฐ??์?ฉ๋?? 
		//?ด๋??ํด inputHandler๋ฅผ๋???? ์ผ๋?๋ก ?ด๋?์ต?๋ค.
		
		private ServerSocket mySocket;
		//?น์ ์ธต์???ค๋ ?๊ตฌ๋ฅ?๋ฐ์?ค์ด๊ธฐ์??๊ณณ์ด??
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
						//์ฑํ?ผ๋ก ?์ฒญ ?๋ ๊ฒฝ์ฐ?๋??
						//์ฒ์?์???ด๊ณณ?ผ๋ก ?์ฒญ?ฉ๋??
						inRead=null;
						new ChatClient(inSocket,name,false).start();
					}else{
						//?์ผ?์ก?ผ๋ก ?์ฒญ?๋ ๊ฒฝ์ฐ?๋??
						//?์?๋ง??คํธ๋ฆ?PrintWiriter๋ก์ด๋ฏ??์บฃ???ค์ ?ด๋?๊ธฐ??
						//๊ธฐ์กด???์ผ???์ด๋ฒ๋ฆฌ๊ณ??๋ก???์บฃ?๋ง?ค์ด
						//ObjectStream?์?์ฌ ?ฌ์ฉ?ฉ๋??
						inSocket.close();
						inSocket=mySocket.accept();
						System.out.println("ACCEPT the OK");
						new FileDown(inSocket).start();
						//๊ต์?๊ป??์ฃผ์  fileDown???์?ฉ๋??
					}
					//?์ผ?ธ์? ?? ?์?ธ์? ๊ฒ??๊ธฐ 
					
					System.out.println(port);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
