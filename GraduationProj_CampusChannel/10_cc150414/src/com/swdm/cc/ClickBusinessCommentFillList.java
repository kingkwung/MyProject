package com.swdm.cc;

public class ClickBusinessCommentFillList {

	Integer commentId;
	String reviewId;
	String cWriterId;
	String cWriterName;
	String commentDate;
	String commentText;

	public ClickBusinessCommentFillList(Integer newId, String inputRId,
			String inputWId, String inputName, String inputDate,
			String inputText) {
		this.commentId = newId;
		this.reviewId = inputRId;
		this.cWriterId = inputWId;
		this.cWriterName = inputName;
		this.commentDate = inputDate;
		this.commentText = inputText;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public String getReviewId() {
		return reviewId;
	}

	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}

	public String getcWriterId() {
		return cWriterId;
	}

	public void setcWriterId(String cWriterId) {
		this.cWriterId = cWriterId;
	}

	public String getcWriterName() {
		return cWriterName;
	}

	public void setcWriterName(String cWriterName) {
		this.cWriterName = cWriterName;
	}

	public String getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
}