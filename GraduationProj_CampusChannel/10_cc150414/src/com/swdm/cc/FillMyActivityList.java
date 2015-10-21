package com.swdm.cc;

public class FillMyActivityList {

	String busiName;
	String busiUniv;
	String busiType;
	String busiScore;
	String busiLike;
	String busiImg;
	
	public FillMyActivityList (String name, String univ, String type, String score, String like, String img) {
		this.busiName = name;
		this.busiUniv = univ;
		this.busiType = type;
		this.busiScore = score;
		this.busiLike = like;
		this.busiImg = img;
	}
	
	public String getBusiName() {
		return busiName;
	}

	public void setBusiName(String busiName) {
		this.busiName = busiName;
	}

	public String getBusiUniv() {
		return busiUniv;
	}

	public void setBusiUniv(String busiUniv) {
		this.busiUniv = busiUniv;
	}

	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public String getBusiScore() {
		return busiScore;
	}

	public void setBusiScore(String busiScore) {
		this.busiScore = busiScore;
	}

	public String getBusiLike() {
		return busiLike;
	}

	public void setBusiLike(String busiLike) {
		this.busiLike = busiLike;
	}

	public String getBusiImg() {
		return busiImg;
	}

	public void setBusiImg(String busiImg) {
		this.busiImg = busiImg;
	}
}
