package com.seekika.android.app.tasks;

import com.seekika.android.app.helpers.RestClient;
import com.seekika.android.app.helpers.RestClient.RequestMethod;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class SignUpTask extends AsyncTask<String, String, String> {

	private ProgressDialog dialog;
	public Context applicationContext;
	private static final String SIGNUP_URL="http://192.168.1.102:8080/api/signup/";
	
	@Override
	protected String doInBackground(String... params) {
		
		return signUpUser(params[0],params[1],params[2],params[3]);
	}
	
	@Override
	protected void onPreExecute(){
		this.dialog=ProgressDialog.show(applicationContext, "Signup", 
				"Creating account",true);
		
	}
	
	@Override
	protected void onPostExecute(String result) {
		this.dialog.cancel();
		
	}
	
	private String signUpUser(String name,String email,String username,String password){
		RestClient client = new RestClient(SIGNUP_URL);
		client.AddParam("name", name);
		client.AddParam("email", email);
		client.AddParam("username", username);
		client.AddParam("password", password);
		try{
			client.Execute(RequestMethod.GET);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
