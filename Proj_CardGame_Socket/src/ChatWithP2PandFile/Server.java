package ChatWithP2PandFile;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Random;

public class Server{
	public static final int PORT=777;
	private static LinkedList<SocketPerUser> user = new LinkedList<SocketPerUser>();
	private static LinkedList<ClientDetail> userData = new LinkedList<ClientDetail>();
	
	public static void main(String[] args) throws Exception{
		ServerSocket listener = new ServerSocket(PORT);
		System.out.println("The chat server is running.");
		try{
			while(true){
				new Thread(new SocketPerUser(listener.accept())).start();		//Run thread!
			}
		}finally{
			listener.close();							//End the server.
		}
	}
	public ClientDetail getClient(String name){
		for(ClientDetail user : userData){
			if(user.getCName().equals(name))
				return user;
		}
		return null;	//Error!
	}
	public void addClients(ClientDetail newClient){userData.add(newClient);}
	
	private static class SocketPerUser implements Runnable{
		private Socket clientSocket;
		private BufferedReader inFromClient;
		private PrintStream outToClient;
		private ClientDetail cData;
		
		public SocketPerUser(Socket theClientSocket){
			clientSocket = theClientSocket;
			cData = new ClientDetail();
		}
		private String getClientNames(){
			String names="";
			
			for(ClientDetail person : Server.userData)
				names += (person.getCName()+"//");
			
			return names;
		}
		private String getClientNames(String except){
			String names="";
			
			for(ClientDetail person : Server.userData)
				if(!person.CName.equals(except))
					names += (person.getCName()+"//");
			
			return names;
		}
		private void notify(String txt){
			for(SocketPerUser theUser : Server.user)
					theUser.outToClient.println("UPDATE"+txt);
		}
		private void notify(String except,String txt){
			for(SocketPerUser theUser : Server.user)
				if(!theUser.cData.CName.equals(except))
					theUser.outToClient.println("UPDATE"+txt);
		}
		public int getPossiblePort(){
			int random;
			boolean Denyed;
			
			do{
				Denyed=false;
				random = (new Random()).nextInt(20000)+15000; //range to 15000~35000
				for(SocketPerUser theUser : Server.user){
					if(theUser.cData.portNum == random)
						Denyed=true;
				}
			}while(Denyed);
			
			return random;
		}
		public void run(){
			try{			
				inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				outToClient = new PrintStream(clientSocket.getOutputStream());
				
				outToClient.println("CONNECTED");
				
				//Get UNIQUE client socket's data.
				String name=null;
				boolean IsDuplication=true;
				while(IsDuplication){
					name = inFromClient.readLine();
					
					IsDuplication=false;
					for(SocketPerUser theUser : Server.user){
						if(theUser.cData.CName.equals(name)){
							IsDuplication=true;
							outToClient.println("DENYED");
						}
					}
				}
				outToClient.println("ACCEPTED");
				cData.setCName(name);
				cData.setIP_Adress(""+clientSocket.getInetAddress());
				cData.setPortNum(clientSocket.getPort());
				Server.userData.add(cData);//add this
				Server.user.add(this);
				notify(getClientNames());
				
				while(true){
					String line = inFromClient.readLine();
					if(line.startsWith("REQUESTCHATT")){
						String cname = line.substring(12);
						String ip=null;
						int port = getPossiblePort();
						for(SocketPerUser theUser : Server.user){
							if(theUser.cData.CName.equals(cname)){
								theUser.outToClient.println("CHATTING"+cData.CName+"//"+port);//send to server
								ip = theUser.clientSocket.getInetAddress().toString().substring(1);
								break;
							}
						} outToClient.println("REQUEST"+ip+"//"+port+"//"+cname);//send to client
					}
				}
			}catch(IOException e){
				System.err.println(e);
			}finally{
				try{
					notify(cData.CName,getClientNames(cData.CName));
					inFromClient.close();
					outToClient.close();
					clientSocket.close();
					for(ClientDetail user : userData){
						if(user.getCName().equals(cData.CName))
							userData.remove(user);
					}
					user.remove(this);
				}catch (IOException e){}
			}
		}
	}
	static class ClientDetail{
		private String CName;
		private String IP_Adress;
		private int portNum;

		public String getCName(){return CName;}
		public String getIP_Adress(){return IP_Adress;}
		public int getPortNum(){return portNum;}
		public void setCName(String cName){CName = cName;}
		public void setIP_Adress(String iP_Adress){IP_Adress = iP_Adress;}
		public void setPortNum(int portNum){this.portNum = portNum;}
	}		
}
