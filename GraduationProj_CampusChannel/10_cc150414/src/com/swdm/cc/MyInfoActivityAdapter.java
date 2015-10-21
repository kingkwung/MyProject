package com.swdm.cc;

import java.util.ArrayList;

import com.androidquery.AQuery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyInfoActivityAdapter extends BaseAdapter {
	
	Context ctx;
	int layout;
	ArrayList<FillMyActivityList> myList;
	LayoutInflater inf;
	
	public MyInfoActivityAdapter(Context ctx, int layout, ArrayList<FillMyActivityList> myList) {
		this.ctx = ctx;
		this.layout = layout;
		this.myList = myList;
		
		inf = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myList.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return myList.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null) {
			convertView = inf.inflate(layout, null);
		}
		
		TextView busiName = (TextView)convertView.findViewById(R.id.busiName);
		TextView busiUniv = (TextView)convertView.findViewById(R.id.busiUniv);
		TextView busiType = (TextView)convertView.findViewById(R.id.busiType);
		TextView busiScore = (TextView)convertView.findViewById(R.id.busiScore);
		TextView busiLike = (TextView)convertView.findViewById(R.id.busiLike);
		ImageView busiImg = (ImageView)convertView.findViewById(R.id.busiImg);
		
		FillMyActivityList dto = myList.get(position);
		
		busiName.setText(dto.getBusiName());
		busiUniv.setText(dto.getBusiUniv());
		busiType.setText(dto.getBusiType());
		busiScore.setText(dto.getBusiScore());
		busiLike.setText(dto.getBusiLike());

		AQuery aq5 = new AQuery(busiImg);

		// 이미지뷰에 이미지 집어 넣기
		aq5.id(busiImg).image("http://sangmin9655.dothome.co.kr/files/" + dto.getBusiImg());
		
		return convertView;
	}
	
}
