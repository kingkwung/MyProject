package com.swdm.cc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.util.Log;

/*기본적으로 searchResult클래스에거 가져온 검색결과물들(String타입)을 가져와 각각의 결과에 대해 Store객체를 만들어 매칭시켜준 후 랭크알고리즘을 돌릴 것임 

 * 그 이후에는 그 결과물들은 다시 String타입으로 만들어 searchResult로 보내 줄 것임 그럼 searchResult는 그것을 listView 에 출력시켜 줄 것임 

 */

public class SearchAlgorithm {

	private ArrayList<String> originalStringDataList = new ArrayList<String>();
	private ArrayList<String> calculatedStringDataList = new ArrayList<String>();
	private ArrayList<String> duplicated_calculatedStringDataList = new ArrayList<String>();
	private ArrayList<Store> storeObjectDataList = new ArrayList<Store>();
	private ArrayList<FrdInfo> friendsList = new ArrayList<FrdInfo>();
	private ArrayList<String> friendRecommendStore = new ArrayList<String>();
	private boolean check_finish = false;
	private double highestRank =0; // 결과내 최고 점수를 담기위한 변수 

	public SearchAlgorithm(ArrayList<String> inputStringDataList) {

		this.originalStringDataList = inputStringDataList;
		store_stringToObject();
	}

	// string타입의 업체정보를 object형식으로 만들어 주는 메서드
	public void store_stringToObject() {

		Log.i("inputStringData", originalStringDataList.toString());
		String storeSplited[];

		for (String storeData : originalStringDataList) {

			storeSplited = storeData.split("/-/");
			// Store tempStore = new Store(storeId, storeUniv, storeType ,
			// storeName , storeTags_string , storeImgURL);
			Log.i("오잉잉", storeData);
			Store tempStore = new Store(storeSplited[0], storeSplited[1],
					storeSplited[2], storeSplited[3], storeSplited[4],
					storeSplited[5]);
			storeObjectDataList.add(tempStore);
		}

		/*
		 * 1125 현재 복잡도 N제곱인 알고리즘을 N logN으로 바꾸기 위해서 이진검색방법을 사용할 것임 그러기 위해서 먼저 1)
		 * 결과 업체리스트들을 이름순으로 정렬해야함 2) 이름순으로 정렬된 업체를 이진검색을 통해 점수를 부여할 것임
		 */
		
		
		// 1)번 과정
		Collections.sort(storeObjectDataList, new Comparator<Store>(){
			@Override
			public int compare(Store o1, Store o2) {
			// TODO Auto-generated method stub
			if(Integer.parseInt(o1.getStoreId()) == Integer.parseInt(o2.getStoreId()))
				return 0;
			else if(Integer.parseInt(o1.getStoreId()) > Integer.parseInt(o2.getStoreId()))
				return 1;
			else 
				return -1;
		}});
		
		for(Store resultStore : storeObjectDataList){
			Log.i("솔팅확인1", resultStore.getStoreId());
		}
	}

