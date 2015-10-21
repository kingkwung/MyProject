import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

public class MainServer {

	public static final int PORT = 9001;

	// 회원정보를 파일에서 읽어서 저장해놓는다.
	private static ArrayList<UserInfo> userInfoList = new ArrayList<>();

	// 여기에는 연결을 맺은 애와 걔네를 향하는 스트림을 저장한다.
	private static HashMap<String, PrintWriter> clients = new HashMap<String, PrintWriter>();

	String serverIPaddress;
	
	public MainServer() {
		// 서버 생성 되자 마자
		// 유저 정보 읽어서 저장해 놓기.
		String fileName = "information.txt";
		Scanner inputStream = null;

		// 파일 인풋 스트림 만들기
		try {
			inputStream = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("Error opening the file " + fileName);
			System.exit(0);
		}

		// 읽어오기
		while (inputStream.hasNextLine()) {
			String line = inputStream.nextLine();
			String[] temp = line.split(" ");

			userInfoList.add(new UserInfo(temp[0], temp[1], temp[2], Integer
					.parseInt(temp[3]), Integer.parseInt(temp[4]), Integer
					.parseInt(temp[5]), Integer.parseInt(temp[6]), temp[7]));

			System.out.println(line + " is read successfully");
		}
		
		// IP주소 얻어 놓기 (사실 필요 없음)
		try {
			serverIPaddress = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Local Host:");
			System.out.println("\t" + serverIPaddress);
		} catch (UnknownHostException e) {
			System.out.println("Unable to determine this host's address");
		}
	}

