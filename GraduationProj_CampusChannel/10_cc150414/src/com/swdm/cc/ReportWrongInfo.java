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
	
	String wrongUniv; // ClickBusiness���� ����Ʈ�� ���� ���б��̸�  ����
	String wrongName; // ClickBusiness���� ����Ʈ�� ���� �����̸�  ����
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
		// ClickBusiness���� ����Ʈ�� ���� ���б��̸�  ����
		wrongUniv = intent.getExtras().getString("univName").toString();
		// ClickBusiness���� ����Ʈ�� ���� �����̸�  ����
		wrongName = intent.getExtras().getString("busiName").toString();
		
		aboutInfo.setText("\n\'" + wrongUniv + "\'�� �ִ� " + "\'" + wrongName + "\'�� �����߿���... ");

		// ���� ���� ��ư �׷�
		radioGroup1.setOnCheckedChangeListener(
				new RadioGroup.OnCheckedChangeListener() {					
					
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// �������� ���� ��ư �׷�üũ �������ֱ�
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
							checkedType = "���б�";
							break;
						case R.id.radio1:
							checkedType = "���� �̸�";
							break;
						case R.id.radio2:
							checkedType = "���� ����";
							break;
						case R.id.radio3:
							checkedType = "�ּ�";
							break;
						case R.id.radio4:
							checkedType = "���� ����";
							break;
						case R.id.radio10:
							checkedType = "��Ÿ";
							break;							
						}				
					}
				});
		
		// ������ ������ư �׷�
		radioGroup2.setOnCheckedChangeListener(
				new RadioGroup.OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(RadioGroup group2, int checkedId2) {
						
						// ���� ���� �׷� ��ư �������ֱ�
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
							checkedType = "����";
							break;
						case R.id.radio6:
							checkedType = "����";
							break;
						case R.id.radio7:
							checkedType = "�±�";
							break;
						case R.id.radio8:
							checkedType = "���̹� �˻� ��ũ";
							break;
						case R.id.radio9:
							checkedType = "������ ���ҿ���";
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
		
		// "�Ű��ϱ�"��ư�� Ŭ���Ͽ��� ��
		buttonSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String aboutWrongInfo = textMessage.getText().toString();	// ���� ���� ��������
	        	String[] mailTo = {"campuschannel0@gmail.com"};				// �޴»��
				String subject = "\'" + userName + "\'�Բ���  ���� ������ ��û�մϴ�.";	// ���� ����
				String message = "\'" + userName + "\'�Բ��� \'" + wrongUniv + "\'�� �ִ�  \'" + wrongName + "\'�� ���� �߿��� "
						+ "\'" + checkedType + "\'�� ���� ������ ��û�մϴ�.\n���� ������ \'" + aboutWrongInfo + "\'�Դϴ�.";	// ���� ����

				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, mailTo);			// �޴»��
				email.putExtra(Intent.EXTRA_SUBJECT, subject);		// ���� ����
				email.putExtra(Intent.EXTRA_TEXT, message);			// ���� ����

				// need this to prompts email client only
				email.setType("message/rfc822");
				startActivity(Intent.createChooser(email, "Choose an Email client :"));
				
				/*Intent it = new Intent(Intent.ACTION_SEND);
	        	 String[] mailaddr = {"sangmin9655@hanmail.net"};
	        	 
	        	 it.setType("plaine/text");
	        	 it.putExtra(Intent.EXTRA_EMAIL, mailaddr);	//�޴»��
	        	 it.putExtra(Intent.EXTRA_SUBJECT, "[test]");
	        	 it.putExtra(Intent.EXTRA_TEXT, "\n\n" + "�̰� �ɱ?"); 	// ÷�γ���
	        	 
	        	 startActivity(it);*/
			}
		});
		
		// "�ڷΰ���" ��ư�� Ŭ���Ͽ��� ��
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}