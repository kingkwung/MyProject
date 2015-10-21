package com.swdm.cc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NoticeMainActivity extends Activity {
	
	ListView listViewNotice;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_main_activity);
		
		final String []data = {"- 공지사항", "- 도움말", "- 묻고 답하기", "- 회원탈퇴"};
		
		listViewNotice = (ListView)findViewById(R.id.listViewNotice);
		
		ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, data);
		
		listViewNotice.setAdapter(adapter);
		
		// 이벤트 처리
		listViewNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id){
				
				Log.i("sangmin", data[position]);
				TextView txtView = (TextView)v;
				Log.i("sangmin", txtView.getText().toString());
				
				switch(position)
				{
				case 0:
					Intent intent = new Intent(NoticeMainActivity.this, NoticeNoticeActivity.class);
					startActivity(intent);
					break;
				/*case 1:
					Intent intent2 = new Intent(NoticeMainActivity.this, .class);
					startActivity(intent2);
					break;
				case 2:
					Intent intent3 = new Intent(NoticeMainActivity.this, .class);
					startActivity(intent3);
					break;
				case 3:
					Intent intent4 = new Intent(NoticeMainActivity.this, .class);
					startActivity(intent4);
					break;*/
				}
			}
		});
		
	}
}