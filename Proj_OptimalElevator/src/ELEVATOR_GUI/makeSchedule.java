package ELEVATOR_GUI;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class makeSchedule {

	public int[][] readData(String dayOfWeek)
	{
		int[][] day = new int[24][10];
		
		try {
			String s = "";
			InputStreamReader reader = new InputStreamReader(new FileInputStream(dayOfWeek));
			BufferedReader breader = new BufferedReader(reader);
			StringTokenizer st = null;
			
			int i=0;
			while((s = breader.readLine()) != null){
				st = new StringTokenizer(s," ");
				int k = 0;
				while(st.hasMoreTokens()){
					day[i][k] = Integer.parseInt(st.nextToken());
					k++;
				}
				i++;
			}
			
			reader.close();
			breader.close();
		}catch(FileNotFoundException e){ e.printStackTrace();
		}catch (IOException e){ e.printStackTrace();
		}
		
		return day;
	}
	
	public static void main(String[] args){
		int[][] mon;
		int[][] tue;
		int[][] wed;
		int[][] thu;
		int[][] fri;
		int[][] sat;
		int[][] sun;
	    
		Floor f = new Floor(60);
		LinkedList<Schedule> EleLoc = new LinkedList<Schedule>();
		
		mon = new makeSchedule().readData("mon.txt");
		tue = new makeSchedule().readData("tue.txt");
		wed = new makeSchedule().readData("wed.txt");
		thu = new makeSchedule().readData("thu.txt");
		fri = new makeSchedule().readData("fri.txt");
		sat = new makeSchedule().readData("sat.txt");
		sun = new makeSchedule().readData("sun.txt");
		
		EleLoc.add(new Schedule("MON",mon));
		EleLoc.add(new Schedule("TUE",tue));
		EleLoc.add(new Schedule("WED",wed));
		EleLoc.add(new Schedule("THU",thu));
		EleLoc.add(new Schedule("FRI",fri));
		EleLoc.add(new Schedule("SAT",sat));
		EleLoc.add(new Schedule("SUN",sun));
		
		FileReadWriter file = new FileReadWriter("EleExample1.dat");
		file.storeSchedule(EleLoc);
	}
}
