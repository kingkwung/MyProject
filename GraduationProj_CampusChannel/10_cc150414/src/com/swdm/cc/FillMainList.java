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
			/*메인엑티비티에서 친구추천목록을 누른 경우 
			 * 
			 * 1. 모든친구들을 훑는다 
			 * 2. 훑으면서 이친구놈들이 추천했던 업체들을 모아놓는다(디비에서 일일히 모든 업체와 친구들의 추천목록을 비교하는 것을 막기 위함)
			 * 3. 모으는 과정에서 단순히 모으기만 하는 것이 아니라 친구를 순차적으로 검사하는데 만약 1번친구가 31, 27, 20번 업체를 추천했다하면 
			 *    먼저 어레이리스트에서 31번이 이전에 추천되어있는지 아닌지 본다. 추천되어있으면 점수를 부여해주면서 지나간다. 없다면 추가하고. 
			 *    같은식으로 27번, 20번 업체를 모두 검사한다. 
			 *    위와같은 방식으로 모든 친구들을 체크한다.  
			 * 4. 그렇게 해서 메인엑티비티의 showList의 weather_data를 채워준다 (showList는 메인에서 불러줘도 상관없음)
			 * 5. 그후에 화면에 결과적으루 출력되도록 만든다 
			 * */
			
			friendsList = MainActivity.my_info.getFriendsList();
			
			//친구들을 순차적으로 돌 것인데 
			for(FrdInfo friend : friendsList){
				friendRecommendStore = friend.getRecommendStore();
				
				//추천목록이있는지 없는지 단순검사 
				if(friendRecommendStore.size()>0 && !friendRecommendStore.get(0).equals("")){
					//그 친구가 추천했던 업체들을 하나씩 볼 것이고 
					for(String f_RecommendStore : friendRecommendStore){
						
						//기존에 추천되어있지않았다면.. 새로운 객체를 만들고 객체어레이리스트에 그것을 넣어준다 
						if(!resultStoreList_Str.contains(f_RecommendStore)){
							resultStoreList_Str.add(f_RecommendStore);
							
							Store tempStore = new Store();
							tempStore.setStoreId(f_RecommendStore);
							tempStore.insertBackLinkUser_Recommend(friend);
							tempStore.addHowManyReco();
							resultStoreList_Obj.add(tempStore);
						}
						
						//기존에 추천되어있었다면.. 그냥 기존추가되어있던 객체의 insertBackLinkUser에 넣어주기만한다 
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

			
			/*여기서부터는 전부 DBAsyncTask로 보냄~*/
//			//ArrayList<String> temp_resultStore_arrList = new ArrayList<String>();
//			Weather weather_data[] = new Weather[resultStoreList_Obj.size()];
//			int index=0;
//			//객체로 정렬되어있는 결과업체들을 스트링어레이리스트로 바꿔주는 과정(메인에서 리스트는 스트링arr형태로만 넘겨줘야 하므로..)
//			for(Store resultStore : resultStoreList_Obj){
//				Log.i("메인친구순차테스트", resultStore.getStoreId() + " / " + resultStore.getPagerank());
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
//			Log.i("필메인리스트 다끝났슈","sdfsdfsadfas");
			
			
		}
		else if(insertType.equals("btn_all")){
			/*메인엑티비티에서 모든 사용자 추천목록을 누른 경우 
			 * 
			 * 1. 모든친구들을 훑는다(대신이때는 디비에서 새로 전부 얻어와야한다)
			 * 2. 훑으면서 이사람들이 추천했던 업체들을 모아놓는다(디비에서 일일히 모든 업체와 사람들의 추천목록을 비교하는 것을 막기 위함)
			 * 3. 모으는 과정에서 단순히 모으기만 하는 것이 아니라 새로운 업체가 생기면 어레이리스트에 저장하고, 기존의 업체가 들어오면 그 업체의 카운팅을 올려주는 방식으로 계산 할 것 
			 * 4. 그렇게 해서 메인엑티비티의 showList의 weather_data를 채워준다  (showList는 여기서 불러줘야 할 것이야 )
			 * 5. 그후에 화면에 결과적으루 출력되도록 만든다 
			 * */
		}
	}
}
