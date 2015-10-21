package com.swdm.cc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DBmanager {
   String result = "";
   ArrayList<NameValuePair> nameValuePairs;

   public DBmanager() {
      nameValuePairs = new ArrayList<NameValuePair>();
   }

   ///////////////////////////////////////////////////////////
   // 유저 검색
   public JSONArray searchUser(String sql) {
	      InputStream is = null;
	      nameValuePairs.add(new BasicNameValuePair("sql", sql));

	      try {
	         HttpClient httpclient = new DefaultHttpClient();
	         HttpPost httppost = new HttpPost(
	               "http://sangmin9655.dothome.co.kr/getter.php");
	         httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
	               "euc-kr"));
	         HttpResponse response = httpclient.execute(httppost);
	         HttpEntity entity = response.getEntity();
	         is = entity.getContent();
	      } catch (Exception e) {
	         Log.e("log_tag", "Error in http connection " + e.toString());
	      }

	      // convert response to string
	      try {
	         BufferedReader reader = new BufferedReader(new InputStreamReader(
	               is, "euc-kr"), 8);
	         StringBuilder sb = new StringBuilder();
	         String line = null;
	         while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	         }
	         is.close();
	         result = sb.toString();
	      } catch (Exception e) {
	         Log.e("log_tag", "Error converting result " + e.toString());
	      }

	      // parse json data
	      try {
	         if (result.equals("1\n"))
	            Log.e("DBERROR", "connection error");
	         if (result.equals("2\n"))
	            Log.e("DBERROR", "db selection error");
	         if (result.equals("3\n"))
	            Log.e("DBERROR", "request error");
	         if (result.equals("4\n"))
	            Log.e("DBERROR", "sql query error");

	         else {
	            JSONArray jArray = new JSONArray(result);
	            for (int i = 0; i < jArray.length(); i++) {
	               JSONObject json_data = jArray.getJSONObject(i);

	               //String userIdR = 
	            		   json_data.getString("userId");
	               //String userNameR = 
	               		   json_data.getString("name");
	               //String userSexR = 
	            		   json_data.getString("sex");
	               //String userAgeR = 
	            		   //json_data.getString("age");
	            }	

	            return jArray;
	         }
	      } catch (JSONException e) {
	         Log.e("log_tag", "Error parsing data " + e.toString());
	      }
	      return null;
	   }
   
   
   // 스토어 검색
   public JSONArray search(String sql) {
      InputStream is = null;
      nameValuePairs.add(new BasicNameValuePair("sql", sql));

      try {
         HttpClient httpclient = new DefaultHttpClient();
         HttpPost httppost = new HttpPost(
               "http://sangmin9655.dothome.co.kr/getter.php");
         httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
               "euc-kr"));
         HttpResponse response = httpclient.execute(httppost);
         HttpEntity entity = response.getEntity();
         is = entity.getContent();
      } catch (Exception e) {
         Log.e("log_tag", "Error in http connection " + e.toString());
      }

      // convert response to string
      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(
               is, "euc-kr"), 8);
         StringBuilder sb = new StringBuilder();
         String line = null;
         while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
         }
         is.close();
         result = sb.toString();
      } catch (Exception e) {
         Log.e("log_tag", "Error converting result " + e.toString());
      }

      // parse json data
      try {
         if (result.equals("1\n"))
            Log.e("DBERROR", "connection error");
         if (result.equals("2\n"))
            Log.e("DBERROR", "db selection error");
         if (result.equals("3\n"))
            Log.e("DBERROR", "request error");
         if (result.equals("4\n"))
            Log.e("DBERROR", "sql query error");

         else {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
               JSONObject json_data = jArray.getJSONObject(i);

               /*String univR = json_data.getString("univ");
               String typeR = json_data.getString("type");
               String nameR = json_data.getString("name");
               String tagR = json_data.getString("tag");*/
            }
            return jArray;
         }
      } catch (JSONException e) {
         Log.e("log_tag", "Error parsing data " + e.toString());
      }
      return null;
   }

   // delete inseer update ...
   public boolean insert(String sql) {
      InputStream is = null;

      nameValuePairs.add(new BasicNameValuePair("sql", sql));

      try {
         HttpClient httpclient = new DefaultHttpClient();
         HttpPost httppost = new HttpPost(
               "http://sangmin9655.dothome.co.kr/setter.php");
         httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
               "euc-kr"));
         HttpResponse response = httpclient.execute(httppost);
         HttpEntity entity = response.getEntity();
         is = entity.getContent();
      } catch (Exception e) {
         Log.e("log_tag", "Error in http connection " + e.toString());
      }

      // convert response to string
      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(
               is, "euc-kr"), 8);
         StringBuilder sb = new StringBuilder();
         String line = null;
         while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
         }
         is.close();
         result = sb.toString();

      } catch (Exception e) {
         Log.e("log_tag", "Error converting result " + e.toString());
      }

      if (result.equals("1\n"))
         Log.e("DBERROR", "connection error");
      if (result.equals("2\n"))
         Log.e("DBERROR", "db selection error");
      if (result.equals("3\n"))
         Log.e("DBERROR", "request error");
      if (result.equals("4\n"))
         Log.e("DBERROR", "sql query error");
      if (result.equals("5\n")) {
         Log.i("DB", "query success");
         return true;
      }
      return false;
   }
   
   // 디비의 업체 개수 구하는 메소드
   public int calIndex(String sql) {
      InputStream is = null;
      nameValuePairs.add(new BasicNameValuePair("sql", sql));

      try {
         HttpClient httpclient = new DefaultHttpClient();
         HttpPost httppost = new HttpPost(
               "http://sangmin9655.dothome.co.kr/getter.php");
         httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
               "euc-kr"));
         HttpResponse response = httpclient.execute(httppost);
         HttpEntity entity = response.getEntity();
         is = entity.getContent();
      } catch (Exception e) {
         Log.e("log_tag", "Error in http connection " + e.toString());
      }

      // convert response to string
      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(
               is, "euc-kr"), 8);
         StringBuilder sb = new StringBuilder();
         String line = null;
         while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
         }
         is.close();
         result = sb.toString();
      } catch (Exception e) {
         Log.e("log_tag", "Error converting result " + e.toString());
      }

      // parse json data
      try {
         if (result.equals("1\n"))
            Log.e("DBERROR", "connection error");
         if (result.equals("2\n"))
            Log.e("DBERROR", "db selection error");
         if (result.equals("3\n"))
            Log.e("DBERROR", "request error");
         if (result.equals("4\n"))
            Log.e("DBERROR", "sql query error");

         else {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
               JSONObject json_data = jArray.getJSONObject(i);
//
               String univR = json_data.getString("univ");
               String typeR = json_data.getString("type");
               String nameR = json_data.getString("name");
               String tagR = json_data.getString("tag");
            }
            return jArray.length();
         }
      } catch (JSONException e) {
         Log.e("log_tag", "Error parsing data " + e.toString());
      }
      return 0;
   }
}