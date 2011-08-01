/**
 * Recording Activity
 */
package com.seekika.android.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.seekika.android.app.helpers.AudioRecorder;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;


public class Record extends Activity {
	
	private static final String TAG="RecordActivity";
	
	private int SETTINGS=Menu.FIRST;
	private int LOGOUT=Menu.FIRST + 1;
	private int EXIT=Menu.FIRST + 2;
	
	//components
	private Button mBtnStartRecording;
	private Button mBtnStopRecording;
	
	private Chronometer mChronometer;
	
	private File outfile = null;
	private String myRecording="";
	final AudioRecorder recorder = new AudioRecorder();
	AnimationDrawable yourAnimation;  
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.recordstory);
        //anim
        //final ImageView imageView = (ImageView) findViewById(R.id.freq);   
        
       //imageView.setBackgroundResource(R.drawable.recordinganim);   
       // yourAnimation = (AnimationDrawable) imageView.getBackground(); 
        
        //end anim
        initComponents();
    }
    
    public void initComponents(){
    	mChronometer=(Chronometer)findViewById(R.id.chrono);
    	mBtnStopRecording=(Button)findViewById(R.id.btn_stop_recording);
    	mBtnStopRecording.setEnabled(false);
    	
    	mBtnStopRecording.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//stop timer
				stopRecording();
				//stop recording and save audio file to SD card
				//new activity begins here
				Intent intent=new Intent(Record.this,Capture.class);
				Log.i(TAG,"audio file " + myRecording);
				intent.putExtra("audioFileName", myRecording);
				startActivity(intent);
				finish();
				
			}
		});
    	
    	mBtnStartRecording=(Button)findViewById(R.id.btn_start_recording);
    	mBtnStartRecording.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//yourAnimation.start();
				startRecording();
				mBtnStartRecording.setEnabled(false);
				mBtnStopRecording.setEnabled(true);
				
				
			}
		});
    	
    }
    
    private void startRecording(){
    	mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        
        
        try{
        	myRecording="Seekika-" + System.currentTimeMillis();
        	Log.i(TAG, "Start Recording");
        	recorder.startRecording(myRecording);
        }catch(IOException e){
        	Log.e(TAG,"IOException error");
        	e.printStackTrace();
        }
        
    }
    
    private void stopRecording(){
    	mChronometer.stop();
    	getElapsedTime();
    	try{
    		recorder.stop();
    		
    		
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    	
    }
    
    private void getElapsedTime(){
    	long elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();            
        //Toast.makeText(Record.this, "Elapsed milliseconds: " + elapsedMillis, 
          //      Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	if(recorder.mRecorder!=null){
    		recorder.mRecorder.release();
    		recorder.mRecorder=null;
    	}
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    }
    
   
    
   
}
