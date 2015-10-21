package ChatWithP2PandFile;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class Echo implements ActionListener, Runnable {
    private static String HOST;
    private static int PORT;
    private String other;
    private final JFrame f = new JFrame();
    private final JTextField tf = new JTextField(25);
    private final JTextArea ta = new JTextArea(15, 25);
    private final JButton send = new JButton("Send");
    private final JButton file = new JButton("File");
    private volatile PrintWriter out;
    private Scanner in;
    private Thread thread;
    private Kind kind;
    private JFileChooser fileSelector = new JFileChooser("./");
    private String source=null;

    public static enum Kind {
        Client(100, "name1"), Server(500, "name2");
        private int offset;
        private String activity;

        private Kind(int offset, String activity) {
            this.offset = offset;
            this.activity = activity;
        }
    }

    public Echo(Kind kind,String name,String o,String ipaddress,int port){
        this.kind = kind;
        kind.activity = name;
        other = o;
        HOST = ipaddress;
        PORT = port;
        f.setTitle(name);
        f.getRootPane().setDefaultButton(send);
        f.add(tf, BorderLayout.NORTH);
        f.add(new JScrollPane(ta), BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(send);
        buttons.add(file);
        f.add(buttons, BorderLayout.SOUTH);
        f.setLocation(kind.offset, 300);
        f.pack();
        send.addActionListener(this);
        file.addActionListener(this);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        DefaultCaret caret = (DefaultCaret) ta.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        display(kind.activity + " " + HOST + " on port " + PORT);
        thread = new Thread(this, kind.toString());
    }
    public void start() {
        f.setVisible(true);
        thread.start();
    }

    public void actionPerformed(ActionEvent ae) {
    	if(ae.getActionCommand().equals("Send")){
    		String s = tf.getText();
    		if (out != null) 
    			out.println(s);
    		
    		display(kind.activity + ": " + s);
    		tf.setText("");
    	}else if(ae.getActionCommand().equals("File")){
    		fileSelector.setDialogTitle("Choose the file you want to transfer.");
    		fileSelector.showOpenDialog(f);
    		if(fileSelector.getSelectedFile() == null || fileSelector.getSelectedFile().getName().length() == 0) {
    			ta.append("You should choose a file!!\n");
				return;
			}
    		source = fileSelector.getSelectedFile().getAbsolutePath();
    		ta.append("Start transmission.\n");
    		out.println("FILETRANSFER");
    	}
    }

    public void run(){
    	String otherIP=null, myIP=null;
    	int otherPort=0, myPort=0;
    	
    	Socket socket;
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
				}catch(ConnectException ce){serverSocket.close();}
			}
			
            display("Connected");
            while (true) {
            	String input = in.nextLine();
            	
            	if(input.equals("FILETRANSFER")){
            		//reciever thread 持失
            		int FilePort = (new Random()).nextInt(20000)+36000;  //range to 36000~56000
            		new Thread(new Reciever(FilePort)).start();
            		
            		out.println("FILEOKAY"+FilePort);//send this if server is on.
            	}else if(input.startsWith("FILEOKAY")){
            		//sender thread 持失
            		int recieverPort = Integer.parseInt(input.substring(8));
            		new Thread(new Sender(otherIP,recieverPort,source)).start();	//cause IP is fixed.
            	}else display(other + ": " + input);
            	
            }
        }catch(Exception e){
            display(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private void display(final String s) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                ta.append(s + "\u23CE\n");
            }
        });
    }
}