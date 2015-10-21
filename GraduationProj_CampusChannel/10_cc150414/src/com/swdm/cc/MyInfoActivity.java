package com.swdm.cc;

import java.util.ArrayList;
import org.androidconnect.listview.horizontal.adapter.HorizontalListView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.kakao.AppActionBuilder;
import com.kakao.AppActionInfoBuilder;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.kakao.exception.KakaoException;

@SuppressLint("NewApi") public class MyInfoActivity extends Activity {

	String userId = ""; // MyInfo의 id를 가져옴
	public static String tempId = "";
	String userGender = ""; // MyInfo의 gender를 가져옴
	String imgURL = ""; // 자신의 페이스북 사진 주소를 저장
	double userPriority = 0; // MyInfo의 rank값을 가져옴
	String flID = MainActivity.fl; // 어플을 사용하는 내친구들 아이디
	String[] flIDs; // 한명씩 자르기
	String flName = MainActivity.friendsList; // 어플을 사용하는 내친구들 이름
	public static String[] flNames; // 한명씩 자르기
	Integer whoClicked = 0;
	ImageView myImage;
	TextView myName;
	TextView myGender;
	TextView myRank;
	TextView myUniv;
	Button editMyUniv;
	Button evaluated;
	Button kakaoInvite;
	Button recommended, frListTitle;
	AutoCompleteTextView typedUniv;
	public static ListView busiListView;
	public static ImageView myRecTitle;
	public static ArrayList<FillMyActivityList> myListRec = new ArrayList<FillMyActivityList>();
	public static ArrayList<FillMyActivityList> myListEval = new ArrayList<FillMyActivityList>();
	public static ArrayList<FillMyActivityList> myListFrEval = new ArrayList<FillMyActivityList>();
	public static ArrayList<FillMyActivityList> myListFrRec = new ArrayList<FillMyActivityList>();
	public static ArrayList<FrdInfo> frList = new ArrayList<FrdInfo>();
	public static String thisFrId = "";
	String thisFrName = "";
	String thisFrUniv = "";
	Integer thisFrRecNum = 0;
	Integer thisFrEvalNum = 0;
	String thisFrRank = "";
	public static String[] thisFrRec;
	public static String[] thisFrEval;
	public static AlertDialog alertDialog;
	public static Context context;

	public static int type1 = 0; // 음식점 개수
	public static int type2 = 0; // 카페 개수
	public static int type3 = 0; // 호프집 개수
	public static int type4 = 0; // 노래방 개수
	public static int type5 = 0; // 당구장 개수
	public static int type6 = 0; // 피시방 개수
	
	public static int numberOfSameUniv = 0;	// 나와 같은 대학교로 등록된 학생수
	public static Boolean isClickInMyInfo = false;

	int numOfRec = 1;
	int numOfEval = 1;

	Integer sizeX = 0; // 핸드폰의 가로 사이즈
	Integer sizeY = 0; // 핸드폰의 세로 사이즈
	String editedUniv = "";
	final int DIALOG_FRIEND = 1; // 친구 리스트 중 한명을 클릭 했을 때 다이얼로그 아이디
	final int DIALOG_EDIT = 2; // 대학교 수정 버튼을 클릭 했을 때 다이얼로그 아이디
	final int DIALOG_MY_EVAL = 3; // 나의 평가 목록 다이얼로그 아이디
	final int DIALOG_MY_REC = 4; // 나의 추천 목록 다이얼로그 아이디
	final int DIALOG_FR_EVAL = 5; // 내친구의 평가 목록 다이얼로그 아이디
	final int DIALOG_FR_REC = 6; // 내친구의 추천 목록 다이얼로그 아이디

