package ChatWithGame;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

public class Gaming extends Thread implements ActionListener{
	private final String ServerIP;
	private final int ServerPort;
	ServerSocket  serverSocket;
	Socket  chatSocket;
	private String ip;
	private int port;
	private GameFrame maro;
	private boolean server;
	BufferedReader chatin;
	PrintWriter chatout;
	//섯다@
	private ArrayList<Integer> user = new ArrayList<Integer>();
	private ArrayList<Integer> user2 = new ArrayList<Integer>();
	private String []name = new String[32];
	private String User1, User2;
	private int user1_money, user2_money;
	private int bet;
	private int previous_money;
	private int previous_select;
	private int a,b,c,d;
	private boolean check = true;
	private boolean die = true;
	private int num;
	private String ID;
	private String tmp = " ";
	private boolean serverClient=false;

	public Gaming(final String serverIP, final int serverPort, String UserData,boolean check, int port, String ip) throws IOException{
		ServerIP = serverIP;
		ServerPort = serverPort;
		//User Data Protocol: myID//myMoney//otherID//otherMoney
		String[] userdatas = UserData.split("//");

		User1 = userdatas[0];
		user1_money = Integer.parseInt(userdatas[1]);
		User2 = userdatas[2];
		user2_money = Integer.parseInt(userdatas[3]);

		maro = new GameFrame(User1,User2);
		serverClient=check;
		this.ip = ip;
		this.server = check;
		this.port = port;

		maro.addWindowListener(new WindowAdapter() {// 창을 끄면 반대쪽에 bye 프로토콜 보내주고 쓰레드 멈춤
			public void windowClosing(WindowEvent e) {
				new Thread(new AlamToServer(ServerIP, ServerPort, User1)).start();
				chatout.println("EXIT");
			}
		});
	}
	/*
	public Gaming(final int port, final String ip, int bet, int userMoney, int user2Money, final Socket clientsocket) throws IOException{
		this.ip = ip;
		this.die = true;
		this.user_money = userMoney;
		this.user2_money = user2Money;
		previous_money = 0;
		previous_select = 0;
		this.tmp = " ";
		this.bet = bet;

		server = true;
		chatSocket = clientsocket;
		this.chatin = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
		this.chatout = new PrintWriter(chatSocket.getOutputStream(), true);
		maro.viewGame();

	}
	public Gaming(final int port, final String ip, int bet, int userMoney, int user2Money, final ServerSocket serversocket, final Socket clientsocket) throws IOException{
		this.ip = ip;
		this.die = true;
		previous_money = 0;
		previous_select = 0;
		this.tmp = " ";
		this.bet = bet;
		server = false;
		serverSocket = serversocket;
		chatSocket = clientsocket;
		this.chatin = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
		this.chatout = new PrintWriter(chatSocket.getOutputStream(), true);
		prepare(userMoney+10000,user2Money+10000);//서버에서 받게함
		maro.viewGame();
	}*/
	public void prepare(int money, int money2) //서버에서 받게함
	{
		int clientMoney, clientMoney2;
		this.user1_money += money;
		this.user2_money += money2;
		previous_money = 0;
		previous_select = 0;
		choose();
		tmp += user.get(0)+" ";
		tmp += user.get(1)+" ";
		tmp += user2.get(0)+" ";
		tmp += user2.get(1);
		maro.getPanel().setCard(tmp.substring(1), true);
		clientMoney = user1_money ;
		clientMoney2 = user2_money;
		chatout.println("CARD "+tmp+" "+clientMoney+" "+clientMoney2);
	}
	public void choose()
	{
		do{
			a = (int) (Math.random()*20) % 20 +1;
			do{
				b = (int) (Math.random()*20) % 20 +1;
			}while(b == a);
			do{
				c = (int) (Math.random()*20) % 20 +1;
			}while(c == b || b == a || c == a);
			do{
				d = (int) (Math.random()*20) % 20 +1;
			}while(c == b || b == a || c == a || d== c || d == b || d==a);
		}while(a+b%10 == c+d%10);
		System.out.println(""+a+" "+b+" "+c+" "+d);
		user.add(a);
		user.add(b);
		user2.add(c);
		user2.add(d);
	}
	public int betting()
	{
		while(true)
		{
			if(!maro.getPanel().getTurn())
			{
				this.num = maro.getPanel().getNum();
				break;
			}
			System.out.checkError();
		}
		if(num == 1) //하프
		{
			maro.getPanel().setInput(bet/2);
			if(user1_money < bet/2) //올인!
			{
				bet += user1_money;
				maro.getPanel().setBet(bet);
				previous_money = user1_money;
				user1_money = 0;
				maro.getPanel().setMoney(user1_money, user2_money);
				if(previous_select == 3 ||previous_select == 2)
					return 5;
				previous_select = 3;
				return 11;
			}
			else
			{
				previous_money = bet/2;
				previous_select = 1;
				user1_money -= bet/2;
				bet += previous_money;
				maro.getPanel().setBet(bet);
				maro.getPanel().setMoney(user1_money, user2_money);
				return 1;
			}
		}
		else if(num == 2) //콜 
		{
			maro.getPanel().setInput(previous_money);
			if(user1_money < previous_money) //올인
			{
				bet += user1_money;
				maro.getPanel().setBet(bet);
				previous_money = user1_money;
				user1_money = 0;
				maro.getPanel().setMoney(user1_money, user2_money);
				if(previous_select == 2 ||previous_select == 3)
					return 5;
				previous_select = 3;
				return 11;
			}
			else
			{
				user1_money -= previous_money;
				bet += previous_money;
				maro.getPanel().setBet(bet);
				maro.getPanel().setMoney(user1_money, user2_money);
				if(previous_select == 2 ||previous_select == 3)
				{
					return 4;
				}
				previous_select = 2;
				return 2;
			}
		}
		else if(num == 3) //다이
		{
			return 3;
		}
		return 0;
	}
	public void run(){ //chatting main
		int result = 100;
		bet += 20000;
		user1_money -= 10000;
		user2_money -= 10000;
		try {
			if(serverClient){//client case
				chatSocket = new Socket(ip,port);
				this.chatin = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
				this.chatout = new PrintWriter(chatSocket.getOutputStream(), true);
				maro.viewGame();
			}else{
				serverSocket = new ServerSocket(port);
				chatSocket = serverSocket.accept();
				this.chatin = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
				this.chatout = new PrintWriter(chatSocket.getOutputStream(), true);
				prepare(0,0);//서버에서 받게함
				maro.viewGame();
			}
			while(true)
			{
				String line = chatin.readLine();
				System.out.println(line);
				if(line.equals("EXIT")){
					new Thread(new AlamToServer(ServerIP, ServerPort, User1)).start();
					maro.dispose();
					return;	//exit...
				}else if(line.startsWith("CARD")){ //client가 카드랑 돈을 읽음
					String temp = line.substring(6);
					String[] temp2 = temp.split(" ");
					user.add(Integer.parseInt(temp2[2]));
					user.add(Integer.parseInt(temp2[3]));
					user2.add(Integer.parseInt(temp2[0]));
					user2.add(Integer.parseInt(temp2[1]));
					this.user2_money = Integer.parseInt(temp2[4]);
					this.user1_money = Integer.parseInt(temp2[5]);
					maro.getPanel().setCard(temp, false);
					maro.getPanel().setTurn();
					new PlaySound("./ChattingData/sound/card.wav").startMusic();
					chatout.println("THANK");
				}
				else if(line.startsWith("THANK"))
				{
					int a = betting();
					if(a == 1) //하프
					{
						new PlaySound("./ChattingData/sound/half.wav").startMusic();
						if(check)
							chatout.println("BET "+ 1 + " "+1+" "+0); // check = true, half, not all-in
						else
							chatout.println("BET "+ 0 + " "+1+" "+0); // check = false, half, not all-in
					}
					else if(a == 4 && check) // 콜1번 - 패나눠주기
					{
						new PlaySound("./ChattingData/sound/call.wav").startMusic();
						previous_select = 0;
						check = false;
						maro.getPanel().setStart();
						chatout.println("SETSTART "+ 0 + " "+2+" "+0); // check = false, call, not all-in
					}
					else if(a==4 && !check) // 콜 2번 - 승부가르기
					{
						new PlaySound("./ChattingData/sound/call.wav").startMusic();
						die = false;
					}
					else if(a == 5 && check) // 콜1번 - 패나눠주기
					{
						new PlaySound("./ChattingData/sound/allin.wav").startMusic();
						previous_select = 0;
						check = false;
						maro.getPanel().setStart();
						chatout.println("SETSTART "+ 0 + " "+3+" "+0); // check = false, call, not all-in
					}
					else if(a==5 && !check) // 콜 2번 - 승부가르기
					{
						new PlaySound("./ChattingData/sound/allin.wav").startMusic();
						die = false;
					}
					else if(a==2)
					{
						new PlaySound("./ChattingData/sound/call.wav").startMusic();
						if(check)
							chatout.println("BET "+ 1 + " "+2+" "+0); // check = true, half, not all-in
						else
							chatout.println("BET "+ 0 + " "+2+" "+0); // check = false, half, not all-in
					}
					else if(a == 3) //다이
					{
						new PlaySound("./ChattingData/sound/die.wav").startMusic();
						die = true;
						result = -1;
						chatout.println("WIN ");
						break;
					}
					else if(a == 11)
					{
						new PlaySound("./ChattingData/sound/allin.wav").startMusic();
						if(check)
							chatout.println("BET "+ 1 + " "+3+" "+1); // check = true, half, not all-in
						else
							chatout.println("BET "+ 0 + " "+3+" "+1); // check = false, half, not all-in
					}
					else
						System.out.println("error1");
				}
				else if(line.startsWith("BET"))
				{
					maro.getPanel().setTurn();
					String []temp = line.substring(4).split(" "); //1 = check, 2 = 하프 콜 다이, 3 = 올인
					maro.getPanel().moveMoney(false, previous_money);
					maro.getPanel().moveMoney(false, previous_money);
					new PlaySound("./ChattingData/sound/money.wav").startMusic();
					if(temp[0].equals("1"))
						this.check = true;
					else
						this.check = false;
					if(temp[1].equals("1")) //하프
					{
						new PlaySound("./ChattingData/sound/half.wav").startMusic();
						this.previous_select = 1;
						this.previous_money = this.bet/2;
						this.bet += this.previous_money;
						maro.getPanel().setBet(bet);
						user2_money -= this.previous_money;
					}
					else if(temp[1].equals("2"))// 콜
					{
						new PlaySound("./ChattingData/sound/call.wav").startMusic();
						this.previous_select = 2;
						this.bet += this.previous_money;
						maro.getPanel().setBet(bet);
						user2_money -= this.previous_money;
					}
					else 	//올인
					{
						this.previous_select = 3;;
					}
					if(temp[2].equals("1"))
					{
						new PlaySound("./ChattingData/sound/allin.wav").startMusic();
						this.previous_select = 3;
						this.bet += user2_money;
						maro.getPanel().setBet(bet);
						user2_money = 0;
					}
					maro.getPanel().setMoney(user1_money, user2_money);
					int a = betting();
					if(a == 1) //하프
					{
						new PlaySound("./ChattingData/sound/half.wav").startMusic();
						if(check)
							chatout.println("BET "+ 1 + " "+1+" "+0); // check = true, half, not all-in
						else
							chatout.println("BET "+ 0 + " "+1+" "+0); // check = false, half, not all-in
					}
					else if(a == 4 && check) // 콜1번 - 패나눠주기
					{
						new PlaySound("./ChattingData/sound/call.wav").startMusic();
						new PlaySound("./ChattingData/sound/card.wav").startMusic();
						previous_select = 0;
						check = false;
						maro.getPanel().setStart();
						chatout.println("SETSTART "+ 0 + " "+2+" "+0); // check = false, call, not all-in
					}
					else if(a==4 && !check) // 콜 2번 - 승부가르기
					{
						new PlaySound("./ChattingData/sound/call.wav").startMusic();
						die = false;
					}
					else if(a == 4 && check) // 콜1번 - 패나눠주기
					{
						new PlaySound("./ChattingData/sound/call.wav").startMusic();
						previous_select = 0;
						check = false;
						maro.getPanel().setStart();
						chatout.println("SETSTART "+ 0 + " "+2+" "+0); // check = false, call, not all-in
					}
					else if(a==5 && !check) // 콜 2번 - 승부가르기
					{
						new PlaySound("./ChattingData/sound/allin.wav").startMusic();
						die = false;
					}
					else if(a == 5 && check) // 콜1번 - 패나눠주기
					{
						new PlaySound("./ChattingData/sound/allin.wav").startMusic();
						previous_select = 0;
						check = false;
						maro.getPanel().setStart();
						chatout.println("SETSTART "+ 0 + " "+3+" "+0); // check = false, call, not all-in
					}
					else if(a==2)
					{
						new PlaySound("./ChattingData/sound/call.wav").startMusic();
						if(check)
							chatout.println("BET "+ 1 + " "+2+" "+0); // check = true, half, not all-in
						else
							chatout.println("BET "+ 0 + " "+2+" "+0); // check = false, half, not all-in
					}
					else if(a == 3) //다이
					{
						new PlaySound("./ChattingData/sound/die.wav").startMusic();
						die = true;
						chatout.println("WIN ");
						result = -1;
						break;
					}
					else if(a == 11)
					{
						new PlaySound("./ChattingData/sound/allin.wav").startMusic();
						if(check)
							chatout.println("BET "+ 1 + " "+3+" "+1); // check = true, half, not all-in
						else
							chatout.println("BET "+ 0 + " "+3+" "+1); // check = false, half, not all-in
					}
					else
						System.out.println("error1");
				}
				else if(line.startsWith("WIN"))
				{
					user1_money += bet;
					bet = 0;
					result = 1;
					break;
				}
				else if(line.startsWith("LOSE"))
				{
					bet = 0;
					result = -1;
					break;
				}
				/**
				else if(line.startsWith("REGAME"))
				{
					if(!server)
						new Thread(new Chatting(port, ip, bet, user_money, user2_money, serverSocket, chatSocket)).start();
					else
						new Thread(new Chatting(port, ip, bet, user_money, user2_money, chatSocket)).start();
					maro.dispose();
					return;
				}
				 */
				else if(line.startsWith("SETSTART "))
				{
					maro.getPanel().setStart();
					maro.getPanel().setTurn();
					maro.getPanel().moveMoney(false, previous_money);
					maro.getPanel().moveMoney(false, previous_money);
					new PlaySound("./ChattingData/sound/money.wav").startMusic();
					String []temp = line.substring(9).split(" "); //1 = check, 2 = 하프 콜 다이, 3 = 올인
					System.out.println(line);
					if(temp[0].equals("1"))
						this.check = true;
					else
						this.check = false;
					if(temp[1].equals("1")) //하프
					{
						new PlaySound("./ChattingData/sound/half.wav").startMusic();
						this.previous_select = 1;
						this.previous_money = this.bet/2;
						this.bet += this.previous_money;
						maro.getPanel().setBet(bet);
						user2_money -= this.previous_money;
					}
					else if(temp[1].equals("2"))// 콜
					{
						new PlaySound("./ChattingData/sound/call.wav").startMusic();
						this.previous_select = 0; //여기서만
						this.bet += this.previous_money;
						maro.getPanel().setBet(bet);
						user2_money -= this.previous_money;
					}
					else 	//올인
					{
						this.previous_select = 0;;
					}
					if(temp[2].equals("1")) //올인
					{
						new PlaySound("./ChattingData/sound/allin.wav").startMusic();
						this.previous_select = 0; //여기서만
						this.bet += user2_money;
						maro.getPanel().setBet(bet);
						user2_money = 0;
					}
					maro.getPanel().setMoney(user1_money, user2_money);
					int a = betting();
					if(a == 1) //하프
					{
						new PlaySound("./ChattingData/sound/half.wav").startMusic();
						if(check)
							chatout.println("BET "+ 1 + " "+1+" "+0); // check = true, half, not all-in
						else
							chatout.println("BET "+ 0 + " "+1+" "+0); // check = false, half, not all-in
					}
					else if(a == 4 && check) // 콜1번 - 패나눠주기
					{
						new PlaySound("./ChattingData/sound/call.wav").startMusic();
						previous_select = 0;
						check = false;
						maro.getPanel().setStart();
						chatout.println("SETSTART "+ 0 + " "+2+" "+0); // check = false, call, not all-in
					}
					else if(a==4 && !check) // 콜 2번 - 승부가르기
					{
						new PlaySound("./ChattingData/sound/call.wav").startMusic();
						die = false;
					}
					else if(a == 5 && check) // 콜1번 - 패나눠주기
					{
						new PlaySound("./ChattingData/sound/allin.wav").startMusic();
						previous_select = 0;
						check = false;
						maro.getPanel().setStart();
						chatout.println("SETSTART "+ 0 + " "+3+" "+0); // check = false, call, not all-in
					}
					else if(a==5 && !check) // 콜 2번 - 승부가르기
					{
						new PlaySound("./ChattingData/sound/allin.wav").startMusic();
						die = false;
					}
					else if(a==2)
					{
						new PlaySound("./ChattingData/sound/call.wav").startMusic();
						if(check)
							chatout.println("BET "+ 1 + " "+2+" "+0); // check = true, half, not all-in
						else
							chatout.println("BET "+ 0 + " "+2+" "+0); // check = false, half, not all-in
					}
					else if(a == 3) //다이
					{
						new PlaySound("./ChattingData/sound/die.wav").startMusic();
						die = true;
						chatout.println("WIN ");
						result = -1;
						break;
					}
					else if(a == 11)
					{
						new PlaySound("./ChattingData/sound/allin.wav").startMusic();
						if(check)
							chatout.println("BET "+ 1 + " "+3+" "+1); // check = true, half, not all-in
						else
							chatout.println("BET "+ 0 + " "+3+" "+1); // check = false, half, not all-in
					}
					else
						System.out.println("error1");
				}
				else
				{
					System.out.println("error");
				}
				if(!die)
				{
					Decision end = new Decision();
					result = end.battle(user,user2);
					/**
					if(result == 0) //무승부일때
					{
						chatout.println("REGAME");
						if(!server)
							new Thread(new Chatting(port, ip, bet, user_money, user2_money, serverSocket, chatSocket)).start();
						else
							new Thread(new Chatting(port, ip, bet, user_money, user2_money, chatSocket)).start();
						maro.dispose();
						return;
					}
					 */
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(1000);
			maro.getPanel().showCard();
			String state = "";
			if(result == 1){
				user1_money += bet;
				bet = 0;
				System.out.println("You win");
				chatout.println("LOSE");
				state = "WIN"+"//"+user1_money;
				maro.getPanel().showWin();
				new PlaySound("./ChattingData/sound/win"+(new Random().nextInt(2)+1)+".wav").startMusic();
			}else if(result == -1){
				bet = 0;
				System.out.println("You lose");
				chatout.println("WIN");
				state = "LOSE"+"//"+user1_money;
				maro.getPanel().showLose();
				new PlaySound("./ChattingData/sound/lose"+(new Random().nextInt(3)+1)+".wav").startMusic();
			}
			Thread.sleep(8000);
			maro.dispose();
			new Thread(new AlamToServer(ServerIP, ServerPort, User1, state)).start();
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
	private class AlamToServer implements Runnable{
		private String ServerIP;
		private int ServerPort;
		private String userid;
		private BufferedReader inFromServer;
		private PrintStream outToServer;
		private Socket clientSocket=null;
		private String State="";

		public AlamToServer(String IP, int Port, String ID){
			ServerIP = IP;
			ServerPort = Port;
			userid = ID;
		}
		public AlamToServer(String IP, int Port, String ID,String state){
			ServerIP = IP;
			ServerPort = Port;
			userid = ID;
			State = state;
		}
		public void run(){
			try{
				clientSocket = new Socket(ServerIP, ServerPort);
				inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				outToServer = new PrintStream(clientSocket.getOutputStream());

				for(;;){
					String line = inFromServer.readLine();
					if(line==null) break;

					if(line.startsWith("SUBMITINFO")){
						outToServer.println("REVIVE"+userid+"//"+State);
					}
				}
			}catch(IOException e){}
			finally{
				try {
					inFromServer.close();
					outToServer.close();
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
