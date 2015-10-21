package com.swdm.cc;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class NoticeNoticeActivity extends Activity {

	public static ListView noticeListView;
	public static Context context;  
	public static ArrayList<NoticeNoticeFillList> originNoticeList = new ArrayList<NoticeNoticeFillList>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_notice_activity);		

		noticeListView = (ListView)findViewById(R.id.noticeListView);
		context = getApplicationContext();
		
		// 공지사항 목록 가져오기
		DBAsyncTask dbaNotice = new DBAsyncTask();
		dbaNotice.setDBmanager_type("from_NoticeNoticeActivity_fillNoticeList");
		dbaNotice.execute(0);		
	}
}