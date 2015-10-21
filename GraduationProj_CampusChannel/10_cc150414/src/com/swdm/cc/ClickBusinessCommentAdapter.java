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

public class ClickBusinessCommentAdapter extends BaseAdapter {
	
	Context ctx;
	int layout;
	ArrayList<ClickBusinessCommentFillList> commentList;
	LayoutInflater inf;
	
	public ClickBusinessCommentAdapter(Context ctx, int layout, ArrayList<ClickBusinessCommentFillList> coList) {
		this.ctx = ctx;
		this.layout = layout;
		this.commentList = coList;
		
		inf = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return commentList.size();
	}
	
	@Override
	public Object getItem(int position) {
		return commentList.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null) {
			convertView = inf.inflate(layout, null);
		}
		
		ImageView cWriterImg = (ImageView)convertView.findViewById(R.id.cWriterImg);
		TextView cWriterName = (TextView)convertView.findViewById(R.id.cWriterName);
		TextView commentDate = (TextView)convertView.findViewById(R.id.commentDate);
		TextView commentText = (TextView)convertView.findViewById(R.id.commentText);
		
		ClickBusinessCommentFillList dto = commentList.get(position);
		
		cWriterName.setText(dto.getcWriterName());
		commentDate.setText(dto.getCommentDate());
		commentText.setText(dto.getCommentText());
		
		cWriterName.setTypeface(MainActivity.jangmeFont);
		commentDate.setTypeface(MainActivity.jangmeFont);
		commentText.setTypeface(MainActivity.jangmeFont);
		
		AQuery aqImg = new AQuery(cWriterImg);

		// 이미지뷰에 이미지 집어 넣기
		aqImg.id(cWriterImg).image("https://graph.facebook.com/" + dto.getcWriterId() + "/picture");
		
		return convertView;
	}	
}