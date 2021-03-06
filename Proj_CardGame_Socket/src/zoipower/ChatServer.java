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
	
	//๊ฐ?ฅ ๊ทผ๋ณธ?์ธ Server ?๋?? 
	//๊ฐ?ฅ์ค์ถ???????๊ณ  ?์ต?๋ค.
	//?๋ฒ???จ์? ?ฌํธ๋ฒํธ???์ด??์ฃผ์๋ง?์ค๋ฟ?๋??
	
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
					
						//REQUEST???น์ ?ด๋ฆ??๊ฐ๊ณ  ๊ทธ๊ฒ???? ?์ด??์ฃผ์???ฌํธ๋ฒํธ๋ฅ?
						//?์?ด๋ ๊ฒ์?๋ค.
						//?ด๊ฒฝ???ฌ์ฉ?๋ผ๋ฆ??ต์ ?์, ?ต์ ???์ฒญ๋ฐ์? ?ฌ๋??						//?ต์ ???์ฒญ???ฌ๋???์ด??์ฃผ์???ฌํธ๋ฒํธ๋ฅ??๊ธฐ?ํ ๊ฒ์?๋ค.
						//?ด๊ฒ????๋ฅ??์ฒญ๋ฐ์??ฌ๋???ฐ์ด?ฐ๋? ?์ก? ๋ ?์?ฉ๋??
						
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
				
							//?ด๋ฆ???์ธ?์?์?? ๋ชจ๋  client??list ์ถ๊? ?๋ค.
							
							for(int i=0;i<personInfo.size();i++){
								out.println("LIST "+personInfo.get(i).getName());
							}
							break;
						}
					}
				}
				
				//?ฌํธ๋ฒํธ๋ฅ??์ ?๋??
			
				out.println("GETPORT");
				String addAndPort=in.readLine();
				myInfo.setPortAndAddress(addAndPort);
				myInfo.setWriter(out);

				//?๋ก?ค์ด?จ์ฌ?์ ?๋ณด๋ฅ???ฅ?๋ค.
			
				personInfo.add(myInfo);

				for(int i=0;i<personInfo.size();i++){
					//?์ฅ???๋ฆฌ??๋ฉ์ธ์ง?? ๋ณด๋ธ??
				
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
					
						//?ฌ์ฉ?๊? ?ฌํธ๋ฒํธ๋ฅ??์ฒญ?๊ฒฝ?ฐ์?๋ค.
						//?ด๊ฒฝ??(REQUEST ?์ฒญ?์ฌ??์ฃผ์๋ฅผ์๊ณ ์ถ??ฌ???๋??
						//์ฃผ์๋ฅผ์๊ณ ์ถ??ฌ?์ ?์ด?ผ์? ?ฌํธ๋ฒํธ๋ฅ??์ก?ฉ๋??
						
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
			
						//?๋ช???ด๋ผ?ด์ธ?ธ๊? ์ข๋ฃ?์?๋, ๋ชจ๋ ?ด๋ผ?ด์ธ?ธ์ ์ข๋ฃ๋ฅ??๋ฆฌ๊ณ?						//client?จ์ผ๋ก?๋ฉ์ธ์ง?? ๋ณด๋ด?? ๋ฆฌ์ค?ธ์?, button๋ฅ??์ ?๋ค.
						for(int i=0;i<personInfo.size();i++){
							personInfo.get(i).getWriter().println("DELETE "+toMessage[1]);
						}
					}

				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				if (name != null&&out!=null) {
			
					//?ด๋?ค๋? ๋ฌถ์ด?์?ผ๋?๋ก?
					//?์???๊ฑฐ?ด์ผ?๋ค.
					
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
	
		//๊ฐ์? ?ด๋ฆ???๊ฑฐ???น์  ๋ช๋ น?ด๋? ?ด๋ฆ?ผ๋ก ?ค์ ? ๊ฒฝ??DROP?๋ค.
		
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