
public class UserInfo {
	private String id;
	private String pw;
	private String name;
	private int win;
	private int lose;
	private String lastTime;
	private int money;
	private int imgNum;

	public UserInfo(String id, String pw, String name, int win, int lose,
			int imgNum, int money, String lastTime) {
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.win = win;
		this.lose = lose;
		this.lastTime = lastTime;
		this.money = money;
		this.imgNum = imgNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public int getLose() {
		return lose;
	}

	public void setLose(int lose) {
		this.lose = lose;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getImgNum() {
		return imgNum;
	}

	public void setImgNum(int imgNum) {
		this.imgNum = imgNum;
	}
}