package elevator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Elevator {

    private static final int PORT = 9001;
    private static final int MAXELEVATORNUMBER=3;
        
    private static LinkedList<String> waitQueue=new LinkedList<String>();    
    private static ArrayList<ElevatorInfo> elevator=new ArrayList<ElevatorInfo>();   
    private static SwingHandler myUI;
    
    public static void main(String[] args) throws Exception {        
    	for(int i=0;i<MAXELEVATORNUMBER;i++){
    		elevator.add(new ElevatorInfo());
    		elevator.get(i).setPresentFloor(1);
    		elevator.get(i).setDirect(0);
    	}

    	System.out.println("The chat server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        Socket clientSocket;
        try {
         	myUI=new SwingHandler(elevator);
               
         	for(int i=0;i<MAXELEVATORNUMBER;i++){
         		elevator.get(i).setMyUI(myUI);
				elevator.get(i).start();                
         	}
         	
           	clientSocket=listener.accept();
            new InputHandler(clientSocket).start();
            new OutputHandler(clientSocket).start();
            
        }finally{ listener.close(); }
    }
    
    private static class InputHandler extends Thread{
    	private Socket socket;
    	private BufferedReader in;
    	    	
    	public InputHandler(Socket socket){
    		this.socket=socket;
    	}
    	public void run(){
            try {
            	 in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 

            	 while(true){
            		 String input=in.readLine();
            		 String[] message= input.split(" ");

            		 int requestFloor=Integer.parseInt(message[0]);
            		 int requestDirect=Integer.parseInt(message[1]);
            		 int nearestElevator;

            		 nearestElevator=checkNearest(requestFloor,requestDirect);
            		 System.out.println("elevator"+nearestElevator);
            		 elevator.get(nearestElevator).addQueue(requestFloor);
            	 }

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    		
    	}
    }
    
    private static class OutputHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        
        public OutputHandler(Socket socket) {
            this.socket = socket;
        }
        public void run(){        	
            try {
                // Create character streams for the socket.
            	out = new PrintWriter(socket.getOutputStream(), true);
                while (true) {
                	StringBuffer sb;                    
                    String test="";
                    String test2="";
                    
                    try {
						OutputHandler.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    sb=new StringBuffer("Present_Floor");
                    
                    for(int j=0;j<MAXELEVATORNUMBER;j++){
                    	sb.append(" "+elevator.get(j).getPresentFloor());
                    	test+=" "+elevator.get(j).getDirect();
                    	test2+=" "+elevator.get(j).getFirstQueue();
                    }
                    
                    out.println(sb.toString());
                    System.out.println(sb.toString());
                    System.out.println("State "+test);
                    System.out.println("queue "+test2);
                    test2=" ";
                    test=" ";

                    
                   	for(int i=0;i<MAXELEVATORNUMBER;i++){
                   		if(elevator.get(i).isWaiting()==true){}
                   		else if((elevator.get(i).getDirect()!=0)){
                   			if(elevator.get(i).getDirect()==1){
                   				elevator.get(i).upperFloor();
                   			}else if(elevator.get(i).getDirect()==2){
                   				elevator.get(i).lowerFloor();
                   			}
                   			if(elevator.get(i).getPresentFloor()==elevator.get(i).getFirstQueue()){
                   				elevator.get(i).setWaiting(true);
                   				elevator.get(i).removeFloorQueue();	
                   			}
                   		}
                   	}         
                }
            } catch (IOException e) {
                System.out.println(e);
            }finally {
                if (waitQueue != null) {
                    waitQueue.remove(waitQueue);
                }
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
    }

    static int checkNearest(int floor,int direct){    	
    	int min=elevator.get(0).getPresentFloor();
    	int minIdx=-1;
    	
    	for(int i=0;i<MAXELEVATORNUMBER;i++){
    		if(elevator.get(i).getDirect()==0&&
    				Math.abs(min-floor)>Math.abs(floor-elevator.get(i).getPresentFloor())){
				min=i;
				minIdx=i;
			}
    	}
    	if(minIdx!=-1){
    		return minIdx;
    	}
    	for(int i=0;i<MAXELEVATORNUMBER;i++){
    		
    		if(elevator.get(i).getPresentFloor()==floor){
    			return i;
    		}
    		if(elevator.get(i).getDirect()==direct){
    			if((elevator.get(i).getDirect()==1)&&
    					elevator.get(i).getPresentFloor()<floor&&
    					Math.abs(min-floor)>Math.abs(floor-elevator.get(i).getPresentFloor())){
    				min=elevator.get(i).getPresentFloor();
    				minIdx=i;
    			}else if((elevator.get(i).getDirect()==2)&&
    					elevator.get(i).getPresentFloor()>floor&&
    					Math.abs(min-floor)>Math.abs(floor-elevator.get(i).getPresentFloor())){
    				min=elevator.get(i).getPresentFloor();
    				minIdx=i;
    			}
    		}
    	}
    	if(minIdx==-1){
    		minIdx=0;
    	}
    	
    	return minIdx;
	}
}