package zoipower;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
 
public class FileDown extends Thread{
	
	//?åÏùº ?§Ïö¥???ÑÌïú ?¥Îûò?§ÏûÖ?àÎã§. 
	//?òÏ†ïÎ∂?∂Ñ???∞Î†à?úÎ? Ï∂îÍ??òÏó¨ ?åÎ†∏?µÎãà?? 
	
    private ObjectInputStream inputStream = null;
    private FileEvent fileEvent;
    private File dstFile = null;
    private FileOutputStream fileOutputStream = null;
    PrintWriter writer;
    Socket socket;
    public FileDown(Socket socket) {
    	this.socket=socket;
    	
    }
    public void run() {
        try {
        	
        	System.out.println("running....");
    		
        	inputStream=new ObjectInputStream(socket.getInputStream());
    	    
            fileEvent=(FileEvent)inputStream.readObject();
            if (fileEvent.getStatus().equalsIgnoreCase("Error")) {
                System.out.println("Error occurred ..So exiting");
            }
            String outputFile = fileEvent.getDestinationDirectory() + fileEvent.getFilename();
            if (!new File(fileEvent.getDestinationDirectory()).exists()) {
                new File(fileEvent.getDestinationDirectory()).mkdirs();
            }
            dstFile = new File(outputFile);
            fileOutputStream = new FileOutputStream(dstFile);
            fileOutputStream.write(fileEvent.getFileData());
            fileOutputStream.flush();
            fileOutputStream.close();
            System.out.println("Output file : " + outputFile + " is successfully saved ");
            Thread.sleep(3000);
    
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
 
}