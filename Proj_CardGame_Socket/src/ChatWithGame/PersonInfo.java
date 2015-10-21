package ChatWithGame;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class PersonInfo implements Serializable{
	private String ID;
	private String password;
	private String name;
	private String Sex;
	private String major;
	private String StudentNum;
	private String phoneNum;
	private String ImagePath;
	private int GameMoney;
	private int WinNum;
	private int LooseNum;
	private String LastestAccessDate;
	
	public PersonInfo(String id,String pw, String theName, String sex, String maj, String sn, String pn, String imgpath){
		ID = id;
		password = pw;
		name = theName;
		Sex = sex;
		major = maj;
		StudentNum = sn;
		phoneNum = pn;
		GameMoney = 10000000;
		WinNum = LooseNum = 0;
		LastestAccessDate = null;
		
		ImagePath = "D:/SUTTA/UserImg/" + ID + "." + imgpath;
	}

	public String getID(){return ID;}
	public String getPassword(){return password;}
	public String getName(){return name;}
	public String getSexString(){return Sex;}
	public String getMajor(){return major;}
	public String getStudentNum(){return StudentNum;}
	public String getPhoneNum(){return phoneNum;}
	public String getImgPath(){return ImagePath;}
	public int getGameMoney(){return GameMoney;}
	public int getWinNum(){return WinNum;}
	public int getLooseNum(){return LooseNum;}
	public void setID(String iD){ID = iD;}
	public void setPassword(String pw){password = pw;}
	public void setName(String na){na = name;}
	public void setSex(String sex){Sex = sex;}
	public void setMajor(String maj){major = maj;}
	public void setStudentNum(String sNum){StudentNum = sNum;}
	public void setPhoneNum(String pNum){phoneNum = pNum;}
	public void setGameMoney(int gm){GameMoney = gm;}
	public void setWinNum(int win){WinNum = win;}
	public void setLooseNum(int loose){LooseNum = loose;}
	
	public String getWinRate(){
		if(WinNum==0 && (LooseNum==0||WinNum==1))
			return "0%";
		
		String returnValue = (""+(double)WinNum/(WinNum+LooseNum)*100);
		
		return returnValue.substring(0, returnValue.indexOf("."))+"%";
		
	}
	public String getStringMoney() {
		int intMoney = GameMoney;
		String stringMoney = "";

		if (intMoney >= 1000000) {
			stringMoney += Integer.toString(intMoney / 1000000);
			stringMoney += "억 ";
			intMoney = intMoney % 1000000;
		}

		if (intMoney >= 100000) {
			stringMoney += Integer.toString(intMoney / 100000);
			stringMoney += "천 ";
			intMoney = intMoney % 100000;
		}

		if (intMoney >= 10000) {
			stringMoney += Integer.toString(intMoney / 10000);
			stringMoney += "백 ";
			intMoney = intMoney % 10000;
		}

		return stringMoney+"만원";
	}
	
	public void updateAccessDate(){
		Date date = Calendar.getInstance().getTime();
		LastestAccessDate  = date.getMonth()+"/"+date.getDate()+"/"+date.getHours()+":"+date.getMinutes();
	}
}