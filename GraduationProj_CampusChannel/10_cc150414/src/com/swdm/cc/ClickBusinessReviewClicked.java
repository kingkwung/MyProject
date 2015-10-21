package com.swdm.cc;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.androidquery.AQuery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ClickBusinessReviewClicked extends Activity implements
		View.OnClickListener {

	public static Context context;
	public static TextView writerName, reviewDate, writersRec, writersEval,
			reviewText, commentTitle, numOfLike, numOfComment;
	public static ListView commentListView;
	public static EditText typeComment;
	public static String cBusiId, cWriterId, cWriterName, cWriteDate,
			cReviewText;
	public static Integer cReviewId, cNumOfLike, cNumOfComment,
			numOfWritersRec, numOfWritersEval;
	public static Boolean duplicated = false;
	public static String likeThisIds;
	public static String[] arrLikeThisIds;
	public static ArrayList<ClickBusinessCommentFillList> originCommentList = new ArrayList<ClickBusinessCommentFillList>();
	ImageView writerImg;
	Integer cntClickLike = 0;
	Button back, likeThisReview, registerComment;
	String whatUpdate1 = "", whatUpdate3 = "", todayDate = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.click_business_review_clicked);

		back = (Button) findViewById(R.id.back);
		writerImg = (ImageView) findViewById(R.id.writerImg);
		writerName = (TextView) findViewById(R.id.writerName);
		reviewDate = (TextView) findViewById(R.id.reviewDate);
		writersRec = (TextView) findViewById(R.id.writersRec);
		writersEval = (TextView) findViewById(R.id.writersEval);
		reviewText = (TextView) findViewById(R.id.reviewText);
		numOfLike = (TextView) findViewById(R.id.numOfLike);
		numOfComment = (TextView) findViewById(R.id.numOfComment);
		likeThisReview = (Button) findViewById(R.id.likeThisReview);
		registerComment = (Button) findViewById(R.id.registerComment);
		commentListView = (ListView) findViewById(R.id.commentListView);
		typeComment = (EditText) findViewById(R.id.typeComment);
		commentTitle = (TextView) findViewById(R.id.commentTitle);

		context = getApplicationContext();

		// 클릭한 리뷰의 정보를 인텐트로 받아서 가져옴
		setData();

		// "뒤로가기" 버튼을 누르면 메인화면으로 넘어간다
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		// "좋아요" 버튼을 누르면
		likeThisReview.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (duplicated) {
					Toast.makeText(getApplicationContext(), "이미 좋아요를 누르셨습니다!",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "좋아요를 누르셨습니다!",
							Toast.LENGTH_SHORT).show();
					cNumOfLike++;
					numOfLike.setText("좋아요 "
							+ ClickBusinessReviewClicked.cNumOfLike);

					// 리스트뷰의 좋아요 수를 업데이트 해준다
					for (int k = 0; k < ClickBusiness.originReviewList.size(); k++) {
						if (cReviewId == ClickBusiness.originReviewList.get(k)
								.getReviewId()) {
							ClickBusiness.originReviewList.get(k).setNumOfLike(
									cNumOfLike);
						}
					}

					// DB에 좋아요 수 업데이트 ,DB에 좋아요 누른 사람 id 추가
					whatUpdate1 = "updateLike";
					UPAsynkTask upaLike = new UPAsynkTask();
					upaLike.execute(0);

					duplicated = true;
				}
			}
		});

		// "댓글 달기" 버튼을 누르면
		registerComment.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (typeComment.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "댓글을 입력해주세요!",
							Toast.LENGTH_SHORT).show();
				} else {
					todayDate = getCurrentTime();
					cNumOfComment++;
					numOfComment.setText("댓글 " + cNumOfComment);

					Toast.makeText(getApplicationContext(),
							"감사합니다. 댓글이 등록 되었습니다!", Toast.LENGTH_SHORT).show();

					// 댓글을 디비에 추가, 댓글 수를 업데이트
					whatUpdate3 = "updateComment";
					UPAsynkTask upaCo = new UPAsynkTask();
					upaCo.execute(0);

					// 리스트뷰의 댓글 수를 업데이트 해준다
					for (int k = 0; k < ClickBusiness.originReviewList.size(); k++) {
						if (cReviewId == ClickBusiness.originReviewList.get(k)
								.getReviewId()) {
							ClickBusiness.originReviewList.get(k)
									.setNumOfComment(cNumOfComment);
						}
					}

					// 업소 리뷰 목록 가져오기
					DBAsyncTask dbaNewComment = new DBAsyncTask();
					dbaNewComment
							.setDBmanager_type("from_ClickBusinessReviewClicked_fillCommentList");
					dbaNewComment.execute(0);
				}
			}
		});
	}

	public void setData() {
		Intent intent = getIntent();

		cReviewId = Integer.parseInt(intent.getExtras().get("cReviewId")
				.toString());
		cBusiId = intent.getExtras().get("cBusiId").toString();
		cWriterId = intent.getExtras().get("cWriterId").toString();
		cWriterName = intent.getExtras().get("cWriterName").toString();
		cWriteDate = intent.getExtras().get("cWriteDate").toString();
		cReviewText = intent.getExtras().get("cReviewText").toString();
		cNumOfLike = Integer.parseInt(intent.getExtras().get("cNumOfLike")
				.toString());
		cNumOfComment = 0;

		AQuery aqImg = new AQuery(writerImg);
		aqImg.id(writerImg).image(
				"https://graph.facebook.com/" + cWriterId + "/picture");

		writerName.setText(cWriterName);
		reviewDate.setText(cWriteDate);
		reviewText.setText(cReviewText);
		numOfLike.setText("좋아요 " + cNumOfLike);
		// numOfComment.setText("댓글 " + cNumOfComment);

		writerName.setTypeface(MainActivity.jangmeFont);
		reviewDate.setTypeface(MainActivity.jangmeFont);
		reviewText.setTypeface(MainActivity.jangmeFont);
		numOfLike.setTypeface(MainActivity.jangmeFont);
		numOfComment.setTypeface(MainActivity.jangmeFont);

		// 좋아요를 체크 했었는지 확인
		DBAsyncTask dbaCheck = new DBAsyncTask();
		dbaCheck.setDBmanager_type("from_ClickBusinessReviewClicked_checkLike");
		dbaCheck.execute(0);

		// 리뷰 쓴 사람의 추천수, 평가수를 가져옴
		DBAsyncTask dbaCheckWriter = new DBAsyncTask();
		dbaCheckWriter
				.setDBmanager_type("from_ClickBusinessReviewClicked_checkWriter");
		dbaCheckWriter.execute(0);

		// 업소 리뷰 목록 가져오기
		DBAsyncTask dbaComment = new DBAsyncTask();
		dbaComment
				.setDBmanager_type("from_ClickBusinessReviewClicked_fillCommentList");
		dbaComment.execute(0);
	}

	// '리스트뷰 사이즈를 조절하는 메소드'
	public static void listViewHeightSet(Adapter listAdapter, ListView listView) {
		int totalHeight = 0;

		if (listAdapter.getCount() > 3) {
			for (int i = 0; i < 3; i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
		} else {
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {

	}

	// 디비에 업데이트 시킨다
	private class UPAsynkTask extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Object doInBackground(Object... params) {

			boolean result = false;
			boolean result2 = false;

			DBmanager db = new DBmanager();

			if (whatUpdate1.equals("updateLike")) {
				result = db.insert("UPDATE reviewlist SET numOfLike = '"
						+ cNumOfLike + "' where reviewId = '" + cReviewId
						+ "' ");

				if (likeThisIds.equals("")) {
					result2 = db.insert("UPDATE reviewlist SET likeThisIds = '"
							+ MainActivity.my_info.getUserId()
							+ "' where reviewId = '" + cReviewId + "' ");
				} else {
					result2 = db.insert("UPDATE reviewlist SET likeThisIds = '"
							+ likeThisIds + ", "
							+ MainActivity.my_info.getUserId()
							+ "' where reviewId = '" + cReviewId + "' ");
				}
				result = result && result2;
			}

			if (whatUpdate3.equals("updateComment")) {
				result = db
						.insert("insert into commentlist(reviewId, cWriterId, cWriterName, commentDate, commentText) values('"
								+ cReviewId
								+ "', '"
								+ MainActivity.my_info.getUserId()
								+ "', '"
								+ MainActivity.my_info.getUserName()
								+ "', '"
								+ todayDate
								+ "','"
								+ typeComment.getText().toString() + "')");
				result2 = db.insert("UPDATE reviewlist SET numOfComment = '"
						+ cNumOfComment + "' where reviewId = '" + cReviewId
						+ "' ");

				result = result && result2;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);

			Toast.makeText(getApplicationContext(), "UPDATE RECORD", 0);
		}

		@Override
		protected void onProgressUpdate(Object... values) {

			super.onProgressUpdate(values);
		}
	}

	// 현재의 시간을 구한다
	public static String getCurrentTime() {
		GregorianCalendar today = new GregorianCalendar();

		int year = today.get(today.YEAR);
		String month = Integer.toString(today.get(today.MONTH) + 1);
		String day = Integer.toString(today.get(today.DAY_OF_MONTH));
		String hour = Integer.toString(today.get(today.HOUR_OF_DAY));
		String minute = Integer.toString(today.get(today.MINUTE));
		String second = Integer.toString(today.get(today.SECOND));

		if (month.length() < 2) {
			month = "0" + month;
		}
		if (day.length() < 2) {
			day = "0" + day;
		}
		if (hour.length() < 2) {
			hour = "0" + hour;
		}
		if (minute.length() < 2) {
			minute = "0" + minute;
		}
		if (second.length() < 2) {
			second = "0" + second;
		}

		return year + "." + month + "." + day + " " + hour + ":" + minute + ":"
				+ second;
	}
}