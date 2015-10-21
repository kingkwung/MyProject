package zoipower;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
 
public class FileSend extends Thread {
	
	//맥북?�기?�문?? ??�� 경로�??�컴?�터�?기�??�로 ?��??�니?? 
	//?�것???�버???�속??채팅???�한 것과 구분?�주�??�해,
	//?�버?�서 ?�정 메세�?? 보냅?�다. 
	
	
    private ObjectOutputStream outputStream = null;
    private String sourceFilePath =null;
    private FileEvent fileEvent = null;
    private String destinationPath = "/Users/sungjinpark/Downloads/temp/";
 
    int port;
    InetAddress address;
    Socket socket;
    
    public FileSend(Socket socket,String sourceFilePath){
    	try {
    		port=socket.getPort();
    		address=socket.getInetAddress();
    		this.socket=socket;
    
    		this.sourceFilePath=sourceFilePath;
    		System.out.println(sourceFilePath);

    		outputStream=new ObjectOutputStream(socket.getOutputStream());
    		
    		//?�트�???��???�르기떄문에 ?�버?�서 ?�못받을 ???��?�?
    		//else ??경우�?처리?��?기에 무조�??�일�??�식?�니?? 
    		
    		outputStream.writeObject((String)"FILE\n");
    		
    		//?�속???�청?�고 ?�이?��? ?�송?�니??
    		socket=new Socket(address,port);
    		outputStream=new ObjectOutputStream(socket.getOutputStream());
    		
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    
    public void run() {
    
		
		fileEvent = new FileEvent();
        String fileName = sourceFilePath.substring(sourceFilePath.lastIndexOf("/") + 1, sourceFilePath.length());
        String path = sourceFilePath.substring(0, sourceFilePath.lastIndexOf("/") + 1);
        fileEvent.setDestinationDirectory(destinationPath);
        fileEvent.setFilename(fileName);
        fileEvent.setSourceDirectory(sourceFilePath);
        File file = new File(sourceFilePath);
        if (file.isFile()) {
            try {
                DataInputStream diStream = new DataInputStream(new FileInputStream(file));
                long len = (int) file.length();
                byte[] fileBytes = new byte[(int) len];
                int read = 0;
                int numRead = 0;
                while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read,
                        fileBytes.length - read)) >= 0) {
                    read = read + numRead;
                }
                fileEvent.setFileSize(len);
                fileEvent.setFileData(fileBytes);
                fileEvent.setStatus("Success");
            } catch (Exception e) {
                e.printStackTrace();
                fileEvent.setStatus("Error");
            }
        } else {
            System.out.println("path specified is not pointing to a file");
            fileEvent.setStatus("Error");
        }
        //Now writing the FileEvent object to socket
        try {
            outputStream.writeObject(fileEvent);
            System.out.println("Done...Going to exit");
            Thread.sleep(3000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
 
    }
 
}