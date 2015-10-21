package zoipower;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	
	//ê°?¥ ê·¼ë³¸?ì¸ Server ?…ë‹ˆ?? 
	//ê°?¥ì¤‘ì¶”????™œ???˜ê³  ?ˆìŠµ?ˆë‹¤.
	//?œë²„???¨ì? ?¬íŠ¸ë²ˆí˜¸???„ì´??ì£¼ì†Œë§?ì¤„ë¿?…ë‹ˆ??
	
	private static final int PORT = 9008;

	private static ArrayList<PersonInfo> personInfo=new ArrayList<PersonInfo>();

	public static void main(String[] args) throws Exception {
		System.out.println("The chat server is running.");
		
		ServerSocket listener = new ServerSocket(PORT);
		try {
			while (true) {
				new Handler(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	private static class Handler extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader in;
		private ObjectOutputStream ObjectOut;
		private PrintWriter out;
		private PersonInfo myInfo;


		public Handler(Socket socket) {
			this.socket = socket;
			myInfo=new PersonInfo();
		}
		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(),true);
				
				while (true) {
					out.println("SUBMITNAME");
					name = in.readLine();

					System.out.println(name);
					if(name.startsWith("REQUEST")){
					
						//REQUEST???¹ì •?´ë¦„??ê°–ê³  ê·¸ê²ƒ????•œ ?„ì´??ì£¼ì†Œ???¬íŠ¸ë²ˆí˜¸ë¥?
						//?Œì•„?´ëŠ” ê²ƒì…?ˆë‹¤.
						//?´ê²½???¬ìš©?ë¼ë¦??µì‹ ?œì—, ?µì‹ ???”ì²­ë°›ì? ?¬ëŒ??						//?µì‹ ???”ì²­???¬ëŒ???„ì´??ì£¼ì†Œ???¬íŠ¸ë²ˆí˜¸ë¥??Œê¸°?„í•œ ê²ƒì…?ˆë‹¤.
						//?´ê²ƒ????™”ë¥??”ì²­ë°›ì??¬ëŒ???°ì´?°ë? ?„ì†¡? ë•Œ ?„ìš”?©ë‹ˆ??
						
						String[] toMessage=name.split(" ");
						for(int i=0;i<personInfo.size();i++){
							if(personInfo.get(i).getName().equals(toMessage[1])){
								for(int j=0;j<personInfo.size();j++){
									if(personInfo.get(j).getName().equals(toMessage[2])){
										System.out.println(personInfo.get(i).getName()+" "+personInfo.get(j).getName());
										out.println("ADDRESS "+personInfo.get(j).getPortAndAddress());
										System.out.println(personInfo.get(j).getPortAndAddress());
										return ;
									}
								}
							}
						}
					}
					if (name == null) {
						return;
					}
					synchronized (personInfo) {
						if (!checkContain(personInfo,name)) {
							myInfo.setName(name);      
				
							//?´ë¦„???•ì¸?˜ì—ˆ?„ì‹œ?? ëª¨ë“  client??list ì¶”ê? ?œë‹¤.
							
							for(int i=0;i<personInfo.size();i++){
								out.println("LIST "+personInfo.get(i).getName());
							}
							break;
						}
					}
				}
				
				//?¬íŠ¸ë²ˆí˜¸ë¥??Œì•„ ?…ë‹ˆ??
			
				out.println("GETPORT");
				String addAndPort=in.readLine();
				myInfo.setPortAndAddress(addAndPort);
				myInfo.setWriter(out);

				//?ˆë¡œ?¤ì–´?¨ì‚¬?Œì˜ ?•ë³´ë¥???¥?œë‹¤.
			
				personInfo.add(myInfo);

				for(int i=0;i<personInfo.size();i++){
					//?…ì¥???Œë¦¬??ë©”ì„¸ì§?? ë³´ë‚¸??
				
					personInfo.get(i).getWriter().println("LIST "+name);
					System.out.println("name:"+personInfo.get(i).getName()+"ip"+personInfo.get(i).getPortAndAddress());
				}
				out.println("NAMEACCEPTED");

				while (true) {
					String input = in.readLine();

					if (input == null) 
						return;
					
					String[] toMessage=input.split(" ");
					if(toMessage[0].equals("REQUEST")){
					
						//?¬ìš©?ê? ?¬íŠ¸ë²ˆí˜¸ë¥??”ì²­?œê²½?°ì…?ˆë‹¤.
						//?´ê²½??(REQUEST ?”ì²­?œì‚¬??ì£¼ì†Œë¥¼ì•Œê³ ì‹¶??‚¬???…ë‹ˆ??
						//ì£¼ì†Œë¥¼ì•Œê³ ì‹¶??‚¬?Œì˜ ?„ì´?¼ì? ?¬íŠ¸ë²ˆí˜¸ë¥??„ì†¡?©ë‹ˆ??
						
						for(int i=0;i<personInfo.size();i++){
							if(personInfo.get(i).getName().equals(toMessage[1])){
								for(int j=0;j<personInfo.size();j++){
									if(personInfo.get(j).getName().equals(toMessage[2])){
										System.out.println(personInfo.get(i).getName()+" "+personInfo.get(j).getName());
										personInfo.get(i).getWriter().println("ADDRESS "+personInfo.get(j).getPortAndAddress()+" ");
										System.out.println(personInfo.get(j).getPortAndAddress());
										break;
									}
								}
								break;
							}
						}
					}
					else if(toMessage[0].equals("EXIT")){
			
						//?œëª…???´ë¼?´ì–¸?¸ê? ì¢…ë£Œ?˜ì—ˆ?„ë•Œ, ëª¨ë“ ?´ë¼?´ì–¸?¸ì˜ ì¢…ë£Œë¥??Œë¦¬ê³?						//client?¨ìœ¼ë¡?ë©”ì„¸ì§?? ë³´ë‚´?? ë¦¬ìŠ¤?¸ì?, buttonë¥??˜ì •?œë‹¤.
						for(int i=0;i<personInfo.size();i++){
							personInfo.get(i).getWriter().println("DELETE "+toMessage[1]);
						}
					}

				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				if (name != null&&out!=null) {
			
					//?´ë˜?¤ë? ë¬¶ì–´?“ì•—?¼ë?ë¡?
					//?™ì‹œ???œê±°?´ì•¼?œë‹¤.
					
					personInfo.remove(myInfo);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static boolean checkContain(ArrayList<PersonInfo> myInfo,String name){
	
		//ê°™ì? ?´ë¦„???ˆê±°???¹ì • ëª…ë ¹?´ë? ?´ë¦„?¼ë¡œ ?¤ì •? ê²½??DROP?œë‹¤.
		
		if(name.equals("EXIT")||name.equals("all")){
			return false;
		}
		for(int i=0;i<myInfo.size();i++){
			if(myInfo.get(i).getName().equals(name)){
				return true;
			}
		}
		return false;
	}
}