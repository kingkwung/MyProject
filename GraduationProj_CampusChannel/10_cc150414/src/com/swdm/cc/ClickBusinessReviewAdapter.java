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

public class ClickBusinessReviewAdapter extends BaseAdapter {
	
	Context ctx;
	int layout;
	ArrayList<ClickBusinessReviewFillList> reviewList;
	LayoutInflater inf;
	
	public ClickBusinessReviewAdapter(Context ctx, int layout, ArrayList<ClickBusinessReviewFillList> reList) {
		this.ctx = ctx;
		this.layout = layout;
		this.reviewList = reList;
		
		inf = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return reviewList.size();
	}
	
	@Override
	public Object getItem(int position) {
		return reviewList.get(position);
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
		
		ImageView writerImg = (ImageView)convertView.findViewById(R.id.writerImg);
		TextView writerName = (TextView)convertView.findViewById(R.id.writerName);
		TextView reviewDate = (TextView)convertView.findViewById(R.id.reviewDate);
		TextView reviewText = (TextView)convertView.findViewById(R.id.reviewText);
		TextView numOfLike = (TextView)convertView.findViewById(R.id.numOfLike);
		TextView numOfComment = (TextView)convertView.findViewById(R.id.numOfComment);
		
		ClickBusinessReviewFillList dto = reviewList.get(position);
		
		writerName.setText(dto.getWriterName());
		reviewDate.setText(dto.getReviewDate());
		reviewText.setText(dto.getReviewText());
		numOfLike.setText("좋아요 " + dto.getNumOfLike());
		numOfComment.setText("댓글 " + dto.getNumOfComment());
		
		writerName.setTypeface(MainActivity.jangmeFont);
		reviewDate.setTypeface(MainActivity.jangmeFont);
		reviewText.setTypeface(MainActivity.jangmeFont);
		numOfLike.setTypeface(MainActivity.jangmeFont);
		numOfComment.setTypeface(MainActivity.jangmeFont);
		
		AQuery aqImg = new AQuery(writerImg);

		// 이미지뷰에 이미지 집어 넣기
		aqImg.id(writerImg).image("https://graph.facebook.com/" + dto.getWriterId() + "/picture");
		
		return convertView;
	}	
}