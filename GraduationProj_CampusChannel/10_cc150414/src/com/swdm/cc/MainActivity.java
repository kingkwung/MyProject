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
	public static ArrayList<Store> stores = new ArrayList<Store>(); // store��
	// ������
	// arrayList
	public static ArrayList<String> storeData = new ArrayList<String>(); // store
	// ������
	// string
	// Ÿ������
	// ����

	public static ArrayList<String> users = new ArrayList<String>(); // ��񿡼� ������
	// user��
	// ������
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
	private int viewId=0;	// ���� ó������ ģ�� ȭ��

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
		// ListView �� ������ ������
		listView = (ListView) findViewById(R.id.listView);

		btn_friends.setOnClickListener(this);
		btn_all.setOnClickListener(this);
		btn_insert.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		btn_myInfo.setOnClickListener(this);

		aaa = this;
		// /////////////////////////////////////////////////////////
		// user�� ���� ��.
		// �α��ν� ���� ������ ��������
		setData(); // ����Ʈ�� ���� ������ ������ �����ϴ� �޼ҵ�
		// //////////////////////////////////////////////////////////
		// ��� ����� ����� ������ ��̸���Ʈ�� �ֱ�
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
				// item2 + " �� �����ϼ̽��ϴ�.", Toast.LENGTH_SHORT)
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

		// ��� �̹� �ִ� �������� �˻�
		// SearchUserInDB search_user = new SearchUserInDB(id);
		// Log.i("DBuser", users.toString());

		// ��� ���� ���̵� ���ٸ� ó�� ������ ��. ��񿡼� userId�� primary key�� �̹� �ִ� �������� �˾Ƽ�
		// ��������.
		// User class ����

		/* ���� ���� ������ ������ ���� Ŭ������ �����ϰ� ������� �� ���ܿ� ���߾� ä���ش� */
		// my_info = new MyInfo(id, name, gender);
		// my_info = new MyInfo();
		my_info.setUserId(id);
		my_info.setUserName(name);
		my_info.setUserGender(gender);
		// my_info = new MyInfo();
		// my_info.setUserId(id);

		// ���� ��õ�� ������ index�� ����!
		// whatSearch = "myRecommend";

		Log.i("��ȸ����", id + name + gender);
		DBAsyncTask dba = new DBAsyncTask();
		dba.setDBmanager_type("from_MainActivity_myRecommend_and_myEvaluate");
		dba.execute(0);

		// �� ģ���� ����Ʈ���� �޾ƿ´�!
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
			// ģ����� ������]�� �κ�
			// taggable_friends�� �±װ����� ģ���� �����.
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
							// ��� ������� ��ģ���� ���̵� ���ؼ� ���� ���̵� ģ����Ͽ� ����
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
						Log.i("�� �����̾Ƽ", Double.toString(my_info
								.getUserPriority()));

						friendsList = name_list.toString();
						fl = my_info.friendsIdList.toString();

						// ��� ���ο� ���� ���� �ø�
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

	// ��� ���� ���� �ø��� Ŭ����
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
			//			// ģ������ �������� �� �͸� �����ش�
			//			Toast.makeText(getApplicationContext(), "ģ��", Toast.LENGTH_SHORT)
			//					.show();
			viewId = 0;

			setButtonImage(viewId);
			handlingView(viewId);
			//
			//			/* 0130�߰��κ� */
			//			fillMainList = new FillMainList("btn_friends", this);

			//			Log.i("���Ʈ�θ���", "sfsfsdf ");
			//			showList();
			//			// �̰� fillListŬ������ �ѱ��
			//			Log.d("saaa", "5 Ŭ��");
		}

		if (v.getId() == btn_all.getId()) {
			// ��� ������� �������� �� �� �� �����ش�
			//			Toast.makeText(getApplicationContext(), "��ü", Toast.LENGTH_SHORT)
			//					.show();
			viewId = 1;

			setButtonImage(viewId);
			handlingView(viewId);

			//showList();
		}

		if (v.getId() == btn_insert.getId()) {
			// ���ҵ���ϱ�
			//			Toast.makeText(getApplicationContext(), "���", Toast.LENGTH_SHORT)
			//					.show();
			//			Intent intent = new Intent(MainActivity.this, Insert.class);
			//			startActivity(intent);
			viewId = 2;

			setButtonImage(viewId);
			handlingView(viewId);

			//showList();
		}

		if (v.getId() == btn_myInfo.getId()) {

			//			Toast.makeText(getApplicationContext(), "������", Toast.LENGTH_SHORT)
			//					.show();
			//			Intent intent = new Intent(MainActivity.this, MyInfoActivity.class);
			//			startActivity(intent);

			viewId = 3;

			setButtonImage(viewId);
			handlingView(viewId);
		}

		if (v.getId() == btn_search.getId()) {
			// �˻� ȭ������ �Ѿ
			//			Toast.makeText(getApplicationContext(), "�˻�", Toast.LENGTH_SHORT)
			//					.show();
			//			Intent intent = new Intent(MainActivity.this, SearchData.class);
			//			startActivity(intent);
			// createDialogBox(); // dialog�� ����� �����ϰ� �Ѵ�

			viewId = 4;

			setButtonImage(viewId);
			handlingView(viewId);
		}
		//
		//		if (btn_selected == 1) {
		//			// ģ�� �������� ��
		//			txtView.setText("ģ������ ��õ�� ���Ҹ� �����ݴϴ�.");
		//			btn_friends.setImageResource(R.drawable.friends_clicked);
		//			btn_all.setImageResource(R.drawable.like_finger);
		//
		//			// showList();
		//		} else {
		//			// ��ü �������� ��
		//			txtView.setText("��� ����ڵ��� ��õ�� ���Ҹ� �����ݴϴ�.");
		//			btn_friends.setImageResource(R.drawable.friends);
		//			btn_all.setImageResource(R.drawable.like_finger_clicked);
		//
		//			showList();
		//		}
	}

	private void showList() {
		// �ȵ���̵忡�� �����ϴ� �⺻���� Adapter ��ü �����ϱ�(īī���尰�� Ư���� ����Ʈ��� ������ �Ѵ�)*Ŀ���͸���¡

		/* 0130. �������� �� �ӽ��� weather_data�� FillMainList�κ��� �޾ƿͼ� ��� �� ���� */
		// Weather weather_data[] = new Weather[] {
		// new Weather(R.drawable.droptop, "ī�� ���ž", "������ ������ �������"),
		// new Weather(R.drawable.droptop, "ī�� ���ž", "������ ������ �������"),
		// new Weather(R.drawable.droptop, "ī�� ���ž", "������ ������ �������"),
		// new Weather(R.drawable.droptop, "ī�� ���ž", "������ ������ �������"),
		// new Weather(R.drawable.droptop, "ī�� ���ž", "������ ������ �������"),
		// new Weather(R.drawable.droptop, "ī�� ���ž", "������ ������ �������"),
		// new Weather(R.drawable.droptop, "ī�� ���ž", "������ ������ �������"),
		// new Weather(R.drawable.droptop, "ī�� ���ž", "������ ������ �������"), };

		// /*���⼭���ʹ� ���� DBAsyncTask�� ����*/
		// Log.i("���Ʈ!!!",
		// "asdfsfsafsaf"+Integer.toString(weather_data.length));
		// WeatherAdapter adapter = new WeatherAdapter(this,
		// R.layout.listview_item_row, weather_data);
		//
		// // adapter = new ArrayAdapter<String>(this,
		// // R.layout.listview_item_layout_01, storeData);
		// // simple_list_item_1�� textview�� ��� �ִ� xml�����̴�.
		//
		// // listview ��ü�� �ƴ��� ��ü �����ϱ�
		// listView.setAdapter(adapter);
		//
		// // ����Ʈ���� ��輱�� �Ķ�������
		// listView.setDivider(new
		// ColorDrawable(Color.parseColor("#FF000080")));
		//
		// // ��輱�� ���⸦ 5px
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
			// ģ�� �������� ��
			txtView.setText("ģ������ ��õ�� ���Ҹ� �����ݴϴ�.");
			btn_friends.setImageResource(R.drawable.friends_clicked);
			btn_all.setImageResource(R.drawable.like_finger);
			btn_insert.setImageResource(R.drawable.add_business);
			btn_myInfo.setImageResource(R.drawable.man);
			btn_search.setImageResource(R.drawable.search);

			Toast.makeText(getApplicationContext(), "ģ��", Toast.LENGTH_SHORT)
			.show();

			/* 0130�߰��κ� */
			fillMainList = new FillMainList("btn_friends", this);

		} else if (viewId == 1) {
			// ��ü �������� ��
			txtView.setText("��� ����ڵ��� ��õ�� ���Ҹ� �����ݴϴ�.");
			btn_friends.setImageResource(R.drawable.friends);
			btn_all.setImageResource(R.drawable.like_finger_clicked);
			btn_insert.setImageResource(R.drawable.add_business);
			btn_myInfo.setImageResource(R.drawable.man);
			btn_search.setImageResource(R.drawable.search);

			Toast.makeText(getApplicationContext(), "��ü", Toast.LENGTH_SHORT)
			.show();

		} else if (viewId == 2) {
			// �����߰� �������� ��
			txtView.setText("���Ҹ� �߰��մϴ�.");
			btn_friends.setImageResource(R.drawable.friends);
			btn_all.setImageResource(R.drawable.like_finger);
			btn_insert.setImageResource(R.drawable.add_business_clicked);
			btn_myInfo.setImageResource(R.drawable.man);
			btn_search.setImageResource(R.drawable.search);


			Toast.makeText(getApplicationContext(), "���", Toast.LENGTH_SHORT)
			.show();
			Intent intent = new Intent(MainActivity.this, Insert.class);
			startActivity(intent);

		} else if (viewId == 3) {
			// ������ �������� ��

			//�մԹ湮�ΰ��
			if(my_info.getUserId().equals("000")){
				Toast.makeText(getApplicationContext(), "ȸ������ �� �̿� �Ͻ� �� �ֽ��ϴ�.", Toast.LENGTH_SHORT).show();
			}
			
			//ȸ���湮�ΰ��
			else{
				txtView.setText("�� ���� ����");
				btn_friends.setImageResource(R.drawable.friends);
				btn_all.setImageResource(R.drawable.like_finger);
				btn_insert.setImageResource(R.drawable.add_business);
				btn_myInfo.setImageResource(R.drawable.man_clicked);
				btn_search.setImageResource(R.drawable.search);
				

				Toast.makeText(getApplicationContext(), "������", Toast.LENGTH_SHORT)
				.show();
				Intent intent = new Intent(MainActivity.this, MyInfoActivity.class);
				startActivity(intent);
			}
			
		} else if (viewId == 4) {
			// �˻� �������� ��
			txtView.setText("�˻��ϱ�");
			btn_friends.setImageResource(R.drawable.friends);
			btn_all.setImageResource(R.drawable.like_finger);
			btn_insert.setImageResource(R.drawable.add_business);
			btn_myInfo.setImageResource(R.drawable.man);
			btn_search.setImageResource(R.drawable.search_clicked);

			// �˻� ȭ������ �Ѿ
			Toast.makeText(getApplicationContext(), "�˻�", Toast.LENGTH_SHORT)
			.show();
			Intent intent = new Intent(MainActivity.this, SearchData.class);
			startActivity(intent);
		}
	}


	// ��ư�� Ŭ���ؼ� �̵��Ҷ��� �ִϸ��̼� ȿ��
	private void handlingView(int index) {
		viewId = viewFlipper.getDisplayedChild();

		// ���� �ִ� ȭ���̶� ��
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


	// �˻���ư�� ������ ������� ���̾�α� â
	private void createDialogBox() {
		final CharSequence[] items = { "ī�װ���", "�˻����" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("�˻��ϱ�");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				Toast.makeText(getApplicationContext(), items[item],
						Toast.LENGTH_SHORT).show();

				if (item == 0) {
					// ī�װ� ����
					category();
				}

				if (item == 1) {
					// �˻��� ����
					search();
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}// createDialogBox

	// ī�װ� UI���
	private void category() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// Get the layout inflater
		LayoutInflater inflater = this.getLayoutInflater();
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(inflater.inflate(R.layout.dialog_category, null))
		.create().show();
	}

	// ī�װ� �������
	public void cateClick(View v) {
		// ���õ� ī�װ��� ����Ʈ�� ������
		// �˻����� ���� activity �θ�
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

	// �˻����� UI���
	private void search() {
		showDialog(1); // onCreateDialog �θ�
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog dialogDetails = null;

		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogview = inflater.inflate(R.layout.dialog_search, null);
		AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);

		dialogbuilder.setTitle("�˻����");
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
					"��������", Toast.LENGTH_SHORT)
					.show();
			Intent intent = new Intent(MainActivity.this, NoticeNoticeActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_help:
			Toast.makeText(getApplicationContext(),
					"����", Toast.LENGTH_SHORT)
					.show();
			//				Intent intent = new Intent(NoticeMainActivity.this, .class);
			//				startActivity(intent);
			return true;
		case R.id.action_qna:
			Toast.makeText(getApplicationContext(),
					"������ϱ�", Toast.LENGTH_SHORT)
					.show();
			//				Intent intent = new Intent(NoticeMainActivity.this, .class);
			//				startActivity(intent);
			return true;
		case R.id.action_withdraw:
			Toast.makeText(getApplicationContext(),
					"ȸ��Ż��", Toast.LENGTH_SHORT)
					.show();
			//				Intent intent = new Intent(NoticeMainActivity.this, .class);
			//				startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}