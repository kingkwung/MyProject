package com.swdm.cc;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.util.Log;



public class MyInfo {
	public  ArrayList<String> friendsIdList = new ArrayList<String>();	// ???�이?�북 친구�???��??목록. ?�이?�만 ??��.
	public ArrayList<FrdInfo> friendsList = new ArrayList<FrdInfo>();
	private ArrayList<Store> recommendStore = new ArrayList<Store>();
	private ArrayList<String> recommendStore_id;
	private ArrayList<Store> InterestingStore = new  ArrayList<Store>(); //내가 관심가는 업체들(아직방문안한것) 
	private ArrayList<FrdInfo> evaluatedFriend = new ArrayList<FrdInfo>();
	private int userC = 0;

	private double maxPriorityOfAllUser=0;
	private double userPriority=0;
	private String userId;
	private String userRec;
	private String userName;
	private String userUniv;		//0208 상민
	private String userGender;
	private String userEvaluate;
	// private String userAge;       //..?�등 ??많�? ?�보???�중??추�? 

	public MyInfo(){
		userId = "";
		userName = "";
		userGender = ""; 
		// userAge = "";
		userRec = "";
		userUniv = "";		//0208 상민
	}
	public MyInfo(String inputId, String inputName, String inputGender){
		userId = inputId;
		userName = inputName;
		userGender = inputGender; 
		// userAge = inputAge;
	}
	/*
   public MyInfo(String inputName, String inputGender, String inputAge){
	      userName = inputName;
	      userGender = inputGender; 
	      userAge = inputAge;
	   }
	 */
	/*
   public MyInfo(String inputName, String inputGender, String inputAge, String inputId){
      userName = inputName;
      userGender = inputGender; 
      userAge = inputAge;
      userId = inputId;
   }
	 */

	/*?�터*/
	public void initFriendsList(){
		friendsList.clear();
	}
	public void initEvaluatedFriend(){
		evaluatedFriend.clear();
	}
	public void setUserId(String inputId){
		userId = inputId;
	}
	public void setUserName(String inputName){
		userName = inputName;
	}
	//0208 상민
	public void setUserUniv(String inputUniv){
		userUniv = inputUniv;
	}
	public void setUserGender(String inputGender){
		userGender = inputGender;
	}
	/* public void setUserAge(String inputAge){
      userAge = inputAge;
   }*/
	public void setMaxPriorityOfAllUser(double maxPriorityOfAllUser) {
		this.maxPriorityOfAllUser = maxPriorityOfAllUser;
	}
	public void initUserC(){
		this.userC=0;
	}
	public void addUserC(){
		this.userC++;
	}
	public void addFriend(FrdInfo inputFriend ){
		friendsList.add(inputFriend);
	}
	public void addFriendId(String inputFriendId ){
		friendsIdList.add(inputFriendId);
	}
	public void addRecommendStore(Store inputRecommendStore){
		recommendStore.add(inputRecommendStore);
	}
	public void addInterestingStore(Store inputInterestingStore){
		InterestingStore.add(inputInterestingStore);
	}

	public void delFriend(FrdInfo deleteFriend){   //??��?�때.. �?��고자?�는 ?�떤 객체�??�자�??�달?�을??ArrayList???�느 �?��??�???��객체�??�는�??�로그램???�떻�??�는�?
		//?�순???�차?�으�?�?��?�건�? 그런경우 ?�러�??�떻�?처리?�줘???�는�?
		friendsList.remove(deleteFriend);
	}
	public void delRecommendStore(Store deleteRecommendStore){
		recommendStore.remove(deleteRecommendStore);
	}
	public void delInterestingStore(Store deleteInterestingStore){
		InterestingStore.remove(deleteInterestingStore);
	}
	public void setUserRec(String rec) {
		userRec = rec;
		//메인 ?�티비티 ?�작?�에.. ??추천목록?�을.. ?�단 ?�줄짜리 ?�트링으�?받아?�기 ?�문???�것?�을 ?�단 ?�누?�서 recommendStore ?�레?�리?�트??차곡차곡 ?�어�?��. 
		String[] userRec_strArr = userRec.split(", ");
		recommendStore_id = new ArrayList<String>( Arrays.asList(userRec_strArr));
		//Log.i("HHHHHHHHHHHHEEEEEEEERRRRRRREEEEEE", recommendStore_id.toString());
	}
	public void setUserEvaluate(String evaluate){
		userEvaluate = evaluate;
		String[] temp_ev = userEvaluate.split(",");
		//Collections.addAll(evaluatedFriend,temp_ev);
	}
	public void setPriority(double input_priority){
		this.userPriority = input_priority;
	}
	
	public void addEvaluateFriend(FrdInfo input_evaluted_Friend){
		this.evaluatedFriend.add(input_evaluted_Friend);
	}	
	//이거일단 필요없음 20150112
	public void addUserEvaluate(String input_evaluate_storeId){
		if(userEvaluate.equals("")){
			userEvaluate = input_evaluate_storeId;
		}
		else{
		userEvaluate += ", "+input_evaluate_storeId;
		}
	}
	/*?�터??/

   /*게터*/
	public String getUserId(){
		return this.userId;
	}
	public String getUserName(){
		return this.userName;
	}
	// 0208 상민
	public String getUserUniv(){
		return this.userUniv;
	}
	public String getUserGender(){
		return this.userGender;
	}
	/*public String getUserAge(){
      return this.userAge;
   }*/
	public double getMaxPriorityOfAllUser() {
		return maxPriorityOfAllUser;
	}
	public ArrayList<FrdInfo> getFriendsList(){
		return this.friendsList;
	}
	public ArrayList<Store> getRecommendStore(){
		return this.recommendStore;
	}
	public ArrayList<Store> getInterestingStore(){
		return this.InterestingStore;
	}
	public int getUserC(){
		return this.userC;
	}
	public double getUserPriority(){
		return this.userPriority;
	}
	public String getUserRec() {
		return userRec;
	}
	
	public ArrayList<FrdInfo> getEvaluatedFriend(){
		return evaluatedFriend;
	}
	public String getUserEvaluate(){
		return userEvaluate;
	}	
}