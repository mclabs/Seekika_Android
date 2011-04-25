package com.seekika.android.app.tasks;

import java.util.ArrayList;

import com.seekika.android.app.Seekika;
import com.seekika.android.app.helpers.RestClient;
import com.seekika.android.app.helpers.RestClient.RequestMethod;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AuthenticateTask extends AsyncTask<String, String, String> {
	private ProgressDialog dialog;
	public Context applicationContext;

	private static final String LOGIN_URL="http://192.168.1.102:8080/api/auth/";
	private static final String SUCCESS="Success";
	private static final String t="AuthenticateTask";
	
	private String status,flag;
	
	public AuthenticateTask(Context ctx){
		super();
		this.applicationContext=ctx;
	}

	@Override
	protected String doInBackground(String... params) {
		status=authenticateUser(params[0],params[1]);
		if(status=="success"){
			flag="success";
		}else{
			flag="success";
		}
		return flag;
	}
	
	@Override
	protected void onPreExecute(){
		this.dialog=ProgressDialog.show(applicationContext, "Authenticating", 
				"Logging in",true);
		
	}
	
	@Override
	protected void onPostExecute(String result) {
		this.dialog.cancel();
		
		Log.i(t, "Result from Seekika Webapp=" + result);
		if(result=="success"){
			Log.i(t, "Successful login");
			Toast.makeText(applicationContext, "" + SUCCESS + "\n\n",
                    Toast.LENGTH_LONG).show();
		}else{
			    Log.i(t, "Successful login");
				Toast.makeText(applicationContext, "FAIL",
	                    Toast.LENGTH_LONG).show();
		}
		
	}
	
	private String authenticateUser(String username, String password){
		
		RestClient client = new RestClient(LOGIN_URL);
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

}
