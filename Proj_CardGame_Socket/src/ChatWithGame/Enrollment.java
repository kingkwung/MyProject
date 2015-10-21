package ChatWithGame;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.border.*;

public class Enrollment extends JFrame implements Runnable, ActionListener{
	//Values for the fields
    private LinkedList<PersonInfo> enrolledUsers;
    private final String ServerIPAddress;
    private final int ServerPortNumber;
    private BufferedReader inFromServer;
	private PrintStream outToServer;
	private Socket clientSocket;
	private String ID;
	private boolean checkID=false;

    private JButton dupCheckButton;
    private JButton picButton;
    private JButton okButton;
    private JButton cancelButton;
    
    private JFileChooser fileSelector;
    private String source;
    
    //Labels to identify the fields
    private JLabel IDLabel;
    private JLabel PasswordLabel;
    private JLabel RePasswordLabel;
    private JLabel nameLabel;
    private JLabel sexLabel;
	private JLabel majorLabel;
	private JLabel StudentNumLabel;
	private JLabel phoneNumLabel;
	private JLabel photoLabel;
	private JButton confirm;
	private JButton cancel;

    //Fields for data entry
    private JTextField IDTextField;
    private JPasswordField PasswordTextField;
    private JPasswordField RePasswordTextField;
    private JTextField nameTextField;
    //private ButtonGroup group = new ButtonGroup();
    private JRadioButton ManButton;
    private JRadioButton WomanButton;
    private JPanel sexPanel;
    private JComboBox MajorList;
    private JTextField StudentNumTextField;
    private JTextField phoneNumTextField1;
    private JTextField phoneNumTextField2;
    private JTextField phoneNumTextField3;
    private JTextField photoTextField;
    
    private JPanel AbataPanel;
    private JLabel AbataLabel;