	public void calculateDataList() {

		friendsList = MainActivity.my_info.getFriendsList();
		// O(N2)방법~
		// for (FrdInfo friend : friendsList) {
		// Log.i("FRIENDHERE!!",friend.getRecommendStore().toString());
		// friendRecommendStore = friend.getRecommendStore();
		// for (String f_RecommendStore : friendRecommendStore) {
		// for (Store resultStore : storeObjectDataList) {
		// if (f_RecommendStore.equals(resultStore.getStoreId())) {
		// resultStore.insertBackLinkUser_Recommend(friend);
		// MainActivity.my_info.addEvaluateFriend(friend.getId());
		// Log.i("친구들어간다",friend.getName() +"/"+friend.getUserC()
		// +"/"+friend.getPriority());
		//
		// }
		// }
		// }
		// }
		// 이진탐색방법~

		for (FrdInfo friend : friendsList) {

			/*
			 * 1031상민이랑 둘이 마지막졸작한부분이여기임 "내친구의 추천업체들" 이 아직 빈 상태임! 이건 메인액티비티에서
			 * 친구목록 채울때 채워주어야함! 결과적으로 디비어싱크부분을 하나 더 추가해야 할 듯!
			 */
			Log.i("FRIENDHERE!!", friend.getRecommendStore().toString());
			// 친구놈이 추천한 업체들을 일단 가져온 후,
			friendRecommendStore = friend.getRecommendStore();
			// 지금부터 그 업체를 살펴볼 것인데,
			if (friendRecommendStore.size() > 0
					&& !friendRecommendStore.get(0).equals("")) {
				// Log.i("$$$$$$$$$",Integer.toString(friendRecommendStore.size()));
				for (String f_RecommendStore : friendRecommendStore) {
					// 순차적으로 검사하는 도중 특정 업체가 내가 찾고자 하는 업체결과 리스트에 들어있다면,
					int index = storeObjectDataList.size() / 2;
					int end_index = storeObjectDataList.size();
					int prev_index = 0;
					check_finish = false;
					Log.i("$$$$$$$$$",
							Integer.toString(storeObjectDataList.size()) + "/"
									+ Integer.toString(index));
					while (!check_finish) {
						Log.i("1125_",
								f_RecommendStore + "index:"
										+ Integer.toString(index));
						if (f_RecommendStore.equals(storeObjectDataList.get(
								index).getStoreId())) {
							storeObjectDataList.get(index)
							.insertBackLinkUser_Recommend(friend);

							/*
							 * 20150112이후 할 것
							 * 
							 * 1. 지금은 내가 업체결과를 비교하며 랭크극 계산할 때 즉시바로 친구들의
							 * priority를 계산해버린다 근데 이것을 클릭비즈니스에서 평가를 완료한 이후에
							 * 진행되도록 바꿀 것(for문을 사용해서)
							 * 
							 * 2. Priority계산하고나서 친구들의 priority를 디비에 써주는 것을 진행 할
							 * 것 (UpsyncTask를 사용해서 Friend 객체의 정보들을 디비에 바로 갈겨준다)
							 */

							check_finish = true;
						}
						// 현재 미드값의 앞쪽에 있을 수 있는 경우
						else if (Integer.parseInt(f_RecommendStore) < Integer
								.parseInt(storeObjectDataList.get(index)
										.getStoreId())) {
							end_index = index;
							index /= 2;

							if (index == 0
									&& !f_RecommendStore
									.equals(storeObjectDataList.get(
											index).getStoreId())) {
								check_finish = true;
							}
						}
						// 현재 미드값의 뒷쪽에 있을 수 있는 경우
						else if (Integer.parseInt(f_RecommendStore) > Integer
								.parseInt(storeObjectDataList.get(index)
										.getStoreId())) {
							prev_index = index;
							index = ((index + end_index) / 2);
							if (prev_index == index) {
								check_finish = true;
							}
						}
					}
				}
			}
		}
		// 결과업체들에 백링크 유저들 다 넣어놓기 완료.
		// 결과업체들 페이지랭크 계산하고 업데이트 시켜주고~
		// (**여기서 모든 결과업체들은 각자의 페이지랭크값을 가진다** )
		for (Store resultStore : storeObjectDataList) {
			resultStore.calculatePagerank();
		}

		/*확인용(지워도무방)*/
		for(Store resultStore:storeObjectDataList)
			Log.i("솔팅전", Double.toString(resultStore.getPagerank()));

		// 아직 객체타입으로 되어있는 업체결과 리스트를 페이지랭크 크기순으로 내림차순 정렬한다(될라나….?)
		Collections.sort(storeObjectDataList, new Comparator<Store>(){
			@Override
			public int compare(Store o1, Store o2) {
				// TODO Auto-generated method stub
				if(o1.getPagerank() == o2.getPagerank())
					return 0;
				else if(o1.getPagerank() < o2.getPagerank())
					return 1;
				else 
					return -1;
			}
		});


		/*확인용(지워도무방)*/
		for(Store resultStore:storeObjectDataList)
			Log.i("솔팅후", Double.toString(resultStore.getPagerank()));



		if(storeObjectDataList.size()!=0){
			this.highestRank =storeObjectDataList.get(0).getPagerank();
		}


		//랭크값에 따라 색깔을 지정해주기 위한 연산. 그리고 실제 지정하는 과정까지.
		for(Store resultStore : storeObjectDataList){

			double temp_highestRank = ((int) (highestRank * 100)) / 100.0;
			double temp_storeRank = ((int) (resultStore.getPagerank() * 100)) / 100.0;
			double temp_resultValue = (temp_storeRank * 100) / temp_highestRank;
			temp_resultValue = ((int) (temp_resultValue * 100)) / 100.0;
			temp_resultValue = 100.0-temp_resultValue;
			temp_resultValue = ((int) (temp_resultValue * 100)) / 100.0;
			
			resultStore.setStorePercentage_forList(temp_resultValue);
			
			if(temp_resultValue>=80 && temp_resultValue<=100){
				resultStore.setStoreColor_inList("*");
			}
			else if(temp_resultValue>=50 && temp_resultValue<80){
				resultStore.setStoreColor_inList("**");
			}
			else if(temp_resultValue>=20 && temp_resultValue<50){
				resultStore.setStoreColor_inList("***");
			}
			else{
				resultStore.setStoreColor_inList("****");
			}
		}

		// 자 이제 calculatedStringDataList를 정렬된 순서대로 채워줄 것이다~!
		for (int i = 0; i < storeObjectDataList.size(); i++) {
			calculatedStringDataList.add(storeObjectDataList.get(i)
					.getStoreId()
					+ "/-/"
					+ storeObjectDataList.get(i).getStoreUniversity()
					+ "/-/"
					+ storeObjectDataList.get(i).getStoreType()
					+ "/-/"
					+ storeObjectDataList.get(i).getStoreName()
					+ "/-/"
					+ storeObjectDataList.get(i).getStoreTags()
					+ "/-/"
					+ storeObjectDataList.get(i).getPagerank()
					+ "/-/"
					+ storeObjectDataList.get(i).getStoreImgURL());

			duplicated_calculatedStringDataList.add(storeObjectDataList.get(i).getStoreColor_inList()+
					"(상위"+storeObjectDataList.get(i).getStorePercentage_forList()+"%)"+"\n"+
					
					"랭크값 : " + String.format("%.2f", storeObjectDataList.get(i).getPagerank()) + "\n" + 
					"이름 :" + storeObjectDataList.get(i).getStoreName() + "\n" + 
					"종류 : " + storeObjectDataList.get(i).getStoreType()+ "\n" + 
					"인근대학교 : "+ storeObjectDataList.get(i).getStoreUniversity() + "\n"+ 
					"태그 : " + storeObjectDataList.get(i).getStoreTags());
		}
		SearchResult.calculatedData = calculatedStringDataList;
		// SearchResult.calcuatedData = this.calculatedStringDataList;
	}

	/* 게터 */
	public ArrayList<String> getCalculatedDataList() {
		return this.duplicated_calculatedStringDataList;
	}
	public ArrayList<Store> getStoreObjectDataList(){
		return this.storeObjectDataList;
	}
	/* 게터끝 */
}