package zoipower;

import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class PersonInfo implements Serializable{

	String name;
	PrintWriter writer;
	String address="127.0.0.1";
	int port;
	String portAndAddress;
	public PersonInfo() {
		// TODO Auto-generated constructor stub
	}
	
	
	public String getPortAndAddress() {
		return address+" "+port;
	}
	public void setPortAndAddress(String portAndAddress){
		this.portAndAddress=portAndAddress;
		String[] data=portAndAddress.split(" ");
		port=Integer.parseInt(data[1]);
		address=data[0];
		
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}
	public String getName() {
		return name;
	}
	public PrintWriter getWriter() {
		return writer;
	}
	

}
