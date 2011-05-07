package com.seekika.android.app.helpers;

import java.io.File;
import java.io.IOException;

import com.seekika.android.app.constants.SeekikaConstants;

import android.content.ContentValues;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;

public class AudioRecorder {
	
	private MediaRecorder recorder = new MediaRecorder();
	private final String path=SeekikaConstants.AUDIO_FILEPATH;
	private File outfile = null;
	
	public AudioRecorder(){}
	
	public void startRecording(String audioFile) throws IOException {
		String state = android.os.Environment.getExternalStorageState();
		if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
			throw new IOException("SD Card is not mounted.  It is " + state + ".");
		}
		 
		// make sure the directory we plan to store the recording in exists
		File directory = new File(path).getParentFile();
		if (!directory.exists() && !directory.mkdirs()) {
			throw new IOException("Path to file could not be created.");
		}
		//try{
			//File storageDir = new File(Environment
	          //      .getExternalStorageDirectory(), "/audio/");
			//storageDir.mkdir();
			
			//outfile
			//outfile=File.createTempFile(audioFile, ".mpeg4",storageDir);
			
			ContentValues values = new ContentValues(3);
		    values.put(MediaStore.MediaColumns.TITLE, audioFile);
		    
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			//recorder.setOutputFile(outfile.getAbsolutePath());
			recorder.setOutputFile(path+audioFile+".mp3");
		//}catch(IOException e){
			//e.printStackTrace();
		//}
		
		try{
			recorder.prepare();
		}catch(IllegalStateException e){
			e.printStackTrace();
		}
		
		recorder.start();
		}
	
	public void stop() throws IOException {
		recorder.stop();
		recorder.release();
		}
}
