package com.swdm.cc;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.model.GraphObject;

public class MainActivity extends Activity implements OnClickListener {
	public static MyInfo my_info = new MyInfo();
	// Weather[] weather_data;
	public static Weather weather_data[] = null;
	public static ArrayList<String> calculatedData = new ArrayList<String>();
	// public static ArrayList<Weather> weather_data = new ArrayList<Weather>();
	ImageButton btn_friends, btn_all, btn_insert, btn_search, btn_myInfo;
	public static int btn_selected = 0;
	Dialog dialog;
	String searchTxt;

	public static String userID_in_DB = "";
	public static String userRec_in_DB = "";
	public static String userEvaluate_in_DB = "";
	public static String frId = "";
	// String whatSearch = "";
	public static String frIdDb = "";
	public static String recDb = "";
	public static String findRec = "";
	public static String findRecs[];
	String id;
	String name;
	String gender;
	String age;

	public static int k;
	public static Integer idR;
	public static String univR;
	public static String typeR;
	public static String nameR;
	public static String tagR;
	public static String imgR;

	public static String findUniv;
	public static String findType;
	public static String findName;
	public static String findTag;
	public static String findImg;

	public static ListView listView;
	public static ArrayList<Store> stores = new ArrayList<Store>(); // store를
	// 저장할
	// arrayList
	public static ArrayList<String> storeData = new ArrayList<String>(); // store
	// 정보를
	// string
	// 타입으로
	// 저장

	public static ArrayList<String> users = new ArrayList<String>(); // 디비에서 가져온
	// user를
	// 저장할
	// arrayList
	ArrayAdapter<String> adapter;

	public static String friendsList = "";
	public static String fl = "";

	public final OpenRequest setPermissions = new OpenRequest(this);
	public static FillMainList fillMainList;
	public static Activity aaa;
	public static Typeface jangmeFont;
	public static Typeface charismaFont;


	private ViewFlipper viewFlipper;
	private float lastX;
	private TextView txtView;
	private int viewId=0;	// 제일 처음에는 친구 화면

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		jangmeFont=Typeface.createFromAsset(getAssets(), "fonts/jangmedabang.ttf");
		charismaFont=Typeface.createFromAsset(getAssets(), "fonts/charismaBK.ttf");

		viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

		StrictMode.ThreadPolicy pol = new StrictMode.ThreadPolicy.Builder()
		.permitNetwork().build();
		StrictMode.setThreadPolicy(pol);

		txtView = (TextView)findViewById(R.id.textView);

		btn_friends = (ImageButton) findViewById(R.id.btn_friends);
		btn_all = (ImageButton) findViewById(R.id.btn_like);
		btn_insert = (ImageButton) findViewById(R.id.btn_addBusiness);
		btn_search = (ImageButton) findViewById(R.id.btn_search);
		btn_myInfo = (ImageButton) findViewById(R.id.btn_myInfo);
		// ListView 의 참조값 얻어오기
		listView = (ListView) findViewById(R.id.listView);

		btn_friends.setOnClickListener(this);
		btn_all.setOnClickListener(this);
		btn_insert.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		btn_myInfo.setOnClickListener(this);

		aaa = this;
		// /////////////////////////////////////////////////////////
		// user에 대한 것.
		// 로그인시 받은 정보들 가져오기
		setData(); // 인텐트로 받은 정보를 변수에 저장하는 메소드
		// //////////////////////////////////////////////////////////
		// 디비에 저장된 스토어 정보들 어레이리스트에 넣기
		SetListView setList = new SetListView();
		setList.getInfoFromDB();
		// showList();

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// String item = data.get(position);
				// String item2 = (String) parent.getItemAtPosition(position);
				String item2 = calculatedData.get(position);
				Log.i("HHHHERE", item2);
				/*
				 * Toast.makeText( getApplicationContext(), item + " " + item2 +
				 * " " + position + " " + id, Toast.LENGTH_SHORT) .show();
				 */

