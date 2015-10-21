package com.swdm.cc;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Insert extends Activity {
	Button upload_img;
	Button insert;
	Button back;

	EditText name;
	EditText tag;
	
	TextView selectUniv;
	TextView selectStoreType;
	TextView insertStorename;
	TextView insertStoreTag;
	Spinner mySpinner1;
	Spinner mySpinner2;

	Integer idx;
	String univText;
	String typeText;
	String nameText;
	String tagText;

	String absolutePath = "";
	String imgfile = "";

	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary = "*****";

	private FileInputStream mFileInputStream = null;
	private URL connectUrl = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert_data);

		//
		final Spinner spinner1 = (Spinner) findViewById(R.id.mySpinner1);
		ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,
				R.array.univ, android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter1);

		//
		final Spinner spinner2 = (Spinner) findViewById(R.id.mySpinner2);
		ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
				R.array.type, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);

		upload_img = (Button) findViewById(R.id.upload_img);
		insert = (Button) findViewById(R.id.insert);
		back = (Button) findViewById(R.id.back);
		name = (EditText) findViewById(R.id.editText1);
		tag = (EditText) findViewById(R.id.editText2);
		
		
		//새로운것들지정
		selectUniv = (TextView)findViewById(R.id.selectUniv);
		selectStoreType= (TextView)findViewById(R.id.selectStoreType);
		insertStorename= (TextView)findViewById(R.id.insertStorename);
		insertStoreTag= (TextView)findViewById(R.id.insertStoreTag);
		
		//글자모양 지정부분
		selectUniv.setTypeface(MainActivity.jangmeFont);
		selectStoreType.setTypeface(MainActivity.jangmeFont);
		insertStorename.setTypeface(MainActivity.jangmeFont);
		insertStoreTag.setTypeface(MainActivity.jangmeFont);
		name.setTypeface(MainActivity.jangmeFont);
		tag.setTypeface(MainActivity.jangmeFont);
		upload_img.setTypeface(MainActivity.jangmeFont);
		insert.setTypeface(MainActivity.jangmeFont);
		back.setTypeface(MainActivity.jangmeFont);
		spinner1.setBackgroundColor(Color.rgb(234, 234, 234));
		spinner2.setBackgroundColor(Color.rgb(234, 234, 234));
		
		//글자색 지정부분
		selectUniv.setTextColor(Color.YELLOW);
		selectStoreType.setTextColor(Color.YELLOW);
		insertStorename.setTextColor(Color.YELLOW);
		insertStoreTag.setTextColor(Color.YELLOW);
		
		//버튼색 지정부분 
		upload_img.setBackgroundColor(Color.rgb(250, 244, 192));
		insert.setBackgroundColor(Color.rgb(250, 244, 192));
		back.setBackgroundColor(Color.rgb(250, 244, 192));
		
		//
		upload_img.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, 0);
			}
		});

		//
		insert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				univText = spinner1.getSelectedItem().toString();
				typeText = spinner2.getSelectedItem().toString();
				nameText = name.getText().toString();
				tagText = tag.getText().toString();

				// //////////////////////////////////////////////////////////////////////////
				//
				// Store addNewStore = new Store(nameText, univText, typeText,
				// tagText);
				// MainActivity.stores.add(addNewStore);
				//
				// String data = univText + " " + typeText + " " + nameText +
				// " "
				// + tagText;
				// MainActivity.storeData.add(data);

				// //////////////////////////////////////////////////////////////////////////

				/*
				 * index++; indexText = index.toString();
				 */
				if (univText.equals("---------")
						|| typeText.equals("---------") || nameText.equals("")) {
					Toast.makeText(getApplicationContext(), "정보를 모두 채워주세요!",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							getApplicationContext(),
							univText + " " + typeText + " " + nameText + " "
									+ tagText + "  ", Toast.LENGTH_SHORT)
							.show();
					/*
					 * DBThread dbt = new DBThread(); dbt.start();
					 */
					UPAsynkTask upa = new UPAsynkTask();
					upa.execute(0);
				}
			}
		});

		// "뒤로가기" 버튼을 클릭하였을 때
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				finish();
			}
		});
	}

	//
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		Uri selPhotoUri = intent.getData();
		System.out.println(selPhotoUri);
		try {
			Bitmap selPhoto = Images.Media.getBitmap(getContentResolver(),
					selPhotoUri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//
		Cursor c = getContentResolver().query(
				Uri.parse(selPhotoUri.toString()), null, null, null, null);
		c.moveToNext();
		System.out.println(c);
		absolutePath = c.getString(c
				.getColumnIndex(MediaStore.MediaColumns.DATA));
		int start = absolutePath.lastIndexOf("/");
		imgfile = absolutePath.substring(start + 1, absolutePath.length());
		System.out.println("  = " + imgfile);
	}// onActivityResult

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

			//
			if (!absolutePath.equals("")) {
				//
				String urlString = "http://sangmin9655.dothome.co.kr/upload.php";
				//
				DoFileUpload(urlString, absolutePath);
			} else {

			}
			DBmanager db = new DBmanager();
			idx = db.calIndex("select * from business_info") + 1; //

			boolean result = db
					.insert("insert into business_info(businessId, univ, type, name, tag, img) values('"
							+ idx
							+ "', '"
							+ univText
							+ "', '"
							+ typeText
							+ "', '"
							+ nameText
							+ "','"
							+ tagText
							+ "', '"
							+ imgfile + "')");

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

		//
		public void DoFileUpload(String apiUrl, String absolutePath) {
			HttpFileUpload(apiUrl, "", absolutePath);

		}// DoFileUpload

		public void HttpFileUpload(String urlString, String params,
				String fileName) {
			try {

				mFileInputStream = new FileInputStream(fileName);
				connectUrl = new URL(urlString);
				Log.d("Test", "mFileInputStream  is " + mFileInputStream);

				// open connection
				HttpURLConnection conn = (HttpURLConnection) connectUrl
						.openConnection();
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);

				// write data

				DataOutputStream dos = new DataOutputStream(
						conn.getOutputStream());

				for (int i = 0; i < 1; i++) {

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\""
							+ "id" + "\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes("abcde");
					dos.writeBytes(lineEnd);

				}// for

				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
						+ fileName + "\"" + lineEnd);
				dos.writeBytes(lineEnd);

				int bytesAvailable = mFileInputStream.available();
				int maxBufferSize = 1024;
				int bufferSize = Math.min(bytesAvailable, maxBufferSize);

				byte[] buffer = new byte[bufferSize];
				int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

				Log.d("Test", "image byte is " + bytesRead);

				// read image
				while (bytesRead > 0) {
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = mFileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
				}

				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// close streams
				Log.e("Test", "File is written");
				mFileInputStream.close();
				dos.flush(); // finish upload...

				// get response
				int ch;
				InputStream is = conn.getInputStream();
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				String s = b.toString();
				Log.e("Test", "result = " + s);

				dos.close();

			} catch (Exception e) {
				System.out.println(e);
				System.out.println(e.getMessage());
				Log.e("Test", "exception " + e.getMessage());
				// TODO: handle exception
			}
		}
	}
}

/*
 * class DBThread extends Thread { public void run() { DBmanager db = new
 * DBmanager();
 * 
 * db.requestQuery("select * from users"); } }
 */