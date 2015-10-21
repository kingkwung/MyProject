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

	// ȸ�������� ���Ͽ��� �о �����س��´�.
	private static ArrayList<UserInfo> userInfoList = new ArrayList<>();

	// ���⿡�� ������ ���� �ֿ� �³׸� ���ϴ� ��Ʈ���� �����Ѵ�.
	private static HashMap<String, PrintWriter> clients = new HashMap<String, PrintWriter>();

	String serverIPaddress;
	
	public MainServer() {
		// ���� ���� ���� ����
		// ���� ���� �о ������ ����.
		String fileName = "information.txt";
		Scanner inputStream = null;

		// ���� ��ǲ ��Ʈ�� �����
		try {
			inputStream = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			System.out.println("Error opening the file " + fileName);
			System.exit(0);
		}

		// �о����
		while (inputStream.hasNextLine()) {
			String line = inputStream.nextLine();
			String[] temp = line.split(" ");

			userInfoList.add(new UserInfo(temp[0], temp[1], temp[2], Integer
					.parseInt(temp[3]), Integer.parseInt(temp[4]), Integer
					.parseInt(temp[5]), Integer.parseInt(temp[6]), temp[7]));

			System.out.println(line + " is read successfully");
		}
		
		// IP�ּ� ��� ���� (��� �ʿ� ����)
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

		new MainServer();// ����Ʈ���Ͱ� ���� �Ǿ�� ������ ���� �� �����Ƿ�

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

		// ���� �ð� ���ϴ� �޼ҵ�
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

				// Ŭ���̾�Ʈ �ʿ� �α�â�� ����.
				// ID�� PW�� ���ش޶�� �ǹ̷� Submit_id_pw
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

						// ���Ϸ� ���� ���� �Ͱ� ������ ����
						// userinfoList[i] ��� userInfoList.get(i)
						boolean loginOK = false;

						for (int i = 0; i < userInfoList.size(); i++) {
							if (userInfoList.get(i).getId().equals(id)
									&& userInfoList.get(i).getPw().equals(pw)) {
								loginOK = true;
								break;
							}
						}

						// ����
						if (loginOK) {
							synchronized (clients) {
								// �̹� ���������� �ʴٸ�
								if (!clients.containsKey(id)) {
									// ���� ����
									// id�� output stream�� ����Ѵ�.
									// (Ŭ���̾�Ʈ ������ �޽����� �������� �� ����Ϸ���)
									clients.put(id, out);
									// Ŭ���̾�Ʈ���� ���� �޽��� ������.
									out.println("LOGIN_OK");
									// ������ ���������� �Ǿ����� ���� ������ ����������.
									break;
								} else {
									/* �̹� �������Դϴ�. */
									out.println("AlREADY_LOGON");
								}
							}
						} else {
							/* ���� ���� */
							out.println("LOGIN_CANCEL");
						}

					} else if (inputLine.startsWith("TRY_REGISTER")) {
						// [ "TRY_REGISTER" protocol : Ŭ���̾�Ʈ�� ���� ��û�� �� ��Ȳ ]
						String t[] = inputLine.split(" ");

						// �������� userInfoList�� �߰��ϰ� ���Ͽ� ����.
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

						// �߰��� ��ġ�� ��� �޽����� ���������ش�.
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
						// [ "GET_INFO" protocol : �ѹ� Ŭ���� ���� ������ ���� �޶�� ��û ]

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
				// client�� ���� �� ���Ⱑ ����Ǵµ�,
				// �̶� ��Ŭ���̾�Ʈ�� ������ ������Ʈ �Ǿ��� �� �����Ƿ� ���� �ѹ� ������Ʈ.
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
						userInfoList.get(i).setLastTime(getDate()); // ���� ���а�����
						// ������Ʈ �ȵǴϱ�
						// �׽�Ʈ�� ��¥��
						// ������Ʈ �غ���
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