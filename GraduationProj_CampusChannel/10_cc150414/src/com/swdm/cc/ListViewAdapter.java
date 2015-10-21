package com.swdm.cc;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


@SuppressLint("NewApi")
public class ListViewAdapter extends BaseAdapter{
	
	private Context mContext = null;
	private ArrayList<Store> mStoreData = new ArrayList<Store>();
	
	public ListViewAdapter(Context mContext){
		super();
		this.mContext = mContext;
	}
	
	@Override
	public int getCount(){
		return mStoreData.size();
	}
	
	@Override
	public Store getItem(int position){
		return mStoreData.get(position);
	}
	
	@Override
	public long getItemId(int position){
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder;
		
		if(convertView == null){
			holder = new ViewHolder();

			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.search_result_listview_item, null);
			
			holder.mIcon = (ImageView)convertView.findViewById(R.id.searchResult_image);
			holder.mPercentage = (TextView)convertView.findViewById(R.id.searchResult_percentage);
			holder.mRank = (TextView)convertView.findViewById(R.id.searchResult_rank);
			holder.mName = (TextView)convertView.findViewById(R.id.searchResult_name);
			holder.mType = (TextView)convertView.findViewById(R.id.searchResult_type);
			holder.mUniv = (TextView)convertView.findViewById(R.id.searchResult_univ);
			holder.mTags = (TextView)convertView.findViewById(R.id.searchResult_tags);			
			
			//글자색 지정부분 (글자크기는 search_result_listview_item.xml에서 지정해줌)
			holder.mPercentage.setTextColor(Color.BLACK);
			holder.mRank.setTextColor(Color.rgb(255, 54, 54));
			//holder.mName.setTextColor(Color.rgb(255,187,0));
			holder.mName.setTextColor(Color.parseColor("#FF00BF"));
			holder.mType.setTextColor(Color.BLACK);
			holder.mUniv.setTextColor(Color.BLACK);
			holder.mTags.setTextColor(Color.BLACK);	
			
			//글씨체 지정부분
			holder.mPercentage.setTypeface(MainActivity.jangmeFont);
			holder.mRank.setTypeface(MainActivity.jangmeFont);
			holder.mName.setTypeface(MainActivity.charismaFont);
			holder.mType.setTypeface(MainActivity.jangmeFont);
			holder.mUniv.setTypeface(MainActivity.jangmeFont);
			holder.mTags.setTypeface(MainActivity.jangmeFont);
			
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		Store mData = mStoreData.get(position);		
		
		//색상지정부분
		if(mData.getStoreColor_inList().equals("****")){
			//convertView.setBackgroundColor(Color.rgb(124, 119, 255));
			convertView.setBackgroundColor(Color.parseColor("#F9C8F8"));
			holder.mIcon.setImageResource(R.drawable.like4);
		}
		else if(mData.getStoreColor_inList().equals("***")){
			//convertView.setBackgroundColor(Color.rgb(107, 102, 255));
			convertView.setBackgroundColor(Color.parseColor("#F9DFF8"));
			holder.mIcon.setImageResource(R.drawable.like3);
		}
		else if(mData.getStoreColor_inList().equals("**")){
			//convertView.setBackgroundColor(Color.rgb(181, 178, 255));
			convertView.setBackgroundColor(Color.parseColor("#F7E1F7"));
			holder.mIcon.setImageResource(R.drawable.like2);
		}
		else if(mData.getStoreColor_inList().equals("*")){
			//convertView.setBackgroundColor(Color.rgb(218, 217, 255));F1F8F4
			convertView.setBackgroundColor(Color.parseColor("#FAF0F9"));
			holder.mIcon.setImageResource(R.drawable.like1);
		}
		//		
		//holder.mIcon.setVisibility(View.GONE);
		//		
		holder.mPercentage.setText("상위"+Double.toString(mData.getStorePercentage_forList())+"%");
		holder.mRank.setText("점수:"+Double.toString(mData.getPagerank())+"점");
		holder.mName.setText("이름::"+mData.getStoreName());
		holder.mType.setText("종류:"+mData.getStoreType());
		holder.mUniv.setText("인근학교:"+mData.getStoreUniversity());
		holder.mTags.setText("태그:"+mData.getStoreTags());
		
		return convertView;
	}	

	public void set_mStoreData(ArrayList<Store> input_storeArrayList){
		this.mStoreData = input_storeArrayList;
	}
	
	// 0329 상민
	public void set_emptyData(){
		this.mStoreData.clear();;
	}
	
	private class ViewHolder{
		public ImageView mIcon;		
		public TextView mPercentage;		
		public TextView mRank;		
		public TextView mName;		
		public TextView mType;		
		public TextView mUniv;		
		public TextView mTags;
	}
}