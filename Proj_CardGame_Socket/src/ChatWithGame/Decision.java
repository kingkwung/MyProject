package ChatWithGame;
import java.util.ArrayList;


public class Decision {
	public int select(ArrayList<Integer> gamer)
	{
		int a;
		a = 0;
		for(Integer num : gamer)
			a += num;
		a = a%10;
		if(gamer.contains(3) && gamer.contains(8)) //堡动 3俺
			return 0;
		else if((gamer.contains(1) && gamer.contains(3)) || (gamer.contains(1) && gamer.contains(8)))
			return 1;
		else if(gamer.contains(10) && gamer.contains(20)) //动 矫累
			return 2;
		else if(gamer.contains(9) && gamer.contains(19))
			return 3;
		else if(gamer.contains(8) && gamer.contains(18))
			return 4;
		else if(gamer.contains(7) && gamer.contains(17))
			return 5;
		else if(gamer.contains(6) && gamer.contains(16))
			return 6;
		else if(gamer.contains(5) && gamer.contains(15))
			return 7;
		else if(gamer.contains(4) && gamer.contains(14))
			return 8;
		else if(gamer.contains(3) && gamer.contains(13))
			return 9;
		else if(gamer.contains(2) && gamer.contains(12))
			return 10;
		else if(gamer.contains(1) && gamer.contains(11)) //动 昌
			return 11;
		else if((gamer.contains(1) && gamer.contains(2)) || (gamer.contains(11) && gamer.contains(2)) || (gamer.contains(1) && gamer.contains(12)) || (gamer.contains(11) && gamer.contains(12)))
			return 12;
		else if((gamer.contains(1) && gamer.contains(4)) || (gamer.contains(11) && gamer.contains(4)) || (gamer.contains(1) && gamer.contains(14)) || (gamer.contains(11) && gamer.contains(14)))
			return 13;
		else if((gamer.contains(1) && gamer.contains(9)) || (gamer.contains(11) && gamer.contains(9)) || (gamer.contains(1) && gamer.contains(19)) || (gamer.contains(11) && gamer.contains(19)))
			return 14;
		else if((gamer.contains(1) && gamer.contains(10)) || (gamer.contains(11) && gamer.contains(10)) || (gamer.contains(1) && gamer.contains(20)) || (gamer.contains(11) && gamer.contains(20)))
			return 15;
		else if((gamer.contains(4) && gamer.contains(10)) || (gamer.contains(14) && gamer.contains(10)) || (gamer.contains(4) && gamer.contains(20)) || (gamer.contains(14) && gamer.contains(20)))
			return 16;
		else if((gamer.contains(4) && gamer.contains(6)) || (gamer.contains(14) && gamer.contains(6)) || (gamer.contains(4) && gamer.contains(16)) || (gamer.contains(14) && gamer.contains(16)))
			return 17;
		else if(gamer.contains(3) && gamer.contains(7)) //动棱捞
			return 18;
		else if(gamer.contains(4) && gamer.contains(7)) //鞠青绢荤
			return 19;
		//else if(gamer.contains(4) && gamer.contains(9)) //港胖备府 备荤
		//	return 20;
		//else if((gamer.contains(14) && gamer.contains(9)) || (gamer.contains(14) && gamer.contains(19)) || (gamer.contains(4) && gamer.contains(19)))
		//		return 21;
		else if(a == 9) //昌 矫累
			return 22;
		else if(a == 8)
			return 23;
		else if(a == 7)
			return 24;
		else if(a == 6)
			return 25;
		else if(a == 5)
			return 26;
		else if(a == 4)
			return 27;
		else if(a == 3)
			return 28;
		else if(a == 2)
			return 29;
		else if(a == 1)
			return 30;
		else if(a == 0) //昌 场
			return 31;
		else 	
			return -1;
	}
	public int battle(ArrayList<Integer> user, ArrayList<Integer> user2)
	{
		int priority = select(user);
		int priority2 = select(user2);
		//System.out.println(priority+" // "+priority2);
		if(priority == priority2) //公铰何
			return 0;
		//else if((priority == 20 && priority2 >= 2) ||(priority2 == 20 && priority >= 2)) //港胖备府 备荤
		//	return 0;
		//else if((priority == 21 && priority2 >= 12) ||(priority2 == 21 && priority >= 12)) // 备荤
		//	return 0;
		else if(priority == 0)
			return 1;
		else if(priority2 == 0)
			return -1;
		else if(priority == 1 && priority2 != 19)
			return 1;
		else if(priority2 == 1 && priority != 19)
			return -1;
		else if(priority >= 2 && priority <= 11 && priority2 == 18)
			return -1;
		else if(priority2 >= 2 && priority2 <= 11 && priority == 18)
			return 1;
		else if(priority == 18 || priority == 19)
			return -1;
		else if(priority2 == 18 || priority2 == 19)
			return 1;
		else
		{
			if(priority < priority2)
				return 1;
			else
				return -1;
		}
	}
}
