package ELEVATOR_GUI;

import java.io.Serializable;

public class Schedule implements Serializable{
	//Elevator use records variables.
	private final String day;
	public int[][] useRecords;
	public int[][] perRecords;
	public int lank[];
	
	//constructor
	public Schedule(String d, int[][] dayOfWeek){
		day = d;
		useRecords = new int[24][10];
		perRecords = new int[24][10];
		
		for(int i=0;i<24;i++){
			for(int j=0; j<10; j++){
				useRecords[i][j] = dayOfWeek[i][j];
			}
		}
		calculateForPercentage();
	}
	
	//other metods
	public void calculateForPercentage(){
		int[] tempSum = new int[24];
		for(int i=0; i<24; i++){
			for(int j=0; j<10; j++){
				tempSum[i] += useRecords[i][j];
			}
		}
		for(int i=0; i<24; i++){
			for(int j=0; j<10; j++){
				perRecords[i][j] = (int)(((double)useRecords[i][j]/(double)tempSum[i])*100);
			}
		}
	}
	public void addInput(int time, int floor){
		useRecords[time][floor]++; 
		calculateForPercentage();
	}
	public int[] getTotalPercentageForSchedule(int time){
		return perRecords[time];
	}
	public int[] getLankForSchedule(int time){
		int temp=0;
		int tempIdx=0;
		lank = new int[4];

		int[] tempArray = new int[10];
		
		for(int i=0; i<tempArray.length; i++){
			tempArray[i] = perRecords[time][i];
		}
		
		for(int k = 0; k < 4; k++) {
			for(int i = 0; i < tempArray.length; i++){
				if(temp <= tempArray[i]){
					temp = tempArray[i];
					tempIdx = i;
				}
			}
			lank[k]=tempIdx;
			temp = 0;
			tempArray[tempIdx] = 0;
		}
		
		return lank;
	}
}