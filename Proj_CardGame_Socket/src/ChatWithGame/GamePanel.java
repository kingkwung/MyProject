package ChatWithGame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class GamePanel extends JPanel implements ActionListener {
	private JPanel buttonPanel = new JPanel();
	private JButton harfButton;
	private JButton callButton;
	private JButton dieButton;

	private Toolkit myToolkit = Toolkit.getDefaultToolkit();
	private ArrayList<Integer> userCard = new ArrayList<Integer>();
	private ArrayList<Integer> user2Card = new ArrayList<Integer>();;
	Image[] img = new Image[21];
	Image[] person = new Image[2];
	Image[] cash = new Image[5];
	Image[] button = new Image[5];
	JTextField textField1 = new JTextField(20);
	JTextField textField2 = new JTextField(20);
	JTextField textField3 = new JTextField(20);
	private int check;
	private int start;
	private int input;
	private int num;
	private int bet;
	private boolean turn;
	private boolean wcheck;
	private boolean lcheck;
	private int user_money;
	private int user2_money;
	private boolean showCard;
	private cardMove cm;
	private ArrayList<Money> moneyImg = new ArrayList();
	private Image[] wlImage = new Image[2];
	private String user1;
	private String user2;
	

	public GamePanel(String u1,String u2){
		user1 = u1;
		user2 = u2;
		
		img[0] = myToolkit.getImage("img/card.png");
		img[1] = myToolkit.getImage("img/a1.png");
		img[2] = myToolkit.getImage("img/a2.png");
		img[3] = myToolkit.getImage("img/a3.png");
		img[4] = myToolkit.getImage("img/a4.png");
		img[5] = myToolkit.getImage("img/a5.png");
		img[6] = myToolkit.getImage("img/a6.png");
		img[7] = myToolkit.getImage("img/a7.png");
		img[8] = myToolkit.getImage("img/a8.png");
		img[9] = myToolkit.getImage("img/a9.png");
		img[10] = myToolkit.getImage("img/a10.png");
		img[11] = myToolkit.getImage("img/b1.png");
		img[12] = myToolkit.getImage("img/b2.png");
		img[13] = myToolkit.getImage("img/b3.png");
		img[14] = myToolkit.getImage("img/b4.png");
		img[15] = myToolkit.getImage("img/b5.png");
		img[16] = myToolkit.getImage("img/b6.png");
		img[17] = myToolkit.getImage("img/b7.png");
		img[18] = myToolkit.getImage("img/b8.png");
		img[19] = myToolkit.getImage("img/b9.png");
		img[20] = myToolkit.getImage("img/b10.png");
		person[0] = myToolkit.getImage("./ChattingData/userimages/"+u1+".png");
		person[1] = myToolkit.getImage("./ChattingData/userimages/"+u2+".png");
		cash[0] = myToolkit.getImage("img/coin.png");
		cash[1] = myToolkit.getImage("img/coin2.png");
		cash[2] = myToolkit.getImage("img/bill.png");
		cash[3] = myToolkit.getImage("img/bill2.png");
		cash[4] = myToolkit.getImage("img/bill3.png");
		wlImage[0] = myToolkit.getImage("./ChattingData/img/win.png");
		wlImage[1] = myToolkit.getImage("./ChattingData/img/lose.png");
		check = -1;
		start = -1;
		turn = true;
		showCard = false;
		wcheck = lcheck = false;
		setLayout(null);
		setBounds(0, 0, 1200, 700);
		ButtonPanel();
		textField1.setBounds(990, 240, 170, 30);
		textField2.setBounds(20, 620, 170, 30);
		textField3.setBounds(10, 30, 180, 50);
		
		textField1.setText(user2+": "+getStringMoney(user2_money));
		textField2.setText(user1+": "+getStringMoney(user_money));
		textField3.setText("Total Bet:" +getStringMoney(bet));
		textField1.setBackground(new Color(0f,0f,0f,.0f));
		textField2.setBackground(new Color(0f,0f,0f,.0f));
		textField3.setBackground(new Color(0f,0f,0f,.0f));
		textField1.setBorder(new EtchedBorder(Color.black,Color.black));
		textField2.setBorder(new EtchedBorder(Color.black,Color.black));
		textField3.setBorder(new EtchedBorder(Color.black,Color.black));
		textField1.setFont(new Font("Dialog", Font.BOLD, 12));
		textField2.setFont(new Font("Dialog", Font.BOLD, 12));
		textField3.setFont(new Font("Dialog", Font.BOLD, 16));
		textField1.setForeground(Color.white);
		textField2.setForeground(Color.white);
		textField3.setForeground(Color.white);
		
		add(textField1);
		add(textField2);
		add(textField3);
		add(buttonPanel);
		
		cm = new cardMove(this);
		cm.start();
	}
	public void showCard()
	{
		showCard = true;
	}
	
	public void setStart()
	{
		this.start = 0;
	}
	public void setMoney(int money, int money2)
	{
		this.user_money = money;
		this.user2_money = money2;
		textField1.setText(user2+": "+getStringMoney(user2_money));
		textField2.setText(user1+": "+getStringMoney(user_money));
		textField3.setText("Total Bet:" +getStringMoney(bet));
		textField1.setBackground(new Color(0f,0f,0f,.0f));
		textField2.setBackground(new Color(0f,0f,0f,.0f));
		textField3.setBackground(new Color(0f,0f,0f,.0f));
		textField1.setBorder(new EtchedBorder(Color.lightGray,Color.lightGray));
		textField2.setBorder(new EtchedBorder(Color.lightGray,Color.lightGray));
		textField3.setBorder(new EtchedBorder(Color.lightGray,Color.lightGray));
		textField1.setFont(new Font("Dialog", Font.BOLD, 12));
		textField2.setFont(new Font("Dialog", Font.BOLD, 12));
		textField3.setFont(new Font("Dialog", Font.BOLD, 16));
		textField1.setForeground(Color.white);
		textField2.setForeground(Color.white);
		textField3.setForeground(Color.white);
	}
	public void setBet(int bet)
	{
		this.bet = bet;
	}
	public void setInput(int input)
	{
		this.input = input;
	}
	public int getNum()
	{
		return this.num;
	}
	public boolean getTurn()
	{
		return turn;
	}
	public void setTurn()
	{
		if(turn){
			turn = false;
			harfButton.setEnabled(false);
			callButton.setEnabled(false);
			dieButton.setEnabled(false);
		}
		else{
			turn = true;
			harfButton.setEnabled(true);
			callButton.setEnabled(true);
			dieButton.setEnabled(true);
		}
	}
	public void setCard(String cards, boolean check)
	{
		String []tmp = cards.split(" ");
		if(check)
		{
			userCard.add(Integer.parseInt(tmp[0]));
			userCard.add(Integer.parseInt(tmp[1]));
			user2Card.add(Integer.parseInt(tmp[2]));
			user2Card.add(Integer.parseInt(tmp[3]));
		}
		else
		{
			userCard.add(Integer.parseInt(tmp[2]));
			userCard.add(Integer.parseInt(tmp[3]));
			user2Card.add(Integer.parseInt(tmp[0]));
			user2Card.add(Integer.parseInt(tmp[1]));
			this.user_money = Integer.parseInt(tmp[4]);
			this.user_money = Integer.parseInt(tmp[5]);
		}
		this.check = 1;
	}
	public void showWin(){
		wcheck = true;
	}
	public void showLose(){
		lcheck = true;
	}
	public void ButtonPanel()
	{
		buttonPanel.setLayout(new GridLayout(1, 3, 5, 0));

		harfButton = new JButton(new ImageIcon("./ChattingData/img/half.png"));
		harfButton.setActionCommand("half");
		harfButton.setFocusable(false);
		harfButton.setBorderPainted(false);
		buttonPanel.add(harfButton);
		harfButton.addActionListener(this);
		callButton = new JButton(new ImageIcon("./ChattingData/img/call.png"));
		callButton.setActionCommand("call");
		callButton.setFocusable(false);
		callButton.setBorderPainted(false);
		buttonPanel.add(callButton);
		callButton.addActionListener(this);
		dieButton = new JButton(new ImageIcon("./ChattingData/img/die.png"));
		dieButton.setActionCommand("die");
		dieButton.setFocusable(false);
		dieButton.setBorderPainted(false);
		buttonPanel.add(dieButton);
		dieButton.addActionListener(this);

		buttonPanel.setBounds(850, 550, 315, 91);
		buttonPanel.setBackground(new Color(0f,0f,0f,.0f));
	}
	public void moveMoney(boolean check, int money)
	{
		int X = (int) (Math.random()*400) + 400;
		int Y = (int) (Math.random()*200) + 200;
		Money tmp = new Money(check, money, X, Y);
		moneyImg.add(tmp);
	}
	public void distributeCard(Graphics G)
	{
		if(cm.getX2()<900 || cm.getY1()<500)
			cm.plusEachLocation();
		else
			check = 0;
	}
	public void distributeCard2(Graphics G)
	{
		if(cm.getX4()<800 || cm.getY3()<500)
			cm.plusEachLocation2();
		else
			start = 1;
	}
	public ArrayList<Money> getList()
	{
		return moneyImg;
	}
	public void paintComponent(Graphics G)
	{
		G.setColor(Color.green);
		//G.fillRect(0, 0, 1200, 700);
		G.drawImage(Toolkit.getDefaultToolkit().getImage("./ChattingData/img/background(game).png"),0,0,this);   
		G.drawImage(img[0],220,35,80,110,this); // you card
		G.drawImage(img[0],200,55,80,110,this); // me card
		G.drawImage(person[0], 30, 400, 150, 200, this);
		G.drawImage(person[1], 1000, 30, 150, 200, this);
		if(wcheck){
			G.drawImage(wlImage[0], 400, 100, 346, 82,this);
		}else if(lcheck){
			G.drawImage(wlImage[1],  400, 100, 346, 82,this);
		}

		if(check == 1)
		{
			this.distributeCard(G);
			G.drawImage(img[0],cm.getX1(),cm.getY1(),80,110,this);
			G.drawImage(img[0],cm.getX2(),cm.getY2(),80,110,this);
		}
		else if(check == 0)
		{
			G.drawImage(img[userCard.get(0)],cm.getX1(),cm.getY1(),80,110,this);
			if(!showCard)
				G.drawImage(img[0],cm.getX2(),cm.getY2(),80,110,this);
			else
				G.drawImage(img[user2Card.get(0)],cm.getX2(),cm.getY2(),80,110,this);
		}
		if(check == 0 && start == 0)
		{
			this.distributeCard2(G);
			G.drawImage(img[0],cm.getX3(),cm.getY3(),80,110,this);
			G.drawImage(img[0],cm.getX4(),cm.getY4(),80,110,this);
		}
		else if(check == 0 && start == 1)
		{
			G.drawImage(img[userCard.get(1)],cm.getX3(),cm.getY3(),80,110,this);
			if(!showCard)
				G.drawImage(img[0],cm.getX4(),cm.getY4(),80,110,this);
			else
				G.drawImage(img[user2Card.get(1)],cm.getX4(),cm.getY4(),80,110,this);
		}
		for(int i=0; i<moneyImg.size(); i++)
		{
			G.drawImage(cash[moneyImg.get(i).getSelect()],(int)moneyImg.get(i).getX(),(int)moneyImg.get(i).getY(),80,80,this);
		}
	}
	public void actionPerformed(ActionEvent arg0) {
		String tmp = arg0.getActionCommand();
		if(tmp.equals("half"))
		{
			this.num = 1;
			moveMoney(true, input);
			moveMoney(true, input);
			new PlaySound("./ChattingData/sound/money.wav").startMusic();
		}
		else if(tmp.equals("call"))
		{
			this.num = 2;
			moveMoney(true, input);
			moveMoney(true, input);
			new PlaySound("./ChattingData/sound/money.wav").startMusic();
		}
		else
		{
			this.num = 3;
		}
		setTurn();
	}
	public String getStringMoney(int input) {
		int intMoney = input;
		String stringMoney = "";

		if (intMoney >= 1000000) {
			stringMoney += Integer.toString(intMoney / 1000000);
			stringMoney += "억 ";
			intMoney = intMoney % 1000000;
		}

		if (intMoney >= 100000) {
			stringMoney += Integer.toString(intMoney / 100000);
			stringMoney += "천 ";
			intMoney = intMoney % 100000;
		}

		if (intMoney >= 10000) {
			stringMoney += Integer.toString(intMoney / 10000);
			stringMoney += "백 ";
			intMoney = intMoney % 10000;
		}

		return stringMoney+"만원";
	}
}