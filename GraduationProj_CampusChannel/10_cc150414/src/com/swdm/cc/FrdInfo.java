package com.swdm.cc;

import java.util.ArrayList;

public class FrdInfo {

	private String id;
	private String name;
	private String univ; // Ä£±¸ÀÚ½ÅÀÇ ÇÐ±³ 0208 »ó¹Î
	private double priority = 0;
	private int userC = 0; // ÇöÀç ÀÌ³ðÀÌ ÃßÃµÇÑ ¾÷Ã¼ÀÇ °¹¼ö(¸¹À» ¼ö·Ï ¾ÈÁÁÀº °Í)
	private ArrayList<String> recommendStore_id = new ArrayList<String>();
	private ArrayList<String> evaluatedStore_id = new ArrayList<String>(); // Æò°¡ÇÑ
																			// ¾÷Ã¼
																			// ¸ñ·Ï
																			// 0208
																			// »ó¹Î
	private String[] frdsRecs;
	private String[] frdsEvals;
	private Integer theNumOfRec = 0; // ÃßÃµÇÑ ¾÷Ã¼ ¼ö 0208 »ó¹Î
	private Integer theNumOfEval = 0; // Æò°¡ÇÑ ¾÷Ã¼ ¼ö 0208 »ó¹Î
	private double maxPriorityOfAllUser=0;

	public FrdInfo(String input_id, String input_name) {
		this.id = input_id;
		this.name = input_name;
	}

	public void setMaxPriorityOfAllUser(double maxPriorityOfAllUser) {
		this.maxPriorityOfAllUser = maxPriorityOfAllUser;
	}
	
	public void setId(String input_id) {
		this.id = input_id;
	}

	public void setName(String input_name) {
		this.name = input_name;
	}

	// 0208 »ó¹Î
	public void setUniv(String input_univ) {
		this.univ = input_univ;
	}

	// 0208 »ó¹Î
	public void setTheNumOfRec(Integer input_num) {
		this.theNumOfRec = input_num;
	}

	// 0208 »ó¹Î
	public void setTheNumOfEval(Integer input_num) {
		this.theNumOfEval = input_num;
	}

	public void setPriority(double input_priority) {
		this.priority = input_priority;
	}

	public void initUserC() {
		this.userC = 0;
	}

	public void addUserC() {
		this.userC++;
	}

	public void addRecommendStore(String input_storeId) {
		recommendStore_id.add(input_storeId);
	}

	public void delRecommendStore(String del_storeId) {
		recommendStore_id.remove(del_storeId);
	}

	public void convertStrArr_to_ArrayList(String[] input_findRecs) {

		this.frdsRecs = input_findRecs;

		for (int i = 0; i < input_findRecs.length; i++) {
			recommendStore_id.add(input_findRecs[i]);
		}
		this.userC = recommendStore_id.size();
		this.theNumOfRec = recommendStore_id.size(); // 0208 »ó¹Î
		// 0208 »ó¹Î
		if (input_findRecs.length == 1 && input_findRecs[0].equals(""))
			this.theNumOfRec = 0;
	}

	// 0208 »ó¹Î
	public void convertEvalStrArr_to_ArrayList(String[] arr) {

		this.frdsEvals = arr;
		for (int i = 0; i < arr.length; i++) {
			evaluatedStore_id.add(arr[i]);
		}
		this.theNumOfEval = evaluatedStore_id.size();
		// 0208 »ó¹Î
		if (arr.length == 1 && arr[0].equals(""))
			this.theNumOfEval = 0;
	}

	public void addPriority(double input_priority) {
		this.priority += input_priority;
	}

	// 0208 »ó¹Î
	public String getUniv() {
		return this.univ;
	}

	// 0208 »ó¹Î
	public Integer getTheNumOfRec() {
		return this.theNumOfRec;
	}

	// 0208 »ó¹Î
	public Integer getTheNumOfEval() {
		return this.theNumOfEval;
	}

	public double getPriority() {
		return this.priority;
	}

	public String getId() {
		return this.id;
	}

	public ArrayList<String> getRecommendStore() {
		return this.recommendStore_id;
	}

	// 0208 »ó¹Î
	public ArrayList<String> getEvaluatedStore() {
		return this.evaluatedStore_id;
	}

	// 0225 »ó¹Î
	public String[] getEvalArr() {
		return this.frdsEvals;
	}

	// 0225 »ó¹Î
	public String[] getRecArr() {
		return this.frdsRecs;
	}

	public String getName() {
		return this.name;
	}

	public int getUserC() {
		return this.userC;
	}

	public double getMaxPriorityOfAllUser() {
		return maxPriorityOfAllUser;
	}
}