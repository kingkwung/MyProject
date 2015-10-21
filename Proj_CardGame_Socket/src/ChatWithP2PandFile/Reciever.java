package ChatWithP2PandFile;

import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
 
public class Reciever implements Runnable{
	private int PORT;
    private ServerSocket serverSocket = null;    
    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private FileOutputStream fileOutputStream = null;
    private FileEvent fileEvent;
    private File dstFile = null;
 
    public Reciever(int portNum){
    	PORT = portNum;
    }
    //Accepts socket connection
    public void doConnect() {
        try {
            serverSocket = new ServerSocket(PORT);
            socket = serverSocket.accept();
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    //Reading the FileEvent object and copying the file to disk.
    public void downloadFile() {
        try {
            fileEvent = (FileEvent) inputStream.readObject();
            if (fileEvent.getStatus().equalsIgnoreCase("Error")) {
                System.out.println("Error occurred ..So exiting");
                System.exit(0);
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
			JOptionPane.showMessageDialog(new JFrame(), "Output file: "
					+ outputFile + " is successfully saved.");
            Thread.sleep(3000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
	public void run() {
		doConnect();
		downloadFile();
	}
}