	// 카카오톡 친구 초대를 위한 변수들
	KakaoLink kakaoLink;
	KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
	String linkContents = "";
	String kakaoMainText = "";
	String kakaoButtonText = "";
	String kakaoMainImage = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_activity);

		myImage = (ImageView) findViewById(R.id.myImage);
		myName = (TextView) findViewById(R.id.myName);
		myGender = (TextView) findViewById(R.id.myGender);
		myRank = (TextView) findViewById(R.id.myRank);
		myUniv = (TextView) findViewById(R.id.myUniv);

		editMyUniv = (Button) findViewById(R.id.editMyUniv);
		recommended = (Button) findViewById(R.id.recommended);
		evaluated = (Button) findViewById(R.id.evaluated);
		kakaoInvite = (Button) findViewById(R.id.kakaoInvite);
		frListTitle = (Button) findViewById(R.id.frListTitle);

		// 카카오톡 api 가져오기
		try {
			kakaoLink = KakaoLink.getKakaoLink(this);
		} catch (KakaoParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kakaoTalkLinkMessageBuilder = kakaoLink
				.createKakaoTalkLinkMessageBuilder();

		// 액티비티 사이즈 가져오기
		sizeX = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getWidth();
		sizeY = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getHeight();
		// 나의 페이스북 아이디 가져오기
		userId = MainActivity.my_info.getUserId();
		// 내 페이스북 사진 주소
		imgURL = "https://graph.facebook.com/" + userId + "/picture";
		// 어플을 사용하는 내친구 아이디 한명씩 자르기
		flIDs = flID.split(", ");
		flIDs[0] = flIDs[0].substring(1, flIDs[0].length());
		flIDs[flIDs.length - 1] = flIDs[flIDs.length - 1].substring(0,
				flIDs[flIDs.length - 1].length() - 1);
		// 어플을 사용하는 내친구 이름 한명씩 자르기
		flNames = flName.split(", ");
		flNames[0] = flNames[0].substring(1, flNames[0].length());
		flNames[flNames.length - 1] = flNames[flNames.length - 1].substring(0,
				flNames[flNames.length - 1].length() - 1);

		// ArrayList<FrdInfo> frList = new ArrayList<FrdInfo>();
		frList = MainActivity.my_info.getFriendsList();
		frListTitle.setText("친구목록(" + frList.size() + "명)");

		// 친구리스트 뿌려줄 수평 리스트뷰 만들기
		HorizontalListView listview = (HorizontalListView) findViewById(R.id.friendListView);
		listview.setAdapter(new HAdapter());

		// 페이스북 프로필 이미지 가져오기 위해 AQuery 라이브러리 사용
		AQuery aq = new AQuery(myImage);

		// 이미지뷰에 이미지 집어 넣기
		aq.id(myImage).image(imgURL);

		// 내 이름 채워주기
		myName.setText(" " + MainActivity.my_info.getUserName());
		// 내 학교 채워주기
		if (MainActivity.my_info.getUserUniv().equals("")) {
			myUniv.setText("-");
		} else {
			myUniv.setText(MainActivity.my_info.getUserUniv());

			// 각 업소 종류의 개수 초기화
			type1 = type2 = type3 = type4 = type5 = type6 = 0;
			// 학교의 업소 종류별 개수 구하기
			DBAsyncTask dbaU = new DBAsyncTask();
			dbaU.setDBmanager_type("from_MyInfoActivity_findUnivBusiness");
			dbaU.execute(0);
			// 내학교로 등록된 학생수 구하기
			numberOfSameUniv = 0;
			DBAsyncTask dbaS = new DBAsyncTask();
			dbaS.setDBmanager_type("from_MyInfoActivity_findUnivStudent");
			dbaS.execute(0);
		}
		// 내 성별 채워주기
		if (MainActivity.my_info.getUserGender().equalsIgnoreCase("male"))
			userGender = "Male";
		else if (MainActivity.my_info.getUserGender()
				.equalsIgnoreCase("female"))
			userGender = "Female";
		else
			userGender = "-";
		myGender.setText(" " + userGender);

		// 내 랭크값 채워주기
		myRank.setText(" 나의 점수 : " + MainActivity.my_info.getUserPriority());

		// 내가 평가한 업소 개수 구하기
		if (MainActivity.my_info.getUserEvaluate().equals("")) {
			evaluated.setText(" 내가 평가한 업소 : 없음");
		} else {
			for (int k = 0; k < MainActivity.my_info.getUserEvaluate().length(); k++) {
				if (MainActivity.my_info.getUserEvaluate().charAt(k) != ',') {
					continue;
				}
				numOfEval++;
			}
			evaluated.setText(" 내가 평가한 업소 : " + numOfEval + "개");
		}

		// 내가 추천한 업소 개수 구하기
		if (MainActivity.my_info.getUserRec().equals("")) {
			recommended.setText(" 내가 추천한 업소 : 없음");
		} else {
			for (int k = 0; k < MainActivity.my_info.getUserRec().length(); k++) {
				if (MainActivity.my_info.getUserRec().charAt(k) != ',') {
					continue;
				}
				numOfRec++;
			}
			recommended.setText(" 내가 추천한 업소 : " + numOfRec + "개");
		}

		// '대학교 수정'를 클릭 하였을 때
		editMyUniv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DIALOG_EDIT);
			}
		});

		// '카카오톡 친구 초대'를 클릭 하였을 때
		kakaoInvite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				kakaoMainText = "[campus channel]\n\n내 친구들이 평가한 학교 주변의 맛집, 카페, 호프집, 노래방 등을 확인할 수 있는 '캠퍼스 채널'로 당신을 초대합니다.";
				kakaoButtonText = "앱으로 이동";
				kakaoMainImage = "http://sangmin9655.dothome.co.kr/kakaolink.png";

				// 이미지를 포함한 카톡 초대
				/*
				 * try { kakaoTalkLinkMessageBuilder.addText(kakaoMainText)
				 * .addImage(kakaoMainImage, 130, 170)
				 * .addAppButton(kakaoButtonText); } catch
				 * (KakaoParameterException e1) { e1.printStackTrace(); }
				 */

				// 이미지를 제거한 카톡 초대
				try {
					kakaoTalkLinkMessageBuilder.addText(kakaoMainText)
							.addAppButton(kakaoButtonText);
				} catch (KakaoParameterException e1) {
					e1.printStackTrace();
				}

				sendToFriends();
			}
		});

		// '추천한 업소 개수'를 클릭 하였을 때
		recommended.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDialog(DIALOG_MY_REC);
			}
		});

		// '평가한 업소 개수'를 클릭 하였을 때
		evaluated.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDialog(DIALOG_MY_EVAL);
			}
		});

		// '내 점수'를 클릭 했을 때
		myRank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "마이인포내점수버튼",
						Toast.LENGTH_SHORT).show();

				double maxPrio = MainActivity.my_info.getMaxPriorityOfAllUser();
				maxPrio = ((int) (maxPrio * 100)) / 100.0;

				double myPrio = MainActivity.my_info.getUserPriority();
				myPrio = ((int) (myPrio * 100)) / 100.0;

				double percentage = (myPrio / maxPrio) * 100;
				percentage = ((int) (percentage * 100)) / 100.0;

				new AlertDialog.Builder(MyInfoActivity.this)
						.setTitle("내Weight는?")
						.setMessage(
								"최고 Weight : " + maxPrio + "\n" + "내 Weight : "
										+ myPrio + "\n" + "백분율 : " + percentage
										+ "점")
						.setNeutralButton("닫기",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		});

		// '내 학교'를 클릭 했을 때
		myUniv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!MainActivity.my_info.getUserUniv().equals("")) {
					String t1 = "";
					String t2 = "";
					String t3 = "";
					String t4 = "";
					String t5 = "";
					String t6 = "";

					if (type1 == 0) {
						t1 = "없음";
					} else {
						t1 = type1 + " 개";
					}
					if (type2 == 0) {
						t2 = "없음";
					} else {
						t2 = type2 + " 개";
					}
					if (type3 == 0) {
						t3 = "없음";
					} else {
						t3 = type3 + " 개";
					}
					if (type4 == 0) {
						t4 = "없음";
					} else {
						t4 = type4 + " 개";
					}
					if (type5 == 0) {
						t5 = "없음";
					} else {
						t5 = type5 + " 개";
					}
					if (type6 == 0) {
						t6 = "없음";
					} else {
						t6 = type6 + " 개";
					}
					
					new AlertDialog.Builder(MyInfoActivity.this)
							.setTitle(
									"'" + MainActivity.my_info.getUserUniv()
											+ "'의 등록현황")
							.setMessage(
									"음식점 : " + t1 + "\n" + "카페  : " + t2 + "\n"
											+ "호프집 : " + t3 + "\n" + "노래방 : "
											+ t4 + "\n" + "당구장 : " + t5 + "\n"
											+ "피시방 : " + t6 + "\n\n학생수 : " + numberOfSameUniv + " 명")
							.setPositiveButton("닫기",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									})
							.setNegativeButton("검색하기",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
											isClickInMyInfo = true;
											Intent intent = new Intent(MyInfoActivity.this, SearchData.class);
											startActivity(intent);
										}
									}).show();
				}
			}
		});

		/*
		 * Log.i("imagename", userId);
		 * 
		 * URL url = null; try { url = new
		 * URL("https://graph.facebook.com/774980629225904/picture"); } catch
		 * (MalformedURLException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } URLConnection conn = null; try { conn =
		 * url.openConnection(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } try {
		 * conn.connect(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } BufferedInputStream bis = null;
		 * try { bis = new BufferedInputStream(conn.getInputStream()); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } Bitmap bm = BitmapFactory.decodeStream(bis);
		 * try { bis.close(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } myImage.setImageBitmap(bm);
		 */
	}

	// 설정된 카카오톡 링크 정보를 친구에게 보낸다
	protected void sendToFriends() {

		try {
			linkContents = kakaoTalkLinkMessageBuilder.build();
		} catch (KakaoParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			kakaoLink.sendMessage(linkContents, this);
		} catch (KakaoParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 카카오톡 에러가 있을 때 다이얼로그를 띄운다.
	private void alert(String message) {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.app_name).setMessage(message)
				.setPositiveButton(android.R.string.ok, null).create().show();
	}

	/*
	 * public void alertShow(View v) { MyDialogFragment frag =
	 * MyDialogFragment.newInstance(); frag.show(getFragmentManager(), "TAG"); }
	 * 
	 * public static class MyDialogFragment extends DialogFragment {
	 * 
	 * public static MyDialogFragment newInstance() { return new
	 * MyDialogFragment(); }
	 * 
	 * public Dialog onCreateDialog(Bundle savedInstanceState) {
	 * 
	 * LayoutInflater inf =
	 * (LayoutInflater)getActivity().getSystemService(Context
	 * .LAYOUT_INFLATER_SERVICE); View convertView =
	 * inf.inflate(R.layout.myinfo_friend_activity, null); AlertDialog.Builder
	 * builder = new AlertDialog.Builder(getActivity());
	 * builder.setTitle("친구의 정보"); builder.setView(convertView);
	 * 
	 * builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * 
	 * Log.i("sangmin", "확인 선택"); } }); return builder.create(); } }
	 */

	// private static String[] dataObjects = flNames;

	private class HAdapter extends BaseAdapter {

		public HAdapter() {
			super();
		}

		private OnClickListener mOnButtonClicked = new OnClickListener() {

			public void onClick(View v) {
				Button bt = (Button) v;
				thisFrId = bt.getText().toString();
				thisFrId = thisFrId.substring(6, thisFrId.length());
				tempId = thisFrId;

				thisFrName = "";
				thisFrUniv = "";
				thisFrRecNum = 0;
				thisFrEvalNum = 0;
				thisFrRank = "";
				double tempFrRank = 0;
				double tempFrRank2 = 0;
				double tempForDoubleType = 0;

				for (int k = 0; k < frList.size(); k++) {
					if (frList.get(k).getId().equals(tempId)) {
						thisFrName = frList.get(k).getName();
						thisFrUniv = frList.get(k).getUniv();
						thisFrRecNum = frList.get(k).getTheNumOfRec();
						thisFrEvalNum = frList.get(k).getTheNumOfEval();

						tempFrRank = frList.get(k).getPriority();
						tempFrRank = ((int) (tempFrRank * 100)) / 100.0;
						thisFrRank = Double.toString(tempFrRank);

						tempForDoubleType = frList.get(k)
								.getMaxPriorityOfAllUser();
						tempForDoubleType = ((int) (tempForDoubleType * 100)) / 100.0;
						tempFrRank2 = (tempFrRank / tempForDoubleType) * 100;
						tempFrRank2 = ((int) (tempFrRank2 * 100)) / 100.0;
						thisFrRank += "(" + Double.toString(tempFrRank2) + "점)";

						thisFrRec = frList.get(k).getRecArr();
						thisFrEval = frList.get(k).getEvalArr();					

						break;
					}
				}
				showDialog(DIALOG_FRIEND);
				// makeFrDialog(thisFrId);

				/*
				 * AlertDialog.Builder builder = new AlertDialog.Builder(this);
				 * 
				 * // Get the layout inflater LayoutInflater inflater =
				 * this.getLayoutInflater(); // Inflate and set the layout for
				 * the dialog // Pass null as the parent view because its going
				 * in the dialog layout
				 * builder.setView(inflater.inflate(R.layout.dialog_category,
				 * null)) .create().show();
				 */
				// Toast.makeText(getApplicationContext(), "친구아이디 : " + thisFrId
				// + "\n친구 이름 : " + frName + ", 추천수 : " + frRecNum.toString() +
				// ", 평가수 : " + frEvalNum.toString(),
				// Toast.LENGTH_SHORT).show();
			}
		};

		public int getCount() {
			return flNames.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View retval = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.friendslistview, null);
			TextView title = (TextView) retval.findViewById(R.id.title);
			ImageView imgF = (ImageView) retval.findViewById(R.id.imgF);
			// 친구의 이름 한명씩 채워주기
			title.setText(" " + flNames[position]);
			// 페이스북 프로필 이미지 가져오기 위해 AQuery 라이브러리 사용
			AQuery aq1 = new AQuery(imgF);
			// 이미지를 둥글게 하기 위해 객체 생성
			ImageOptions op1 = new ImageOptions();
			// 이미지가 둥근 정도 설정
			op1.round = 25;
			// 이미지뷰에 이미지 집어 넣기
			aq1.id(imgF).image(
					"https://graph.facebook.com/" + flIDs[position]
							+ "/picture", op1);

			Button button = (Button) retval.findViewById(R.id.clickbutton);
			button.setText("click\n" + flIDs[position]);
			whoClicked = position;
			button.setAlpha((float) 0.0);
			button.setOnClickListener(mOnButtonClicked);

			return retval;
		}
	};

	/*
	 * private void makeFrDialog(String frID) { tempId = frID;
	 * 
	 * thisFrName = ""; thisFrUniv = ""; thisFrRecNum = 0; thisFrEvalNum = 0;
	 * thisFrRank = 0;
	 * 
	 * ArrayList<FrdInfo> frList = new ArrayList<FrdInfo>(); frList =
	 * MainActivity.my_info.getFriendsList();
	 * 
	 * for (int k = 0; k < frList.size(); k++) { if
	 * (frList.get(k).getId().equals(tempId)) { thisFrName =
	 * frList.get(k).getName(); thisFrUniv = frList.get(k).getUniv();
	 * thisFrRecNum = frList.get(k).getTheNumOfRec(); thisFrEvalNum =
	 * frList.get(k).getTheNumOfEval(); thisFrRank =
	 * frList.get(k).getPriority(); break; } } AlertDialog.Builder builder = new
	 * AlertDialog.Builder(this);
	 * 
	 * builder.setTitle("\'" + thisFrName + "\'님의 활동 정보 "); // Get the layout
	 * inflater LayoutInflater inflater = this.getLayoutInflater(); // Inflate
	 * and set the layout for the dialog // Pass null as the parent view because
	 * its going in the dialog layout
	 * builder.setView(inflater.inflate(R.layout.myinfo_friend_activity, null))
	 * .create().show(); }
	 */

	// "내 친구의 활동정보가 뜬다"
	@Override
	protected Dialog onCreateDialog(int id) {

		AlertDialog dialogDetails = null;
		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogview = null;
		AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
		switch (id) {
		case DIALOG_FRIEND:
			dialogview = inflater
					.inflate(R.layout.myinfo_friend_activity, null);
			dialogbuilder.setPositiveButton("확인",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			break;
		case DIALOG_EDIT:
			dialogview = inflater.inflate(R.layout.edit_my_univ, null);
			dialogbuilder.setPositiveButton("확인",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							editedUniv = typedUniv.getText().toString();
							myUniv.setText(editedUniv);
							// 수정된 나의 대학교를 업데이트 시킨다
							UPAsynkTask upa = new UPAsynkTask();
							upa.execute(0);
							MainActivity.my_info.setUserUniv(editedUniv);
							
							// 각 업소 종류의 개수 초기화
							type1 = type2 = type3 = type4 = type5 = type6 = 0;

							DBAsyncTask dbaU = new DBAsyncTask();
							dbaU.setDBmanager_type("from_MyInfoActivity_findUnivBusiness");
							dbaU.execute(0);

							// 내학교로 등록된 학생수 구하기
							numberOfSameUniv = 0;
							DBAsyncTask dbaS = new DBAsyncTask();
							dbaS.setDBmanager_type("from_MyInfoActivity_findUnivStudent");
							dbaS.execute(0);
							
							Toast.makeText(getApplicationContext(),
									"나의 학교가 수정되었습니다", Toast.LENGTH_SHORT)
									.show();
						}
					});
			dialogbuilder.setNegativeButton("취소",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			break;
		case DIALOG_MY_REC:
			dialogview = inflater.inflate(R.layout.myinfo_my_rec_eval, null);
			dialogbuilder.setPositiveButton("확인",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			break;
		case DIALOG_MY_EVAL:
			dialogview = inflater.inflate(R.layout.myinfo_my_rec_eval, null);
			dialogbuilder.setPositiveButton("확인",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			break;
		case DIALOG_FR_REC:
			dialogview = inflater.inflate(R.layout.myinfo_my_rec_eval, null);
			dialogbuilder.setPositiveButton("확인",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
						}
					});
			break;
		case DIALOG_FR_EVAL:
			dialogview = inflater.inflate(R.layout.myinfo_my_rec_eval, null);
			dialogbuilder.setPositiveButton("확인",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			break;
		}
		// dialogbuilder.setTitle("\'" + thisFrName + "\'님의 활동 정보 ");

		dialogbuilder.setView(dialogview);
		dialogDetails = dialogbuilder.create();

		return dialogDetails;

	}

	// "내 친구의 활동정보가 뜬다"
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		alertDialog = (AlertDialog) dialog;

		switch (id) {
		case DIALOG_FRIEND:
			LayoutParams params = alertDialog.getWindow().getAttributes();
			// params.height = (int)(sizeY * 0.9);
			// Log.i("액티비티 가로사이즈", sizeX.toString());
			params.width = (int) (sizeX * 0.7);
			// alertDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams)
			// params);
			// lp.width = (int)(context.getWindow( ).getWindowManager(
			// ).getDefaultDisplay( ).getWidth( ) * 0.9);

			// alertDialog.setTitle("\'" + thisFrName + "\'님의 활동 정보 ");
			// tempId = frID;
			ImageView frImage = (ImageView) alertDialog
					.findViewById(R.id.frImage);
			TextView frName = (TextView) alertDialog.findViewById(R.id.frName);
			TextView frUniv = (TextView) alertDialog.findViewById(R.id.frUniv);
			TextView frRank = (TextView) alertDialog.findViewById(R.id.frRank);
			Button frEval = (Button) alertDialog.findViewById(R.id.frEval);
			Button frRec = (Button) alertDialog.findViewById(R.id.frRec);

			// 친구의 프로필 사진 띄어주기
			// 페이스북 프로필 이미지 가져오기 위해 AQuery 라이브러리 사용
			AQuery aq3 = new AQuery(frImage);
			// 이미지를 둥글게 하기 위해 객체 생성
			ImageOptions op3 = new ImageOptions();
			// 이미지가 둥근 정도 설정
			op3.round = 25;
			// 이미지뷰에 이미지 집어 넣기
			aq3.id(frImage).image(
					"https://graph.facebook.com/" + tempId + "/picture", op3);

			if (thisFrUniv.equals(""))
				thisFrUniv = "-";
			frName.setText("이름 : " + thisFrName);
			frUniv.setText("학교 : " + thisFrUniv);
			frRank.setText("점수 : " + thisFrRank);
			frEval.setText(" 평가한 업소 : " + thisFrEvalNum + "개");
			frRec.setText(" 추천한 업소 : " + thisFrRecNum + "개");

			// '친구의 평가 목록'을 클릭 하였을 때
			frEval.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// alertDialog.dismiss();
					showDialog(DIALOG_FR_EVAL);

				}
			});
			// '친구의 추천 목록'을 클릭 하였을 때
			frRec.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// alertDialog.dismiss();
					showDialog(DIALOG_FR_REC);
				}
			});
			break;

		case DIALOG_EDIT:
			LayoutParams params2 = alertDialog.getWindow().getAttributes();
			// params.height = (int)(sizeY * 0.9);
			// Log.i("액티비티 가로사이즈", sizeX.toString());
			params2.width = (int) (sizeX * 0.6);
			typedUniv = (AutoCompleteTextView) alertDialog
					.findViewById(R.id.typedUniv);
			// ArrayAdapter adap = new ArrayAdapter(this,
			// android.R.layout.simple_dropdown_item_1line, R.array.univ);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(getApplicationContext(), R.array.univ,
							android.R.layout.simple_dropdown_item_1line);
			typedUniv.setAdapter(adapter);
			break;

		case DIALOG_MY_REC:
			// LayoutParams params3 = alertDialog.getWindow().getAttributes();
			// params.height = (int)(sizeY * 0.9);
			// params3.width = (int) (sizeX * 0.8);

			context = getApplicationContext();

			// 내 추천 목록 구하기
			DBAsyncTask dba = new DBAsyncTask();
			dba.setDBmanager_type("from_MyInfoActivity_fillList_rec");
			dba.execute(0);

			break;
		case DIALOG_MY_EVAL:
			// LayoutParams params3 = alertDialog.getWindow().getAttributes();
			// params.height = (int)(sizeY * 0.9);
			// params3.width = (int) (sizeX * 0.8);

			context = getApplicationContext();

			// 내 추천 목록 구하기
			DBAsyncTask dba2 = new DBAsyncTask();
			dba2.setDBmanager_type("from_MyInfoActivity_fillList_eval");
			dba2.execute(0);

			break;
		case DIALOG_FR_REC:
			// LayoutParams params3 = alertDialog.getWindow().getAttributes();
			// params.height = (int)(sizeY * 0.9);
			// params3.width = (int) (sizeX * 0.8);

			context = getApplicationContext();

			// 내 추천 목록 구하기
			DBAsyncTask dba3 = new DBAsyncTask();
			dba3.setDBmanager_type("from_MyInfoActivity_fillList_fr_rec");
			dba3.execute(0);

			break;
		case DIALOG_FR_EVAL:
			// LayoutParams params3 = alertDialog.getWindow().getAttributes();
			// params.height = (int)(sizeY * 0.9);
			// params3.width = (int) (sizeX * 0.8);

			context = getApplicationContext();

			// 내 추천 목록 구하기
			DBAsyncTask dba4 = new DBAsyncTask();
			dba4.setDBmanager_type("from_MyInfoActivity_fillList_fr_eval");
			dba4.execute(0);

			break;
		}
	}

	// 수정된 나의 대학교를 업데이트 시킨다
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
			DBmanager db = new DBmanager();
			boolean result = db.insert("UPDATE users_info SET myUniv =  '"
					+ editedUniv + "' where userId = '"
					+ MainActivity.my_info.getUserId() + "' ");
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