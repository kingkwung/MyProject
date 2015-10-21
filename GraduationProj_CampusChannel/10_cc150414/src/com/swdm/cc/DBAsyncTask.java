package com.swdm.cc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class DBAsyncTask extends AsyncTask<Integer, JSONArray, JSONArray>
implements OnItemClickListener {

	private Context gotContext;
	private Activity mainAct;
	public String frId;
	public String[] findRecs;
	public String findRec = "";
	public String frIdDb;
	public String recDb;
	public String univDb; // 0208 상민 : 디비의 친구 학교
	public String evalDb; // 0208 상민
	public String[] evalDbs; // 0208 상민
	public String priorityDb;
	public String userUniv; // 0208 상민 : 나의 대학교

	public DBAsyncTask(Activity act) {
		this.mainAct = act;
	}

	public DBAsyncTask() {

	}

	int k;
	// 이 변수로 모든 DBAsynkTask를 다스릴 것이다....
	private String DBmanager_type = "";

	public void setContext(Context inputContext) {
		this.gotContext = inputContext;
	}

	public void setDBmanager_type(String inputType) {
		this.DBmanager_type = inputType;
	}

	public String getDBmanager_type() {
		return this.DBmanager_type;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected JSONArray doInBackground(Integer... params) {

		DBmanager db = new DBmanager();
		JSONArray jArray = new JSONArray();

		if (DBmanager_type.equals("from_SetListView")
				|| DBmanager_type
				.equals("from_MyInfoActivity_findUnivBusiness")) {
			jArray = db.search("select * from business_info");
		} else if (DBmanager_type
				.equals("from_NoticeNoticeActivity_fillNoticeList")) {
			jArray = db.search("select * from noticelist");
		} else if (DBmanager_type
				.equals("from_ClickBusiness_fillReviewList") || DBmanager_type.equals("from_ClickBusinessReviewClicked_checkLike")) {
			jArray = db.search("select * from reviewlist");
		} else if (DBmanager_type
				.equals("from_ClickBusinessReviewClicked_fillCommentList")) {
			jArray = db.search("select * from commentlist");
		} else if (DBmanager_type.equals("from_MainActivity_myFriends")
				|| DBmanager_type.equals("from_MainActivity_myFriends_for_visitor")
				|| DBmanager_type
				.equals("from_MainActivity_myRecommend_and_myEvaluate")
				|| DBmanager_type.equals("from_MyInfoActivity_findUnivStudent")|| DBmanager_type.equals("from_ClickBusinessReviewClicked_checkWriter")) {
			jArray = db.searchUser("select * from users_info");
		} else if (DBmanager_type.equals("from_MainActivity_allBusiness")) {
			jArray = db.search("select * from business_info");
		} else if (DBmanager_type.equals("from_ClickBusiness")) {
			jArray = db.search("select * from business_info");
		} else if (DBmanager_type.equals("from_SearchResult")) {
			jArray = db.search("select * from business_info");
		} else if (DBmanager_type.equals("from_MyInfoActivity_fillList_rec")
				|| DBmanager_type.equals("from_MyInfoActivity_fillList_eval")
				|| DBmanager_type
				.equals("from_MyInfoActivity_fillList_fr_eval")
				|| DBmanager_type.equals("from_MyInfoActivity_fillList_fr_rec")) {
			jArray = db.search("select * from business_info");
		} else if (DBmanager_type
				.equals("from_FillMainList_gettingStoreInfoForMainList")) {
			jArray = db.search("select * from business_info");
		} /* 이거 나중에 사용해야함 */
		else if (DBmanager_type.equals("from_ClickBusiness_evaluate")) {
			jArray = db.search("select * from ");// 이거나중에다시수정
		}
		/* 여기까지 */

		// else
		// if(DBmanager_type.equals("from_MainActivity_fillFriendsRecommend")){
		// jArray = db.search("select * from users_info");
		// }
		return jArray;
	}

	@Override
	protected void onPostExecute(JSONArray result) {
		super.onPostExecute(result);

		if (DBmanager_type.equals("from_SetListView")) {
			try {
				// count = 0;
				for (int i = 0; i < result.length(); i++) {
					JSONObject json_data = result.getJSONObject(i);
					String univR = json_data.getString("univ");
					String typeR = json_data.getString("type");
					String nameR = json_data.getString("name");
					String tagR = json_data.getString("tag");
					// count++;
					MainActivity.storeData.add(univR + " " + typeR + " "
							+ nameR + " " + tagR);
					// Log.i("log_tag", univR + typeR + nameR + tagR);
				}
			} catch (Exception e) {
			}
		} else if (DBmanager_type
				.equals("from_MyInfoActivity_findUnivBusiness")) {
			try {
				String univDB = "";
				String whatTypeDB = "";

				for (int i = 0; i < result.length(); i++) {
					JSONObject json_data = result.getJSONObject(i);

					univDB = json_data.getString("univ");
					whatTypeDB = json_data.getString("type");

					if (MainActivity.my_info.getUserUniv().equals(univDB)) {
						if (whatTypeDB.equals("음식점")) {
							MyInfoActivity.type1++;
						} else if (whatTypeDB.equals("카페")) {
							MyInfoActivity.type2++;
						} else if (whatTypeDB.equals("술집")) {
							MyInfoActivity.type3++;
						} else if (whatTypeDB.equals("노래방")) {
							MyInfoActivity.type4++;
						} else if (whatTypeDB.equals("당구장")) {
							MyInfoActivity.type5++;
						} else if (whatTypeDB.equals("피시방")) {
							MyInfoActivity.type6++;
						}
					}
				}
				Log.i("sangmin", MyInfoActivity.type1 + " / "
						+ MyInfoActivity.type2 + " / " + MyInfoActivity.type3
						+ " / " + MyInfoActivity.type4 + " / "
						+ MyInfoActivity.type5 + " / " + MyInfoActivity.type6);
			} catch (Exception e) {
			}
		} else if (DBmanager_type.equals("from_MyInfoActivity_findUnivStudent")) {
			try {
				String myUnivInDB = "";

				for (int i = 0; i < result.length(); i++) {
					JSONObject json_data = result.getJSONObject(i);

					myUnivInDB = json_data.getString("myUniv");

					if (MainActivity.my_info.getUserUniv().equals(myUnivInDB)) {
						MyInfoActivity.numberOfSameUniv++;
					}
				}

			} catch (Exception e) {
			}
		} else if (DBmanager_type.equals("from_ClickBusinessReviewClicked_checkWriter")) {
			try {
				String writerIdDB = "";
				String numOfRecString = "";
				String numOfEvalString = "";
				Integer numOfRec = 0;
				Integer numOfEval = 0;

				for (int i = 0; i < result.length(); i++) {
					JSONObject json_data = result.getJSONObject(i);

					writerIdDB = json_data.getString("userId");
					numOfRecString = json_data.getString("recommended");
					numOfEvalString = json_data.getString("evaluated");

					if (ClickBusinessReviewClicked.cWriterId.equals(writerIdDB)) {
						if(numOfRecString.equals("")) {
							numOfRec = 0;
						}
						else {
							numOfRec = 1;
							for(int k = 0; k<numOfRecString.length(); k++) {
								if(numOfRecString.charAt(k) != ',') {
									continue;
								}
								numOfRec++;
							}
						}

						if(numOfEvalString.equals("")) {
							numOfEval = 0;
						}
						else {
							numOfEval = 1;
							for(int k = 0; k<numOfEvalString.length(); k++) {
								if(numOfEvalString.charAt(k) != ',') {
									continue;
								}
								numOfEval++;
							}
						}
						ClickBusinessReviewClicked.writersRec.setText(" " + numOfRec + " 추천");
						ClickBusinessReviewClicked.writersEval.setText(" " + numOfEval + " 평가");
						ClickBusinessReviewClicked.writersRec.setTypeface(MainActivity.jangmeFont);
						ClickBusinessReviewClicked.writersEval.setTypeface(MainActivity.jangmeFont);
						break;
					}
				}

			} catch (Exception e) {
			}
		}else if (DBmanager_type.equals("from_MainActivity_myFriends_for_visitor")) {
			try {
				Log.i("내친구사이즈확인ㅋㅋ", Integer.toString(MainActivity.my_info.getFriendsList().size()));
				// 내 friends list의 정보를 세팅!


				for (int i = 0; i < result.length(); i++) {
					JSONObject json_data = result.getJSONObject(i);
					frIdDb = json_data.getString("userId");
					recDb = json_data.getString("recommended");
					// 0208 상민
					//univDb = json_data.getString("myUniv");
					// 0208 상민
					evalDb = json_data.getString("evaluated");
					priorityDb = json_data.getString("priority");

					// 전체 사용자들중에 가장 높은 Priority를 갖고 있는 녀석을 찾아내기 위함(친구정보채우는
					// 것과는 별개임)
					if (MainActivity.my_info.getMaxPriorityOfAllUser() < Double
							.parseDouble(priorityDb)) {
						MainActivity.my_info.setMaxPriorityOfAllUser(Double
								.parseDouble(priorityDb));
						Log.i("바뀐MAXWEIGHT", Double
								.toString(MainActivity.my_info
										.getMaxPriorityOfAllUser()));
					}

					findRec = recDb;
					findRecs = findRec.split(", ");
					evalDbs = evalDb.split(", ");

					FrdInfo friend = new FrdInfo(frIdDb, "방문자임시친구");
					friend.convertStrArr_to_ArrayList(findRecs);
					// 0208 상민
					friend.convertEvalStrArr_to_ArrayList(evalDbs);
					// 0208 상민
					//friend.setUniv(univDb);
					friend.setPriority(Double.parseDouble(priorityDb));

					if(!friend.getId().equals("000")){
						MainActivity.my_info.addFriend(friend);
					}
				}








				for (FrdInfo friend : MainActivity.my_info.getFriendsList()) {
					Log.i("친구들어가는 MAXWEIGHT", Double
							.toString(MainActivity.my_info
									.getMaxPriorityOfAllUser()));
					friend.setMaxPriorityOfAllUser(MainActivity.my_info
							.getMaxPriorityOfAllUser());
				}
				MainActivity.btn_selected = 1;
				MainActivity.fillMainList = new FillMainList("btn_friends",
						MainActivity.aaa);
			} catch (Exception e) {
			}
		} 

		else if (DBmanager_type.equals("from_MainActivity_myFriends")) {
			try {
				// 내 friends list의 정보를 세팅!
				for (FrdInfo friend : MainActivity.my_info.getFriendsList()) {
					frId = friend.getId();

					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);
						frIdDb = json_data.getString("userId");
						recDb = json_data.getString("recommended");
						// 0208 상민
						univDb = json_data.getString("myUniv");
						// 0208 상민
						evalDb = json_data.getString("evaluated");
						priorityDb = json_data.getString("priority");

						// 전체 사용자들중에 가장 높은 Priority를 갖고 있는 녀석을 찾아내기 위함(친구정보채우는
						// 것과는 별개임)
						if (MainActivity.my_info.getMaxPriorityOfAllUser() < Double
								.parseDouble(priorityDb)) {
							MainActivity.my_info.setMaxPriorityOfAllUser(Double
									.parseDouble(priorityDb));
							Log.i("바뀐MAXWEIGHT", Double
									.toString(MainActivity.my_info
											.getMaxPriorityOfAllUser()));
						}

						if (frId.equals(frIdDb)) {
							findRec = recDb;
							findRecs = findRec.split(", ");
							evalDbs = evalDb.split(", ");
							Log.i("CHECK findRec!", friend.getName() + ":"
									+ findRec);
							friend.convertStrArr_to_ArrayList(findRecs);
							// 0208 상민
							friend.convertEvalStrArr_to_ArrayList(evalDbs);
							// 0208 상민
							friend.setUniv(univDb);
							friend.setPriority(Double.parseDouble(priorityDb));
						}
					}
				}

				for (FrdInfo friend : MainActivity.my_info.getFriendsList()) {
					Log.i("친구들어가는 MAXWEIGHT", Double
							.toString(MainActivity.my_info
									.getMaxPriorityOfAllUser()));
					friend.setMaxPriorityOfAllUser(MainActivity.my_info
							.getMaxPriorityOfAllUser());
				}
				MainActivity.btn_selected = 1;
				MainActivity.fillMainList = new FillMainList("btn_friends",
						MainActivity.aaa);
			} catch (Exception e) {
			}
		} else if (DBmanager_type.equals("from_MyInfoActivity_fillList_rec")) {
			try {
				String[] eachRecs;
				String tempRecIdx;
				Integer tempDbIdx;
				String[] eachImgs;

				String busiNameT;
				String busiUnivT;
				String busiTypeT;
				// String busiScoreT;
				String busiEvaled;
				String busiLikeT;
				String busiImgT;

				MyInfoActivity.myListRec.clear();
				eachRecs = MainActivity.my_info.getUserRec().split(", ");
				for (int k = 0; k < eachRecs.length; k++) {
					tempRecIdx = eachRecs[k];

					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);
						tempDbIdx = json_data.getInt("businessId");
						busiUnivT = json_data.getString("univ");
						busiTypeT = json_data.getString("type");
						busiNameT = json_data.getString("name");
						// MainActivity.tagR = json_data.getString("tag");
						busiImgT = json_data.getString("img");
						busiLikeT = json_data.getString("countrec");
						busiEvaled = json_data.getString("counteval");

						if (tempDbIdx.toString().equals(tempRecIdx)) {
							if (busiImgT.contains(",")) {
								eachImgs = busiImgT.split(", "); // ', '를 구분자로
								// 쪼개기
								MyInfoActivity.myListRec
								.add(new FillMyActivityList(busiNameT,
										busiUnivT, busiTypeT, "4.5("
												+ busiEvaled + "명)",
												"추천(" + busiLikeT + "명)",
												eachImgs[0]));
							} else {
								MyInfoActivity.myListRec
								.add(new FillMyActivityList(busiNameT,
										busiUnivT, busiTypeT, "4.5("
												+ busiEvaled + "명)",
												"추천(" + busiLikeT + "명)",
												busiImgT));
							}
							break;
						}
					}
				}
				MyInfoActivity.busiListView = (ListView) MyInfoActivity.alertDialog
						.findViewById(R.id.busiListView);

				MyInfoActivityAdapter adapter3 = new MyInfoActivityAdapter(
						MyInfoActivity.context,
						R.layout.myinfo_my_rec_eval_list,
						MyInfoActivity.myListRec);
				MyInfoActivity.busiListView.setAdapter(adapter3);

			} catch (Exception e) {
			}
		} else if (DBmanager_type.equals("from_MyInfoActivity_fillList_eval")) {
			try {
				String[] eachEvals;
				String tempEvalIdx;
				Integer tempDbIdx;
				String[] eachImgs;

				String busiNameT;
				String busiUnivT;
				String busiTypeT;
				// String busiScoreT;
				String busiLikeT;
				String busiImgT;
				String busiEvaled;

				MyInfoActivity.myListEval.clear();
				eachEvals = MainActivity.my_info.getUserEvaluate().split(", ");
				for (int k = 0; k < eachEvals.length; k++) {
					tempEvalIdx = eachEvals[k];

					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);
						tempDbIdx = json_data.getInt("businessId");
						busiUnivT = json_data.getString("univ");
						busiTypeT = json_data.getString("type");
						busiNameT = json_data.getString("name");
						// MainActivity.tagR = json_data.getString("tag");
						busiImgT = json_data.getString("img");
						busiLikeT = json_data.getString("countrec");
						busiEvaled = json_data.getString("counteval");

						if (tempDbIdx.toString().equals(tempEvalIdx)) {
							if (busiImgT.contains(",")) {
								eachImgs = busiImgT.split(", "); // ', '를 구분자로
								// 쪼개기
								MyInfoActivity.myListEval
								.add(new FillMyActivityList(busiNameT,
										busiUnivT, busiTypeT, "4.5("
												+ busiEvaled + "명)",
												"추천(" + busiLikeT + "명)",
												eachImgs[0]));
							} else {
								MyInfoActivity.myListEval
								.add(new FillMyActivityList(busiNameT,
										busiUnivT, busiTypeT, "4.5("
												+ busiEvaled + "명)",
												"추천(" + busiLikeT + "명)",
												busiImgT));
							}

							break;
						}
					}
				}

				MyInfoActivity.busiListView = (ListView) MyInfoActivity.alertDialog
						.findViewById(R.id.busiListView);

				// 타이틀 이미지 바꿔주기
				MyInfoActivity.myRecTitle = (ImageView) MyInfoActivity.alertDialog
						.findViewById(R.id.myRecTitle);
				MyInfoActivity.myRecTitle
				.setImageResource(R.drawable.myevallist);

				MyInfoActivityAdapter adapter4 = new MyInfoActivityAdapter(
						MyInfoActivity.context,
						R.layout.myinfo_my_rec_eval_list,
						MyInfoActivity.myListEval);
				MyInfoActivity.busiListView.setAdapter(adapter4);

			} catch (Exception e) {
			}
		} else if (DBmanager_type
				.equals("from_MyInfoActivity_fillList_fr_eval")) {
			try {
				String[] eachEvals;
				String tempEvalIdx;
				Integer tempDbIdx;
				String[] eachImgs;

				String busiNameT;
				String busiUnivT;
				String busiTypeT;
				// String busiScoreT;
				String busiLikeT;
				String busiImgT;
				String busiEvaled;

				MyInfoActivity.myListFrEval.clear();

				eachEvals = MyInfoActivity.thisFrEval;
				for (int k = 0; k < eachEvals.length; k++) {
					tempEvalIdx = eachEvals[k];

					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);
						tempDbIdx = json_data.getInt("businessId");
						busiUnivT = json_data.getString("univ");
						busiTypeT = json_data.getString("type");
						busiNameT = json_data.getString("name");
						// MainActivity.tagR = json_data.getString("tag");
						busiImgT = json_data.getString("img");
						busiLikeT = json_data.getString("countrec");
						busiEvaled = json_data.getString("counteval");

						if (tempDbIdx.toString().equals(tempEvalIdx)) {
							if (busiImgT.contains(",")) {
								eachImgs = busiImgT.split(", "); // ', '를 구분자로
								// 쪼개기
								MyInfoActivity.myListFrEval
								.add(new FillMyActivityList(busiNameT,
										busiUnivT, busiTypeT, "4.5("
												+ busiEvaled + "명)",
												"추천(" + busiLikeT + "명)",
												eachImgs[0]));
							} else {
								MyInfoActivity.myListFrEval
								.add(new FillMyActivityList(busiNameT,
										busiUnivT, busiTypeT, "4.5("
												+ busiEvaled + "명)",
												"추천(" + busiLikeT + "명)",
												busiImgT));
							}

							break;
						}
					}
				}
				MyInfoActivity.busiListView = (ListView) MyInfoActivity.alertDialog
						.findViewById(R.id.busiListView);

				MyInfoActivity.myRecTitle = (ImageView) MyInfoActivity.alertDialog
						.findViewById(R.id.myRecTitle);
				MyInfoActivity.myRecTitle
				.setImageResource(R.drawable.frevallist);

				MyInfoActivityAdapter adapter6 = new MyInfoActivityAdapter(
						MyInfoActivity.context,
						R.layout.myinfo_my_rec_eval_list,
						MyInfoActivity.myListFrEval);
				MyInfoActivity.busiListView.setAdapter(adapter6);

			} catch (Exception e) {
			}
		} else if (DBmanager_type.equals("from_MyInfoActivity_fillList_fr_rec")) {
			try {
				String[] eachRecs;
				String tempRecIdx;
				Integer tempDbIdx;
				String[] eachImgs;

				String busiNameT;
				String busiUnivT;
				String busiTypeT;
				// String busiScoreT;
				String busiLikeT;
				String busiImgT;
				String busiEvaled;

				MyInfoActivity.myListFrRec.clear();

				eachRecs = MyInfoActivity.thisFrRec;
				for (int k = 0; k < eachRecs.length; k++) {
					tempRecIdx = eachRecs[k];

					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);
						tempDbIdx = json_data.getInt("businessId");
						busiUnivT = json_data.getString("univ");
						busiTypeT = json_data.getString("type");
						busiNameT = json_data.getString("name");
						// MainActivity.tagR = json_data.getString("tag");
						busiImgT = json_data.getString("img");
						busiLikeT = json_data.getString("countrec");
						busiEvaled = json_data.getString("counteval");

						if (tempDbIdx.toString().equals(tempRecIdx)) {
							if (busiImgT.contains(",")) {
								eachImgs = busiImgT.split(", "); // ', '를 구분자로
								// 쪼개기
								MyInfoActivity.myListFrRec
								.add(new FillMyActivityList(busiNameT,
										busiUnivT, busiTypeT, "4.5("
												+ busiEvaled + "명)",
												"추천(" + busiLikeT + "명)",
												eachImgs[0]));
							} else {
								MyInfoActivity.myListFrRec
								.add(new FillMyActivityList(busiNameT,
										busiUnivT, busiTypeT, "4.5("
												+ busiEvaled + "명)",
												"추천(" + busiLikeT + "명)",
												busiImgT));
							}

							break;
						}
					}
				}

				MyInfoActivity.busiListView = (ListView) MyInfoActivity.alertDialog
						.findViewById(R.id.busiListView);

				MyInfoActivity.myRecTitle = (ImageView) MyInfoActivity.alertDialog
						.findViewById(R.id.myRecTitle);
				MyInfoActivity.myRecTitle
				.setImageResource(R.drawable.frreclist);

				MyInfoActivityAdapter adapter5 = new MyInfoActivityAdapter(
						MyInfoActivity.context,
						R.layout.myinfo_my_rec_eval_list,
						MyInfoActivity.myListFrRec);
				MyInfoActivity.busiListView.setAdapter(adapter5);

			} catch (Exception e) {
			}
		} else if (DBmanager_type
				.equals("from_NoticeNoticeActivity_fillNoticeList")) {
			try {

				String noticeDateDB;
				String noticeTextDB;
				String[] noticeTextDBs;
				// String realText;

				// 공지사항 리스트 초기화
				NoticeNoticeActivity.originNoticeList.clear();

				for (int i = 0; i < result.length(); i++) {
					// realText = "";
					JSONObject json_data = result.getJSONObject(i);

					noticeDateDB = json_data.getString("noticeDate");
					noticeTextDB = json_data.getString("noticeText");

					noticeTextDBs = noticeTextDB.split("en");

					if (noticeTextDB.contains("en")) {
						if (noticeTextDBs.length == 2) {
							NoticeNoticeActivity.originNoticeList
							.add(new NoticeNoticeFillList(noticeDateDB,
									"\n" + noticeTextDBs[0] + "\n"
											+ noticeTextDBs[1]));
						} else if (noticeTextDBs.length == 3) {
							NoticeNoticeActivity.originNoticeList
							.add(new NoticeNoticeFillList(noticeDateDB,
									"\n" + noticeTextDBs[0] + "\n"
											+ noticeTextDBs[1] + "\n"
											+ noticeTextDBs[2]));
						}
					} else {
						NoticeNoticeActivity.originNoticeList
						.add(new NoticeNoticeFillList(noticeDateDB,
								"\n" + noticeTextDB));
					}
				}
				/*
				 * MyInfoActivity.busiListView = (ListView)
				 * MyInfoActivity.alertDialog .findViewById(R.id.busiListView);
				 */

				// 정렬
				Collections.sort(NoticeNoticeActivity.originNoticeList,
						new Comparator<NoticeNoticeFillList>() {
					@Override
					public int compare(NoticeNoticeFillList o1,
							NoticeNoticeFillList o2) {

						if (o1.getDate().equals(o2.getDate()))
							return 0;
						else if (o1.getDate().compareTo(o2.getDate()) < 0)
							return 1;
						else
							return -1;
					}
				});

				// 리스트뷰의 경계선을 파란색으로
				NoticeNoticeActivity.noticeListView
				.setDivider(new ColorDrawable(Color
						.parseColor("#FFA9A9A9")));
				// 경계선의 굵기를 3px
				NoticeNoticeActivity.noticeListView.setDividerHeight(3);

				NoticeNoticeAdapter adapter = new NoticeNoticeAdapter(
						NoticeNoticeActivity.context,
						R.layout.notice_notice_activity_list,
						NoticeNoticeActivity.originNoticeList);

				NoticeNoticeActivity.noticeListView.setAdapter(adapter);

			} catch (Exception e) {
			}
		}  else if (DBmanager_type
				.equals("from_ClickBusiness_fillReviewList")) {
			try {

				ClickBusiness.typeReview.setText("");

				Integer reviewIdDB;
				String reviewBusiIdDB;
				String writerIdDB;
				String writerNameDB;
				String reviewDateDB;
				String reviewTextDB;
				Integer numOfLikeDB;
				Integer numOfCommentDB;

				// 리뷰 리스트 초기화
				ClickBusiness.originReviewList.clear();

				for (int i = 0; i < result.length(); i++) {
					JSONObject json_data = result.getJSONObject(i);

					reviewIdDB = json_data.getInt("reviewId");
					reviewBusiIdDB = json_data.getString("reviewBusiId");
					writerIdDB = json_data.getString("writerId");
					writerNameDB = json_data.getString("writerName");					
					reviewDateDB = json_data.getString("reviewDate");
					reviewTextDB = json_data.getString("reviewText");
					numOfLikeDB = json_data.getInt("numOfLike");
					numOfCommentDB = json_data.getInt("numOfComment");

					//Log.i("sangmin", reviewIdDB + "/" + reviewBusiIdDB + "/" + writerIdDB + "/" + writerNameDB + "/" + reviewDateDB + "/" + reviewTextDB + "/" + numOfLikeDB + "/" + numOfCommentDB);
					if (ClickBusiness.sel_id.equals(reviewBusiIdDB)) {
						//Log.i("sangmin", "찾음");
						ClickBusiness.originReviewList
						.add(new ClickBusinessReviewFillList(reviewIdDB, reviewBusiIdDB, writerIdDB, writerNameDB, reviewDateDB, reviewTextDB, numOfLikeDB, numOfCommentDB));
					}
				}

				// 정렬
				Collections.sort(ClickBusiness.originReviewList,
						new Comparator<ClickBusinessReviewFillList>() {
					@Override
					public int compare(ClickBusinessReviewFillList o1,
							ClickBusinessReviewFillList o2) {

						if (o1.getReviewDate().equals(o2.getReviewDate()))
							return 0;
						else if (o1.getReviewDate().compareTo(o2.getReviewDate()) < 0)
							return 1;
						else
							return -1;
					}
				});

				// 리스트뷰의 경계선을 파란색으로
				ClickBusiness.reviewListView
				.setDivider(new ColorDrawable(Color
						.parseColor("#FFA9A9A9")));
				// 경계선의 굵기를 3px
				ClickBusiness.reviewListView.setDividerHeight(3);

				ClickBusinessReviewAdapter adapterR = new ClickBusinessReviewAdapter(
						ClickBusiness.context,
						R.layout.click_business_review_list,
						ClickBusiness.originReviewList);

				// 리스트뷰를 어댑터로 뿌려준다
				ClickBusiness.reviewListView.setAdapter(adapterR);
				// 리스트뷰의 사이즈를 조절한다
				ClickBusiness.listViewHeightSet(adapterR, ClickBusiness.reviewListView);
				// 등록된 리뷰의 개수를 채워줌
				ClickBusiness.reviewTitle.setText("업소에 대한 리뷰(" + adapterR.getCount() + "개)");

			} catch (Exception e) {
			}
		} else if (DBmanager_type
				.equals("from_ClickBusinessReviewClicked_fillCommentList")) {
			try {

				ClickBusinessReviewClicked.typeComment.setText("");

				Integer commentIdDB;
				String reviewIdDB;
				String cWriterIdDB;
				String cWriterNameDB;
				String commentDateDB;
				String commentTextDB;
				Integer cntComment = 0;

				// 리뷰 리스트 초기화
				ClickBusinessReviewClicked.originCommentList.clear();

				for (int i = 0; i < result.length(); i++) {
					JSONObject json_data = result.getJSONObject(i);

					commentIdDB = json_data.getInt("commentId");
					reviewIdDB = json_data.getString("reviewId");
					cWriterIdDB = json_data.getString("cWriterId");
					cWriterNameDB = json_data.getString("cWriterName");					
					commentDateDB = json_data.getString("commentDate");
					commentTextDB = json_data.getString("commentText");

					if ((ClickBusinessReviewClicked.cReviewId - Integer.parseInt(reviewIdDB)) == 0) {
						cntComment++;
						ClickBusinessReviewClicked.originCommentList
						.add(new ClickBusinessCommentFillList(commentIdDB, reviewIdDB, cWriterIdDB, cWriterNameDB, commentDateDB, commentTextDB));
					}
				}
				ClickBusinessReviewClicked.cNumOfComment = cntComment;
				ClickBusinessReviewClicked.numOfComment.setText("댓글 " + cntComment);

				// 정렬
				Collections.sort(ClickBusinessReviewClicked.originCommentList,
						new Comparator<ClickBusinessCommentFillList>() {
					@Override
					public int compare(ClickBusinessCommentFillList o1,
							ClickBusinessCommentFillList o2) {

						if (o1.getCommentId().equals(o2.getCommentId()))
							return 0;
						else if (o1.getCommentId().compareTo(o2.getCommentId()) < 0)
							return -1;
						else
							return 1;
					}
				});

				// 리스트뷰의 경계선을 파란색으로
				ClickBusinessReviewClicked.commentListView
				.setDivider(new ColorDrawable(Color
						.parseColor("#FFA9A9A9")));
				// 경계선의 굵기를 3px
				ClickBusinessReviewClicked.commentListView.setDividerHeight(3);

				ClickBusinessCommentAdapter adapterC = new ClickBusinessCommentAdapter(
						ClickBusinessReviewClicked.context,
						R.layout.click_business_comment_list,
						ClickBusinessReviewClicked.originCommentList);

				// 리스트뷰를 어댑터로 뿌려준다
				ClickBusinessReviewClicked.commentListView.setAdapter(adapterC);
				// 리스트뷰의 사이즈를 조절한다
				ClickBusinessReviewClicked.listViewHeightSet(adapterC, ClickBusinessReviewClicked.commentListView);
				// 등록된 댓글의 개수를 채워줌
				ClickBusinessReviewClicked.commentTitle.setText("리뷰에 대한 댓글(" + adapterC.getCount() + "개)");
				ClickBusinessReviewClicked.commentTitle.setTypeface(MainActivity.jangmeFont);

			} catch (Exception e) {
			}
		} else if (DBmanager_type
				.equals("from_ClickBusinessReviewClicked_checkLike")) {
			try {				
				Integer reviewIdDB;
				String tempThisIds;
				int k = 0;

				for (int i = 0; i < result.length(); i++) {
					JSONObject json_data = result.getJSONObject(i);

					reviewIdDB = json_data.getInt("reviewId");
					tempThisIds = json_data.getString("likeThisIds");					

					if ( (ClickBusinessReviewClicked.cReviewId - reviewIdDB) == 0) {
						ClickBusinessReviewClicked.likeThisIds = tempThisIds;
						ClickBusinessReviewClicked.arrLikeThisIds = ClickBusinessReviewClicked.likeThisIds.split(", ");

						// 이 리뷰를 좋아요 눌렀었는지 확인
						// 눌렀었으면 dup : true
						for (k = 0; k < ClickBusinessReviewClicked.arrLikeThisIds.length; k++) {
							if (ClickBusinessReviewClicked.arrLikeThisIds[k].equals( MainActivity.my_info.getUserId() )) {
								ClickBusinessReviewClicked.duplicated = true;
								break;
							}
						}
						if (k == ClickBusinessReviewClicked.arrLikeThisIds.length) {
							ClickBusinessReviewClicked.duplicated = false;
						}
						break;
					}
				}

			} catch (Exception e) {
			}
		} else if (DBmanager_type
				.equals("from_MainActivity_myRecommend_and_myEvaluate")) {
			try {
				for (int i = 0; i < result.length(); i++) {

					JSONObject json_data = result.getJSONObject(i);
					MainActivity.userID_in_DB = json_data.getString("userId");
					priorityDb = json_data.getString("priority");
					userUniv = json_data.getString("myUniv"); // 0208 상민

					if (MainActivity.userID_in_DB.toString().equals(
							MainActivity.my_info.getUserId())) {
						MainActivity.userRec_in_DB = json_data
								.getString("recommended");
						MainActivity.userEvaluate_in_DB = json_data
								.getString("evaluated");
						MainActivity.my_info
						.setUserRec(MainActivity.userRec_in_DB);
						MainActivity.my_info.setPriority(Double
								.parseDouble(priorityDb));
						MainActivity.my_info
						.setUserEvaluate(MainActivity.userEvaluate_in_DB);
						MainActivity.my_info.setUserUniv(userUniv); // 0208 상민
					}
				}

			} catch (Exception e) {
			}
		} else if (DBmanager_type.equals("from_MainActivity_allBusiness")) {
			try {
				for (int i = 0; i < result.length(); i++) {
					JSONObject json_data = result.getJSONObject(i);
					MainActivity.idR = json_data.getInt("businessId");
					MainActivity.univR = json_data.getString("univ");
					MainActivity.typeR = json_data.getString("type");
					MainActivity.nameR = json_data.getString("name");
					MainActivity.tagR = json_data.getString("tag");
					MainActivity.imgR = json_data.getString("img");

					if (MainActivity.idR.toString().equals(
							MainActivity.findRecs[MainActivity.k])) {
						MainActivity.findUniv = MainActivity.univR;
						MainActivity.findType = MainActivity.typeR;
						MainActivity.findName = MainActivity.nameR;
						MainActivity.findTag = MainActivity.tagR;
						MainActivity.findImg = MainActivity.imgR;
					}
				}

			} catch (Exception e) {
			}
		} else if (DBmanager_type.equals("from_ClickBusiness")) {
			String busi_ex = ""; // 업소에 대한 설명을 디비에서 가져옴
			String searchDomain = ""; // 업소의 네이버 검색 도메인을 디비에서 가져옴

			try {
				for (int i = 0; i < result.length(); i++) {
					JSONObject json_data = result.getJSONObject(i);
					ClickBusiness.busi_id = json_data.getString("businessId");
					ClickBusiness.busi_add = json_data
							.getString("busi_address");
					ClickBusiness.imgURL = json_data.getString("img");
					searchDomain = json_data.getString("domain"); // 네이버 검색 주소
					busi_ex = json_data.getString("explanation"); // 업소 설명
					ClickBusiness.countEval = json_data.getString("counteval"); // 평가한
					// 사람수
					ClickBusiness.countRec = json_data.getString("countrec"); // 추천한
					// 사람수
					ClickBusiness.totalScore = json_data.getString("totalsco"); // 평가
					// 점수
					// 합산

					// DB에서 해당 업소정보들을 세팅해준다
					if (ClickBusiness.busi_id.equals(ClickBusiness.sel_id)) {
						ClickBusiness.aboutAddress
						.setText(ClickBusiness.busi_add); // 업소 주소 채우기
						ClickBusiness.domainAddress = searchDomain; // 네이버 검색 주소
						// 저장

						// DB에 업소에 대한 상세설명이 있는지 없는지
						if (busi_ex.equals("")) {
							ClickBusiness.aboutInfo
							.setText("(업소에 대한 설명을 추가해 주세요!)");
						} else {
							ClickBusiness.aboutInfo.setText(busi_ex);
						}

						// 변수 초기화
						ClickBusiness.numOfImg = 1;
						ClickBusiness.indexOfCurrentImg = 1;
						// 업소 이미지가 2개 이상인 경우
						if (ClickBusiness.imgURL.contains(",")) {
							// 이미지의 개수를 구하고, "imgURLs"에 이미지 주소를 잘라서 저장.
							for (int k = 0; k < ClickBusiness.imgURL.length(); k++) {
								if (ClickBusiness.imgURL.charAt(k) != ',') {
									continue;
								}
								ClickBusiness.numOfImg++;
							}
							ClickBusiness.imgURLs = ClickBusiness.imgURL
									.split(", "); // ', '를 구분자로 쪼개기
							// 첫번째 이미지 보여주기
							ClickBusiness.aboutPic
							.loadUrl("http://sangmin9655.dothome.co.kr/files/"
									+ ClickBusiness.imgURLs[0]);

							// 페이스북에 글 올리는 사진
							ClickBusiness.feedPicture = "http://sangmin9655.dothome.co.kr/files/"
									+ ClickBusiness.imgURLs[0];
						} else {
							// 업소 이미지가 1개인 경우
							ClickBusiness.aboutPic
							.loadUrl("http://sangmin9655.dothome.co.kr/files/"
									+ ClickBusiness.imgURL);

							// 페이스북에 글 올리는 사진
							ClickBusiness.feedPicture = "http://sangmin9655.dothome.co.kr/files/"
									+ ClickBusiness.imgURLs;
						}
						ClickBusiness.aboutPic.setInitialScale(50); // 35%
						ClickBusiness.aboutPic.getSettings()
						.setUseWideViewPort(true);
						ClickBusiness.aboutPic.setFocusable(false);
						ClickBusiness.aboutPic.setClickable(false);
						ClickBusiness.aboutPic.setLongClickable(false);
						ClickBusiness.aboutPic.setFocusableInTouchMode(false);
						ClickBusiness.aboutPic.getSettings().setSupportZoom(
								true); // 줌 기능
						ClickBusiness.aboutPic.getSettings()
						.setBuiltInZoomControls(true); // 줌 기능
						ClickBusiness.aboutPic.getSettings()
						.setUseWideViewPort(true); // 줌기능
						ClickBusiness.aboutPic.getSettings()
						.setLayoutAlgorithm(
								LayoutAlgorithm.SINGLE_COLUMN); // 화면 크기
						// 맞추기
						break;
					}
				}
			} catch (Exception e) {
			}
		}

		else if (DBmanager_type
				.equals("from_FillMainList_gettingStoreInfoForMainList")) {

			ArrayList<Store> resultStoreList_Obj = new ArrayList<Store>();
			resultStoreList_Obj = FillMainList.resultStoreList_Obj;
			ArrayList<String> calculatedStringDataList_forFillMain = new ArrayList<String>();

			int count = 0;
			try {
				for (int i = 0; i < result.length(); i++) {
					JSONObject json_data = result.getJSONObject(i);
					// MainActivity.idR = json_data.getInt("businessId");
					// MainActivity.univR = json_data.getString("univ");
					// MainActivity.typeR = json_data.getString("type");
					// MainActivity.nameR = json_data.getString("name");
					// MainActivity.tagR = json_data.getString("tag");
					// MainActivity.imgR = json_data.getString("img");

					for (Store resultStore : resultStoreList_Obj) {

						if (Integer.toString(json_data.getInt("businessId"))
								.equals(resultStore.getStoreId())) {
							resultStore.setStoreName(json_data
									.getString("name"));
							resultStore.setStoreAddress(json_data
									.getString("busi_address"));
							resultStore.setStoreType(json_data
									.getString("type"));
							resultStore.setStoreUniversity(json_data
									.getString("univ"));
							resultStore.setStoreTag(json_data.getString("tag"));
							resultStore.setStoreImgURL(json_data
									.getString("img"));
							count++;// 이건로그확인용. 걍지워도
						}
					}
				}

				// ArrayList<String> temp_resultStore_arrList = new
				// ArrayList<String>();
				Weather weather_data[] = new Weather[resultStoreList_Obj.size()];
				int index = 0;

				// 객체로 정렬되어있는 결과업체들을 스트링어레이리스트로 바꿔주는 과정(메인에서 리스트는 스트링arr형태로만
				// 넘겨줘야 하므로..)
				for (Store resultStore : resultStoreList_Obj) {

					calculatedStringDataList_forFillMain.add(resultStore
							.getStoreId()
							+ "/-/"
							+ resultStore.getStoreUniversity()
							+ "/-/"
							+ resultStore.getStoreType()
							+ "/-/"
							+ resultStore.getStoreName()
							+ "/-/"
							+ resultStore.getStoreTags()
							+ "/-/"
							+ resultStore.getPagerank()
							+ "/-/"
							+ resultStore.getStoreImgURL());
					// Log.i("메인친구순차테스트", resultStore.getStoreId() + " / " +
					// resultStore.getPagerank());
					// temp_resultStore_arrList.add(resultStore.getStoreId());
					weather_data[index++] = new Weather(R.drawable.droptop,
							resultStore.getStoreName()
							+ "("
							+ String.format("%.2f",
									resultStore.getPagerank()) + ", "
									+ resultStore.getHowManyReco() + "명)",
									resultStore.getStoreAddress());
					Log.i("웨더확인", weather_data[index - 1].title + "/"
							+ weather_data[index - 1].address);
					resultStore.initPagerank();
					resultStore.initHowManyReco();
				}
				// for(String gogo :calculatedStringDataList_forFillMain)
				// Log.i("뿌잉이잉이이이이ㅣㅇ",gogo);

				// Log.i("ASFSFASFASFAFSAF", weather_data[0].title);
				// MainActivity.weather_data = weather_data;

				MainActivity.calculatedData = calculatedStringDataList_forFillMain;

				// Log.i("weather_data길이몇이냐몇",
				// Integer.toString(weather_data.length));
				Log.i("디비어씽크", "aa");

				/* 여기서부분은 MainActivity에서 온 부분 */
				WeatherAdapter adapter = new WeatherAdapter(this.mainAct,
						R.layout.listview_item_row, weather_data);
				Log.i("디비어씽크", "bb");
				// adapter = new ArrayAdapter<String>(this,
				// R.layout.listview_item_layout_01, storeData);
				// simple_list_item_1는 textview를 담고 있는 xml형태이다.

				// listview 객체에 아답터 객체 연결하기
				MainActivity.listView.setAdapter(adapter);
				// 리스트뷰의 경계선을 파란색으로
				MainActivity.listView.setDivider(new ColorDrawable(Color
						.parseColor("#FF000080")));
				// 경계선의 굵기를 5px
				MainActivity.listView.setDividerHeight(5);
				/* 여기까지 */
				// 이부분 위아래 부분은 FIllMainList에서 온 부분이다
				resultStoreList_Obj.clear();
				Log.i("필메인리스트 다끝났슈", "sdfsdfsadfas");
			} catch (Exception e) {
			}
		} else if (DBmanager_type.equals("from_SearchResult")) {

			SearchResult.data.clear();

			if (SearchResult.tagText.equals("")) {
				try {
					SearchResult.count = 0;
					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);
						SearchResult.idR = json_data.getInt("businessId");
						SearchResult.univR = json_data.getString("univ");
						SearchResult.typeR = json_data.getString("type");
						SearchResult.nameR = json_data.getString("name");
						SearchResult.tagR = json_data.getString("tag");
						SearchResult.imgR = json_data.getString("img");

						if ((SearchResult.univText.equals(SearchResult.univR) || SearchResult.univText
								.equals("모두 선택"))
								&& (SearchResult.typeText
										.equals(SearchResult.typeR) || SearchResult.typeText
										.equals("모두 선택"))
										&& (SearchResult.nameR
												.contains(SearchResult.nameText) || SearchResult.nameText
												.equals(""))
												&& (SearchResult.tagR
														.contains(SearchResult.tagText) || SearchResult.tagText
														.equals(""))) {
							SearchResult.count++;
							// storeId_data.add(Integer.toString(idR));
							SearchResult.data.add(SearchResult.idR + "/-/"
									+ SearchResult.univR + "/-/"
									+ SearchResult.typeR + "/-/"
									+ SearchResult.nameR + "/-/"
									+ SearchResult.tagR + "/-/"
									+ SearchResult.imgR);
						}
					}
					Toast.makeText(null, SearchResult.count + " 곳이 검색 되었습니다.",
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
				}
			} else {
				try {
					SearchResult.count = 0;
					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);
						SearchResult.tagR = json_data.getString("tag");

						if (SearchResult.tagR.contains(SearchResult.tagText)) {
							SearchResult.allTags = SearchResult.allTags + ", "
									+ SearchResult.tagR;
						}
					}
					SearchResult.splitAllTags = SearchResult.allTags
							.split(", ");

					for (int i = 0; i < result.length(); i++) {
						JSONObject json_data = result.getJSONObject(i);
						SearchResult.idR = json_data.getInt("businessId");
						SearchResult.univR = json_data.getString("univ");
						SearchResult.typeR = json_data.getString("type");
						SearchResult.nameR = json_data.getString("name");
						SearchResult.tagR = json_data.getString("tag");
						SearchResult.imgR = json_data.getString("img");

						if ((SearchResult.univText.equals(SearchResult.univR) || SearchResult.univText
								.equals("모두 선택"))
								&& (SearchResult.typeText
										.equals(SearchResult.typeR) || SearchResult.typeText
										.equals("모두 선택"))
										&& (SearchResult.nameR
												.contains(SearchResult.nameText) || SearchResult.nameText
												.equals(""))) {

							for (int k = 0; k < SearchResult.splitAllTags.length; k++) {
								if (SearchResult.tagR
										.contains(SearchResult.splitAllTags[k])) {
									SearchResult.count++;
									// storeId_data.add(Integer.toString(idR));
									SearchResult.data.add(SearchResult.idR
											+ "/-/" + SearchResult.univR
											+ "/-/" + SearchResult.typeR
											+ "/-/" + SearchResult.nameR
											+ "/-/" + SearchResult.tagR + "/-/"
											+ SearchResult.imgR);

									break;
								}
							}
						}
					}
					Toast.makeText(null, SearchResult.count + " 곳이 검색 되었습니다.",
							Toast.LENGTH_SHORT).show();

				} catch (Exception e) {
				}
			}
			SearchResult.mAdapter = new ListViewAdapter(mainAct);

			if (SearchResult.count != 0) {
				SearchResult.gotoAlgorithm = new SearchAlgorithm(
						SearchResult.data);
				//				ArrayList<String> calculatedData = new ArrayList<String>();
				SearchResult.gotoAlgorithm.calculateDataList();
				//				calculatedData = SearchResult.gotoAlgorithm
				//						.getCalculatedDataList();		

				ArrayList<Store> result_storeObjectDataList = new ArrayList<Store>();
				result_storeObjectDataList = SearchResult.gotoAlgorithm.getStoreObjectDataList();					
				//				// // ////////////영남추가부분 ////////////////
				//				SearchResult.Adapter = new ArrayAdapter<String>(
				//						this.gotContext, android.R.layout.simple_list_item_1,
				//						calculatedData);
				//				// // ////////////영남수정부분 ////////////////
				//				// */				
				SearchResult.mAdapter.set_mStoreData(result_storeObjectDataList);
			}
			else {	
				SearchResult.mAdapter.set_emptyData();
			}
			SearchResult.mListView.setAdapter(SearchResult.mAdapter);
		}
	}

	//
	@Override
	protected void onProgressUpdate(JSONArray... values) {
		super.onProgressUpdate(values);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}
}