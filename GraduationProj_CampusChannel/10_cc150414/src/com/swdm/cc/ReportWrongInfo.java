package com.swdm.cc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ReportWrongInfo extends Activity {

	Button buttonSend;
	Button back;
	TextView aboutInfo;
	EditText textTo;
	EditText textSubject;
	EditText textMessage;
	RadioGroup radioGroup1;
	RadioGroup radioGroup2;
	
	public static RadioButton radio0;
	public static RadioButton radio1;
	public static RadioButton radio2;
	public static RadioButton radio3;
	public static RadioButton radio4;
	public static RadioButton radio5;
	public static RadioButton radio6;
	public static RadioButton radio7;
	public static RadioButton radio8;
	public static RadioButton radio9;
	public static RadioButton radio10;
	
	String wrongUniv; // ClickBusiness에서 인텐트로 받은 대학교이름  저장
	String wrongName; // ClickBusiness에서 인텐트로 받은 업소이름  저장
	String checkedType;
	String userName = MainActivity.my_info.getUserName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_wrong_info);

		buttonSend = (Button) findViewById(R.id.buttonSend);
		back = (Button) findViewById(R.id.back);
		aboutInfo = (TextView) findViewById(R.id.aboutInfo);
		textMessage = (EditText) findViewById(R.id.editTextMessage);
		radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
		radio0 = (RadioButton) findViewById(R.id.radio0);
		radio1 = (RadioButton) findViewById(R.id.radio1);
		radio2 = (RadioButton) findViewById(R.id.radio2);
		radio3 = (RadioButton) findViewById(R.id.radio3);
		radio4 = (RadioButton) findViewById(R.id.radio4);
		radio5 = (RadioButton) findViewById(R.id.radio5);
		radio6 = (RadioButton) findViewById(R.id.radio6);
		radio7 = (RadioButton) findViewById(R.id.radio7);
		radio8 = (RadioButton) findViewById(R.id.radio8);
		radio9 = (RadioButton) findViewById(R.id.radio9);
		radio10 = (RadioButton) findViewById(R.id.radio10);
		
		
		Intent intent = getIntent();
		// ClickBusiness에서 인텐트로 받은 대학교이름  저장
		wrongUniv = intent.getExtras().getString("univName").toString();
		// ClickBusiness에서 인텐트로 받은 업소이름  저장
		wrongName = intent.getExtras().getString("busiName").toString();
		
		aboutInfo.setText("\n\'" + wrongUniv + "\'에 있는 " + "\'" + wrongName + "\'의 정보중에서... ");

		// 왼쪽 라디오 버튼 그룹
		radioGroup1.setOnCheckedChangeListener(
				new RadioGroup.OnCheckedChangeListener() {					
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// 오른쪽의 라디오 버튼 그룹체크 해제해주기
						if(radio5.isChecked())
							radioGroup2.clearCheck();
						if(radio6.isChecked())
							radioGroup2.clearCheck();
						if(radio7.isChecked())
							radioGroup2.clearCheck();
						if(radio8.isChecked())
							radioGroup2.clearCheck();
						if(radio9.isChecked())
							radioGroup2.clearCheck();
						/*if(radio5.isChecked())
							radio5.setChecked(false);
						if(radio6.isChecked())
							radio6.setChecked(false);
						if(radio7.isChecked())
							radio7.setChecked(false);
						if(radio8.isChecked())
							radio8.setChecked(false);
						if(radio9.isChecked())
							radio9.setChecked(false);*/
						
						switch(checkedId) {
						case R.id.radio0:
							checkedType = "대학교";
							break;
						case R.id.radio1:
							checkedType = "업소 이름";
							break;
						case R.id.radio2:
							checkedType = "업소 종류";
							break;
						case R.id.radio3:
							checkedType = "주소";
							break;
						case R.id.radio4:
							checkedType = "지도 정보";
							break;
						case R.id.radio10:
							checkedType = "기타";
							break;							
						}				
					}
				});
		
		// 오른쪽 라디오버튼 그룹
		radioGroup2.setOnCheckedChangeListener(
				new RadioGroup.OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group2, int checkedId2) {
						
						// 왼쪽 라디오 그룹 버튼 해제해주기
						if(radio0.isChecked())
							radioGroup1.clearCheck();
						if(radio1.isChecked())
							radioGroup1.clearCheck();
						if(radio2.isChecked())
							radioGroup1.clearCheck();
						if(radio3.isChecked())
							radioGroup1.clearCheck();
						if(radio4.isChecked())
							radioGroup1.clearCheck();
						if(radio10.isChecked())
							radioGroup1.clearCheck();

						switch(checkedId2) {
						case R.id.radio5:
							checkedType = "사진";
							break;
						case R.id.radio6:
							checkedType = "설명";
							break;
						case R.id.radio7:
							checkedType = "태그";
							break;
						case R.id.radio8:
							checkedType = "네이버 검색 링크";
							break;
						case R.id.radio9:
							checkedType = "없어진 업소에요";
							break;					
						}
/*
						if(radio0.isChecked())
							radio0.setChecked(false);
						if(radio1.isChecked())
							radio1.setChecked(false);
						if(radio2.isChecked())
							radio2.setChecked(false);
						if(radio3.isChecked())
							radio3.setChecked(false);
						if(radio4.isChecked())
							radio4.setChecked(false);
						if(radio10.isChecked())
							radio10.setChecked(false);*/
					}
				});
		
		// "신고하기"버튼을 클릭하였을 때
		buttonSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String aboutWrongInfo = textMessage.getText().toString();	// 수정 내용 가져오기
	        	String[] mailTo = {"campuschannel0@gmail.com"};				// 받는사람
				String subject = "\'" + userName + "\'님께서  정보 수정을 요청합니다.";	// 메일 제목
				String message = "\'" + userName + "\'님께서 \'" + wrongUniv + "\'에 있는  \'" + wrongName + "\'의 정보 중에서 "
						+ "\'" + checkedType + "\'의 정보 수정을 요청합니다.\n수정 내용은 \'" + aboutWrongInfo + "\'입니다.";	// 메일 내용

				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, mailTo);			// 받는사람
				email.putExtra(Intent.EXTRA_SUBJECT, subject);		// 메일 제목
				email.putExtra(Intent.EXTRA_TEXT, message);			// 메일 내용

				// need this to prompts email client only
				email.setType("message/rfc822");
				startActivity(Intent.createChooser(email, "Choose an Email client :"));
				
				/*Intent it = new Intent(Intent.ACTION_SEND);
	        	 String[] mailaddr = {"sangmin9655@hanmail.net"};
	        	 
	        	 it.setType("plaine/text");
	        	 it.putExtra(Intent.EXTRA_EMAIL, mailaddr);	//받는사람
	        	 it.putExtra(Intent.EXTRA_SUBJECT, "[test]");
	        	 it.putExtra(Intent.EXTRA_TEXT, "\n\n" + "이게 될까여?"); 	// 첨부내용
	        	 
	        	 startActivity(it);*/
			}
		});
		
		// "뒤로가기" 버튼을 클릭하였을 때
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}