				// Toast.makeText(getApplicationContext(),
				// item2 + " 을 선택하셨습니다.", Toast.LENGTH_SHORT)
				// .show();

				Intent intent = new Intent(MainActivity.this,
						ClickBusiness.class);
				intent.putExtra("selected", item2);
				startActivity(intent);
			}
		});		
	}

	private void setData() {
		Intent intent = getIntent();

		id = intent.getExtras().get("user_id").toString();
		name = intent.getExtras().get("user_name").toString();
		gender = intent.getExtras().get("user_gender").toString();
		// age = intent.getExtras().get("age").toString();

		// 디비에 이미 있는 정보인지 검색
		// SearchUserInDB search_user = new SearchUserInDB(id);
		// Log.i("DBuser", users.toString());

		// 디비에 같은 아이디가 없다면 처음 접속한 것. 디비에서 userId를 primary key로 이미 있는 정보인지 알아서
		// 구분해줌.
		// User class 생성

		/* 이제 드디어 내정보 저장을 위한 클래스를 생성하고 내용들을 각 스텝에 맞추어 채워준다 */
		// my_info = new MyInfo(id, name, gender);
		// my_info = new MyInfo();
		my_info.setUserId(id);
		my_info.setUserName(name);
		my_info.setUserGender(gender);
		// my_info = new MyInfo();
		// my_info.setUserId(id);

		// 내가 추천한 업소의 index를 세팅!
		// whatSearch = "myRecommend";

		Log.i("비회원용", id + name + gender);
		DBAsyncTask dba = new DBAsyncTask();
		dba.setDBmanager_type("from_MainActivity_myRecommend_and_myEvaluate");
		dba.execute(0);

		// 내 친구의 리스트들을 받아온다!
		my_info.initFriendsList();
		Log.i("AAA","AAA");
		getFriends();

		// dba.setDBmanager_type("from_MainActivity_myFriends");
		// dba.execute(0);

		for (FrdInfo frd : my_info.getFriendsList()) {
			Log.i("CHECK FRIENDDDDDDD", frd.getId() + frd.getName());
		}
	}

	private void getFriends() {
		Session activeSession = Session.getActiveSession();
		if (activeSession.getState().isOpened()) {

			Log.i("session",
					"session is open " + activeSession.getPermissions());

			// ////////////////////////////////////////////////////////////////////////////////////
			// 친구목록 가져오]는 부분
			// taggable_friends는 태그가능한 친구들 목록임.
			/* make the API call */
			new Request(activeSession, "/me/friends", null, HttpMethod.GET,
					new Request.Callback() {
				public void onCompleted(Response response) {
					/* handle the result */
					// Log.i("INFO4", response.toString());

					GraphObject graphObject = response.getGraphObject();

					if (graphObject == null) {
						return;
					}

					JSONObject jsonObject = graphObject
							.getInnerJSONObject();
					JSONArray array;
					ArrayList<String> name_list = new ArrayList<String>();
					ArrayList<String> id_list = new ArrayList<String>();

					try {
						array = jsonObject.getJSONArray("data");

						JSONObject o;
						Log.i("CHECK FRIENDDDDDDD",
								Integer.toString(array.length()));

						for (int i = 0; i < array.length(); i++) {

							o = array.getJSONObject(i);

							// Log.i("INFO friend",
							// o.getString("name")+o.getString("id"));
							String temp_friend_id = o.getString("id");
							String temp_friend_name = o
									.getString("name");

							id_list.add(temp_friend_id);
							name_list.add(temp_friend_name);
							// name_list.add(o.getString("name"));
							// id_list.add(o.getString("id"));
							// Log.i("CHECK FRIENDDDDDDD",
							// temp_friend_id);
							Log.i("CHECK ID", temp_friend_id + "/"
									+ temp_friend_name);

							// String friendID = o.getString("id");
							// 모든 유저들과 페친들의 아이디를 비교해서 같은 아이디만 친구목록에 저장
							// if
							// (users.toString().contains(temp_friend_id))
							// {
							FrdInfo inputFriend = new FrdInfo(
									temp_friend_id, temp_friend_name);
							my_info.friendsList.add(inputFriend);
							my_info.friendsIdList.add(temp_friend_id);

						}
						DBAsyncTask dba = new DBAsyncTask();
						dba.setDBmanager_type("from_MainActivity_myFriends");
						dba.execute(0);
						Log.i("FriendsSize!! ", Integer
								.toString(my_info.friendsList.size()));
						Log.i("내 프라이어리티", Double.toString(my_info
								.getUserPriority()));

						friendsList = name_list.toString();
						fl = my_info.friendsIdList.toString();

						// 디비에 새로운 유저 정보 올림
						UPAsynkTask upa = new UPAsynkTask();
						upa.execute(0);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).executeAsync();

		}
		else{
			DBAsyncTask dba = new DBAsyncTask();
			dba.setDBmanager_type("from_MainActivity_myFriends_for_visitor");
			dba.execute(0);
		}
	}

	// 디비에 유저 정보 올리는 클래스
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
			boolean result = db
					.insert("insert into users_info(userId, name, sex, friends, fl) values('"
							+ id
							+ "', '"
							+ name
							+ "', '"
							+ gender
							+ "', '"
							+ friendsList + "', '" + fl + "')");

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == btn_friends.getId()) {
			//			// 친구들이 가져오기 한 것만 보여준다
			//			Toast.makeText(getApplicationContext(), "친구", Toast.LENGTH_SHORT)
			//					.show();
			viewId = 0;

			setButtonImage(viewId);
			handlingView(viewId);
			//
			//			/* 0130추가부분 */
			//			fillMainList = new FillMainList("btn_friends", this);

			//			Log.i("쇼리스트부른당", "sfsfsdf ");
			//			showList();
			//			// 이건 fillList클래스로 넘긴다
			//			Log.d("saaa", "5 클릭");
		}

		if (v.getId() == btn_all.getId()) {
			// 모든 사람들이 가져오기 한 것 다 보여준다
			//			Toast.makeText(getApplicationContext(), "전체", Toast.LENGTH_SHORT)
			//					.show();
			viewId = 1;

			setButtonImage(viewId);
			handlingView(viewId);

			//showList();
		}

		if (v.getId() == btn_insert.getId()) {
			// 업소등록하기
			//			Toast.makeText(getApplicationContext(), "등록", Toast.LENGTH_SHORT)
			//					.show();
			//			Intent intent = new Intent(MainActivity.this, Insert.class);
			//			startActivity(intent);
			viewId = 2;

			setButtonImage(viewId);
			handlingView(viewId);

			//showList();
		}

		if (v.getId() == btn_myInfo.getId()) {

			//			Toast.makeText(getApplicationContext(), "내정보", Toast.LENGTH_SHORT)
			//					.show();
			//			Intent intent = new Intent(MainActivity.this, MyInfoActivity.class);
			//			startActivity(intent);

			viewId = 3;

			setButtonImage(viewId);
			handlingView(viewId);
		}

		if (v.getId() == btn_search.getId()) {
			// 검색 화면으로 넘어감
			//			Toast.makeText(getApplicationContext(), "검색", Toast.LENGTH_SHORT)
			//					.show();
			//			Intent intent = new Intent(MainActivity.this, SearchData.class);
			//			startActivity(intent);
			// createDialogBox(); // dialog를 띄워서 선택하게 한다

			viewId = 4;

			setButtonImage(viewId);
			handlingView(viewId);
		}
		//
		//		if (btn_selected == 1) {
		//			// 친구 선택했을 때
		//			txtView.setText("친구들이 추천한 업소를 보여줍니다.");
		//			btn_friends.setImageResource(R.drawable.friends_clicked);
		//			btn_all.setImageResource(R.drawable.like_finger);
		//
		//			// showList();
		//		} else {
		//			// 전체 선택했을 때
		//			txtView.setText("모든 사용자들이 추천한 업소를 보여줍니다.");
		//			btn_friends.setImageResource(R.drawable.friends);
		//			btn_all.setImageResource(R.drawable.like_finger_clicked);
		//
		//			showList();
		//		}
	}

	private void showList() {
		// 안드로이드에서 제공하는 기본적인 Adapter 객체 생성하기(카카오톡같은 특별한 리스트뷰는 만들어야 한다)*커스터마이징

		/* 0130. 이제부터 이 임시의 weather_data는 FillMainList로부터 받아와서 사용 할 것임 */
		// Weather weather_data[] = new Weather[] {
		// new Weather(R.drawable.droptop, "카페 드롭탑", "성남시 수정구 성남대로"),
		// new Weather(R.drawable.droptop, "카페 드롭탑", "성남시 수정구 성남대로"),
		// new Weather(R.drawable.droptop, "카페 드롭탑", "성남시 수정구 성남대로"),
		// new Weather(R.drawable.droptop, "카페 드롭탑", "성남시 수정구 성남대로"),
		// new Weather(R.drawable.droptop, "카페 드롭탑", "성남시 수정구 성남대로"),
		// new Weather(R.drawable.droptop, "카페 드롭탑", "성남시 수정구 성남대로"),
		// new Weather(R.drawable.droptop, "카페 드롭탑", "성남시 수정구 성남대로"),
		// new Weather(R.drawable.droptop, "카페 드롭탑", "성남시 수정구 성남대로"), };

		// /*여기서부터는 전부 DBAsyncTask로 보냄*/
		// Log.i("쇼리스트!!!",
		// "asdfsfsafsaf"+Integer.toString(weather_data.length));
		// WeatherAdapter adapter = new WeatherAdapter(this,
		// R.layout.listview_item_row, weather_data);
		//
		// // adapter = new ArrayAdapter<String>(this,
		// // R.layout.listview_item_layout_01, storeData);
		// // simple_list_item_1는 textview를 담고 있는 xml형태이다.
		//
		// // listview 객체에 아답터 객체 연결하기
		// listView.setAdapter(adapter);
		//
		// // 리스트뷰의 경계선을 파란색으로
		// listView.setDivider(new
		// ColorDrawable(Color.parseColor("#FF000080")));
		//
		// // 경계선의 굵기를 5px
		// listView.setDividerHeight(5);

	}


	// Using the following method, we will handle all screen swaps.
	public boolean onTouchEvent(MotionEvent touchevent) {
		switch (touchevent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX = touchevent.getX();
			break;

		case MotionEvent.ACTION_UP:
			float currentX = touchevent.getX();

			viewId = viewFlipper.getDisplayedChild();

			// Handling left to right screen swap.
			//if (lastX < currentX ) 
			if (currentX - lastX > 100){
				// If there aren't any other children, just break.
				if (viewFlipper.getDisplayedChild() == 0) {
					// Next screen comes in from left.
					viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
					// Current screen goes out from right.
					viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);
					viewFlipper.setDisplayedChild(4);
					viewId = 4;
					setButtonImage(viewId);
					break;
				}

				// Next screen comes in from left.
				viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
				// Current screen goes out from right.
				viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);
				// Display next screen.
				viewFlipper.showPrevious();
				viewId--;
			}

			// Handling right to left screen swap.
			//if (lastX > currentX)
			if (lastX - currentX > 100) {
				// If there is a child (to the left), kust break.
				if (viewFlipper.getDisplayedChild() == 4) {
					// Next screen comes in from right.
					viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
					// Current screen goes out from left.
					viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
					viewFlipper.setDisplayedChild(0);
					viewId = 0;
					setButtonImage(viewId);
					break;
				}

				// Next screen comes in from right.
				viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
				// Current screen goes out from left.
				viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
				// Display previous screen.
				viewFlipper.showNext();
				viewId++;
			}
			setButtonImage(viewId);
			break;
		}

		return false;
	}

	void setButtonImage(int viewId) {
		if (viewId == 0) {
			// 친구 선택했을 때
			txtView.setText("친구들이 추천한 업소를 보여줍니다.");
			btn_friends.setImageResource(R.drawable.friends_clicked);
			btn_all.setImageResource(R.drawable.like_finger);
			btn_insert.setImageResource(R.drawable.add_business);
			btn_myInfo.setImageResource(R.drawable.man);
			btn_search.setImageResource(R.drawable.search);

			Toast.makeText(getApplicationContext(), "친구", Toast.LENGTH_SHORT)
			.show();

			/* 0130추가부분 */
			fillMainList = new FillMainList("btn_friends", this);

		} else if (viewId == 1) {
			// 전체 선택했을 때
			txtView.setText("모든 사용자들이 추천한 업소를 보여줍니다.");
			btn_friends.setImageResource(R.drawable.friends);
			btn_all.setImageResource(R.drawable.like_finger_clicked);
			btn_insert.setImageResource(R.drawable.add_business);
			btn_myInfo.setImageResource(R.drawable.man);
			btn_search.setImageResource(R.drawable.search);

			Toast.makeText(getApplicationContext(), "전체", Toast.LENGTH_SHORT)
			.show();

		} else if (viewId == 2) {
			// 업소추가 선택했을 때
			txtView.setText("업소를 추가합니다.");
			btn_friends.setImageResource(R.drawable.friends);
			btn_all.setImageResource(R.drawable.like_finger);
			btn_insert.setImageResource(R.drawable.add_business_clicked);
			btn_myInfo.setImageResource(R.drawable.man);
			btn_search.setImageResource(R.drawable.search);


			Toast.makeText(getApplicationContext(), "등록", Toast.LENGTH_SHORT)
			.show();
			Intent intent = new Intent(MainActivity.this, Insert.class);
			startActivity(intent);

		} else if (viewId == 3) {
			// 내정보 선택했을 때

			//손님방문인경우
			if(my_info.getUserId().equals("000")){
				Toast.makeText(getApplicationContext(), "회원가입 후 이용 하실 수 있습니다.", Toast.LENGTH_SHORT).show();
			}
			
			//회원방문인경우
			else{
				txtView.setText("내 정보 보기");
				btn_friends.setImageResource(R.drawable.friends);
				btn_all.setImageResource(R.drawable.like_finger);
				btn_insert.setImageResource(R.drawable.add_business);
				btn_myInfo.setImageResource(R.drawable.man_clicked);
				btn_search.setImageResource(R.drawable.search);
				

				Toast.makeText(getApplicationContext(), "내정보", Toast.LENGTH_SHORT)
				.show();
				Intent intent = new Intent(MainActivity.this, MyInfoActivity.class);
				startActivity(intent);
			}
			
		} else if (viewId == 4) {
			// 검색 선택했을 때
			txtView.setText("검색하기");
			btn_friends.setImageResource(R.drawable.friends);
			btn_all.setImageResource(R.drawable.like_finger);
			btn_insert.setImageResource(R.drawable.add_business);
			btn_myInfo.setImageResource(R.drawable.man);
			btn_search.setImageResource(R.drawable.search_clicked);

			// 검색 화면으로 넘어감
			Toast.makeText(getApplicationContext(), "검색", Toast.LENGTH_SHORT)
			.show();
			Intent intent = new Intent(MainActivity.this, SearchData.class);
			startActivity(intent);
		}
	}


	// 버튼을 클릭해서 이동할때도 애니매이션 효과
	private void handlingView(int index) {
		viewId = viewFlipper.getDisplayedChild();

		// 전에 있던 화면이랑 비교
		if (viewId == index)
			return;

		if (viewId < index && viewId != 4) {
			// Next screen comes in from right.
			viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
			// Current screen goes out from left.
			viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);
		} else {
			// Next screen comes in from left.
			viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
			// Current screen goes out from right.
			viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);
		}

		viewFlipper.setDisplayedChild(index);
	}


	// 검색버튼을 누르면 띄워지는 다이얼로그 창
	private void createDialogBox() {
		final CharSequence[] items = { "카테고리로", "검색어로" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("검색하기");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				Toast.makeText(getApplicationContext(), items[item],
						Toast.LENGTH_SHORT).show();

				if (item == 0) {
					// 카테고리 선택
					category();
				}

				if (item == 1) {
					// 검색어 선택
					search();
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}// createDialogBox

	// 카테고리 UI띄움
	private void category() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// Get the layout inflater
		LayoutInflater inflater = this.getLayoutInflater();
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(inflater.inflate(R.layout.dialog_category, null))
		.create().show();
	}

	// 카테고리 골랐을때
	public void cateClick(View v) {
		// 선택된 카테고리를 인텐트로 보낸다
		// 검색했을 때의 activity 부름
		String cate = "";

		switch (v.getId()) {
		case R.id.btn_cate_all:
			cate = "all";
			break;
		case R.id.btn_cate_none:
			cate = "none";
			break;
		case R.id.btn_cate_cafe:
			cate = "cafe";
			break;
		case R.id.btn_cate_trip:
			cate = "trip";
			break;
		case R.id.btn_cate_food:
			cate = "food";
			break;
		case R.id.btn_cate_bar:
			cate = "bar";
			break;
		case R.id.btn_cate_game:
			cate = "game";
			break;
		case R.id.btn_cate_movie:
			cate = "movie";
			break;
		}
		Toast.makeText(this, cate, Toast.LENGTH_SHORT).show();
	}

	// 검색으로 UI띄움
	private void search() {
		showDialog(1); // onCreateDialog 부름
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog dialogDetails = null;

		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogview = inflater.inflate(R.layout.dialog_search, null);
		AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);

		dialogbuilder.setTitle("검색어로");
		dialogbuilder.setView(dialogview);
		dialogDetails = dialogbuilder.create();

		return dialogDetails;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		final AlertDialog alertDialog = (AlertDialog) dialog;

		final EditText searchTxt = (EditText) alertDialog
				.findViewById(R.id.txt_search);
		Button OKbutton = (Button) alertDialog.findViewById(R.id.btn_OK);

		// this.searchTxt = searchTxt.getText().toString();

		OKbutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				Toast.makeText(getApplicationContext(),
						searchTxt.getText().toString(), Toast.LENGTH_SHORT)
						.show();
				searchTxt.setText("");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_notice:
			Toast.makeText(getApplicationContext(),
					"공지사항", Toast.LENGTH_SHORT)
					.show();
			Intent intent = new Intent(MainActivity.this, NoticeNoticeActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_help:
			Toast.makeText(getApplicationContext(),
					"도움말", Toast.LENGTH_SHORT)
					.show();
			//				Intent intent = new Intent(NoticeMainActivity.this, .class);
			//				startActivity(intent);
			return true;
		case R.id.action_qna:
			Toast.makeText(getApplicationContext(),
					"묻고답하기", Toast.LENGTH_SHORT)
					.show();
			//				Intent intent = new Intent(NoticeMainActivity.this, .class);
			//				startActivity(intent);
			return true;
		case R.id.action_withdraw:
			Toast.makeText(getApplicationContext(),
					"회원탈퇴", Toast.LENGTH_SHORT)
					.show();
			//				Intent intent = new Intent(NoticeMainActivity.this, .class);
			//				startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}