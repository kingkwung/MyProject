package com.swdm.cc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

@SuppressLint("NewApi")
public class ClickBusiness extends Activity implements View.OnClickListener {

	// 합친날짜 150414..
	public static Context context;
	public static String countRec, countEval, sel_id, totalScore, todayDate;
	String whatInsert = "", recommend = "";
	int countR, countE, totalS;
	String selectedVal; // searchResult.class 에서 선택한 업소의 정보를 인텐트로 받아와서 저장
	String sel_univ; // selectedVal 에서 대학교이름을 잘라서 저장
	String sel_type; // selectedVal 에서 업소종류를 잘라서 저장
	String sel_name; // selectedVal 에서 업소이름을 잘라서 저장
	String sel_pageRank; // selectedVal 에서 업소페이지랭크값을 잘라서 저장
	public static String imgURL;
	public static String[] imgURLs;
	String onlyTags; // selectedVal 에서 태그를 잘라서 저장
	String[] existedTags; // onlyTags 에서 태그를 하나씩 잘라서 저장
	String add_newTag; // 사용자가 새로운 태그를 쓰면 여기에 저장
	String[] saveValues; // saveValues[0] = sel_univ
	// saveValues[1] = sel_type
	// saveValues[2] = sel_name
	int cursorTag = 0; // selectedVal 에서 태그가 시작되는 위치
	int available_plus = 0; // 0이면 중복태그가 없고, 1이면 중복되는 태그가 이미 존재
	public static String busi_id; // 디비에서 가져온 업소 아이드를 담는다
	public static String busi_add; // 디비에서 가져온 주소를 담는다
	public static TextView aboutUniv, aboutName, aboutTag, phonenum,
			reviewTitle, aboutAddress;
	public static WebView aboutPic, goNaver;
	public static TextView aboutInfo, reportMis; // sangmin 0201
	public static EditText new_tag, typeReview;
	Button showMap; // '지도 보기' 버튼
	Button call; // '전화 걸기' 버튼
	Button insert_tag, registerReview, back, facebook, recommendation;
	Button searchNaver; // 네이버로 검색 0118상민추가
	Button evaluate_tag; // 평가하기 버튼 1203영남새로추가 부분
	Button preImg; // 이전 사진 보기 0118상민추가
	Button nextImg; // 다음 사진 보기 0118상민추가
	public static int numOfImg = 1; // 사진개수 0118상민추가
	public static int indexOfCurrentImg = 1; // 사진 0118상민추가
	public static String domainAddress = ""; // naver 검색결과 주소
	public static boolean isAlreadyEvaluate = false; // 기존에 내가 평가했던 것인지 판별하기 위한
	// 변수, 평가하기 버튼
	private ArrayList<FrdInfo> friendsList = new ArrayList<FrdInfo>();
	private ArrayList<String> friendRecommendStore = new ArrayList<String>();

	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	private String feedName = "", feedCaption = "", feedDescription = "",
			feedLink = "";
	public static String feedPicture = "";
	private String feedMessage = "";
	private String userRec = MainActivity.my_info.getUserRec();
	private String[] userRec_strArr = userRec.split(", ");
	private boolean isUserRec = false;
	private String userEval = MainActivity.my_info.getUserEvaluate();
	private String[] userEval_strArr = userEval.split(", ");
	private boolean isUserEval = false;
	private UiLifecycleHelper uiHelper;
	public static ListView reviewListView;
	public static ArrayList<ClickBusinessReviewFillList> originReviewList = new ArrayList<ClickBusinessReviewFillList>();
	ScrollView ScrollView1;
	boolean lastItemVisibleFlag = false;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.click_business);
		showMap = (Button) findViewById(R.id.showMap);
		call = (Button) findViewById(R.id.call);
		insert_tag = (Button) findViewById(R.id.insert_tag);
		back = (Button) findViewById(R.id.back);
		recommendation = (Button) findViewById(R.id.recommendation);
		aboutUniv = (TextView) findViewById(R.id.aboutUniv);
		aboutName = (TextView) findViewById(R.id.aboutName);
		phonenum = (TextView) findViewById(R.id.phonenum);
		aboutAddress = (TextView) findViewById(R.id.aboutAddress);
		aboutPic = (WebView) findViewById(R.id.aboutPic);
		searchNaver = (Button) findViewById(R.id.searchNaver); // '네이버로 검색'버튼
		goNaver = (WebView) findViewById(R.id.goNaver); // naver검색 결과로 이동하기 위해
		reportMis = (TextView) findViewById(R.id.reportMis); // sangmin 0201
		aboutInfo = (TextView) findViewById(R.id.aboutInfo);
		aboutTag = (TextView) findViewById(R.id.aboutTag);
		reviewTitle = (TextView) findViewById(R.id.reviewTitle);
		new_tag = (EditText) findViewById(R.id.new_tag);
		evaluate_tag = (Button) findViewById(R.id.evaluate_tag);
		preImg = (Button) findViewById(R.id.preImg); // 이전 사진 보기 0118상민추가
		nextImg = (Button) findViewById(R.id.nextImg); // 다음 사진 보기 0118상민추가
		friendsList = MainActivity.my_info.getFriendsList();
		facebook = (Button) findViewById(R.id.feed);
		reviewListView = (ListView) findViewById(R.id.reviewListView);
		ScrollView1 = (ScrollView) findViewById(R.id.ScrollView1);
		typeReview = (EditText) findViewById(R.id.typeReview); // 리뷰 적는 칸
		registerReview = (Button) findViewById(R.id.registerReview); // 리뷰 등록

		// 글씨체지정부분
		aboutUniv.setTypeface(MainActivity.jangmeFont);
		aboutName.setTypeface(MainActivity.jangmeFont);
		phonenum.setTypeface(MainActivity.jangmeFont);
		aboutAddress.setTypeface(MainActivity.jangmeFont);
		reportMis.setTypeface(MainActivity.jangmeFont);
		aboutInfo.setTypeface(MainActivity.jangmeFont);
		aboutTag.setTypeface(MainActivity.jangmeFont);
		new_tag.setTypeface(MainActivity.jangmeFont);
		reviewTitle.setTypeface(MainActivity.jangmeFont);
		typeReview.setTypeface(MainActivity.jangmeFont);

		aboutName.setTextColor(Color.YELLOW);
		aboutTag.setTextColor(Color.rgb(181, 178, 255));

		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);

		context = getApplicationContext();

		this.setData(); // sarchResult.class 에서 인텐트로 받아온 데이터를 처리하는 메소드

		userRec = MainActivity.my_info.getUserRec();
		userRec_strArr = userRec.split(", ");
		isUserRec = false;

		// Log.i("공유공유", userRec.toString());
		for (String record : userRec_strArr) {
			// Log.i("공유업체", sel_id + "지금 " + record);
			if (record.equals(sel_id)) {
				isUserRec = true;
				Log.i("이미공유", record);
				break;
			}
		}

		if (!isUserRec) {
			facebook.setAlpha((float) 0.5);
		} else {
			facebook.setAlpha((float) 1);
		}

		for (FrdInfo friend : friendsList) {

			friendRecommendStore = friend.getRecommendStore();
			for (String f_RecommendStore : friendRecommendStore) {
				if (f_RecommendStore.equals(sel_id)) {
					MainActivity.my_info.addEvaluateFriend(friend);
					// friend.addPriority(MainActivity.my_info.getUserPriority()/10);
					Log.i("이업체를 추천한놈",
							friend.getName() + "/" + friend.getUserC() + "/"
									+ friend.getPriority());
				}
			}
		}

		// 업소 리뷰 목록 가져오기
		DBAsyncTask dbaReview = new DBAsyncTask();
		dbaReview.setDBmanager_type("from_ClickBusiness_fillReviewList");
		dbaReview.execute(0);

		// 리뷰와 스크롤뷰와의 스크롤 제어
		reviewListView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				/*
				 * if(scrollState == OnScrollListener.SCROLL_STATE_IDLE &&
				 * lastItemVisibleFlag) {
				 * ScrollView1.requestDisallowInterceptTouchEvent(false); }
				 */
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastItemVisibleFlag = (totalItemCount > 0)
						&& (firstVisibleItem + visibleItemCount >= totalItemCount + 1);

				if (lastItemVisibleFlag) {
					ScrollView1.requestDisallowInterceptTouchEvent(false);
				} else {
					ScrollView1.requestDisallowInterceptTouchEvent(true);
				}
			}
		});

		// 리뷰를 클릭했을 때
		reviewListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Integer cReviewId;
				String cBusiId;
				String cWriterId;
				String cWriterName;
				String cWriteDate;
				String cReviewText;
				Integer cNumOfLike;
				Integer cNumOfComment;

				ClickBusinessReviewFillList clickItem = (ClickBusinessReviewFillList) parent
						.getItemAtPosition(position);

				cReviewId = clickItem.getReviewId();
				cBusiId = clickItem.getReviewBusiId();
				cWriterId = clickItem.getWriterId();
				cWriterName = clickItem.getWriterName();
				cWriteDate = clickItem.getReviewDate();
				cReviewText = clickItem.getReviewText();
				cNumOfLike = clickItem.getNumOfLike();
				cNumOfComment = clickItem.getNumOfComment();

				Intent intent = new Intent(ClickBusiness.this,
						ClickBusinessReviewClicked.class);

				intent.putExtra("cReviewId", cReviewId);
				intent.putExtra("cBusiId", cBusiId);
				intent.putExtra("cWriterId", cWriterId);
				intent.putExtra("cWriterName", cWriterName);
				intent.putExtra("cWriteDate", cWriteDate);
				intent.putExtra("cReviewText", cReviewText);
				intent.putExtra("cNumOfLike", cNumOfLike);
				intent.putExtra("cNumOfComment", cNumOfComment);

				startActivity(intent);
			}
		});

		// '리뷰 등록' 버튼을 눌렀을 때
		registerReview.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (typeReview.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "리뷰를 입력해주세요!",
							Toast.LENGTH_SHORT).show();
				} else {
					todayDate = getCurrentTime();
					whatInsert = "review";

					Toast.makeText(getApplicationContext(),
							"감사합니다. 리뷰가 등록 되었습니다!", Toast.LENGTH_SHORT).show();

					// 리뷰를 디비에 추가
					UPAsynkTask upaRe = new UPAsynkTask();
					upaRe.execute(0);

					// 업소 리뷰 목록 가져오기
					DBAsyncTask dbaReview = new DBAsyncTask();
					dbaReview
							.setDBmanager_type("from_ClickBusiness_fillReviewList");
					dbaReview.execute(0);
				}
			}
		});

		// '잘못된 정보 신고'를 눌렀을 때 //sangmin 0201
		reportMis.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 학교, 업소 이름
				// 학교, 업소 이름, 업소종류, 주소, 사진, 설명, 태그, 지도, 네이버 검색 링크, 사라진 업소 에요,
				// 기타.

				Intent intent = new Intent(ClickBusiness.this,
						ReportWrongInfo.class);
				intent.putExtra("univName", sel_univ); // 대학 이름 보내기
				intent.putExtra("busiName", sel_name); // 업소 이름 보내기
				startActivity(intent);
			}
		});

		// '전화 걸기' 버튼을 눌렀을 때
		call.setOnClickListener(new OnClickListener() {

			String phNum = phonenum.getText().toString();

			@Override
			public void onClick(View v) {
				if (phNum.equals("")) {
					Toast.makeText(getApplicationContext(), "등록된 전화번호가 없습니다.",
							Toast.LENGTH_SHORT).show();
				} else {
					// Intent intent = new Intent(Intent.ACTION_CALL,
					// Uri.parse("tel:" + phNum));

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse("tel:" + phNum));
					startActivity(intent);
				}

			}
		});

		// 'naver로 검색' 버튼을 눌렀을 때
		searchNaver.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (domainAddress.equals("")) {
					goNaver.loadUrl("http://www.naver.com"); // 검색결과가 없을 경우 네이버
																// 홈으로 이동
				} else {
					goNaver.loadUrl(domainAddress); // 해당 업소의 네이버 검색결과로 이동
				}
			}
		});

		// 이전 이미지 보여주기
		preImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				indexOfCurrentImg--;

				if (indexOfCurrentImg == 0) {
					Toast.makeText(getApplicationContext(), "이전 이미지가 없습니다",
							Toast.LENGTH_SHORT).show();
					indexOfCurrentImg = 1;
				} else {
					Toast.makeText(getApplicationContext(),
							indexOfCurrentImg + "/" + numOfImg,
							Toast.LENGTH_SHORT).show();
					aboutPic.loadUrl("http://sangmin9655.dothome.co.kr/files/"
							+ imgURLs[indexOfCurrentImg - 1]);
				}
			}
		});
		
		// 다음 이지미 보여주기
		nextImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				indexOfCurrentImg++;

				if (indexOfCurrentImg > numOfImg) {
					Toast.makeText(getApplicationContext(), "다음 이미지가 없습니다",
							Toast.LENGTH_SHORT).show();
					indexOfCurrentImg = numOfImg;
				} else {
					Toast.makeText(getApplicationContext(),
							indexOfCurrentImg + "/" + numOfImg,
							Toast.LENGTH_SHORT).show();
					aboutPic.loadUrl("http://sangmin9655.dothome.co.kr/files/"
							+ imgURLs[indexOfCurrentImg - 1]);
				}
			}
		});

		// "평가하기" 버튼을 누르면
		evaluate_tag.setOnClickListener(new OnClickListener() {

			private int selectedItem = -1;

			final String[] items = new String[] { "★★★★★", "★★★★", "★★★", "★★",
					"★" };

			public void onClick(View v) {

				userEval = MainActivity.my_info.getUserEvaluate();
				userEval_strArr = userEval.split(", ");
				isUserEval = false;

				Log.i("레코드", userEval);
				for (String record : userEval_strArr) {
					Log.i("레코드", record + " 평가업소 " + sel_id);
					if (record.equals(sel_id)) { // 평가했던 업소라면
						isUserEval = true;
						break;
					}
				}

				if (isUserEval) { // 디비에 접근해서 이거 내가 이미 "평가했던" 것인지 알아 두어야 한다.

					new AlertDialog.Builder(ClickBusiness.this)
							.setTitle("에이~")
							.setMessage("이미평가했어요")
							.setNeutralButton("닫기",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									}).show();
				} else {
					// 내가 이전에 평가했던 것들이 아니면~
					countE = Integer.parseInt(countEval) + 1;

					new AlertDialog.Builder(ClickBusiness.this)
							.setTitle("이 곳을 평가해주세요!")
							.setIcon(R.drawable.evaluateicon)
							.setSingleChoiceItems(items, -1,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											selectedItem = which;
										}
									})
							.setPositiveButton("확인",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											// 평가하기 버튼을 눌렀으나, 별 점수를 주지 않고 확인을
											// 눌렀을 때 예외처리
											if (selectedItem < 0) {
												Toast.makeText(
														getApplicationContext(),
														"평가를 하지 않았습니다",
														Toast.LENGTH_SHORT)
														.show();
											} else {
												totalS = Integer
														.parseInt(totalScore)
														+ 5 - selectedItem;

												whatInsert = "plusScore";
												UPAsynkTask upa3 = new UPAsynkTask();
												upa3.execute(0);

												Toast.makeText(
														getApplicationContext(),
														"점수 : "
																+ items[selectedItem]
																+ totalS,
														Toast.LENGTH_SHORT)
														.show();

												// 디비의 전체점수에 사용자가 부여한 점수 더하기

												MainActivity.my_info
														.addUserEvaluate(sel_id);

												if (selectedItem == 0) {
													for (FrdInfo friend : MainActivity.my_info
															.getEvaluatedFriend()) {
														friend.addPriority(MainActivity.my_info
																.getUserPriority() / 10.0 * 0.5);
													}
												} else if (selectedItem == 1) {
													for (FrdInfo friend : MainActivity.my_info
															.getEvaluatedFriend()) {
														friend.addPriority(MainActivity.my_info
																.getUserPriority() / 10.0 * 0.4);
													}
												} else if (selectedItem == 2) {
													for (FrdInfo friend : MainActivity.my_info
															.getEvaluatedFriend()) {
														friend.addPriority(MainActivity.my_info
																.getUserPriority() / 10.0 * 0.3);
													}
												}

												else if (selectedItem == 3) {
													for (FrdInfo friend : MainActivity.my_info
															.getEvaluatedFriend()) {
														friend.addPriority(MainActivity.my_info
																.getUserPriority() / 10.0 * 0.2);
													}
												} else if (selectedItem == 4) {
													for (FrdInfo friend : MainActivity.my_info
															.getEvaluatedFriend()) {
														friend.addPriority(MainActivity.my_info
																.getUserPriority() / 10.0 * 0.1);
													}
												}
												// 확인용
												for (FrdInfo friend : MainActivity.my_info
														.getEvaluatedFriend()) {
													Log.i("지금니가방금weight올려준놈들",
															friend.getName()
																	.toString()
																	+ Double.toString(friend
																			.getPriority()));
												}
												whatInsert = "priority_and_evaluated";
												UPAsynkTask upa = new UPAsynkTask();
												upa.execute(0);
												/*
												 * 
												 * 0119 친구들 한테 평가 기준으로 내
												 * weight부여해주는 것 완료 할일 : (완료)1.
												 * 1점부터 5점까지 기준으로 점수 차등으로주는것
												 * if문으로 구현하기 2. 친구들 올려놓은것 디비에
												 * 올리는 코드짜기(MainActivity
												 * .my_info.getEvaluatedFriend하면
												 * 주르륵 나온다 ) 3. 그리고
												 * userEvaluate(평가한 업체들)을 디비에
												 * 올려놓는 것도 같이
												 */
											}
										}
									})
							.setNegativeButton("취소",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 평가하기 버튼 누른 후 -> 별 점수를 주고 취소버튼을
											// 누른후에 -> 다시 평가하기 버튼을 누르고 별 점수를 주지
											// 않은 채로 확인버튼을 눌렀을 시에
											// 취소 했을 당시에 눌렀던 점수가 등록이 되는 경우의
											// 예외처리를 위해
											selectedItem = -1;
										}
									}).show();
					whatInsert = "countEval";
					UPAsynkTask upa2 = new UPAsynkTask();
					upa2.execute(0);
				}
			}
		});

		// "지도 보기" 버튼을 클릭 했을 때
		showMap.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (busi_add.equals("")) {
					Toast.makeText(getApplicationContext(), "주소가 등록되어있지 않습니다!",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(ClickBusiness.this,
							GoogleMapView.class);
					intent.putExtra("name", sel_name);
					intent.putExtra("address", busi_add);
					intent.putExtra("tags", onlyTags);
					startActivity(intent);
				}
			}
		});

		// "태그 등록" 버튼을 눌렀을 경우
		insert_tag.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				whatInsert = "tag";
				add_newTag = new_tag.getText().toString(); // 사용자가 입력한 태그를
				// add_newTag에 담는다
				// 중복된 태그가 있는지 확인한다
				for (int k = 0; k < existedTags.length; k++) {
					if (existedTags[k].equals(add_newTag)) {
						available_plus++;
					}
				}
				Log.i("log_tag", ">>> " + available_plus + " <<<");

				// 중복된 태그가 있는 경우(태그를 업데이트 할 수 없다( 추후에 태그 카운트를 업 시킨다))
				if (available_plus != 0) {
					Toast.makeText(getApplicationContext(), "이미 등록되어있는 태그입니다!",
							Toast.LENGTH_SHORT).show();
					available_plus = 0;
				}
				// 중복된 태그가 없는 경우
				else {
					Toast.makeText(getApplicationContext(), "태그가 등록되었습니다!!!",
							Toast.LENGTH_SHORT).show();
					add_newTag = onlyTags + ", " + add_newTag;
					// 태그정보를 업데이트 시킨다
					UPAsynkTask upa = new UPAsynkTask();
					upa.execute(0);
				}
			}
		});

		// "추천하기" 버튼을 누르면
		recommendation.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 업체 아이드를 디비에 추가
				whatInsert = "id";
				recommend = sel_id;

				userRec = MainActivity.my_info.getUserRec();
				userRec_strArr = userRec.split(", ");
				isUserRec = false;

				for (String record : userRec_strArr) {
					if (record.equals(recommend)) {
						isUserRec = true;
						break;
					}
				}

				if (!isUserRec) {
					countR = Integer.parseInt(countRec) + 1;
					// userRec = MainActivity.my_info.getUserRec() + ", " +
					// sel_id;
					MainActivity.my_info.setUserRec(MainActivity.my_info
							.getUserRec() + ", " + sel_id);

					UPAsynkTask upa = new UPAsynkTask();
					upa.execute(0);

					Toast.makeText(getApplicationContext(), "추천이 되었습니다!!",
							Toast.LENGTH_SHORT).show();

					Toast.makeText(getApplicationContext(),
							"페이스북에 공유를 할 수 있습니다!!", Toast.LENGTH_SHORT).show();

					facebook.setAlpha((float) 1);

					whatInsert = "countRec";
					UPAsynkTask upa2 = new UPAsynkTask();
					upa2.execute(0);
				} else {
					Toast.makeText(getApplicationContext(), "이미 추천 하셨습니다!!",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		// "뒤로가기" 버튼을 누르면 메인화면으로 넘어간다
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

		// "공유하기" 버튼을 누르면
		facebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				userRec = MainActivity.my_info.getUserRec();
				userRec_strArr = userRec.split(", ");
				isUserRec = false;

				for (String record : userRec_strArr) {
					if (record.equals(sel_id)) {
						isUserRec = true;
						Log.i("이미공유", record);
						break;
					}
				}

				if (!isUserRec) {
					Toast.makeText(getApplicationContext(),
							"공유를 하려면 추천을 먼저 해야해요!↗", Toast.LENGTH_SHORT).show();
				} else {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							ClickBusiness.this); // 여기서 this는 Activity의 this

					// 여기서 부터는 알림창의 속성 설정
					builder.setTitle("공유 확인 대화 상자")
							// 제목 설정
							.setMessage("페이스북에 이 업소를 공유하시겠습니까?")
							// 메세지 설정
							.setCancelable(true)
							// 뒤로 버튼 클릭시 취소 가능 설정
							.setPositiveButton("확인",
									new DialogInterface.OnClickListener() {
										// 확인 버튼 클릭시 설정
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											publishFeedDialog();
										}
									})
							.setNegativeButton("취소",
									new DialogInterface.OnClickListener() {
										// 취소 버튼 클릭시 설정
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											dialog.cancel();
										}
									});
					AlertDialog dialog = builder.create(); // 알림창 객체 생성
					dialog.show(); // 알림창 띄우기
				}
			}
		});
	}

	// facebook에 포스팅
	private void publishStory() {
		Session session = Session.getActiveSession();
		if (session != null) {

			Log.i("session공유", "session is open " + session.getPermissions());

			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			// Log.i("session3", "session is open " + session.getPermissions());
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);

				// Session.NewPermissionsRequest newPermissionsRequest = new
				// Session.NewPermissionsRequest(this,
				// PERMISSIONS).setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO).setDefaultAudience(SessionDefaultAudience.EVERYONE).setRequestCode(Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE);
				// session.requestNewPublishPermissions(newPermissionsRequest);
				// Log.i("permission", PERMISSIONS.toString());
				Log.i("session2", "session is open " + session.getPermissions());
				return;
			}

			Bundle postParams = new Bundle();
			// postParams.putString("name", "Campus Channel");
			// postParams.putString("caption", "-페이스북 글남기기-");
			// postParams.putString("description", "테스트테스트2");
			// postParams.putString("link", "http://www.naver.com");
			// postParams.putString("picture",
			// "http://graph.facebook.com/610627229057686/picture?type=small");
			// postParams.putString("message", "lowmans test");

			postParams.putString("name", feedName);
			postParams.putString("caption", feedCaption);
			postParams.putString("description", feedDescription);
			postParams.putString("link", feedLink);
			postParams.putString("picture", feedPicture);
			postParams.putString("message", feedMessage);

			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					JSONObject graphResponse = response.getGraphObject()
							.getInnerJSONObject();
					String postId = null;
					try {
						postId = graphResponse.getString("id");
						Toast.makeText(getApplicationContext(), "등록성공",
								Toast.LENGTH_SHORT).show();
					} catch (JSONException e) {
						Log.i("JSON error", "JSON error " + e.getMessage());
					}

					FacebookRequestError error = response.getError();
					if (error != null) {
						Toast.makeText(getApplicationContext(),
								error.getErrorMessage(), Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(getApplicationContext(), postId,
								Toast.LENGTH_LONG).show();
					}
				}
			};

			Request request = new Request(session, "me/feed", postParams,
					HttpMethod.POST, callback);
			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();

			//
			// /* make the API call */
			// new Request(
			// session,
			// "/me/permissions/{publish_actions}",
			// null,
			// HttpMethod.DELETE,
			// new Request.Callback() {
			// public void onCompleted(Response response) {
			// /* handle the result */
			// }
			// }
			// ).executeAsync();
		}
	}

	private void publishFeedDialog() {
		Bundle postParams = new Bundle();
		// postParams.putString("name", "Campus Channel");
		// postParams.putString("caption", "-페이스북 글남기기-");
		// postParams.putString("description", "테스트테스트2");
		// postParams.putString("link", "http://www.naver.com");
		// postParams.putString("picture",
		// "http://graph.facebook.com/610627229057686/picture?type=small");
		// postParams.putString("message", "lowmans test");

		postParams.putString("name", feedName);
		postParams.putString("caption", feedCaption);
		postParams.putString("description", feedDescription);
		postParams.putString("link", feedLink);
		postParams.putString("picture", feedPicture);
		postParams.putString("message", feedMessage);

		// boolean didCancel = FacebookDialog
		// .getNativeDialogDidComplete(postParams);
		// String completionGesture = FacebookDialog
		// .getNativeDialogCompletionGesture(postParams);
		// String postId = FacebookDialog.getNativeDialogPostId(postParams);

		if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			// Publish the post using the Share Dialog
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
					ClickBusiness.this).setName(feedName)
					.setCaption(feedCaption).setDescription(feedDescription)
					.setPicture(feedPicture).setLink(feedLink).build();
			uiHelper.trackPendingDialogCall(shareDialog.present());

		} else {
			// Fallback. For example, publish the post using the Feed Dialog
		}
	}

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data,
				new FacebookDialog.Callback() {
					@Override
					public void onError(FacebookDialog.PendingCall pendingCall,
							Exception error, Bundle data) {
						Log.e("Activity",
								String.format("Error: %s", error.toString()));
					}

					@Override
					public void onComplete(
							FacebookDialog.PendingCall pendingCall, Bundle data) {
						Log.i("Activity", "Success!");
					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	// 인텐트로 받아온 데이터를 처리하고 각각의 변수에 나누어 담는 메소드
	public void setData() {

		Intent intent = getIntent();
		selectedVal = intent.getExtras().getString("selected").toString();
		saveValues = selectedVal.split("/-/");
		sel_id = saveValues[0];
		sel_univ = saveValues[1];
		sel_type = saveValues[2];
		sel_name = saveValues[3];
		onlyTags = saveValues[4];
		sel_pageRank = saveValues[5];

		// sel_img = saveValues[4]; 이건 여기서안해주고 DBAsyncTask에서 업소주소가져올 때 같이 세팅해준다!

		/*
		 * cursorTag = sel_id.length() + sel_univ.length() + sel_type.length() +
		 * sel_name.length() + 4; onlyTags = selectedVal.substring(cursorTag,
		 * selectedVal.length());
		 */

		existedTags = onlyTags.split(", ");
		aboutUniv.setText(sel_univ);
		aboutName.setText(sel_name + " / " + sel_pageRank);
		aboutTag.setText(onlyTags);
		// Log.i("log_tag", "///" + sel_img + " // ");

		feedName = "[Campus Channel]친구가 추천하는 맛집!";
		feedDescription = "[" + sel_univ + " 맛집] " + sel_name;
		feedCaption = onlyTags;
		// feedLink =
		// "https://play.google.com/store/apps/details?id=com.swdm.cc";
		feedLink = "http://sangmin9655.dothome.co.kr/business_info_html/"
				+ sel_id + ".html";
		feedMessage = sel_univ + " " + sel_name;

		// // 디비에서 업소의 주소를 가져와서 주소칸에 채워준다
		DBAsyncTask dba = new DBAsyncTask();
		dba.setDBmanager_type("from_ClickBusiness");
		dba.execute(0);
	}

	// '리스트뷰 사이즈를 조절하는 메소드'
	public static void listViewHeightSet(Adapter listAdapter, ListView listView) {
		int totalHeight = 0;

		if (listAdapter.getCount() > 4) {
			for (int i = 0; i < 4; i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
				Log.i("sangmin", i + " " + totalHeight);
			}
		} else {
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
				Log.i("sangmin", i + " " + totalHeight);
			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	// 현재의 시간을 구함
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
			boolean result2 = true;
			DBmanager db = new DBmanager();
			if (whatInsert.equals("priority_and_evaluated")) {
				// userEvaluate(평가한 업체들)을 디비에 올려놓는 것
				result = db.insert("UPDATE users_info SET evaluated = '"
						+ MainActivity.my_info.getUserEvaluate()
						+ "' where userId = '"
						+ MainActivity.my_info.getUserId() + "' ");
				// 친구들 계산된 priority디비에 다시 업데이트 시켜주는 것

				for (FrdInfo friend : MainActivity.my_info.getEvaluatedFriend()) {
					result2 = result2
							&& db.insert("UPDATE users_info SET priority = '"
									+ friend.getPriority()
									+ "' where userId = '" + friend.getId()
									+ "' ");

					if (result2) {
						Log.i("디비업데이트중",
								friend.getName()
										+ Double.toString(friend.getPriority()));
					}
				}
				// 친구들 weight디비에 안써지는 것 해결하기..
				// 태그등록도 업데이트 안되는것 확인하기
				result = result && result2;
				MainActivity.my_info.initEvaluatedFriend();
			}

			else if (whatInsert.equals("tag")) {
				result = db.insert("UPDATE business_info SET tag = '"
						+ add_newTag + "' where tag = '" + onlyTags + "' ");
				// update business set tag = "바뀔내용" where tag ="바꾸고싶은 부분"

				/*
				 * boolean result =
				 * db.insert("insert into business(univ, type, name, tag) values('"
				 * + univText + "', '" + typeText + "', '" + nameText + "','" +
				 * tagText + "')");
				 */
			} else if (whatInsert.equals("id")) {

				if (!MainActivity.my_info.getUserRec().equals("")) {
					recommend = MainActivity.my_info.getUserRec() + ", "
							+ recommend;
				}
				MainActivity.my_info.setUserRec(recommend);
				result = db.insert("UPDATE users_info SET recommended =  '"
						+ recommend + "' where userId = '"
						+ MainActivity.my_info.getUserId() + "' ");
			} else if (whatInsert.equals("review")) {
				result = db
						.insert("insert into reviewlist(reviewBusiId, writerId, writerName, reviewDate, reviewText) values('"
								+ sel_id
								+ "', '"
								+ MainActivity.my_info.getUserId()
								+ "', '"
								+ MainActivity.my_info.getUserName()
								+ "', '"
								+ todayDate
								+ "','"
								+ typeReview.getText().toString() + "')");
			} else if (whatInsert.equals("countRec")) {
				result = db.insert("UPDATE business_info SET countrec =  '"
						+ countR + "' where businessId = '" + sel_id + "' ");
			} else if (whatInsert.equals("countEval")) {
				result = db.insert("UPDATE business_info SET counteval =  '"
						+ countE + "' where businessId = '" + sel_id + "' ");
			} else if (whatInsert.equals("plusScore")) {
				result = db.insert("UPDATE business_info SET totalsco =  '"
						+ totalS + "' where businessId = '" + sel_id + "' ");
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
}