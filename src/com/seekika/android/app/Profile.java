package com.seekika.android.app;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seekika.android.app.constants.SeekikaConstants;
import com.seekika.android.app.helpers.RestClient;
import com.seekika.android.app.helpers.RestClient.RequestMethod;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Profile extends Activity {
	
	TextView mName;
	TextView mEmail;
	TextView mStories;
	TextView mFollowers;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        ProfileTask profileTask=new ProfileTask();
        profileTask.appContext=this;
        SharedPreferences prefs = Prefs.get(this);
        String userKey=prefs.getString("userKey", null);
       
        profileTask.execute(userKey);
    }
    
    public void populateView(String name,String stories,String followers){
    	mName=(TextView)findViewById(R.id.name);
    	mEmail=(TextView)findViewById(R.id.email);
    	mStories=(TextView)findViewById(R.id.stories);
    	mFollowers=(TextView)findViewById(R.id.followers);
    	if(name!=null){
    		mName.setText(name);
    	}
    	if(stories!=null){
    		mStories.setText(stories + " Published Story(s)");
    	}
    	if(followers!=null){
    		mFollowers.setText(followers + " Follower(s)");
    	}
    }
    
    private class ProfileTask extends AsyncTask<String, String, String> {
    	private ProgressDialog mProgressDialog;
    	protected Context appContext;
    	
    	@Override
    	protected void onPreExecute(){
    		this.mProgressDialog=new ProgressDialog(appContext); 
    		this.mProgressDialog.setTitle("");
    		this.mProgressDialog.setMessage("Loading profile");
    		this.mProgressDialog.show();
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			
			return retrieveProfile(params[0]);
		}
		
		@Override
		protected void onPostExecute(String result) {
			this.mProgressDialog.dismiss();
			try{
				JSONObject jsonObj = new JSONObject();
				JSONArray aryJSONStrings = new JSONArray(result);
				int i=0;
				String result_message=aryJSONStrings.getJSONObject(i).getString("message");
				String name=aryJSONStrings.getJSONObject(i).getString("name");
				String stories=aryJSONStrings.getJSONObject(i).getString("stories");
				String followers=aryJSONStrings.getJSONObject(i).getString("followers");
				if(result_message.equalsIgnoreCase("success")){
					populateView(name,stories,followers);
					Log.i("Profile","success");
				}else{
					Log.i("Profile","fail");
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		
		private String retrieveProfile(String userKey){
			RestClient client=new RestClient(SeekikaConstants.PROFILE_URL);
			client.AddParam("userkey", userKey);
			try{
				client.Execute(RequestMethod.GET);
			}catch(Exception e){
				e.printStackTrace();
			}
			String response=client.getResponse();
			return response;
			
		}

		
    	
    }

	
}
