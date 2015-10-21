package ChatWithGame;
import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Random;

public class ChatServer{
	private static final int PORT = 4343;
	private static LinkedList<User> userData;
	private static LinkedList<User> GamingPerson = new LinkedList<User>();
	private static FileReadWriter PersonFILE;
	private static LinkedList<PersonInfo> Person = getPersonInfo();

	public static LinkedList<PersonInfo> getPersonInfo(){
		userData = new LinkedList<User>();
		PersonFILE = new FileReadWriter("./ChattingData/etc/people.dat");

		return PersonFILE.LoadPersonInfo();
	}
	public static void storePersonInfo(){
		PersonFILE.storePersonInfo(Person);
	}
	public static void main(String[] args) throws Exception {
		System.out.println("The chat server is running.");
		//Person Initiation.(userData도 필요함... 접속한 사용자 접속정보는 알고 있어야지...)
		//Person = getpersoninfo();
		
		/*make default people
		Person = new LinkedList<PersonInfo>();
		//Person.add(null);
		PersonFILE = new FileReadWriter("./ChattingData/etc/people.dat");
		storePersonInfo();
		System.exit(0);//*/
		
		ServerSocket listener = new ServerSocket(PORT);
		try{
			while(true){
				//Run thread!
				new Thread(new User(listener.accept(), userData)).start();
			}
		}finally{
			//End the server.
			listener.close();
		}
	}
	private static class User implements Runnable{		//User class implements 'Runnable' class to use threads
		private String name;
		private Socket clientSocket;
		private BufferedReader inFromClient;
		private PrintStream outToClient;
		private PersonInfo pInfo;
		private final LinkedList<User> userData;	//Use Linked list to manage data of User class.
													//It has lots of user clients who are connected to server.
													/*If it runs, each thread has client name, client's socket data,
													  and client's stream data for one's member variables.*/
		
