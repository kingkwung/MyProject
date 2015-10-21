package com.swdm.cc;

public class NoticeNoticeFillList {

	String noticeImg;
	String admin;
	String date;
	String text;
	
	public NoticeNoticeFillList (String inputDate, String inputText) {
		this.noticeImg = "logo";
		this.admin = "Campus Channel";
		this.date = inputDate;
		this.text = inputText;
	}

	public String getNoticeImg() {
		return noticeImg;
	}

	public void setNoticeImg(String noticeImg) {
		this.noticeImg = noticeImg;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
