package com.swdm.cc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.util.Log;

/*�⺻������ searchResultŬ�������� ������ �˻��������(StringŸ��)�� ������ ������ ����� ���� Store��ü�� ����� ��Ī������ �� ��ũ�˰����� ���� ���� 

 * �� ���Ŀ��� �� ��������� �ٽ� StringŸ������ ����� searchResult�� ���� �� ���� �׷� searchResult�� �װ��� listView �� ��½��� �� ���� 

 */

public class SearchAlgorithm {

	private ArrayList<String> originalStringDataList = new ArrayList<String>();
	private ArrayList<String> calculatedStringDataList = new ArrayList<String>();
	private ArrayList<String> duplicated_calculatedStringDataList = new ArrayList<String>();
	private ArrayList<Store> storeObjectDataList = new ArrayList<Store>();
	private ArrayList<FrdInfo> friendsList = new ArrayList<FrdInfo>();
	private ArrayList<String> friendRecommendStore = new ArrayList<String>();
	private boolean check_finish = false;
	private double highestRank =0; // ����� �ְ� ������ ������� ���� 

	public SearchAlgorithm(ArrayList<String> inputStringDataList) {

		this.originalStringDataList = inputStringDataList;
		store_stringToObject();
	}

	// stringŸ���� ��ü������ object�������� ����� �ִ� �޼���
	public void store_stringToObject() {

		Log.i("inputStringData", originalStringDataList.toString());
		String storeSplited[];

		for (String storeData : originalStringDataList) {

			storeSplited = storeData.split("/-/");
			// Store tempStore = new Store(storeId, storeUniv, storeType ,
			// storeName , storeTags_string , storeImgURL);
			Log.i("������", storeData);
			Store tempStore = new Store(storeSplited[0], storeSplited[1],
					storeSplited[2], storeSplited[3], storeSplited[4],
					storeSplited[5]);
			storeObjectDataList.add(tempStore);
		}

		/*
		 * 1125 ���� ���⵵ N������ �˰����� N logN���� �ٲٱ� ���ؼ� �����˻������ ����� ���� �׷��� ���ؼ� ���� 1)
		 * ��� ��ü����Ʈ���� �̸������� �����ؾ��� 2) �̸������� ���ĵ� ��ü�� �����˻��� ���� ������ �ο��� ����
		 */
		
		
		// 1)�� ����
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
			Log.i("����Ȯ��1", resultStore.getStoreId());
		}
	}

	public void calculateDataList() {

		friendsList = MainActivity.my_info.getFriendsList();
		// O(N2)���~
		// for (FrdInfo friend : friendsList) {
		// Log.i("FRIENDHERE!!",friend.getRecommendStore().toString());
		// friendRecommendStore = friend.getRecommendStore();
		// for (String f_RecommendStore : friendRecommendStore) {
		// for (Store resultStore : storeObjectDataList) {
		// if (f_RecommendStore.equals(resultStore.getStoreId())) {
		// resultStore.insertBackLinkUser_Recommend(friend);
		// MainActivity.my_info.addEvaluateFriend(friend.getId());
		// Log.i("ģ������",friend.getName() +"/"+friend.getUserC()
		// +"/"+friend.getPriority());
		//
		// }
		// }
		// }
		// }
		// ����Ž�����~

		for (FrdInfo friend : friendsList) {

			/*
			 * 1031����̶� ���� �����������Ѻκ��̿����� "��ģ���� ��õ��ü��" �� ���� �� ������! �̰� ���ξ�Ƽ��Ƽ����
			 * ģ����� ä�ﶧ ä���־����! ��������� �����ũ�κ��� �ϳ� �� �߰��ؾ� �� ��!
			 */
			Log.i("FRIENDHERE!!", friend.getRecommendStore().toString());
			// ģ������ ��õ�� ��ü���� �ϴ� ������ ��,
			friendRecommendStore = friend.getRecommendStore();
			// ���ݺ��� �� ��ü�� ���캼 ���ε�,
			if (friendRecommendStore.size() > 0
					&& !friendRecommendStore.get(0).equals("")) {
				// Log.i("$$$$$$$$$",Integer.toString(friendRecommendStore.size()));
				for (String f_RecommendStore : friendRecommendStore) {
					// ���������� �˻��ϴ� ���� Ư�� ��ü�� ���� ã���� �ϴ� ��ü��� ����Ʈ�� ����ִٸ�,
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
							 * 20150112���� �� ��
							 * 
							 * 1. ������ ���� ��ü����� ���ϸ� ��ũ�� ����� �� ��ùٷ� ģ������
							 * priority�� ����ع����� �ٵ� �̰��� Ŭ������Ͻ����� �򰡸� �Ϸ��� ���Ŀ�
							 * ����ǵ��� �ٲ� ��(for���� ����ؼ�)
							 * 
							 * 2. Priority����ϰ��� ģ������ priority�� ��� ���ִ� ���� ���� ��
							 * �� (UpsyncTask�� ����ؼ� Friend ��ü�� �������� ��� �ٷ� �����ش�)
							 */

							check_finish = true;
						}
						// ���� �̵尪�� ���ʿ� ���� �� �ִ� ���
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
						// ���� �̵尪�� ���ʿ� ���� �� �ִ� ���
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
		// �����ü�鿡 �鸵ũ ������ �� �־���� �Ϸ�.
		// �����ü�� ��������ũ ����ϰ� ������Ʈ �����ְ�~
		// (**���⼭ ��� �����ü���� ������ ��������ũ���� ������** )
		for (Store resultStore : storeObjectDataList) {
			resultStore.calculatePagerank();
		}

		/*Ȯ�ο�(����������)*/
		for(Store resultStore:storeObjectDataList)
			Log.i("������", Double.toString(resultStore.getPagerank()));

		// ���� ��üŸ������ �Ǿ��ִ� ��ü��� ����Ʈ�� ��������ũ ũ������� �������� �����Ѵ�(�ɶ󳪡�.?)
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


		/*Ȯ�ο�(����������)*/
		for(Store resultStore:storeObjectDataList)
			Log.i("������", Double.toString(resultStore.getPagerank()));



		if(storeObjectDataList.size()!=0){
			this.highestRank =storeObjectDataList.get(0).getPagerank();
		}


		//��ũ���� ���� ������ �������ֱ� ���� ����. �׸��� ���� �����ϴ� ��������.
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

		// �� ���� calculatedStringDataList�� ���ĵ� ������� ä���� ���̴�~!
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
					"(����"+storeObjectDataList.get(i).getStorePercentage_forList()+"%)"+"\n"+
					
					"��ũ�� : " + String.format("%.2f", storeObjectDataList.get(i).getPagerank()) + "\n" + 
					"�̸� :" + storeObjectDataList.get(i).getStoreName() + "\n" + 
					"���� : " + storeObjectDataList.get(i).getStoreType()+ "\n" + 
					"�αٴ��б� : "+ storeObjectDataList.get(i).getStoreUniversity() + "\n"+ 
					"�±� : " + storeObjectDataList.get(i).getStoreTags());
		}
		SearchResult.calculatedData = calculatedStringDataList;
		// SearchResult.calcuatedData = this.calculatedStringDataList;
	}

	/* ���� */
	public ArrayList<String> getCalculatedDataList() {
		return this.duplicated_calculatedStringDataList;
	}
	public ArrayList<Store> getStoreObjectDataList(){
		return this.storeObjectDataList;
	}
	/* ���ͳ� */
}