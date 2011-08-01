package com.seekika.android.app.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;



import com.seekika.android.app.Home;
import com.seekika.android.app.constants.SeekikaConstants;
import com.seekika.android.app.helpers.FileUtils;
import com.seekika.android.app.helpers.RestClient;
import com.seekika.android.app.helpers.S3;
import com.seekika.android.app.helpers.RestClient.RequestMethod;
import com.seekika.android.app.listeners.PublishStoryListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class PublishStoryTask extends AsyncTask<String, String, String> {
	private static final String TAG="PublishStoryTask";
	private ProgressDialog mProgressDialog;
	public Context ctx;
	public Activity act;
	private PublishStoryListener mPublishStoryListener;
	
	public PublishStoryTask(Activity act){
		this.act=act;
	}
	
	@Override
	protected void onPreExecute(){
		/**this.mProgressDialog=new ProgressDialog(ctx); 
		this.mProgressDialog.setTitle("");
		this.mProgressDialog.setMessage("Publishing Story");
		this.mProgressDialog.show(); **/
	}
	@Override
	protected void onPostExecute(String result){
		super.onPostExecute(result);
		mPublishStoryListener.uploadComplete(result);
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return publishStory(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7]);
	}
	
	private String publishStory(String title,String fileName,String lat,String lon,String android_id,String description,String userKey,String status){
		
		
		File file=null;
		String fullFileName=fileName + ".mp4";
		file = new File(SeekikaConstants.AUDIO_FILEPATH + fullFileName);
		if(file.exists()){
			Log.e(TAG,"upload to s3");
			//check for internet connection
			S3.putObjectInBucket(SeekikaConstants.S3BUCKET, fullFileName, file);
		}else{
			Log.e(TAG,"File does not exist");

		}

		RestClient client = new RestClient(SeekikaConstants.PUBLISH_URL);
		client.AddParam("title", title);
		client.AddParam("filename", fileName);
		client.AddParam("lat", lat);
		client.AddParam("lon", lon);
		client.AddParam("android_id", android_id);
		client.AddParam("description", description);
		client.AddParam("userkey", userKey);
		client.AddParam("status", status);
		try{
			client.Execute(RequestMethod.GET);
		}catch(Exception e){
			e.printStackTrace();
		}
		String response=client.getResponse();
		return response;
	}

	public void setmPublishStoryListener(PublishStoryListener mPublishStoryListener) {
		this.mPublishStoryListener = mPublishStoryListener;
	}
	
	
	
	

}
