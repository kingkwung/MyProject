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

	String userId = ""; // MyInfo�� id�� ������
	public static String tempId = "";
	String userGender = ""; // MyInfo�� gender�� ������
	String imgURL = ""; // �ڽ��� ���̽��� ���� �ּҸ� ����
	double userPriority = 0; // MyInfo�� rank���� ������
	String flID = MainActivity.fl; // ������ ����ϴ� ��ģ���� ���̵�
	String[] flIDs; // �Ѹ� �ڸ���
	String flName = MainActivity.friendsList; // ������ ����ϴ� ��ģ���� �̸�
	public static String[] flNames; // �Ѹ� �ڸ���
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

	public static int type1 = 0; // ������ ����
	public static int type2 = 0; // ī�� ����
	public static int type3 = 0; // ȣ���� ����
	public static int type4 = 0; // �뷡�� ����
	public static int type5 = 0; // �籸�� ����
	public static int type6 = 0; // �ǽù� ����
	
	public static int numberOfSameUniv = 0;	// ���� ���� ���б��� ��ϵ� �л���
	public static Boolean isClickInMyInfo = false;

	int numOfRec = 1;
	int numOfEval = 1;

	Integer sizeX = 0; // �ڵ����� ���� ������
	Integer sizeY = 0; // �ڵ����� ���� ������
	String editedUniv = "";
	final int DIALOG_FRIEND = 1; // ģ�� ����Ʈ �� �Ѹ��� Ŭ�� ���� �� ���̾�α� ���̵�
	final int DIALOG_EDIT = 2; // ���б� ���� ��ư�� Ŭ�� ���� �� ���̾�α� ���̵�
	final int DIALOG_MY_EVAL = 3; // ���� �� ��� ���̾�α� ���̵�
	final int DIALOG_MY_REC = 4; // ���� ��õ ��� ���̾�α� ���̵�
	final int DIALOG_FR_EVAL = 5; // ��ģ���� �� ��� ���̾�α� ���̵�
	final int DIALOG_FR_REC = 6; // ��ģ���� ��õ ��� ���̾�α� ���̵�

	// īī���� ģ�� �ʴ븦 ���� ������
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

		// īī���� api ��������
		try {
			kakaoLink = KakaoLink.getKakaoLink(this);
		} catch (KakaoParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		kakaoTalkLinkMessageBuilder = kakaoLink
				.createKakaoTalkLinkMessageBuilder();

		// ��Ƽ��Ƽ ������ ��������
		sizeX = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getWidth();
		sizeY = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getHeight();
		// ���� ���̽��� ���̵� ��������
		userId = MainActivity.my_info.getUserId();
		// �� ���̽��� ���� �ּ�
		imgURL = "https://graph.facebook.com/" + userId + "/picture";
		// ������ ����ϴ� ��ģ�� ���̵� �Ѹ� �ڸ���
		flIDs = flID.split(", ");
		flIDs[0] = flIDs[0].substring(1, flIDs[0].length());
		flIDs[flIDs.length - 1] = flIDs[flIDs.length - 1].substring(0,
				flIDs[flIDs.length - 1].length() - 1);
		// ������ ����ϴ� ��ģ�� �̸� �Ѹ� �ڸ���
		flNames = flName.split(", ");
		flNames[0] = flNames[0].substring(1, flNames[0].length());
		flNames[flNames.length - 1] = flNames[flNames.length - 1].substring(0,
				flNames[flNames.length - 1].length() - 1);

		// ArrayList<FrdInfo> frList = new ArrayList<FrdInfo>();
		frList = MainActivity.my_info.getFriendsList();
		frListTitle.setText("ģ�����(" + frList.size() + "��)");

		// ģ������Ʈ �ѷ��� ���� ����Ʈ�� �����
		HorizontalListView listview = (HorizontalListView) findViewById(R.id.friendListView);
		listview.setAdapter(new HAdapter());

		// ���̽��� ������ �̹��� �������� ���� AQuery ���̺귯�� ���
		AQuery aq = new AQuery(myImage);

		// �̹����信 �̹��� ���� �ֱ�
		aq.id(myImage).image(imgURL);

		// �� �̸� ä���ֱ�
		myName.setText(" " + MainActivity.my_info.getUserName());
		// �� �б� ä���ֱ�
		if (MainActivity.my_info.getUserUniv().equals("")) {
			myUniv.setText("-");
		} else {
			myUniv.setText(MainActivity.my_info.getUserUniv());

			// �� ���� ������ ���� �ʱ�ȭ
			type1 = type2 = type3 = type4 = type5 = type6 = 0;
			// �б��� ���� ������ ���� ���ϱ�
			DBAsyncTask dbaU = new DBAsyncTask();
			dbaU.setDBmanager_type("from_MyInfoActivity_findUnivBusiness");
			dbaU.execute(0);
			// ���б��� ��ϵ� �л��� ���ϱ�
			numberOfSameUniv = 0;
			DBAsyncTask dbaS = new DBAsyncTask();
			dbaS.setDBmanager_type("from_MyInfoActivity_findUnivStudent");
			dbaS.execute(0);
		}
		// �� ���� ä���ֱ�
		if (MainActivity.my_info.getUserGender().equalsIgnoreCase("male"))
			userGender = "Male";
		else if (MainActivity.my_info.getUserGender()
				.equalsIgnoreCase("female"))
			userGender = "Female";
		else
			userGender = "-";
		myGender.setText(" " + userGender);

		// �� ��ũ�� ä���ֱ�
		myRank.setText(" ���� ���� : " + MainActivity.my_info.getUserPriority());

		// ���� ���� ���� ���� ���ϱ�
		if (MainActivity.my_info.getUserEvaluate().equals("")) {
			evaluated.setText(" ���� ���� ���� : ����");
		} else {
			for (int k = 0; k < MainActivity.my_info.getUserEvaluate().length(); k++) {
				if (MainActivity.my_info.getUserEvaluate().charAt(k) != ',') {
					continue;
				}
				numOfEval++;
			}
			evaluated.setText(" ���� ���� ���� : " + numOfEval + "��");
		}

		// ���� ��õ�� ���� ���� ���ϱ�
		if (MainActivity.my_info.getUserRec().equals("")) {
			recommended.setText(" ���� ��õ�� ���� : ����");
		} else {
			for (int k = 0; k < MainActivity.my_info.getUserRec().length(); k++) {
				if (MainActivity.my_info.getUserRec().charAt(k) != ',') {
					continue;
				}
				numOfRec++;
			}
			recommended.setText(" ���� ��õ�� ���� : " + numOfRec + "��");
		}

		// '���б� ����'�� Ŭ�� �Ͽ��� ��
		editMyUniv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DIALOG_EDIT);
			}
		});

		// 'īī���� ģ�� �ʴ�'�� Ŭ�� �Ͽ��� ��
		kakaoInvite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				kakaoMainText = "[campus channel]\n\n�� ģ������ ���� �б� �ֺ��� ����, ī��, ȣ����, �뷡�� ���� Ȯ���� �� �ִ� 'ķ�۽� ä��'�� ����� �ʴ��մϴ�.";
				kakaoButtonText = "������ �̵�";
				kakaoMainImage = "http://sangmin9655.dothome.co.kr/kakaolink.png";

				// �̹����� ������ ī�� �ʴ�
				/*
				 * try { kakaoTalkLinkMessageBuilder.addText(kakaoMainText)
				 * .addImage(kakaoMainImage, 130, 170)
				 * .addAppButton(kakaoButtonText); } catch
				 * (KakaoParameterException e1) { e1.printStackTrace(); }
				 */

				// �̹����� ������ ī�� �ʴ�
				try {
					kakaoTalkLinkMessageBuilder.addText(kakaoMainText)
							.addAppButton(kakaoButtonText);
				} catch (KakaoParameterException e1) {
					e1.printStackTrace();
				}

				sendToFriends();
			}
		});

		// '��õ�� ���� ����'�� Ŭ�� �Ͽ��� ��
		recommended.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDialog(DIALOG_MY_REC);
			}
		});

		// '���� ���� ����'�� Ŭ�� �Ͽ��� ��
		evaluated.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDialog(DIALOG_MY_EVAL);
			}
		});

		// '�� ����'�� Ŭ�� ���� ��
		myRank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "����������������ư",
						Toast.LENGTH_SHORT).show();

				double maxPrio = MainActivity.my_info.getMaxPriorityOfAllUser();
				maxPrio = ((int) (maxPrio * 100)) / 100.0;

				double myPrio = MainActivity.my_info.getUserPriority();
				myPrio = ((int) (myPrio * 100)) / 100.0;

				double percentage = (myPrio / maxPrio) * 100;
				percentage = ((int) (percentage * 100)) / 100.0;

				new AlertDialog.Builder(MyInfoActivity.this)
						.setTitle("��Weight��?")
						.setMessage(
								"�ְ� Weight : " + maxPrio + "\n" + "�� Weight : "
										+ myPrio + "\n" + "����� : " + percentage
										+ "��")
						.setNeutralButton("�ݱ�",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		});

		// '�� �б�'�� Ŭ�� ���� ��
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
						t1 = "����";
					} else {
						t1 = type1 + " ��";
					}
					if (type2 == 0) {
						t2 = "����";
					} else {
						t2 = type2 + " ��";
					}
					if (type3 == 0) {
						t3 = "����";
					} else {
						t3 = type3 + " ��";
					}
					if (type4 == 0) {
						t4 = "����";
					} else {
						t4 = type4 + " ��";
					}
					if (type5 == 0) {
						t5 = "����";
					} else {
						t5 = type5 + " ��";
					}
					if (type6 == 0) {
						t6 = "����";
					} else {
						t6 = type6 + " ��";
					}
					
					new AlertDialog.Builder(MyInfoActivity.this)
							.setTitle(
									"'" + MainActivity.my_info.getUserUniv()
											+ "'�� �����Ȳ")
							.setMessage(
									"������ : " + t1 + "\n" + "ī��  : " + t2 + "\n"
											+ "ȣ���� : " + t3 + "\n" + "�뷡�� : "
											+ t4 + "\n" + "�籸�� : " + t5 + "\n"
											+ "�ǽù� : " + t6 + "\n\n�л��� : " + numberOfSameUniv + " ��")
							.setPositiveButton("�ݱ�",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									})
							.setNegativeButton("�˻��ϱ�",
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

	// ������ īī���� ��ũ ������ ģ������ ������
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

	// īī���� ������ ���� �� ���̾�α׸� ����.
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
	 * builder.setTitle("ģ���� ����"); builder.setView(convertView);
	 * 
	 * builder.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * 
	 * Log.i("sangmin", "Ȯ�� ����"); } }); return builder.create(); } }
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
						thisFrRank += "(" + Double.toString(tempFrRank2) + "��)";

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
				// Toast.makeText(getApplicationContext(), "ģ�����̵� : " + thisFrId
				// + "\nģ�� �̸� : " + frName + ", ��õ�� : " + frRecNum.toString() +
				// ", �򰡼� : " + frEvalNum.toString(),
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
			// ģ���� �̸� �Ѹ� ä���ֱ�
			title.setText(" " + flNames[position]);
			// ���̽��� ������ �̹��� �������� ���� AQuery ���̺귯�� ���
			AQuery aq1 = new AQuery(imgF);
			// �̹����� �ձ۰� �ϱ� ���� ��ü ����
			ImageOptions op1 = new ImageOptions();
			// �̹����� �ձ� ���� ����
			op1.round = 25;
			// �̹����信 �̹��� ���� �ֱ�
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
	 * builder.setTitle("\'" + thisFrName + "\'���� Ȱ�� ���� "); // Get the layout
	 * inflater LayoutInflater inflater = this.getLayoutInflater(); // Inflate
	 * and set the layout for the dialog // Pass null as the parent view because
	 * its going in the dialog layout
	 * builder.setView(inflater.inflate(R.layout.myinfo_friend_activity, null))
	 * .create().show(); }
	 */

	// "�� ģ���� Ȱ�������� ���"
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
			dialogbuilder.setPositiveButton("Ȯ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			break;
		case DIALOG_EDIT:
			dialogview = inflater.inflate(R.layout.edit_my_univ, null);
			dialogbuilder.setPositiveButton("Ȯ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							editedUniv = typedUniv.getText().toString();
							myUniv.setText(editedUniv);
							// ������ ���� ���б��� ������Ʈ ��Ų��
							UPAsynkTask upa = new UPAsynkTask();
							upa.execute(0);
							MainActivity.my_info.setUserUniv(editedUniv);
							
							// �� ���� ������ ���� �ʱ�ȭ
							type1 = type2 = type3 = type4 = type5 = type6 = 0;

							DBAsyncTask dbaU = new DBAsyncTask();
							dbaU.setDBmanager_type("from_MyInfoActivity_findUnivBusiness");
							dbaU.execute(0);

							// ���б��� ��ϵ� �л��� ���ϱ�
							numberOfSameUniv = 0;
							DBAsyncTask dbaS = new DBAsyncTask();
							dbaS.setDBmanager_type("from_MyInfoActivity_findUnivStudent");
							dbaS.execute(0);
							
							Toast.makeText(getApplicationContext(),
									"���� �б��� �����Ǿ����ϴ�", Toast.LENGTH_SHORT)
									.show();
						}
					});
			dialogbuilder.setNegativeButton("���",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			break;
		case DIALOG_MY_REC:
			dialogview = inflater.inflate(R.layout.myinfo_my_rec_eval, null);
			dialogbuilder.setPositiveButton("Ȯ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			break;
		case DIALOG_MY_EVAL:
			dialogview = inflater.inflate(R.layout.myinfo_my_rec_eval, null);
			dialogbuilder.setPositiveButton("Ȯ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			break;
		case DIALOG_FR_REC:
			dialogview = inflater.inflate(R.layout.myinfo_my_rec_eval, null);
			dialogbuilder.setPositiveButton("Ȯ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();
						}
					});
			break;
		case DIALOG_FR_EVAL:
			dialogview = inflater.inflate(R.layout.myinfo_my_rec_eval, null);
			dialogbuilder.setPositiveButton("Ȯ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			break;
		}
		// dialogbuilder.setTitle("\'" + thisFrName + "\'���� Ȱ�� ���� ");

		dialogbuilder.setView(dialogview);
		dialogDetails = dialogbuilder.create();

		return dialogDetails;

	}

	// "�� ģ���� Ȱ�������� ���"
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		alertDialog = (AlertDialog) dialog;

		switch (id) {
		case DIALOG_FRIEND:
			LayoutParams params = alertDialog.getWindow().getAttributes();
			// params.height = (int)(sizeY * 0.9);
			// Log.i("��Ƽ��Ƽ ���λ�����", sizeX.toString());
			params.width = (int) (sizeX * 0.7);
			// alertDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams)
			// params);
			// lp.width = (int)(context.getWindow( ).getWindowManager(
			// ).getDefaultDisplay( ).getWidth( ) * 0.9);

			// alertDialog.setTitle("\'" + thisFrName + "\'���� Ȱ�� ���� ");
			// tempId = frID;
			ImageView frImage = (ImageView) alertDialog
					.findViewById(R.id.frImage);
			TextView frName = (TextView) alertDialog.findViewById(R.id.frName);
			TextView frUniv = (TextView) alertDialog.findViewById(R.id.frUniv);
			TextView frRank = (TextView) alertDialog.findViewById(R.id.frRank);
			Button frEval = (Button) alertDialog.findViewById(R.id.frEval);
			Button frRec = (Button) alertDialog.findViewById(R.id.frRec);

			// ģ���� ������ ���� ����ֱ�
			// ���̽��� ������ �̹��� �������� ���� AQuery ���̺귯�� ���
			AQuery aq3 = new AQuery(frImage);
			// �̹����� �ձ۰� �ϱ� ���� ��ü ����
			ImageOptions op3 = new ImageOptions();
			// �̹����� �ձ� ���� ����
			op3.round = 25;
			// �̹����信 �̹��� ���� �ֱ�
			aq3.id(frImage).image(
					"https://graph.facebook.com/" + tempId + "/picture", op3);

			if (thisFrUniv.equals(""))
				thisFrUniv = "-";
			frName.setText("�̸� : " + thisFrName);
			frUniv.setText("�б� : " + thisFrUniv);
			frRank.setText("���� : " + thisFrRank);
			frEval.setText(" ���� ���� : " + thisFrEvalNum + "��");
			frRec.setText(" ��õ�� ���� : " + thisFrRecNum + "��");

			// 'ģ���� �� ���'�� Ŭ�� �Ͽ��� ��
			frEval.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// alertDialog.dismiss();
					showDialog(DIALOG_FR_EVAL);

				}
			});
			// 'ģ���� ��õ ���'�� Ŭ�� �Ͽ��� ��
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
			// Log.i("��Ƽ��Ƽ ���λ�����", sizeX.toString());
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

			// �� ��õ ��� ���ϱ�
			DBAsyncTask dba = new DBAsyncTask();
			dba.setDBmanager_type("from_MyInfoActivity_fillList_rec");
			dba.execute(0);

			break;
		case DIALOG_MY_EVAL:
			// LayoutParams params3 = alertDialog.getWindow().getAttributes();
			// params.height = (int)(sizeY * 0.9);
			// params3.width = (int) (sizeX * 0.8);

			context = getApplicationContext();

			// �� ��õ ��� ���ϱ�
			DBAsyncTask dba2 = new DBAsyncTask();
			dba2.setDBmanager_type("from_MyInfoActivity_fillList_eval");
			dba2.execute(0);

			break;
		case DIALOG_FR_REC:
			// LayoutParams params3 = alertDialog.getWindow().getAttributes();
			// params.height = (int)(sizeY * 0.9);
			// params3.width = (int) (sizeX * 0.8);

			context = getApplicationContext();

			// �� ��õ ��� ���ϱ�
			DBAsyncTask dba3 = new DBAsyncTask();
			dba3.setDBmanager_type("from_MyInfoActivity_fillList_fr_rec");
			dba3.execute(0);

			break;
		case DIALOG_FR_EVAL:
			// LayoutParams params3 = alertDialog.getWindow().getAttributes();
			// params.height = (int)(sizeY * 0.9);
			// params3.width = (int) (sizeX * 0.8);

			context = getApplicationContext();

			// �� ��õ ��� ���ϱ�
			DBAsyncTask dba4 = new DBAsyncTask();
			dba4.setDBmanager_type("from_MyInfoActivity_fillList_fr_eval");
			dba4.execute(0);

			break;
		}
	}

	// ������ ���� ���б��� ������Ʈ ��Ų��
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