package com.seekika.android.app.helpers;

import java.io.File;
import java.io.IOException;

import com.seekika.android.app.constants.SeekikaConstants;

import android.content.ContentValues;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;

public class AudioRecorder {
	
	public MediaRecorder mRecorder = new MediaRecorder();
	private final String path=SeekikaConstants.AUDIO_FILEPATH;
	private File outfile = null;
	
	public AudioRecorder(){}
	
	public void startRecording(String audioFile) throws IOException {
		String state = android.os.Environment.getExternalStorageState();
		if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
			throw new IOException("SD Card is not mounted.  It is " + state + ".");
		}
		 
		// make sure the directory we plan to store the recording in exists
		//File directory = new File(path).getParentFile();
		File directory = new File(path);
		if (!directory.exists() && !directory.isDirectory()) {
			directory.mkdirs();
			//throw new IOException("Path to file could not be created.");
		}
	
			
				ContentValues values = new ContentValues(3);
			    values.put(MediaStore.MediaColumns.TITLE, audioFile);
			    
			    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			    mRecorder.setOutputFile(path+audioFile+".mp3");
			
			
		
		
		try{
			mRecorder.prepare();
		}catch(IllegalStateException e){
			e.printStackTrace();
		}
		
		mRecorder.start();
		}
	
	public void stop() throws IOException {
		mRecorder.stop();
		mRecorder.release();
		mRecorder=null;
		}
}
