package com.swdm.cc;
import java.util.ArrayList;

import android.util.Log;




public class Store {
	private String storeName;
	private String storeUniversity;
	private String storeType;
	private String storeId;
	private String storeImgURL;  //////////영남수정부분//////////// 
	private String storeTags;
	private ArrayList<FrdInfo> backLinkUser_Recommend = new ArrayList<FrdInfo>();
	
	private String storeAddress;    // 여기서부터는 나중에 사용할 정보들..

	private String storeHour;
	private String storeIntroduce;
	private String storeColor_inList="";
	private double storePercentage_forList=0;
	public double getStorePercentage_forList() {
		return storePercentage_forList;
	}

	public void setStorePercentage_forList(double storePercentage_forList) {
		this.storePercentage_forList = storePercentage_forList;
	}

	public String getStoreColor_inList() {
		return storeColor_inList;
	}
	
	public void setStoreColor_inList(String storeColor_inList) {
		this.storeColor_inList = storeColor_inList;
	}

	private int storeRecommendCount;
	
	private int howManyReco=0;
	

	
	

	private double pagerank = 0.0;
	private double compPagerank = 0.0;
	private double d = 0.85;
	
	
	public Store(){
		
	}
	///////////////영남수정부분////////////////// 
	public Store(String inputId, String inputUniversity, String inputType, String inputName, String inputTags_string, String inputImgURL){
		storeId = inputId;
		storeName = inputName;
		storeUniversity = inputUniversity;
		storeType = inputType;
		storeImgURL = inputImgURL;
		storeTags = inputTags_string;
		/*
		String[] inputTags_strArr = inputTags_string.split(",");
		
		for(int i=0; i<inputTags_strArr.length; i++){
			storeTags.add(inputTags_strArr[i]);
		}
		*/
	}
	
	/*세터*/
	public void setStoreId(String inputStoreId){
		storeId = inputStoreId;
	}
	public void setStoreName(String inputStoreName){
		storeName = inputStoreName;
	}
	public void setStoreImgURL(String inputStoreImgURL){
		storeImgURL = inputStoreImgURL;
	}
	public void setStoreUniversity(String inputStoreUniversity){
		storeUniversity = inputStoreUniversity;
	}
	public void setStoreType(String inputStoreType){
		storeType = inputStoreType;
	}
	public void setStoreTag(String inputTag){
		storeTags = inputTag;
	}
	public void setHowManyReco(int howManyReco) {
		this.howManyReco = howManyReco;
	}
	public void clearBackLinkUser_Recommend(){
		backLinkUser_Recommend.clear();
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public void setStoreRecommendCount(int storeRecommendCount) {
		this.storeRecommendCount = storeRecommendCount;
	}
	public void addHowManyReco(){
		this.howManyReco++;
	}
	/*세터끝*/
	
	/*게터*/
	
	public int getStoreRecommendCount() {
		return storeRecommendCount;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	
	/////////영남추가부분/////////////
	public String getStoreId(){
		return this.storeId;
	}
	////////영남추가부분///////////// 
	public String getStoreName(){
		return this.storeName;
	}
	public String getStoreUniversity(){
		return this.storeUniversity;
	}
	public String getStoreType(){
		return this.storeType;
	}
	public String getStoreTags(){
		return this.storeTags;
	}
	public String getStoreImgURL(){
		return this.storeImgURL;
	}
	public ArrayList<FrdInfo> getBackListUser_Recommend(){
		return this.backLinkUser_Recommend;
	}
	public int getHowManyReco() {
		return howManyReco;
	}
	public double getPagerank(){
		return this.pagerank;
	}
	public double getCompPagerank(){
		return this.compPagerank;
	}
	/*게터끝*/
	public void printBackLinkUser_Recommend(){
		if(backLinkUser_Recommend.size()<1){ // 만약 추천백링크유저가 하나도 없다면 그냥 리턴시켜버림~ 시간아끼기위해서
			return;
		}
		
		System.out.print(this.getStoreName() + " : ");
		for(FrdInfo user : backLinkUser_Recommend){
			System.out.print(user.getName()+"/");
		}
		System.out.println();
	}
	
	public void insertBackLinkUser_Recommend(FrdInfo insertUser){
		backLinkUser_Recommend.add(insertUser);
		//insertUser.addUserC();
	}

	public void initPagerank(){
		pagerank = 0.0;
		compPagerank = 0.0;
	}
	public void initHowManyReco(){
		howManyReco = 0;
	}
	public void calculatePagerank(){
		
		initPagerank();
		
		double votes = 0;
		for(FrdInfo user : backLinkUser_Recommend){
			votes = votes + user.getPriority() / user.getUserC();
			compPagerank = (1.0 - d) +d*votes;
		}
		Log.i("votes, comPagerank",Double.toString(votes)+"/"+Double.toString(compPagerank));
		updatePagerank();
	}
	
	public void updatePagerank(){
		pagerank = compPagerank;
		pagerank = ((int) (pagerank * 1000)) / 1000.0;
	}
}