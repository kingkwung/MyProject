package com.swdm.cc;

import java.util.ArrayList;

public class FrdInfo {

	private String id;
	private String name;
	private String univ; // ģ���ڽ��� �б� 0208 ���
	private double priority = 0;
	private int userC = 0; // ���� �̳��� ��õ�� ��ü�� ����(���� ���� ������ ��)
	private ArrayList<String> recommendStore_id = new ArrayList<String>();
	private ArrayList<String> evaluatedStore_id = new ArrayList<String>(); // ����
																			// ��ü
																			// ���
																			// 0208
																			// ���
	private String[] frdsRecs;
	private String[] frdsEvals;
	private Integer theNumOfRec = 0; // ��õ�� ��ü �� 0208 ���
	private Integer theNumOfEval = 0; // ���� ��ü �� 0208 ���
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

	// 0208 ���
	public void setUniv(String input_univ) {
		this.univ = input_univ;
	}

	// 0208 ���
	public void setTheNumOfRec(Integer input_num) {
		this.theNumOfRec = input_num;
	}

	// 0208 ���
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
		this.theNumOfRec = recommendStore_id.size(); // 0208 ���
		// 0208 ���
		if (input_findRecs.length == 1 && input_findRecs[0].equals(""))
			this.theNumOfRec = 0;
	}

	// 0208 ���
	public void convertEvalStrArr_to_ArrayList(String[] arr) {

		this.frdsEvals = arr;
		for (int i = 0; i < arr.length; i++) {
			evaluatedStore_id.add(arr[i]);
		}
		this.theNumOfEval = evaluatedStore_id.size();
		// 0208 ���
		if (arr.length == 1 && arr[0].equals(""))
			this.theNumOfEval = 0;
	}

	public void addPriority(double input_priority) {
		this.priority += input_priority;
	}

	// 0208 ���
	public String getUniv() {
		return this.univ;
	}

	// 0208 ���
	public Integer getTheNumOfRec() {
		return this.theNumOfRec;
	}

	// 0208 ���
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

	// 0208 ���
	public ArrayList<String> getEvaluatedStore() {
		return this.evaluatedStore_id;
	}

	// 0225 ���
	public String[] getEvalArr() {
		return this.frdsEvals;
	}

	// 0225 ���
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