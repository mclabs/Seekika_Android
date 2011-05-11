package com.seekika.android.app;

import com.seekika.android.app.constants.SeekikaConstants;
import com.seekika.android.app.tasks.PublishStoryTask;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Capture extends Activity {
	
	private static final String TAG="CaptureActivity";
	
	private EditText mTitle;
	private EditText mStoryDescription;
	private Spinner mStatusSpinner;
	private Button mBtnPublish;
	
	private PublishStoryTask mPublishStoryTask;
	
	private String _title;
	private String _lat;
	private String _lon;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 setContentView(R.layout.capture);
		 initComponents();
	}
	
	public void initComponents(){
		mTitle=(EditText)findViewById(R.id.story_title);
		mStoryDescription=(EditText)findViewById(R.id.story_description);
		mBtnPublish=(Button)findViewById(R.id.btn_publish);
		
		mStatusSpinner = (Spinner) findViewById(R.id.spn_status);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this,R.array.status_array,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mStatusSpinner.setAdapter(adapter);
		
		//get bundles
		Bundle extras=getIntent().getExtras();
		final String audioFileName=extras.getString("audioFileName");
		Log.i(TAG,"file name" + audioFileName);
		
		mBtnPublish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//persist to local DB
				
				//persist to online DB
				_title=mTitle.getText().toString();
				_lat="45.000";
				_lon="45.000";
				mPublishStoryTask=new PublishStoryTask();
				mPublishStoryTask.ctx=Capture.this;
				mPublishStoryTask.execute(_title,audioFileName,_lat,_lon,SeekikaConstants.ANDROID_ID);
				
			}
		});
		
		//add listener for save/upload button
		//progress bar
	
	}
}
