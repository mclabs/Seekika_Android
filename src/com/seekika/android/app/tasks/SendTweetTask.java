package com.seekika.android.app.tasks;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;

import com.seekika.android.app.constants.SeekikaConstants;
import com.seekika.android.app.helpers.FileUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SendTweetTask extends AsyncTask<String, Void, Integer> {

	
    private ProgressDialog dialog;
	public Context applicationContext;
	private String reason;
	private int statusCode;
	
	public SendTweetTask(Context ctx){
		this.applicationContext=ctx;
	}
	
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		this.dialog=new ProgressDialog(applicationContext);
		this.dialog.setTitle("");
		this.dialog.setMessage("Tweeting your story");
		this.dialog.show();
		
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		this.dialog.dismiss();
		if(result!=200 || result==null){
			//toast message 
			Toast.makeText(applicationContext, "Unable to send tweet", Toast.LENGTH_LONG).show();
		}else{
			//toast success message
			Toast.makeText(applicationContext, "Woohoo!! Tweet Sent", Toast.LENGTH_LONG).show();
		}
	}
	@Override
	protected Integer doInBackground(String... params) {
		// TODO Auto-generated method stub
		return sendTweet(params[0],params[1],params[2],params[3]);
	}
	
	public int sendTweet(String dbUserToken,String dbUserSecret,String title,String storyKey){
		
		
		
		CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(SeekikaConstants.consumerKey,
                SeekikaConstants.consumerSecret);
		
		//check if twitter tokens exist in the pref
		
		//String userToken=prefs.getString("userToken",null);
		//String userSecret = prefs.getString("userTokenSecret", null);
		Log.i("Story","userToken " + dbUserToken);
		Log.i("Story","userSecret " + dbUserSecret);
		
		consumer.setTokenWithSecret(dbUserToken, dbUserSecret);
		
		String url=SeekikaConstants.STORY_URL + storyKey;
		String shortUrl=FileUtils.shortenUrl(url);
		String message="Listen to my story-- # " + title + " " + shortUrl + " via @Seekika";
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postUpdate = new HttpPost("http://api.twitter.com/1/statuses/update.xml");
		List<NameValuePair> myList = new ArrayList<NameValuePair>();
		myList.add(new BasicNameValuePair("status", message));
		try {
			postUpdate.setEntity(new UrlEncodedFormEntity(myList, HTTP.UTF_8));
			postUpdate.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);  
		
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			consumer.sign(postUpdate);
		} catch (OAuthMessageSignerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (OAuthExpectationFailedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (OAuthCommunicationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			//client.Execute(RequestMethod.POST);
			HttpResponse response=httpClient.execute(postUpdate);
			statusCode = response.getStatusLine().getStatusCode(); 
			reason = response.getStatusLine().getReasonPhrase();  
			response.getEntity().consumeContent();  
			if (statusCode != 200)  {
                Log.i("Story", "reason " + reason);
			} else {
				Log.i("Story","tweet sent");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return statusCode; 
		
	}

}
