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
	
	//�?�� 근본?�인 Server ?�니?? 
	//�?��중추????��???�고 ?�습?�다.
	//?�버???��? ?�트번호???�이??주소�?줄뿐?�니??
	
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
					
						//REQUEST???�정?�름??갖고 그것????�� ?�이??주소???�트번호�?
						//?�아?�는 것입?�다.
						//?�경???�용?�끼�??�신?�에, ?�신???�청받�? ?�람??						//?�신???�청???�람???�이??주소???�트번호�??�기?�한 것입?�다.
						//?�것????���??�청받�??�람???�이?��? ?�송?�때 ?�요?�니??
						
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
				
							//?�름???�인?�었?�시?? 모든 client??list 추�? ?�다.
							
							for(int i=0;i<personInfo.size();i++){
								out.println("LIST "+personInfo.get(i).getName());
							}
							break;
						}
					}
				}
				
				//?�트번호�??�아 ?�니??
			
				out.println("GETPORT");
				String addAndPort=in.readLine();
				myInfo.setPortAndAddress(addAndPort);
				myInfo.setWriter(out);

				//?�로?�어?�사?�의 ?�보�???��?�다.
			
				personInfo.add(myInfo);

				for(int i=0;i<personInfo.size();i++){
					//?�장???�리??메세�?? 보낸??
				
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
					
						//?�용?��? ?�트번호�??�청?�경?�입?�다.
						//?�경??(REQUEST ?�청?�사??주소를알고싶??��???�니??
						//주소를알고싶??��?�의 ?�이?��? ?�트번호�??�송?�니??
						
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
			
						//?�명???�라?�언?��? 종료?�었?�때, 모든?�라?�언?�의 종료�??�리�?						//client?�으�?메세�?? 보내?? 리스?��?, button�??�정?�다.
						for(int i=0;i<personInfo.size();i++){
							personInfo.get(i).getWriter().println("DELETE "+toMessage[1]);
						}
					}

				}
			} catch (IOException e) {
				System.out.println(e);
			} finally {
				if (name != null&&out!=null) {
			
					//?�래?��? 묶어?�앗?��?�?
					//?�시???�거?�야?�다.
					
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
	
		//같�? ?�름???�거???�정 명령?��? ?�름?�로 ?�정?�경??DROP?�다.
		
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