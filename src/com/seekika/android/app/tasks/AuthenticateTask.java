package com.seekika.android.app.tasks;

import java.util.ArrayList;

import com.seekika.android.app.Login;
import com.seekika.android.app.R;
import com.seekika.android.app.SeekikaStart;
import com.seekika.android.app.constants.SeekikaConstants;
import com.seekika.android.app.helpers.RestClient;
import com.seekika.android.app.helpers.RestClient.RequestMethod;
import com.seekika.android.app.listeners.LoginListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AuthenticateTask extends AsyncTask<String, String, String> {
	private ProgressDialog dialog;
	public Context applicationContext;
	private LoginListener mStateListener;

	
	private static final String SUCCESS="success";
	private static final String FAIL="fail";
	private static final String t="AuthenticateTask";
	
	
	private String flag;
	
	//public AuthenticateTask(Context ctx){
		//super();
		//this.applicationContext=ctx;
	//}

	@Override
	protected String doInBackground(String... params) {
		return authenticateUser(params[0],params[1]);
		
		
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		
		
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		//this.dialog.cancel();
		
		Log.i(t, "Result from Seekika Webapp=" + result);
		
		mStateListener.loginComplete(result);
		
		
		
		
		
	}
	
	private String authenticateUser(String username, String password){
		
		RestClient client = new RestClient(SeekikaConstants.LOGIN_URL);
		client.AddParam("username", username);
		client.AddParam("password", password);
		
		
		try{
			client.Execute(RequestMethod.GET);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		String response=client.getResponse();
		Log.i(t, "Response from Seekika Webapp=" + response);
		return response;
	}
	
	public void setLoginListener(LoginListener sl){
		mStateListener=sl;
	}

}
