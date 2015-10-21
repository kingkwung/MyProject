package ELEVATOR_GUI;
/*
import java.util.LinkedList;

public class makePerson {
	public static LinkedList<Integer> numTest = new LinkedList<Integer>();

	public static void main(String[] args) {
		Floor f = new Floor(60); 
		PersonInfo[] test = new PersonInfo[12];
		
		test[0] = new PersonInfo(-1123,f.getFloorPixelByNum(7),280,"LEFT",f.getFloorPixelByNum(7),f.getFloorPixelByNum(8));
		test[1] = new PersonInfo(-1234,f.getFloorPixelByNum(5),280,"LEFT",f.getFloorPixelByNum(5),f.getFloorPixelByNum(2));
		test[2] = new PersonInfo(-1144,f.getFloorPixelByNum(3),280,"LEFT",f.getFloorPixelByNum(3),f.getFloorPixelByNum(1));
		test[3] = new PersonInfo(-1120,f.getFloorPixelByNum(2),280,"LEFT",f.getFloorPixelByNum(2),f.getFloorPixelByNum(5));
		test[4] = new PersonInfo(-1300,f.getFloorPixelByNum(3),280,"LEFT",f.getFloorPixelByNum(3),f.getFloorPixelByNum(7));
		test[5] = new PersonInfo(-1021,f.getFloorPixelByNum(3),280,"LEFT",f.getFloorPixelByNum(3),f.getFloorPixelByNum(8));
		test[6] = new PersonInfo(-1230,f.getFloorPixelByNum(4),280,"LEFT",f.getFloorPixelByNum(4),f.getFloorPixelByNum(8));
		test[7] = new PersonInfo(-1150,f.getFloorPixelByNum(5),280,"LEFT",f.getFloorPixelByNum(5),f.getFloorPixelByNum(6));
		test[8] = new PersonInfo(-1200,f.getFloorPixelByNum(3),280,"LEFT",f.getFloorPixelByNum(3),f.getFloorPixelByNum(7));
		test[9] = new PersonInfo(-1121,f.getFloorPixelByNum(3),280,"LEFT",f.getFloorPixelByNum(3),f.getFloorPixelByNum(8));
		test[10] = new PersonInfo(-1130,f.getFloorPixelByNum(4),280,"LEFT",f.getFloorPixelByNum(4),f.getFloorPixelByNum(8));
		test[11] = new PersonInfo(-1180,f.getFloorPixelByNum(5),280,"LEFT",f.getFloorPixelByNum(5),f.getFloorPixelByNum(6));
		
		FileReadWriter file = new FileReadWriter("./DataSample/testSOS.dat");
		file.storePersonGUI(test);
	}
}
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class makePerson {
	public static LinkedList<Integer> numTest = new LinkedList<Integer>();

	public static void main(String[] args) throws IOException {
		Floor f = new Floor(60); 
		BufferedReader b = new BufferedReader(new FileReader("./text/10.txt"));
		int i = 0;
		int j  = 0;
		String temp;
		String[] arr;
		int num = 0;
		int check = 0;
		String a = "0";
		char c;
		String use = null;
		int startx = 0;
		PersonInfo[] test = null;
		while ((temp = b.readLine()) != null) {
			startx = 0;
			if (check == 0) {
				test = new PersonInfo[Integer.parseInt(temp)];
				num = Integer.parseInt(temp);
				check = 1;
			} else if (num == j) {
				break;
			} else {
				arr = temp.split("/");
				if (arr[0].substring(0, 1).equals(a)){
					use = arr[0].substring(3, arr[0].length());
					startx = startx - Integer.parseInt(use);

					use = null;
				} else{// 978
					use = arr[0].substring(5, arr[0].length());
					startx = Integer.parseInt(use);
					startx = startx + 978;
					use = null;
				}
				test[j] = new PersonInfo(startx, f.getFloorPixelByNum(Integer
						.parseInt(arr[1])), Integer.parseInt(arr[2]),
						arr[3].toUpperCase(), f.getFloorPixelByNum(Integer
								.parseInt(arr[4])),
						f.getFloorPixelByNum(Integer.parseInt(arr[5])));
				j++;
			}
		}
		FileReadWriter file = new FileReadWriter("./DataSample/GET OFF(NON_VICINITY).dat");
		file.storePersonGUI(test);
	}
}