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
		
		// �����ͺ��̽��� ������ ���ϴ� ������ �˻�
		DBAsyncTask dba = new DBAsyncTask();
		/*DBAsynkTask�� ����ϱ������� �׻� ��� DBAsynkTask�� �θ����� ���������Ѵ�*/
		dba.setDBmanager_type("from_SetListView");
		dba.execute(0);
		
	}

}
