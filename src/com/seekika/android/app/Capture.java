package com.seekika.android.app;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seekika.android.app.constants.SeekikaConstants;
import com.seekika.android.app.database.SeekikaDbAdapter;
import com.seekika.android.app.listeners.PublishStoryListener;
import com.seekika.android.app.models.Story;
import com.seekika.android.app.tasks.PublishStoryTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Capture extends Activity implements PublishStoryListener {
	
	private static final String TAG="CaptureActivity";
	
	
	private EditText mTitle;
	private EditText mStoryDescription;
	private Spinner mStatusSpinner;
	private Button mBtnPublish;
	private Button mBtnPublishLater;
	
	private PublishStoryTask mPublishStoryTask;
	private SeekikaDbAdapter seekikaDb;
	
	private String _title;
	private String _lat="";
	private String _lon="";
	private String androidId="";
	private String status;
	private String storyDescription;
	private String userKey;
	private String audioFileName;
	private boolean mError=false;
	
	private static final int PROGRESS_DIALOG = 1;
	private static final int ALERT_DIALOG = 2;
	
	private ProgressDialog mProgressDialog;
	private AlertDialog mAlertDialog;
	private SharedPreferences prefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 setContentView(R.layout.capture);
		 prefs = Prefs.get(this);
		 userKey=prefs.getString("userKey", null);
		 
		 LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		 LocationListener mlocListener = new MyLocationListener();
		 //mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
		 Criteria criteria = new Criteria();
		 criteria.setAccuracy(Criteria.ACCURACY_FINE);
		 String provider = mlocManager.getBestProvider(criteria, true);
		 Location mostRecentLocation = mlocManager.getLastKnownLocation(provider);
		 if(mostRecentLocation!=null){
			 _lat=Double.toString(mostRecentLocation.getLatitude());
			 _lon=Double.toString(mostRecentLocation.getLongitude());
		 }
		 mlocManager.requestLocationUpdates(provider, 1, 0, mlocListener);
		 initComponents();
	}
	
	public void initComponents(){
		mTitle=(EditText)findViewById(R.id.story_title);
		mStoryDescription=(EditText)findViewById(R.id.story_description);
		mBtnPublish=(Button)findViewById(R.id.btn_publish);
		mBtnPublishLater=(Button)findViewById(R.id.btn_publishlater);
		
		mStatusSpinner = (Spinner) findViewById(R.id.spn_status);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this,R.array.status_array,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mStatusSpinner.setAdapter(adapter);
		
		mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				Object item=parent.getItemAtPosition(pos);	
				status=parent.getItemAtPosition(pos).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		//get bundles
		Bundle extras=getIntent().getExtras();
		audioFileName=extras.getString("audioFileName");
		Log.i(TAG,"file name" + audioFileName);
		
		mBtnPublish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(mTitle.getText())){
					new AlertDialog.Builder(Capture.this).setTitle("").setMessage(getString(R.string.error_empty_title)).setNeutralButton("Close", null).show();  
					mError=true;
				}
				if(TextUtils.isEmpty(mStoryDescription.getText())){
					new AlertDialog.Builder(Capture.this).setTitle("").setMessage(getString(R.string.error_empty_description)).setNeutralButton("Close", null).show();  
					mError=true;
				}
				if(!mError){
					//persist to online DB
					_title=mTitle.getText().toString();
					storyDescription=mStoryDescription.getText().toString();
					androidId=android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
					
					if(isInternetOn()){
						showDialog(PROGRESS_DIALOG);
						mPublishStoryTask=new PublishStoryTask(Capture.this);
						mPublishStoryTask.ctx=Capture.this;
						
						Log.i(TAG,"Latitude " + _lat);
						Log.i(TAG,"Longitude " + _lon);
						Log.i(TAG,"Status " + status);
						mPublishStoryTask.setmPublishStoryListener(Capture.this);
						mPublishStoryTask.execute(_title,audioFileName,_lat,_lon,androidId,storyDescription,userKey,status);
					}else{
						Toast.makeText(Capture.this, "No internet connection available. Story not published online. Please upload later",
	                            Toast.LENGTH_LONG);
						//save to local DB and flag as not uploaded
						addStory(0,"");
						finish();
					}
				}//no error
				
			}
		});
		
		mBtnPublishLater.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				addStory(0,"");
				Toast.makeText(Capture.this, "Story saved. Please upload the story later and share with your friends", Toast.LENGTH_LONG).show();
				finish();
				
			}
		});
		
		
	
	}

	public class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				_lat=Double.toString(location.getLatitude());
				_lon=Double.toString(location.getLongitude());
				Log.i(TAG,"Latitude set in listener " + _lat);
				Log.i(TAG,"Longitude set in listener " + _lon);
			}
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public void addStory(int uploaded,String storyKey){
		seekikaDb=new SeekikaDbAdapter(Capture.this);
		seekikaDb.open();
		Story story=new Story();
		story.setTitle(mTitle.getText().toString());
		story.setDescription(mStoryDescription.getText().toString());
		story.setStatus(status);
		story.setLat(_lat);
		story.setLon(_lon);
		story.setFileName(audioFileName);
		story.setUserKey(prefs.getString("userKey",null));
		story.setUploaded(uploaded);
		story.setStoryKey(storyKey);
		
		seekikaDb.addStory(story);
		seekikaDb.close();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(seekikaDb !=null){
			seekikaDb.close();
		}
	}
	@Override
	protected void onStop(){
		super.onStop();
		if(seekikaDb !=null){
			seekikaDb.close();
		}
	}
	
	public final boolean isInternetOn() {

		ConnectivityManager cm =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
	
	 @Override
	    protected Dialog onCreateDialog(int id) {
	    	switch(id){
			case PROGRESS_DIALOG:
				mProgressDialog=new ProgressDialog(this);
				DialogInterface.OnClickListener loadingButtonListener=new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
						mPublishStoryTask.setmPublishStoryListener(null);
					}
				};
				mProgressDialog.setTitle("");
				mProgressDialog.setMessage("Publishing Story");
				mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
	            mProgressDialog.setIndeterminate(true);
	            mProgressDialog.setCancelable(false);
				return mProgressDialog;
				
	    	}
	    	return null;
	    }

	@Override
	public void uploadComplete(String result) {
		String dialogMessage = null;
        String dialogTitle = null;
		dismissDialog(PROGRESS_DIALOG);
		Log.i(TAG,result);
		if(result==null){
			new AlertDialog.Builder(Capture.this).setTitle("Unable to complete").setMessage(getString(R.string.connection_error)).setNeutralButton("Close", null).show();
			addStory(0,"");
		}else{
			try{
				JSONObject jsonObj = new JSONObject();
				JSONArray aryJSONStrings = new JSONArray(result);
				int i=0;
				String result_message=aryJSONStrings.getJSONObject(i).getString("message");
				String storyKey=aryJSONStrings.getJSONObject(i).getString("key");
				if(result_message.equalsIgnoreCase("success")){
					addStory(1,storyKey);
					Intent intent=new Intent(Capture.this,Home.class);
					startActivity(intent);
					finish();
				}else{
					
				}
				
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}	

}
	
	

	
