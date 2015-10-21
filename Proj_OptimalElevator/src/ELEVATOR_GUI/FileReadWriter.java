package ELEVATOR_GUI;

import java.io.*;
import java.util.*;

public class FileReadWriter{
	private String fileName;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	public FileReadWriter(String fn){fileName=fn;}
	//Load schedule.
	public LinkedList<Schedule> LoadSchedule(){	
		LinkedList<Schedule> users = new LinkedList<Schedule>();
		try{
			inputStream = new ObjectInputStream(new FileInputStream(fileName));			
			while(true){
			try{
					Object ob = inputStream.readObject();
					if (ob instanceof Schedule)
						users.add((Schedule)ob);
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
	//store schedule.
	public boolean storeSchedule(LinkedList<Schedule> Users){
		try{
			outputStream = new ObjectOutputStream(new FileOutputStream(fileName));

			for(Schedule user : Users){				
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
	
	//Load person
	public PersonGUI[] LoadPersonGUI(){
		LinkedList<PersonGUI> users = new LinkedList<PersonGUI>();
		try{
			inputStream = new ObjectInputStream(new FileInputStream(fileName));
			int i=0;
			while(true){
				try{
					Object ob = inputStream.readObject();
					if (ob instanceof PersonInfo)
						users.add(new PersonGUI((PersonInfo)ob,i++));
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
		
		PersonGUI[] userArray = new PersonGUI[users.size()];
		for(int i=0;i<users.size();i++)
			userArray[i] = users.get(i);
						
		return userArray;
	}
	//store person
	public boolean storePersonGUI(PersonInfo[] Users){		
		try{
			outputStream = new ObjectOutputStream(new FileOutputStream(fileName));

			for(int i=0;i<Users.length;i++){				
				outputStream.writeObject(Users[i]);
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
class PersonInfo implements Serializable{
	public final int x;
	public final int y;
	public final int dest;
	public final String direction;
	public final int startFloor;
	public final int destFloor;	
	
	public PersonInfo(int x,int y,int dest,String dir,int sf,int df){
		this.x = x;
		this.y = y;
		this.dest = dest;
		this.direction = dir;
		this.startFloor = sf;
		this.destFloor = df;
		
		Floor f = new Floor(60);
		int floorNum = f.getFloorNumByPixel(y);
		int startFNum = f.getFloorNumByPixel(sf);
		int destFNum = f.getFloorNumByPixel(df);
		if(floorNum<1 || floorNum>10){
			System.err.println("두번째 값 잘못입력함.");
			System.exit(1);
		}else if(startFNum<1 || startFNum>10){
			System.err.println("세번째 값 잘못입력함.");
			System.exit(1);
		}else if(destFNum<1 || destFNum>10){
			System.err.println("다섯번째 값 잘못입력함.");
			System.exit(1);
		}
		
		if(floorNum != startFNum){
			System.err.println("두번때 값이랑 다섯번째 값 같아야함!!!");
			System.exit(1);
		}			
		if(!dir.equals("LEFT") && !dir.equals("RIGHT")){
			System.err.println("방향값(네번째값) 잘못입력함");
			System.exit(1);
		}
		
		System.out.println("입력OK");
	}
}