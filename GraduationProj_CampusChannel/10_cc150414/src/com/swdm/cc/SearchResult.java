package com.swdm.cc;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchResult extends Activity {
	Button back;
	public static Integer idR;
	public static String univR;
	public static String typeR;
	public static String nameR;
	public static String tagR;
	public static String imgR;
	public static String univText;
	public static String typeText;
	public static String nameText;
	public static String tagText;
	String imgText;
	public static String allTags;
	public static String[] splitAllTags;
	public static int count = 0;
	
	public static SearchAlgorithm gotoAlgorithm;
	public static ArrayList<String> calculatedData;
	
	// public static ListView list;
	
	final static ArrayList<String> data = new ArrayList<String>();
	public static ArrayAdapter<String> Adapter;
	
	
	public static ListView mListView=null;
	public static ListViewAdapter mAdapter=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);
		back = (Button) findViewById(R.id.back);
		mListView = (ListView) findViewById(R.id.list);
		Log.i("ERRORCHECK","AAAAA");

		this.setData(); // 인텐트로 받은 정보를 변수에 저장하는 메소드
		// 데이터베이스에 접근해 원하는 정보를 검색
		DBAsyncTask dba = new DBAsyncTask(this);
		dba.setDBmanager_type("from_SearchResult");
		dba.setContext(getApplicationContext());
		dba.execute(0);
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		//list. setBackgroundColor(Color.BLUE);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				
				String item2 = calculatedData.get(position);
				Log.i("HHHHERE", item2);
				/*
				 * Toast.makeText( getApplicationContext(), item + " " +
				 * item2 + " " + position + " " + id, Toast.LENGTH_SHORT)
				 * .show();
				 */

				Intent intent = new Intent(SearchResult.this,
						ClickBusiness.class);
				intent.putExtra("selected", item2);
				startActivity(intent);
			}
		});
		

	}

	public void setData() {
		Intent intent = getIntent();

		univText = intent.getExtras().get("univ").toString();
		typeText = intent.getExtras().get("type").toString();
		nameText = intent.getExtras().get("name").toString();
		tagText = intent.getExtras().get("tag").toString();
		// imgText = intent.getExtras().get("img").toString();
	}
	
	
	
	
	
//	
//	private class ViewHolder{
//		public ImageView mIcon;
//		
//		public TextView mPercentage;
//		
//		public TextView mRank;
//		
//		public TextView mName;
//		
//		public TextView mType;
//		
//		public TextView mUniv;
//		
//		public TextView mTags;
//	}
//	
//	private class ListViewAdapter extends BaseAdapter{
//		
//		private Context mContext = null;
//		private ArrayList<Store> mStoreData = new ArrayList<Store>();
//		
//		public ListViewAdapter(Context mContext){
//			super();
//			this.mContext = mContext;
//		}
//		
//		@Override
//		public int getCount(){
//			return mStoreData.size();
//		}
//		
//		@Override
//		public Store getItem(int position){
//			return mStoreData.get(position);
//		}
//		
//		@Override
//		public long getItemId(int position){
//			return position;
//		}
//		
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent){
//			ViewHolder holder;
//			
//			if(convertView == null){
//				holder = new ViewHolder();
//				
//				LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				convertView = inflater.inflate(R.layout.listview_item_layout_01, null);
//				
//				holder.mIcon = (ImageView)convertView.findViewById(R.id.myImage);
//				holder.mPercentage = (TextView)convertView.findViewById(R.id.searchResult_percentage);
//				holder.mRank = (TextView)convertView.findViewById(R.id.searchResult_rank);
//				holder.mName = (TextView)convertView.findViewById(R.id.searchResult_name);
//				holder.mType = (TextView)convertView.findViewById(R.id.searchResult_type);
//				holder.mUniv = (TextView)convertView.findViewById(R.id.searchResult_univ);
//				holder.mTags = (TextView)convertView.findViewById(R.id.searchResult_tags);
//				
//				convertView.setTag(holder);
//			}
//			else{
//				holder = (ViewHolder)convertView.getTag();
//			}
//			
//			Store mData = mStoreData.get(position);
//			
//			
//			//
//			holder.mIcon.setVisibility(View.GONE);
//			//
//			
//			holder.mPercentage.setText(Double.toString(mData.getStorePercentage_forList()));
//			holder.mRank.setText(Double.toString(mData.getPagerank()));
//			holder.mName.setText(mData.getStoreName());
//			holder.mType.setText(mData.getStoreType());
//			holder.mUniv.setText(mData.getStoreUniversity());
//			holder.mTags.setText(mData.getStoreTags());
//			
//			return convertView;
//		}
//
//	}

}