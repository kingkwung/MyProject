import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import javax.swing.*;

public class ChatClient {
	//Member variable for chatting model.
	String clientName="";
    BufferedReader inFromServer;
    PrintStream outToServer;
    static String users[];
    //Member variable for chatting runner.
    JFrame frame = new JFrame("Chatter");
    JTextField textField = new JTextField();
    JTextArea messageArea = new JTextArea(30, 20);
    JTextArea myInfo = new JTextArea(5,10);
    JTextArea userInfo = new JTextArea(25,10);
    JTextField chatInfo = new JTextField("Chatting room with zero user.");
    final JButton talkWith = new JButton("[To all]:");

    public ChatClient() {
        // Layout GUI
    	/*The three Panel consist of outer-most frame and each panel has some component for compose themselves.
    	  One whose name is DataBox is using for showing users data and
    	  another thing whose name is CharBox is just for the chatting data and
    	  at last the other(inputBox) is for input as literal meaning*/
    	frame.setPreferredSize(new Dimension(520,623));    	
        textField.setEditable(false);
        messageArea.setEditable(false);
        myInfo.setEditable(false);
        userInfo.setEditable(false);
        chatInfo.setEditable(false);
        
        JPanel DataBox = new JPanel(new BorderLayout());
        myInfo.setText("<My Information>");
        userInfo.setText("<Others>");
        DataBox.add(new JScrollPane(myInfo),"North");
        DataBox.add(new JScrollPane(userInfo),"Center");
        
        JPanel ChatBox = new JPanel(new BorderLayout());
        chatInfo.setBackground(Color.white);
        ChatBox.add(chatInfo,"North");
        ChatBox.add(new JScrollPane(messageArea),"South");
        ChatBox.setPreferredSize(new Dimension(25, 20));
        
        JPanel inputBox = createSubUI();
                 
        frame.getContentPane().add(inputBox,"South");
        frame.getContentPane().add(DataBox,"East");
        frame.getContentPane().add(ChatBox, "Center");
        frame.pack();
		frame.setLocation(300,80);
		
        // Add Listeners _ This listener do for client's input.
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(talkWith.getText().equals("[To all]:")){
            		outToServer.println(textField.getText());
                    textField.setText("");
            	}else{
            		String aName = talkWith.getText();
            		aName=aName.substring(0,aName.length()-1);
            		outToServer.println("<<"+aName+">>"+textField.getText());
                    textField.setText("");
            	}
            }
        });
    }    
    private String getServerAddress(){
        return JOptionPane.showInputDialog(frame,
        								   "Enter IP Address of the Server:",
        								   "Welcome to the Chatter",
        								   JOptionPane.QUESTION_MESSAGE);
    }
    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }
    private void run() throws IOException {
        String serverAddress = getServerAddress();
        Socket clientSocket = new Socket(serverAddress, 777);
        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer = new PrintStream(clientSocket.getOutputStream());
        
        try{
        	while(true) {        		
        		String line = inFromServer.readLine();
        		if(line.startsWith("SUBMITNAME"))
        			outToServer.println(getName());
        		else if(line.startsWith("NAMEACCEPTED")){	//From here, a client has capacity to communicate with the server.
        			textField.setEditable(true);
        			outToServer.println("GiveMeMyData");
        		}
        		else if(line.startsWith("UPDATE")){			//If server pass notation of "NAMEACCEPTED" or "UPDATE", then a client then update all user's state.
        			messageArea.append(line.substring(6)+"\n");//Cooperation with under two elseif() condition is done to update user data.
        			outToServer.println("GiveMeMyData");
        		}
        		else if(line.startsWith("MyInfo:")){
        			myInfo.setText("<My Information>\n" + line.substring(7));
        			clientName=line.substring(7);
        			outToServer.println("GiveMeOthersData"+clientName);
        		}
        		else if(line.startsWith("UserInfo:")){
        			userInfo.setText("<Others>\n");
        			users = line.substring(9).split(",");
        			for(int i=0;i<users.length;i++)
        				userInfo.append(users[i]+"\n");
        			int length = users.length+1;
        			if(users[0].isEmpty()) length = 1;
        			chatInfo.setText("Chatting room with "+ length +" user.");
        		}
        		else if(line.startsWith("MESSAGE")){				//It catches Message which is come from server.
        			messageArea.append(line.substring(8) + "\n");
        			messageArea.selectAll();
        			int x = messageArea.getSelectionEnd();
        			messageArea.select(x,x);
        		}
        	}
        }catch(IOException e){}
        finally{
        	inFromServer.close();
        	outToServer.close();
        	clientSocket.close();
        }
    }
    public JPanel createSubUI() {
        //Create the button.
        talkWith.setFocusable(false);
        talkWith.setPreferredSize(new Dimension(100, 30));
        talkWith.setFont(new Font("sansserif", Font.BOLD, 14));
        talkWith.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedName = WhisperWindow.showUsers(
                                        frame,
                                        talkWith,
                                        "Users in chatting room:",
                                        "Secret",
                                        users,
                                        "[To all]",
                                        "roosevelt  ");
                talkWith.setText(selectedName+":");
            }
        });
        
        textField.setPreferredSize(new Dimension(412,30));

        //Create the panel we'll return and set up the layout.
        JPanel inputBox =new JPanel(new BorderLayout());
        inputBox.setLayout(new FlowLayout(FlowLayout.LEADING,1,0));
        
        //Add the labels to the content pane.
        inputBox.add(talkWith);        //Add the button.
        inputBox.add(textField);	   //Add the text field.
        inputBox.setPreferredSize(new Dimension(520,30));
        return inputBox;
    }
    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();        
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setResizable(false);
        client.frame.setVisible(true);
        
        client.run();
    }
}