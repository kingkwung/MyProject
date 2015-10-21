package com.swdm.cc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class SearchData extends Activity implements View.OnClickListener {
   
	TextView univMent;
	Spinner mySpinner3;
	TextView storetypeMent;
	Spinner mySpinner4;
	TextView storenameMent;
	EditText nameSearch;
	TextView searchinfoMent;
	EditText tagSearch;
	Button searchStart;
	Button back;
	

   String initialUniv = "";
   int initialUnivNum = 0;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      //requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.search_data);

      // ���б� ��ϵ�
      final Spinner spinner3 = (Spinner) findViewById(R.id.mySpinner3);
      ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this,
            R.array.univSearch, android.R.layout.simple_spinner_item);
      adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner3.setAdapter(adapter3);
      // ���࿡ �������� ���б� �˻��ϱ⿡�� Ŭ������ �Ҹ��Ŷ��..�˻��б��� ���б��� �������ش�.
      if(MyInfoActivity.isClickInMyInfo) {
    	  MyInfoActivity.isClickInMyInfo = false;
    	  initialUniv = MainActivity.my_info.getUserUniv();
    	  
    	  if(initialUniv.equals("��õ���б�")) {
    		  initialUnivNum = 1;
    	  } else if(initialUniv.equals("���縯���б�")) {
    		  initialUnivNum = 2;
    	  } else if(initialUniv.equals("������б�����б�")) {
    		  initialUnivNum = 18;
    	  } else {
    		  initialUnivNum = 0;
    	  }
    	  spinner3.setSelection(initialUnivNum);
      }

      // ���� ���� ��ϵ�
      final Spinner spinner4 = (Spinner) findViewById(R.id.mySpinner4);
      ArrayAdapter adapter4 = ArrayAdapter.createFromResource(this,
            R.array.typeSearch, android.R.layout.simple_spinner_item);
      adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner4.setAdapter(adapter4);

      
		univMent = (TextView)findViewById(R.id.univMent);
		mySpinner3 = (Spinner)findViewById(R.id.mySpinner3);
		storetypeMent = (TextView)findViewById(R.id.storetypeMent);
		mySpinner4 = (Spinner)findViewById(R.id.mySpinner4);
		storenameMent = (TextView)findViewById(R.id.storenameMent);
		nameSearch = (EditText)findViewById(R.id.nameSearch);
		searchinfoMent = (TextView)findViewById(R.id.searchinfoMent);
		tagSearch = (EditText)findViewById(R.id.tagSearch);
		searchStart = (Button)findViewById(R.id.searchStart);
		back = (Button)findViewById(R.id.back);
		
		//���ڸ�������κ�
		univMent.setTypeface(MainActivity.jangmeFont);
		//mySpinner3.setTypeface(MainActivity.jangmeFont);
		storetypeMent.setTypeface(MainActivity.jangmeFont);
		//mySpinner4.setTypeface(MainActivity.jangmeFont);
		storenameMent.setTypeface(MainActivity.jangmeFont);
		nameSearch.setTypeface(MainActivity.jangmeFont);
		searchinfoMent.setTypeface(MainActivity.jangmeFont);
		tagSearch.setTypeface(MainActivity.jangmeFont);
		searchStart.setTypeface(MainActivity.jangmeFont);
		back.setTypeface(MainActivity.jangmeFont);
		
		//���ڻ������κ�
		univMent.setTextColor(Color.YELLOW);
		storetypeMent.setTextColor(Color.YELLOW);
		storenameMent.setTextColor(Color.YELLOW);
		mySpinner3.setBackgroundColor(Color.rgb(234, 234, 234));
		mySpinner4.setBackgroundColor(Color.rgb(234, 234, 234));
		nameSearch.setBackgroundColor(Color.rgb(234, 234, 234));
		tagSearch.setBackgroundColor(Color.rgb(234, 234, 234));
		
		//��ư�������κ�
		searchStart.setBackgroundColor(Color.rgb(250, 244, 192));
		back.setBackgroundColor(Color.rgb(250, 244, 192));
		

      
      
      searchStart.setOnClickListener(new OnClickListener()
      {
         public void onClick(View v) {
            
            // ����Ʈ�� ������ ���б�, ��������, �̸�, �±׸� �����ش�
            Intent intent = new Intent(SearchData.this, SearchResult.class);
            
            intent.putExtra("univ", spinner3.getSelectedItem().toString());
            intent.putExtra("type", spinner4.getSelectedItem().toString());
            intent.putExtra("name", nameSearch.getText());
            intent.putExtra("tag", tagSearch.getText());
            
            startActivity(intent);            
         }
      });      
      
      back.setOnClickListener(new OnClickListener()
      {
         public void onClick(View v)
         {
        	 finish();
//            Intent intent = new Intent(SearchData.this, MainActivity.class);
//            startActivity(intent);
         }
      });
   }

   @Override
   public void onClick(View v) {
   }
}