		public User(Socket theClientSocket, LinkedList<User> theUserData){
			clientSocket = theClientSocket;
			userData = theUserData;
			pInfo=null;
		}
		public String getName(){ return name; }
		public PersonInfo getPersonInfo(String ID){
			if(Person==null) return null;
			for(PersonInfo p : Person){
				if(p.getID().equals(ID))
					return p;
			}
			return null;//Can't Find!!!
		}
		public void run(){
			try{
				inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				outToClient = new PrintStream(clientSocket.getOutputStream());

				outToClient.println("SUBMITINFO");
				while(true){
					String INFO = inFromClient.readLine();
					
					if(INFO.startsWith("REVIVE")){
						String[] input = INFO.substring(6).split("//"); 
						String Data = input[0];
						if(input.length > 1){	//Update protocol: ID//win_loose//gamemoney
							String targetID = input[0];
							for(User person : ChatServer.GamingPerson){
								if(person.pInfo.getID().equals(targetID)){
									if(input[1].equals("WIN"))
										person.pInfo.setWinNum(person.pInfo.getWinNum()+1);
									else person.pInfo.setLooseNum(person.pInfo.getLooseNum()+1);
									
									person.pInfo.setGameMoney(Integer.parseInt(input[2]));
									
									ChatServer.GamingPerson.remove(person);
									ChatServer.userData.add(person);
									synchronized (person){
										ChatServer.storePersonInfo();
									}
									person.outToClient.println("REVIVAL");
									break;
								}
							}
						
							for(User users : ChatServer.userData)
								users.outToClient.println("UPDATE  SERVER->>[User '"+targetID+"' is arrived from game.]");
						}else{
							String targetID = Data;
							for(User person : ChatServer.GamingPerson){
								if(person.pInfo.getID().equals(targetID)){
									ChatServer.GamingPerson.remove(person);
									ChatServer.userData.add(person);
									person.outToClient.println("REVIVAL");
									break;
								}
							}
						
							for(User users : ChatServer.userData)
								users.outToClient.println("UPDATE  SERVER->>[User '"+targetID+"' is arrived from game.]");
						}
						
						return;	//exit~
					}
					
					if(INFO.startsWith("JustAccess")){	//그냥 access하려는 경우
						String theID = INFO.substring(10);
						pInfo = getPersonInfo(theID);
						name = theID;
						synchronized(userData){
							if(!containedUser(theID)){
								ChatServer.userData.add(this);			//	connected to server now.
							}
						}
						break;
					}
					
					if(INFO.startsWith("JOIN")){	//Join
						String Request = INFO.substring(4);
						if(Request.startsWith("CHECK")){	//중복확인
							boolean check = false;
							if(Person==null){
								outToClient.println("ACCEPT");
								continue;
							}
							
							for(PersonInfo p : Person){
								String newID = Request.substring(5);
								if(p.getID().equals(newID)){
									check = true;
									outToClient.println("REJECT");
									break;
								}
							}
							if(!check) outToClient.println("ACCEPT");
							
							continue;
						}else if(Request.startsWith("DATA")){	//회원가입
							String[] data = Request.substring(4).split("//");
							PersonInfo newPerson = new PersonInfo(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]);
							if(Person==null) Person = new LinkedList<PersonInfo>();
							
							Person.add(newPerson);
							storePersonInfo();
							outToClient.println("JOINCOMPLETE");
							continue;
						}
					}else if(INFO.equals("FILETRANSFER")){
		            		//reciever thread 생성
		            		int FilePort = (new Random()).nextInt(20000)+36000;  //range to 36000~56000
		            		new Thread(new Reciever(FilePort)).start();
		            		
		            		outToClient.println("FILEOKAY"+FilePort);//send this if server is on.
		            		
		            		return;
					}else if(INFO.startsWith("LOGINFILEOKAY")){
						String recieverIP = clientSocket.getInetAddress().toString().substring(1);
						String[] data = INFO.substring(13).split("<!!>");
						
						int recieverPort = Integer.parseInt(data[0]);
						String dest = data[1];
	            		new Thread(new Sender(recieverIP,recieverPort, data[2], data[3], dest)).start();	//cause IP is fixed.
	            		outToClient.println("OK");
	            		
	            		return;
					}else{	//Login trial
						if(INFO.startsWith("SORRY"))
							return;
						
						String theID = INFO.substring(0, INFO.indexOf("//"));
						String thePW = INFO.substring(INFO.indexOf("//")+2);
						if(theID == null) return;

						PersonInfo p=getPersonInfo(theID);
						if(p == null){
							outToClient.println("REJECTED"+"WRONGID");
							continue;	//New user aready exist! reject Access.
						}
						if(!p.getPassword().equals(thePW)){	//wrong password...
							outToClient.println("REJECTED"+"WRONGPW");
							continue;
						}
						outToClient.println("ACCEPTED");	//If one client success to connect server then, notify it to all clients who are 
						continue;
					}
				}
				
				for(User users : ChatServer.userData)
					users.outToClient.println("UPDATE  SERVER->>[User '"+pInfo.getID()+"' is arrived.]");

				//Login, Join window do not enter here~!
				//outToClient.println("ACCESSACCEPT");
				while(true){
					String input = inFromClient.readLine();
					if(input == null) return; //Do nothing.
										
					if(input.equals("GiveMeMyData")){//My Information Protocol("name//win//lose//money//major")
						outToClient.println("MyInfo:"+pInfo.getID()+"//"+pInfo.getWinNum()+"//"+pInfo.getLooseNum()+"//"+pInfo.getGameMoney()+"//"+pInfo.getMajor()+"//"+pInfo.getImgPath());
						continue;
					}
					else if(input.startsWith("GiveMeOthersData")){
						String id = input.substring(16);
						String others="";
						for(User users : ChatServer.userData){
							if(users.pInfo.getID().equals(id)) continue;
							//Getting userinfo protocol("'ID/winRate'//'ID/winRate'//")
							others += users.pInfo.getMajor()+"/("+users.pInfo.getWinRate()+") "+users.pInfo.getID()+"//";
						}
						
						outToClient.println("UserInfo:"+others);
						continue;
					}else if(input.startsWith("REQUESTGAME")){	//requesting person
						String targetID = input.substring(11);
						for(User users : ChatServer.userData){
							if(users.pInfo.getID().equals(targetID))
								users.outToClient.println("REQUESTACCESS"+pInfo.getID()+"//"+pInfo.getGameMoney());
						}
						continue;
					}else if(input.startsWith("RESPOND")){	//requesting person
						String respond = input.substring(7);
						if(respond.startsWith("YES")){
							//data protocol: myIP//WaitPort//requestUser
							String[] data = respond.substring(3).split("//");
							String requestID = data[2];
							for(User users : ChatServer.userData){
								if(users.pInfo.getID().equals(requestID)){
									//response_yes protocol: IP//PORT//TargetID//TargetGameMoney
									users.outToClient.println("RESPONSEYES"+data[0]+"//"+data[1]+"//"+pInfo.getID()+"//"+pInfo.getGameMoney());
									//일반 데이터에서 이름빼고 게임중인 애들만 모아놓은 곳에 집어넣는다.
									ChatServer.GamingPerson.add(this);
									ChatServer.userData.remove(this);
									ChatServer.GamingPerson.add(users);
									ChatServer.userData.remove(users);
								}
							}
							for(User users : ChatServer.userData){
								users.outToClient.println("UPDATE  SERVER->>[User '"+requestID+"' is arrived from game.]");
								users.outToClient.println("UPDATE  SERVER->>[User '"+pInfo.getID()+"' is arrived from game.]");
							}
						}else{
							String requestID = respond.substring(2);
							for(User users : ChatServer.userData){
								if(users.pInfo.getID().equals(requestID))
									users.outToClient.println("RESPONSENO"+pInfo.getID());
							}

						}
						continue;
					}else if(input.startsWith("FILEOKAY")){
						String[] data = input.substring(8).split("<!!>");
						String recieverIP = clientSocket.getInetAddress().toString().substring(1);
						int recieverPort = Integer.parseInt(data[0]);
						String dest = data[1];
						PersonInfo other = getPersonInfo(data[2]);
	            		new Thread(new Sender(recieverIP,recieverPort, other.getImgPath(), other.getID(), dest)).start();	//cause IP is fixed.
	            		outToClient.println("OK");
	            		continue;
					}
					
					if(input.startsWith("[#!#?#!#]")){		//If message from one client starts with "[#!#?#!#]" it indicates this message is for whisper.
						String message = input.substring(9);
						for(User users : ChatServer.userData){
							if(users.pInfo.getMajor().equals(pInfo.getMajor()))
								users.outToClient.println("MESSAGE "+ "["+users.pInfo.getMajor() + "] " + name + ": " + message);
						}
					}else{
						for(User users : ChatServer.userData)
							users.outToClient.println("MESSAGE " + name + ": " + input);
					}
				}
			}catch(IOException e){
				System.err.println(e);
			}finally{
				try{
					inFromClient.close();
					outToClient.close();
					clientSocket.close();
				}catch (IOException e){}
				
				if(pInfo!=null){
					for(User users : ChatServer.userData){		//If one client exit, then notify it to all user who are in chatting room.
						if(!users.name.equals(name))
							users.outToClient.println("UPDATE  SERVER->>[User '"+name+"' is left.]");
					}
					ChatServer.userData.remove(this);
				}
			}
		}
		public boolean containedUser(String newName){	//It returns whether user who has passed number is exist or not.
			for(User users : ChatServer.userData){
				if(users.getName().contains(newName))
					return true;
			}			
			return false;
		}
	}
}