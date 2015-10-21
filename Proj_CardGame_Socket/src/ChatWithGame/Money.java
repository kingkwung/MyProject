package ChatWithGame;

public class Money {
	private double x;
	private double y;
	private int destX;
	private int destY;
	private double addX;
	private double addY;
	private int select;
	public Money(boolean check, int amount, int destx, int desty)
	{
		if(check)
		{
			x = 600;
			y = 650;
		}
		else
		{
			x = 600;
			y = 50;
		}
		destX = destx;
		destY = desty;
		addX = (double)(destX - x)/200;
		addY = (double)(destY - y)/200;
		if(amount < 10000)
			select = 0;
		else if(amount < 50000)
			select = 1;
		else if(amount < 100000)
			select = 2;
		else if(amount < 150000)
			select = 3;
		else
			select = 4;
	}
	public void setX()
	{
		x+=addX;
	}
	public void setY()
	{
		y+=addY;
	}
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
	public int getDestX()
	{
		return destX;
	}
	public int getDestY()
	{
		return destY;
	}
	public int getSelect()
	{
		return select;
	}
}
