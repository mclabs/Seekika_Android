package com.seekika.android.app.tasks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seekika.android.app.constants.SeekikaConstants;
import com.seekika.android.app.helpers.RestClient;
import com.seekika.android.app.helpers.RestClient.RequestMethod;

import android.R;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class ActivityFeedTask extends AsyncTask<String, String, String> {

	 private ProgressDialog dialog;
	 private ListActivity activity;
	 private Context context;
	 
	public ActivityFeedTask(ListActivity activity){
		this.activity=activity;
		context=activity;
		dialog = new ProgressDialog(context);
		
	}
	
	@Override
    protected void onPreExecute(){
        this.dialog.setMessage("Fetching activity feed");
        this.dialog.show();
    }
	    
	@Override
	protected String doInBackground(String... params) {
		
		return retrieveFeed(params[0]);
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		String [] activity={};
		if (dialog.isShowing())
            dialog.dismiss();
		if(result!=null){
			try{
				JSONObject jsonObj = new JSONObject();
				JSONArray aryJSONStrings = new JSONArray(result);				//int i=0;
				for(int i=0;i<aryJSONStrings.length();i++){
					activity[i]=aryJSONStrings.getJSONObject(i).getString("activity_description");
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			
			try{
				
	            Toast.makeText(context, "Data loaded succesfully", Toast.LENGTH_LONG).show();
			}catch (Exception e) {
	            Toast.makeText(context, "Failed reading online ressource", Toast.LENGTH_LONG).show();
	        }
		}else{
			
		}
		
	}
	
	private String retrieveFeed(String userKey){
		RestClient client=new RestClient(SeekikaConstants.FEED_URL);
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