    public Enrollment(final String serverIP, final int serverPort){
    	ServerIPAddress = serverIP;
    	ServerPortNumber = serverPort;
    	
    	fileSelector = new JFileChooser("./");
        source="./ChattingData/img/no_img.png";
        
        //ETC
        picButton = new JButton("사진 첨부");
        picButton.setFocusable(false);
        picButton.addActionListener(this);
		   	
        JPanel MainPanel = new JPanel();
        MainPanel.setLayout(null);

        //Create the labels.
        IDLabel = new JLabel("ID: ");
        PasswordLabel = new JLabel("Password: ");
        RePasswordLabel = new JLabel("Retype Password: ");
        nameLabel = new JLabel("Name: ");
        sexLabel = new JLabel("Sex: ");
    	majorLabel = new JLabel("Major: ");
    	StudentNumLabel = new JLabel("Student Number: ");
    	phoneNumLabel = new JLabel("Phone Number: ");
    	photoLabel = new JLabel("File Attachment: ");

        //Create the text fields and set them up.
    	IDTextField = new JTextField("");
        PasswordTextField = new JPasswordField("");
        RePasswordTextField = new JPasswordField("");
        nameTextField = new JTextField("");
        ManButton = new JRadioButton("Man");
        ManButton.setSelected(true);
        WomanButton = new JRadioButton("Woman");
        ButtonGroup group = new ButtonGroup();
        group.add(ManButton);
        group.add(WomanButton);
        sexPanel = new JPanel(new GridLayout(1, 2));
        sexPanel.add(ManButton);
        sexPanel.add(WomanButton);

        //Create Major ComboBox
        String[] majStrings = { "Software Design & Management", "Business Administraion", "Statistics", "Art", "Sculpture" };
        MajorList = new JComboBox(majStrings);
        MajorList.setSelectedIndex(4);
        StudentNumTextField = new JTextField("");
        phoneNumTextField1 = new JTextField("");
        phoneNumTextField2 = new JTextField("");
        phoneNumTextField3 = new JTextField("");
        photoTextField = new JTextField();

        JPanel PhoneNumPanel = new JPanel();        
        phoneNumLabel.setLayout(null);
        phoneNumTextField1.setColumns(3);
        PhoneNumPanel.add(phoneNumTextField1);
        PhoneNumPanel.add(new JLabel("-"));
        phoneNumTextField2.setColumns(3);
        PhoneNumPanel.add(phoneNumTextField2);
        PhoneNumPanel.add(new JLabel("-"));
        phoneNumTextField3.setColumns(3);
        PhoneNumPanel.add(phoneNumTextField3);

        GridLayout grid = new GridLayout(9,1);
        grid.setVgap(10);
        //Lay out the labels in a panel.
        JPanel labelPane = new JPanel(grid);
        labelPane.add(IDLabel);
        labelPane.add(PasswordLabel);
        labelPane.add(RePasswordLabel);
        labelPane.add(nameLabel);
        labelPane.add(sexLabel);
        labelPane.add(majorLabel);
        labelPane.add(StudentNumLabel);
        labelPane.add(phoneNumLabel);
        labelPane.add(photoLabel);
        labelPane.setBounds(0, 120, 110, 300);
        labelPane.setBackground(Color.LIGHT_GRAY);

        //Layout the text fields in a panel.
        JPanel fieldPane = new JPanel(grid);
        fieldPane.add(IDTextField);
        fieldPane.add(PasswordTextField);
        fieldPane.add(RePasswordTextField);
        fieldPane.add(nameTextField);
        fieldPane.add(sexPanel);
        fieldPane.add(MajorList);
        fieldPane.add(StudentNumTextField);
        fieldPane.add(PhoneNumPanel);
        fieldPane.add(picButton);
        fieldPane.setBounds(115, 122, 150, 300);

        //이거슨 나중에 위쪽에 첨부...
        ImageIcon LogoImage = new ImageIcon("./ChattingData/img/logoEnrol.png");
        Image tmpImg = LogoImage.getImage();
        Image newImg = tmpImg.getScaledInstance(200, 110, java.awt.Image.SCALE_SMOOTH);
        LogoImage = new ImageIcon(newImg);
        JLabel Imagelabel = new JLabel(LogoImage);
        JPanel LogoPanel = new JPanel();
        LogoPanel.setBounds(0, 0, 200, 110);
        //LogoPanel.setBackground(Color.black);
        LogoPanel.add(Imagelabel);
        Border paneEdge = BorderFactory.createEmptyBorder(0,10,10,10);         
        paneEdge = BorderFactory.createLineBorder(Color.gray);
        //LogoPanel.setBorder(paneEdge);
        
        ImageIcon UserImage = new ImageIcon(source);
        tmpImg = UserImage.getImage();
        newImg = tmpImg.getScaledInstance(120, 114, java.awt.Image.SCALE_SMOOTH);
        UserImage = new ImageIcon(newImg);
        AbataLabel = new JLabel(UserImage);
        AbataPanel = new JPanel();
        AbataPanel.setBounds(215, -6, 120, 120);
        //AbataPanel.setBackground(Color.red);
        AbataPanel.add(AbataLabel);
        paneEdge = BorderFactory.createEmptyBorder(0,10,10,10);         
        paneEdge = BorderFactory.createLineBorder(Color.gray);
        AbataPanel.setBorder(paneEdge);
        
        //Layout the picture in a panel.
        JPanel pictureAttatchPane = new JPanel(new FlowLayout());
        pictureAttatchPane.add(photoTextField);
        
        confirm = new JButton("CONFIRM");
        confirm.setFocusable(false);
        cancel = new JButton("Cancel");
        cancel.setFocusable(false);
        JPanel buttonPane = new JPanel(new GridLayout(1,0));
        buttonPane.add(confirm);
        buttonPane.add(cancel);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        dupCheckButton = new JButton("중복");
        dupCheckButton.addActionListener(this);
        dupCheckButton.setBounds(270, 125, 60, 20);
        okButton = new JButton("OK");
        okButton.addActionListener(this);
        okButton.setBounds(60, 425, 100, 30);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setBounds(180, 425, 100, 30);
        dupCheckButton.setFocusable(false);
        okButton.setFocusable(false);
        cancelButton.setFocusable(false);
        
        //Put the panels in this panel, labels on left, text fields on right.
        MainPanel.setBorder(BorderFactory.createEmptyBorder(210, 20, 20, 20));
        MainPanel.add(labelPane);
        MainPanel.add(fieldPane);
        MainPanel.add(LogoPanel);
        MainPanel.add(AbataPanel);
        MainPanel.add(dupCheckButton);
        MainPanel.add(okButton);
        MainPanel.add(cancelButton);
        
        MainPanel.add(pictureAttatchPane);
        MainPanel.add(buttonPane);
        MainPanel.setBounds(10, 32, 300, 400);
        //MainPanel.setBackground(Color.black);
        add(MainPanel);
        
        setPreferredSize(new Dimension(350,500));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

	public void run(){
		setTitle("회원가입");

        //Display the window.
        setLocation(240, 200);
        pack();
        setVisible(true);
        try {
			clientSocket = new Socket(ServerIPAddress, ServerPortNumber);
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer = new PrintStream(clientSocket.getOutputStream());

			for(;;){
				String line = inFromServer.readLine();
				if(line.startsWith("SUBMITINFO"))
					System.out.println("Server connection OK.");
				else if(line.equals("REJECT"))
					JOptionPane.showMessageDialog(this,"The ID aready exist!");
				else if(line.startsWith("ACCEPT")){
					checkID=true;
					ID = IDTextField.getText();
					JOptionPane.showMessageDialog(this,"ID ACCEPTED.");
				}else if(line.equals("JOINCOMPLETE")){
					System.out.println(source);
					if(source.equals("./ChattingData/img/no_img.png")){
						source = new File(source).getAbsolutePath();
					}
					outToServer.println("FILETRANSFER");
					dispose();
					JOptionPane.showMessageDialog(this,"Complete Join~");
				}else if(line.startsWith("FILEOKAY")){
            		//sender thread 생성
            		int recieverPort = Integer.parseInt(line.substring(8));
            		new Thread(new Sender(ServerIPAddress,recieverPort, source, ID)).start();	//cause IP is fixed.
            	}
			}
		}catch(IOException e){
		}finally{
			try{
				inFromServer.close();
				outToServer.close();
				clientSocket.close();
			}catch (IOException e){}
		}
	}
	
	/*/Runner
	public static void main(String[] args) {
		(new Thread(new Enrollment("127.0.0.1",777))).start();
	}*/

	public void actionPerformed(ActionEvent ev) {
		if(ev.getActionCommand().equals("OK")){
			//check ID repetition
			if(!checkID || !ID.equals(IDTextField.getText())){
				JOptionPane.showMessageDialog(this,"Please check ID repetition!","ERROR!!!",1);
				return;
			}
			
			//check input format
			if(!CheckInputFormat()) return;
			
			//enroll
			String AllInput = IDTextField.getText();
			AllInput += "//" + PasswordTextField.getText();
			AllInput += "//" + nameTextField.getText();
			if(ManButton.isSelected()) AllInput += "//Man";
			else AllInput += "//Woman";
			AllInput += "//" + MajorList.getSelectedItem();
			AllInput += "//" + StudentNumTextField.getText();
			AllInput += "//" + phoneNumTextField1.getText()+"-"+phoneNumTextField2.getText()+"-"+phoneNumTextField3.getText();
			AllInput += "//" + source.substring(source.lastIndexOf(".")+1);
			System.out.println(source.substring(source.lastIndexOf(".")+1));
			//AllInput += "//" + source;
			
			outToServer.println("JOIN"+"DATA"+AllInput);
			//String[] input = AllInput.split("//");
			//for(int i=0;i<input.length;i++){
			//System.out.println(input[i]);
			//}
		}else if(ev.getActionCommand().equals("중복")){
			String inputID = IDTextField.getText();
			outToServer.println("JOIN"+"CHECK"+inputID);
		}else if(ev.getActionCommand().equals("사진 첨부")){
			fileSelector.setDialogTitle("Choose the file you want to transfer.");
    		fileSelector.showOpenDialog(this);
    		if(fileSelector.getSelectedFile()==null || fileSelector.getSelectedFile().getName().length()==0) {
    			JOptionPane.showMessageDialog(this,"You should choose a file!!\n","ERROR!!!",1);
				return;
			}
    		source = fileSelector.getSelectedFile().getAbsolutePath();
            ImageIcon UserImage = new ImageIcon(source);
            Image tmpImg = UserImage.getImage();
            Image newImg = tmpImg.getScaledInstance(120, 114, java.awt.Image.SCALE_SMOOTH);
            UserImage = new ImageIcon(newImg);
            AbataLabel.setIcon(UserImage);
		}else if(ev.getActionCommand().equals("Cancel")){
			dispose();
		}
	}
	public boolean CheckInputFormat(){
		//비어있는값없게
		if(IDTextField.getText().equals("")){
			JOptionPane.showMessageDialog(this,"You should enter ID!!\n","ERROR!!!",1);
			return false;
		}else if(PasswordTextField.getText().equals("") || RePasswordTextField.getText().equals("")){
			JOptionPane.showMessageDialog(this,"You should enter password!!\n","ERROR!!!",1);
			return false;
		}else if(StudentNumTextField.getText().equals("")){
			JOptionPane.showMessageDialog(this,"You should enter ID!!\n","ERROR!!!",1);
			return false;
		}else if(phoneNumTextField1.getText().equals("")||phoneNumTextField2.getText().equals("")||phoneNumTextField3.getText().equals("")){
			JOptionPane.showMessageDialog(this,"You should enter Phone Number!!\n","ERROR!!!",1);
			return false;
		}

		//비번두개 일치
		if(!PasswordTextField.getText().equals(RePasswordTextField.getText()))
			return false;
		
		return true;
	}
}