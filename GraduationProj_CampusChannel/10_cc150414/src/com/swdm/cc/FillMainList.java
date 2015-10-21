package com.swdm.cc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.ActivityManager;
import android.util.Log;

public class FillMainList {
	
	private ArrayList<FrdInfo> friendsList = new ArrayList<FrdInfo>();
	private ArrayList<String> friendRecommendStore = new ArrayList<String>();
	private ArrayList<String> resultStoreList_Str = new ArrayList<String>();
	public static ArrayList<Store> resultStoreList_Obj = new ArrayList<Store>();
	private Activity mainAct;
	private int howManyReco=0;
	
	public FillMainList(String insertType, Activity act){
		this.mainAct = act;
		if(insertType.equals("btn_friends")){
			/*���ο�Ƽ��Ƽ���� ģ����õ����� ���� ��� 
			 * 
			 * 1. ���ģ������ �ȴ´� 
			 * 2. �����鼭 ��ģ������� ��õ�ߴ� ��ü���� ��Ƴ��´�(��񿡼� ������ ��� ��ü�� ģ������ ��õ����� ���ϴ� ���� ���� ����)
			 * 3. ������ �������� �ܼ��� �����⸸ �ϴ� ���� �ƴ϶� ģ���� ���������� �˻��ϴµ� ���� 1��ģ���� 31, 27, 20�� ��ü�� ��õ�ߴ��ϸ� 
			 *    ���� ��̸���Ʈ���� 31���� ������ ��õ�Ǿ��ִ��� �ƴ��� ����. ��õ�Ǿ������� ������ �ο����ָ鼭 ��������. ���ٸ� �߰��ϰ�. 
			 *    ���������� 27��, 20�� ��ü�� ��� �˻��Ѵ�. 
			 *    ���Ͱ��� ������� ��� ģ������ üũ�Ѵ�.  
			 * 4. �׷��� �ؼ� ���ο�Ƽ��Ƽ�� showList�� weather_data�� ä���ش� (showList�� ���ο��� �ҷ��൵ �������)
			 * 5. ���Ŀ� ȭ�鿡 ��������� ��µǵ��� ����� 
			 * */
			
			friendsList = MainActivity.my_info.getFriendsList();
			
			//ģ������ ���������� �� ���ε� 
			for(FrdInfo friend : friendsList){
				friendRecommendStore = friend.getRecommendStore();
				
				//��õ������ִ��� ������ �ܼ��˻� 
				if(friendRecommendStore.size()>0 && !friendRecommendStore.get(0).equals("")){
					//�� ģ���� ��õ�ߴ� ��ü���� �ϳ��� �� ���̰� 
					for(String f_RecommendStore : friendRecommendStore){
						
						//������ ��õ�Ǿ������ʾҴٸ�.. ���ο� ��ü�� ����� ��ü��̸���Ʈ�� �װ��� �־��ش� 
						if(!resultStoreList_Str.contains(f_RecommendStore)){
							resultStoreList_Str.add(f_RecommendStore);
							
							Store tempStore = new Store();
							tempStore.setStoreId(f_RecommendStore);
							tempStore.insertBackLinkUser_Recommend(friend);
							tempStore.addHowManyReco();
							resultStoreList_Obj.add(tempStore);
						}
						
						//������ ��õ�Ǿ��־��ٸ�.. �׳� �����߰��Ǿ��ִ� ��ü�� insertBackLinkUser�� �־��ֱ⸸�Ѵ� 
						else{
							for(Store resultStore : resultStoreList_Obj){
								if(resultStore.getStoreId().equals(f_RecommendStore)){
									resultStore.insertBackLinkUser_Recommend(friend);
									
									resultStore.addHowManyReco();
								}
							}
							 
						}
					}
				}
			}
			
			for(Store resultStore : resultStoreList_Obj){
				resultStore.calculatePagerank();
			}
			
			Collections.sort(resultStoreList_Obj, new Comparator<Store>() {
				public int compare(Store obj1, Store obj2) {
					return (obj1.getPagerank() > obj2.getPagerank()) ? -1 : (obj1
							.getPagerank() > obj2.getPagerank()) ? 1 : 0;
				}
			});
			
			
			DBAsyncTask dba = new DBAsyncTask(this.mainAct);
			dba.setDBmanager_type("from_FillMainList_gettingStoreInfoForMainList");
			dba.execute(0);			
			
			resultStoreList_Str.clear();

			
			/*���⼭���ʹ� ���� DBAsyncTask�� ����~*/
//			//ArrayList<String> temp_resultStore_arrList = new ArrayList<String>();
//			Weather weather_data[] = new Weather[resultStoreList_Obj.size()];
//			int index=0;
//			//��ü�� ���ĵǾ��ִ� �����ü���� ��Ʈ����̸���Ʈ�� �ٲ��ִ� ����(���ο��� ����Ʈ�� ��Ʈ��arr���·θ� �Ѱ���� �ϹǷ�..)
//			for(Store resultStore : resultStoreList_Obj){
//				Log.i("����ģ�������׽�Ʈ", resultStore.getStoreId() + " / " + resultStore.getPagerank());
//				//temp_resultStore_arrList.add(resultStore.getStoreId());
//				weather_data[index++] = new Weather(R.drawable.droptop,resultStore.getStoreId(),resultStore.getStoreId()+"ADDR");
//				
//				resultStore.initPagerank();
//			}
//			
//			//Log.i("ASFSFASFASFAFSAF", weather_data[0].title);
//			MainActivity.weather_data = weather_data;
//			
//			
//			resultStoreList_Obj.clear();
//			resultStoreList_Str.clear();
//			
//			Log.i("�ʸ��θ���Ʈ �ٳ�����","sdfsdfsadfas");
			
			
		}
		else if(insertType.equals("btn_all")){
			/*���ο�Ƽ��Ƽ���� ��� ����� ��õ����� ���� ��� 
			 * 
			 * 1. ���ģ������ �ȴ´�(����̶��� ��񿡼� ���� ���� ���;��Ѵ�)
			 * 2. �����鼭 �̻������ ��õ�ߴ� ��ü���� ��Ƴ��´�(��񿡼� ������ ��� ��ü�� ������� ��õ����� ���ϴ� ���� ���� ����)
			 * 3. ������ �������� �ܼ��� �����⸸ �ϴ� ���� �ƴ϶� ���ο� ��ü�� ����� ��̸���Ʈ�� �����ϰ�, ������ ��ü�� ������ �� ��ü�� ī������ �÷��ִ� ������� ��� �� �� 
			 * 4. �׷��� �ؼ� ���ο�Ƽ��Ƽ�� showList�� weather_data�� ä���ش�  (showList�� ���⼭ �ҷ���� �� ���̾� )
			 * 5. ���Ŀ� ȭ�鿡 ��������� ��µǵ��� ����� 
			 * */
		}
	}
}
