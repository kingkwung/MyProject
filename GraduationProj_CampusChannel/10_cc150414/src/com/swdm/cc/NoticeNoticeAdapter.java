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

public class NoticeNoticeAdapter extends BaseAdapter {
	
	Context ctx;
	int layout;
	ArrayList<NoticeNoticeFillList> noticeList;
	LayoutInflater inf;
	
	public NoticeNoticeAdapter(Context ctx, int layout, ArrayList<NoticeNoticeFillList> noticeList) {
		this.ctx = ctx;
		this.layout = layout;
		this.noticeList = noticeList;
		
		inf = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return noticeList.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return noticeList.get(position);
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
		
		ImageView noticeImg = (ImageView)convertView.findViewById(R.id.noticeImg);
		TextView admin = (TextView)convertView.findViewById(R.id.admin);
		TextView date = (TextView)convertView.findViewById(R.id.date);
		TextView text = (TextView)convertView.findViewById(R.id.text);
		
		NoticeNoticeFillList dto = noticeList.get(position);
		
		admin.setText(dto.getAdmin());
		date.setText(dto.getDate());
		text.setText(dto.getText());

		AQuery aqLogo = new AQuery(noticeImg);

		// 이미지뷰에 이미지 집어 넣기
		aqLogo.id(noticeImg).image("http://sangmin9655.dothome.co.kr/files/logo.png");
		
		return convertView;
	}
	
}