	/**
	 * The application main method, which just listens on a port and spawns
	 * handler threads.
	 */
	public static void main(String[] args) throws Exception {

		new MainServer();// 컨스트럭터가 실행 되어야 파일을 읽을 수 있으므로

		System.out.println("\n- The game server is running.");
		ServerSocket listener = new ServerSocket(PORT);
		try {
			while (true) {
				new Handler(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	// Connection Handler, which has a role communicating with client.
	private static class Handler extends Thread {
		private String id;
		private String pw;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		// 현재 시간 구하는 메소드
		public static String getDate() {
			int y, mon, d;
			Calendar now = Calendar.getInstance();
			y = now.get(Calendar.YEAR);
			mon = now.get(Calendar.MONTH) + 1;
			d = now.get(Calendar.DAY_OF_MONTH);

			String date = y + "/" + mon + "/" + d;
			return date;
		}

		public void run() {
			try {

				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				// 클라이언트 쪽에 로긴창을 띄운다.
				// ID와 PW를 전해달라는 의미로 Submit_id_pw
				out.println("SUBMIT_ID_PW");

				while (true) {
					String inputLine = in.readLine();

					if (inputLine == null) {
						return;
					} else if (inputLine.startsWith("TRY_LOGIN")) {
						// "TRY_LOGIN " + idField.getText() + " " +
						// pwField.getText()
						String t[] = inputLine.split(" ");
						id = t[1];
						pw = t[2];

						// 파일로 부터 읽은 것과 대조해 보기
						// userinfoList[i] 대신 userInfoList.get(i)
						boolean loginOK = false;

						for (int i = 0; i < userInfoList.size(); i++) {
							if (userInfoList.get(i).getId().equals(id)
									&& userInfoList.get(i).getPw().equals(pw)) {
								loginOK = true;
								break;
							}
						}

						// 인증
						if (loginOK) {
							synchronized (clients) {
								// 이미 접속중이지 않다면
								if (!clients.containsKey(id)) {
									// 접속 성공
									// id와 output stream을 기록한다.
									// (클라이언트 쪽으로 메시지를 돌려보낼 때 사용하려고)
									clients.put(id, out);
									// 클라이언트에게 성공 메시지 보낸다.
									out.println("LOGIN_OK");
									// 인증이 성공적으로 되었으니 무한 루프를 빠져나간다.
									break;
								} else {
									/* 이미 접속중입니다. */
									out.println("AlREADY_LOGON");
								}
							}
						} else {
							/* 인증 실패 */
							out.println("LOGIN_CANCEL");
						}

					} else if (inputLine.startsWith("TRY_REGISTER")) {
						// [ "TRY_REGISTER" protocol : 클라이언트가 가입 신청을 한 상황 ]
						String t[] = inputLine.split(" ");

						// 서버에선 userInfoList에 추가하고 파일에 쓴다.
						UserInfo tmpInfo = new UserInfo(t[1], t[3], t[2], 0, 0,
								0, 0, getDate());

						userInfoList.add(tmpInfo);

						String fileName = "information.txt";

						PrintWriter outputStream = null;
						FileWriter file = null;
						try {
							file = new FileWriter("information.txt", true);
							outputStream = new PrintWriter(file, true);
						} catch (FileNotFoundException e1) {
							System.out.println("Error opening the file"
									+ fileName);
						} catch (IOException e1) {
							e1.printStackTrace();
						}

						System.out.println(inputLine);
						outputStream.println(tmpInfo.getId().trim() + " "
								+ tmpInfo.getPw().trim() + " "
								+ tmpInfo.getName().trim() + " "
								+ tmpInfo.getWin() + " " + tmpInfo.getLose()
								+ " " + tmpInfo.getImgNum() + " "
								+ tmpInfo.getMoney() + " "
								+ tmpInfo.getLastTime().trim());

						outputStream.close();

						// 추가를 마치고 등록 메시지를 돌려보내준다.
						out.println("REGISTER_OK");
					}
				}

				// ///////////////////////////////////////////////////////
				// [ "UPDATE LIST" protocol : new Client ]
				// String tmp = "";
				// for (String key : clients.keySet()) {
				// boolean connected = false;
				// for (int i = 0; i < connectedList.size(); i++) {
				// if (connectedList.get(i).equals(key)) {
				// tmp += key + " < Playing >,";
				// connected = true;
				// break;
				// }
				// }
				// if (!connected)
				// tmp += key + ",";
				// }
				// send update list protocol to all who is connected with main
				// Server.
				// for (String key : clients.keySet()) {
				// PrintWriter out = clients.get(key);
				// out.println("UPDATE_LIST,"
				// + tmp.substring(0, tmp.length() - 1));
				// }
				// ///////////////////////////////////////////////////////

				// processing loop
				while (true) {
					String input = in.readLine();
					System.out.println(input);

					if (input == null) {
						return;
					}else if (input.startsWith("GET_INFO")) {
						// [ "GET_INFO" protocol : 한번 클릭한 것의 정보를 보내 달라는 요청 ]

						String[] t = input.split(" ");
						String targetId = t[1];

						for (int i = 0; i < userInfoList.size(); i++) {

							if (userInfoList.get(i).getId().equals(targetId)) {
								UserInfo tmpInfo = userInfoList.get(i);
								out.println("SEND_INFO," + tmpInfo.getId()
										+ "," + tmpInfo.getName() + ","
										+ tmpInfo.getWin() + ","
										+ tmpInfo.getLose() + ","
										+ tmpInfo.getImgNum() + ","
										+ tmpInfo.getMoney() + ","
										+ tmpInfo.getLastTime());
							}
						}

					} else if (input.startsWith("LOGOUT")) {
						
					}

				}
				
			} catch (IOException e) {
				System.out.println(e);
				
			} finally {
				// client가 나갈 때 여기가 실행되는데,
				// 이때 이클라이언트의 정보가 업데이트 되었을 수 있으므로 파일 한번 업데이트.
				String fileName = "information.txt";

				PrintWriter outputStream = null;
				FileWriter file = null;
				try {
					file = new FileWriter(fileName);
					outputStream = new PrintWriter(file);
				} catch (FileNotFoundException e1) {
					System.out.println("Error opening the file" + fileName);
					System.exit(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				for (int i = 0; i < userInfoList.size(); i++) {
					String tmp;
					if (userInfoList.get(i).getId().equals(id)) {
						userInfoList.get(i).setLastTime(getDate()); // 아직 승패같은게
						// 업데이트 안되니까
						// 테스트로 날짜만
						// 업데이트 해본거
					}

					tmp = userInfoList.get(i).getId() + " "
							+ userInfoList.get(i).getPw() + " "
							+ userInfoList.get(i).getName() + " "
							+ userInfoList.get(i).getWin() + " "
							+ userInfoList.get(i).getLose() + " "
							+ userInfoList.get(i).getImgNum() + " "
							+ userInfoList.get(i).getMoney() + " "
							+ userInfoList.get(i).getLastTime();

					outputStream.println(tmp);
				}
				outputStream.close();

				// This client is going down! Remove its name and its print
				// writer from the sets, and close its socket.
				if (id != null) {
					clients.remove(id);
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}