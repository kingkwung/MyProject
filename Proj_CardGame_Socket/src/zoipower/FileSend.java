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
	
	//ë§¥ë¶?´ê¸°?Œë¬¸?? ??¥ ê²½ë¡œë¥??œì»´?¨í„°ë¥?ê¸°ì??¼ë¡œ ?˜ì??µë‹ˆ?? 
	//?´ê²ƒ???œë²„???‘ì†??ì±„íŒ…???„í•œ ê²ƒê³¼ êµ¬ë¶„?´ì£¼ê¸??„í•´,
	//?œë²„?ì„œ ?¹ì • ë©”ì„¸ì§?? ë³´ëƒ…?ˆë‹¤. 
	
	
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
    		
    		//?¤íŠ¸ë¦???…???¤ë¥´ê¸°ë–„ë¬¸ì— ?œë²„?ì„œ ?˜ëª»ë°›ì„ ???ˆì?ë§?
    		//else ??ê²½ìš°ë¡?ì²˜ë¦¬?˜ì?ê¸°ì— ë¬´ì¡°ê±??Œì¼ë¡??¸ì‹?©ë‹ˆ?? 
    		
    		outputStream.writeObject((String)"FILE\n");
    		
    		//?‘ì†???”ì²­?˜ê³  ?°ì´?°ë? ?„ì†¡?©ë‹ˆ??
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