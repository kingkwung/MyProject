package ChatWithP2PandFile;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.event.*;
import ChatWithP2PandFile.Echo.Kind;

public class ClientGUI extends JPanel{
	private Client client;
	private JList list;
	private DefaultListModel<String> listModel;
	private JFrame frame;
	MouseListener mouseListener;
	
	public ClientGUI(){
		super(new BorderLayout());
		
		client = new Client(getName());
		listModel = new DefaultListModel();
		
		// Create the list and put it in a scroll pane.
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(10);
		JScrollPane listScrollPane = new JScrollPane(list);
		add(listScrollPane, BorderLayout.CENTER);
		list.validate();

		mouseListener = new MouseAdapter(){
			public void mouseClicked(MouseEvent mouseEvent) {
				JList theList = (JList) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2) {
					int index = theList.locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						Object obj = theList.getModel().getElementAt(index);
						/*test 지울거임*/System.out.println("Double-clicked on: " + obj.toString());
						client.outToServer.println("REQUESTCHATT"+obj.toString());
					}
				}
			}
		};
		list.addMouseListener(mouseListener);
	}
	public void addList(String name){
		listModel.addElement(name);
	}
	public void RefreshList(String[] users){
		listModel.removeAllElements();
		for(int i=0;i<users.length;i++){
			if(!users[i].equals(client.clientName))
				addList(users[i]);
		}
	}
	public String getName(){
		return (String) JOptionPane.showInputDialog(
                 frame,"Enter the User name: \neg. les1016",
                 "Input name",
                 JOptionPane.PLAIN_MESSAGE,
                 null,null,"les1016"); 
	}
	private String getServerAddress(){
        return JOptionPane.showInputDialog(frame,
        								   "Enter IP Address of the Server:",
        								   "Welcome to the Chatter",
        								   JOptionPane.QUESTION_MESSAGE);
    }
	private void createAndShowGUI(){
		// Create and set up the window.
		frame = new JFrame("ChattingServer");
		frame.setPreferredSize(new Dimension(250,400));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		// Create and set up the content pane.
		setOpaque(true); // content panes must be opaque
		frame.setContentPane(this);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	public void createP2PChatting(final String otherName, final String ipaddress, final int s_port){	//server
		//이제 구현해야지...
		EventQueue.invokeLater(new Runnable(){
            public void run(){
                new Echo(Kind.Server, client.clientName/*server_client name*/,otherName,ipaddress,s_port).start();
            }
        });
	}
	public void requestP2PChatting(final String ip, final int s_port, final String otherName){		//client		
		EventQueue.invokeLater(new Runnable(){
            public void run(){
            	new Echo(Kind.Client, client.clientName/*client_client name*/,otherName,ip,s_port).start();
            }
        });
	}
	
	public void valueChanged(ListSelectionEvent arg){}
	private void run() throws IOException{
		String serverAddress = getServerAddress();
		Socket clientSocket = new Socket(serverAddress, 777);
		client.inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		client.outToServer = new PrintStream(clientSocket.getOutputStream());
		
		try{
			String line = client.inFromServer.readLine();
			if (line.startsWith("CONNECTED"))
				client.outToServer.println(client.clientName);
			
			synchronized(listModel){
				while(!client.inFromServer.readLine().equals("ACCEPTED")){
					client.outToServer.println(getName());
				}
			}
			
			while(true){				
				//Implements chat room.
				String lineTmp = client.inFromServer.readLine();
				
				if(lineTmp.startsWith("ARRIVE"))
					RefreshList(lineTmp.substring(6).split("//"));
				else if(lineTmp.startsWith("UPDATE"))
					RefreshList(lineTmp.substring(6).split("//"));
				else if(lineTmp.startsWith("REQUEST")){
					String[] input=null;
					
					input = lineTmp.substring(7).split("//");
					String ip = input[0];
					int s_port = Integer.parseInt(input[1]);
					requestP2PChatting(ip,s_port,input[2]);
				}else if(lineTmp.startsWith("CHATTING")){
					String[] input = lineTmp.substring(8).split("//");
					createP2PChatting(input[0],clientSocket.getLocalAddress().toString().substring(1), Integer.parseInt(input[1]));
				}				
			}
		}catch(IOException e){}
		finally{
			client.inFromServer.close();
			client.outToServer.close();
			clientSocket.close();
		}
	}
	private class Client{
		private String clientName;
		private BufferedReader inFromServer;
		private PrintStream outToServer;
		
		public Client(String name){
			clientName = name;		
		}
		public String getClientName(){return clientName;}
		public void setInFromServer(BufferedReader in){inFromServer = in;}
		public void setOutToServer(PrintStream out){outToServer = out;}
		public BufferedReader getInFromServer(){return inFromServer;}
		public PrintStream getOutToServer(){return outToServer;}
	}
	
	public static void main(String[] args) throws IOException{
		final ClientGUI Gclient = new ClientGUI();    
		
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				Gclient.createAndShowGUI();
			}
		});
		Gclient.run();
	}
}