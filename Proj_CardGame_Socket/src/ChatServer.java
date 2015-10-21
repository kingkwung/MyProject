import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class ChatServer{
	private static final int PORT = 777;
	private static LinkedList<User> userData = new LinkedList<User>();

	public static void main(String[] args) throws Exception {
		System.out.println("The chat server is running.");
		ServerSocket listener = new ServerSocket(PORT);
		try{
			while(true){
				new Thread(new User(listener.accept(), userData)).start();		//Run thread!
			}
		}finally{
			listener.close();							//End the server.
		}
	}
	private static class User implements Runnable{		//User class implements 'Runnable' class to use threads
		private String name;
		private Socket clientSocket;
		private BufferedReader inFromClient;
		private PrintStream outToClient;
		private final LinkedList<User> userData;	//Use Linked list to manage data of User class.
													//It has lots of user clients who are connected to server.
													/*If it runs, each thread has client name, client's socket data,
													  and client's stream data for one's member variables.*/
		
		public User(Socket theClientSocket, LinkedList<User> theUserData){
			clientSocket = theClientSocket;
			userData = theUserData;
		}
		public String getName(){ return name; }
		public void run(){
			try{
				inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				outToClient = new PrintStream(clientSocket.getOutputStream());

				while (true) {
					outToClient.println("SUBMITNAME");
					String newName = inFromClient.readLine();
					if (newName == null) return;
					synchronized (userData){
						if (!containedUser(newName)) {
							name = newName;
							break;
						}
					}
				}
				outToClient.println("NAMEACCEPTED");	//If one client success to connect server then, notify it to all clients who are 
				ChatServer.userData.add(this);			//	connected to server now.
				for(User users : userData){
					users.outToClient.println("UPDATE  SERVER->>[User '"+name+"' is arrive.]");
				}

				while(true){
					String input = inFromClient.readLine();
					if(input == null) return; //Do nothing.
										
					if(input.equals("GiveMeMyData")){
						outToClient.println("MyInfo:"+name);
						continue;
					}
					else if(input.startsWith("GiveMeOthersData")){
						name=input.substring(16);
						String others="";
						for(User users : userData){
							if(users.name.equals(name)) continue;
							others += users.name+",";
						}
						outToClient.println("UserInfo:"+others);
						continue;
					}
					
					if(input.startsWith("<<")){		//If message from one client starts with "<<" it indicates this message is for whisper.
						int nameIndex = input.indexOf(">>")+1;
						String aName = input.substring(2,nameIndex-1);
						String message = input.substring(nameIndex+1);
						boolean IsExist=false;
						if(name.equals("[To all]")){
							for(User users : userData)
								users.outToClient.println("MESSAGE " + name + ": " + input);
						}else{
							for(User users : userData){
								if(users.name.equals(aName)){
									users.outToClient.println("MESSAGE " + "<Whisper_ "+name+">: " + message);
									IsExist=true;
								}else if(users.name.endsWith(name)){	//User name is equal to oneself.
									users.outToClient.println("MESSAGE " + "<Whisper to_ "+aName+">: " + message);
								}
							}
							if(!IsExist)	//If there are no user who is targeted by other clients for whispering then show ERROR message to the client.
								outToClient.println("MESSAGE " + "  SERVER->>[ERROR::The user '" + aName + "' is not exist here]");
						}
					}
					else{
						for(User users : userData)
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
					userData.remove(this);
				}catch (IOException e){}
				
				for(User users : userData){		//If one client exit, then notify it to all user who are in chatting room.
					if(!users.name.equals(name))
						users.outToClient.println("UPDATE  SERVER->>[User '"+name+"' is left.]");
				}
			}
		}
		public boolean containedUser(String newName){	//It returns whether user who has passed number is exist or not.
			for(User users : userData){
				if(users.getName().contains(newName))
					return true;
			}			
			return false;
		}
	}
}