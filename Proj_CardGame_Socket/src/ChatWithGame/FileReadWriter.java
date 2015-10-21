package ChatWithGame;

import java.io.*;
import java.util.*;

public class FileReadWriter{
	private String fileName;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	public FileReadWriter(String fn){fileName=fn;}
	public LinkedList<PersonInfo> LoadPersonInfo(){	
		LinkedList<PersonInfo> users = new LinkedList<PersonInfo>();
		try{
			inputStream = new ObjectInputStream(new FileInputStream(fileName));			
			while(true){
			try{
				Object ob = inputStream.readObject();
				if (ob instanceof PersonInfo)
					users.add((PersonInfo)ob);
			}catch (EOFException e){	break;
			}catch(ClassNotFoundException e){ /*...*/}
			}
		}catch(FileNotFoundException e){
			System.out.println("Error opening the file " + fileName);
			System.exit(0);
		}catch(IOException e1){
			e1.printStackTrace();
		}finally{
			try{	inputStream.close();
			}catch(IOException e){	e.printStackTrace(); }
		}
		
		return users;
	}
	public boolean storePersonInfo(LinkedList<PersonInfo> Users){
		try{
			outputStream = new ObjectOutputStream(new FileOutputStream(fileName));

			for(PersonInfo user : Users){				
				outputStream.writeObject(user);
			}
		}
		catch(FileNotFoundException e){
			System.err.println("Error opening the file" + fileName);
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally{
			try{	outputStream.close();
			}catch(IOException e){	e.printStackTrace(); }
		}
		
		return true;
	}
}