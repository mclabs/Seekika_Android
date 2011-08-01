package com.seekika.android.app;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seekika.android.app.constants.SeekikaConstants;
import com.seekika.android.app.helpers.RestClient;
import com.seekika.android.app.helpers.RestClient.RequestMethod;
import com.seekika.android.app.models.Activity;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityFeed extends ListActivity {
	private SharedPreferences prefs;
	private ActivityFeedTask activityFeedTask;
	private static final String TAG="ActivityFeed";
	private Activity activity;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_main);
		prefs = Prefs.get(this);
		String userKey=prefs.getString("userKey", null);
		activityFeedTask=new ActivityFeedTask();
		activityFeedTask.appContext=this;
		activityFeedTask.execute(userKey);
		
		
		
	}
	
	
	
	public void populateView(ArrayList arrayList){
		setListAdapter(new ActivityListAdaptor(  
				ActivityFeed.this, R.layout.list_item, arrayList));  
		
		//activityAdapter=new ArrayAdapter<String>(ActivityFeed.this, R.layout.feeds_list_text, args);
		
		
		//setListAdapter(activityAdapter);
		//adapter.notifyDataSetChanged();
		//ListView lv = getListView();
		//lv.setTextFilterEnabled(true);
	}
	
	private class ActivityListAdaptor extends ArrayAdapter<Activity> {
        private ArrayList<Activity> feeds;
        public ActivityListAdaptor(Context context,
                                    int textViewResourceId,
                                    ArrayList<Activity> items) {
                 super(context, textViewResourceId, items);
                 this.feeds = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                       LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        v = vi.inflate(R.layout.list_item, null);
                }
                Activity a = feeds.get(position);
                TextView tt = (TextView) v.findViewById(R.id.title);
                
                tt.setText(a.activityDescription);
               
                return v;
        }
}
	
	public class ActivityFeedTask extends AsyncTask<String, String, String> {
		private ProgressDialog mProgressDialog;
		protected Context appContext;
		
		@Override
    	protected void onPreExecute(){
    		this.mProgressDialog=new ProgressDialog(appContext); 
    		this.mProgressDialog.setTitle("");
    		this.mProgressDialog.setMessage("Loading feed");
    		this.mProgressDialog.show();
    	}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			String [] ACTIVITY=null;
			ArrayList<Activity> activityList = new ArrayList<Activity>(); 
			

			if (mProgressDialog.isShowing())
				mProgressDialog.dismiss();
			if(result!=null){
				Log.i(TAG,"result from feed " + result);
				try{
					JSONObject jsonObj = new JSONObject();
					JSONArray aryJSONStrings = new JSONArray(result);				
					Log.i(TAG,"length of results " + aryJSONStrings.length());
					for(int i=0;i<aryJSONStrings.length();i++){
						Log.i(TAG,"activity_description " + aryJSONStrings.getJSONObject(i).getString("activity_description"));
						Activity activity=new Activity();
						activity.activityDescription=aryJSONStrings.getJSONObject(i).getString("activity_description");
						activityList.add(activity);
						
						//ACTIVITY[i]=aryJSONStrings.getJSONObject(i).getString("activity_description");
						//al.add(aryJSONStrings.getJSONObject(i).getString("activity_description"));
						populateView(activityList);
						//populateView2(ACTIVITY);
					}
					
					
						
				}catch(JSONException e){
					e.printStackTrace();
				}
				
				
			}else{
				
			}
			
		}
		
		@Override
		protected String doInBackground(String... params) {
			return retrieveFeed(params[0]);
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
}
