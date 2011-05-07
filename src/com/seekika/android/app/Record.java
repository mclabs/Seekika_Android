/**
 * Recording Activity
 */
package com.seekika.android.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.seekika.android.app.helpers.AudioRecorder;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

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
	private String audioFileName="";
	final AudioRecorder recorder = new AudioRecorder();
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.recordstory);
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
				
			}
		});
    	
    	mBtnStartRecording=(Button)findViewById(R.id.btn_start_recording);
    	mBtnStartRecording.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startRecording();
				mBtnStartRecording.setEnabled(false);
				mBtnStopRecording.setEnabled(true);
				//start recording audio
				//start timer
				
			}
		});
    	
    }
    
    private void startRecording(){
    	mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        
        
        try{
        	String myRecording="Seekika-" + System.currentTimeMillis();
        	Log.i(TAG, "Start Recording");
        	recorder.startRecording(myRecording);
        }catch(IOException e){
        	Log.e(TAG,"IOException error");
        	e.printStackTrace();
        }
        
    }
    
    private void stopRecording(){
    	mChronometer.stop();
    	
    	try{
    		recorder.stop();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    	
    }
    
   
    
   
}
