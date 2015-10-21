package com.swdm.cc;


import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SetListView {

	//private int count = 0;
	

	public void getInfoFromDB() {
		
		// 데이터베이스에 접근해 원하는 정보를 검색
		DBAsyncTask dba = new DBAsyncTask();
		/*DBAsynkTask를 사용하기전에는 항상 어디서 DBAsynkTask를 부르는지 명시해줘야한다*/
		dba.setDBmanager_type("from_SetListView");
		dba.execute(0);
		
	}

}
