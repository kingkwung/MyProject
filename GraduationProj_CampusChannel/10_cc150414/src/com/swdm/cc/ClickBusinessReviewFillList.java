package com.swdm.cc;

public class ClickBusinessReviewFillList {

	Integer reviewId;
	String reviewBusiId;
	String writerId;
	String writerName;
	String reviewDate;
	String reviewText;
	Integer numOfLike;
	Integer numOfComment;

	public ClickBusinessReviewFillList(Integer newId, String inputBId,
			String inputWId, String inputName, String inputDate,
			String inputText, Integer inputLike, Integer inputComment) {
		this.reviewId = newId;
		this.reviewBusiId = inputBId;
		this.writerId = inputWId;
		this.writerName = inputName;
		this.reviewDate = inputDate;
		this.reviewText = inputText;
		this.numOfLike = inputLike;
		this.numOfComment = inputComment;
	}

	public Integer getReviewId() {
		return reviewId;
	}

	public void setReviewId(Integer reviewId) {
		this.reviewId = reviewId;
	}

	public String getReviewBusiId() {
		return reviewBusiId;
	}

	public void setReviewBusiId(String reviewBusiId) {
		this.reviewBusiId = reviewBusiId;
	}

	public String getWriterId() {
		return writerId;
	}

	public void setWriterId(String writerId) {
		this.writerId = writerId;
	}

	public String getWriterName() {
		return writerName;
	}

	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}

	public String getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}

	public String getReviewText() {
		return reviewText;
	}

	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	public Integer getNumOfLike() {
		return numOfLike;
	}

	public void setNumOfLike(Integer numOfLike) {
		this.numOfLike = numOfLike;
	}

	public Integer getNumOfComment() {
		return numOfComment;
	}

	public void setNumOfComment(Integer numOfComment) {
		this.numOfComment = numOfComment;
	}
}
