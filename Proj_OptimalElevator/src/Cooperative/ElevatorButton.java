package Cooperative;

//�� �������� ������� ���� 3-4�� ���� ����Ŵϱ�... �������� �̰� ���� �����־���ҵ�....
public class ElevatorButton {
	//Each state indicate whether some people press button to move.(false=Non_Pressed & true=Pressed)
	private boolean UpButton;
	private boolean DownButton;
	//This member has a number which is selected for someone's "Destination".
	private int StopAt;
	
	public ElevatorButton(boolean up, boolean down, boolean StopWhere){
		if(up) UpButton=false;
		if(down) DownButton=false;
		if(StopWhere) StopAt=1;		
	}
	public void settingAllState(boolean up, boolean down, int stop){
		UpButton = up;
		DownButton = down;
		StopAt = stop;
	}
	public void setUp(boolean up){UpButton = up;}
	public void setDown(boolean down){DownButton = down;}
	public void setStop(int stop){StopAt = stop;}
	public boolean isSomeoneWaiting(){return UpButton||DownButton;}
	public boolean isUpPressed(){return UpButton;}
	public boolean isDownPressed(){return DownButton;}
	public boolean isItHasDestination(){return (StopAt!=0);}
}