package com.seekika.android.app;

import com.seekika.android.app.constants.SeekikaConstants;
import com.seekika.android.app.helpers.Encryption;
import com.seekika.android.app.listeners.LoginListener;
import com.seekika.android.app.tasks.AuthenticateTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements LoginListener {
	//components
	private EditText mUsername;
	private EditText mPassword;
	private Button mBtnLogin;
	private Button mBtnSignup;
	private Button mBtnBack;
	
	private String mErrorMessage;
	private boolean mError=false;
	private String _username;
	private String _password;
	private String loginResult;
	
	private static final String SUCCESS="success";
	private static final String TAG="LoginActivity";
	private static final int PROGRESS_DIALOG = 1;
	private static final int ALERT_DIALOG = 2;
	
	private ProgressDialog mProgressDialog;
	private AlertDialog mAlertDialog;
	
	private AuthenticateTask mAuthenticateTask;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle(getString(R.string.app_name));
        initComponents();
    }
	
	@Override
    protected Dialog onCreateDialog(int id) {
		switch(id){
			case PROGRESS_DIALOG:
			mProgressDialog = new ProgressDialog(this);
			DialogInterface.OnClickListener loadingButtonListener=new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					mAuthenticateTask.setLoginListener(null);
					
				}
			};
			mProgressDialog.setTitle("Auth");
			mProgressDialog.setMessage("Authorizing");
			mProgressDialog.setIcon(android.R.drawable.ic_dialog_info);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
           //mProgressDialog.setButton("Cancel",loadingButtonListener);
			return mProgressDialog;
			
			
		}
		
		return null;
	
	}
	
	public void initComponents(){
		mUsername=(EditText)findViewById(R.id.login_username);
		mPassword=(EditText)findViewById(R.id.login_password);
		
		mBtnBack=(Button)findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Login.this,Seekika.class);
				startActivity(intent);
				finish();
				
			}
		});
		
		mBtnSignup=(Button)findViewById(R.id.btn_signup);
		mBtnSignup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Log.i(t,"Sign up activity");
				Intent intent=new Intent(Login.this,Signup.class);
				startActivity(intent);
				
			}
		});
		
		mBtnLogin=(Button)findViewById(R.id.btn_login);
		mBtnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(TextUtils.isEmpty(mUsername.getText())){
					//mErrorMessage=getString(R.string.empty_profile_username) + "\n";
					new AlertDialog.Builder(Login.this).setTitle("").setMessage(getString(R.string.empty_profile_username)).setNeutralButton("Close", null).show();  
					mError=true;
				}
				if(TextUtils.isEmpty(mPassword.getText())){
					new AlertDialog.Builder(Login.this).setTitle("").setMessage(getString(R.string.empty_profile_password)).setNeutralButton("Close", null).show();
					//mErrorMessage+=getString(R.string.empty_profile_password) + "\n";
					mError=true;
				}
				
				if(!mError){
					//authenticate on Web app
					showDialog(PROGRESS_DIALOG);
					mAuthenticateTask=new AuthenticateTask();
					//authTask.applicationContext=Login.this;
					//pass the username & password
					_username=mUsername.getText().toString();
					_password=mPassword.getText().toString();
					Encryption enc=new Encryption();
					
					mAuthenticateTask.setLoginListener(Login.this);
					mAuthenticateTask.execute(_username,enc.md5(_password));
					mErrorMessage = "";
					
				}else{
					
				}
				
			}
			
		});
		
	}

	@SuppressWarnings("unused")
	@Override
	public void loginComplete(String result) {
		String dialogMessage = null;
        String dialogTitle = null;
		dismissDialog(PROGRESS_DIALOG);
		SharedPreferences settings = getSharedPreferences(SeekikaConstants.PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
		
		
		if(result == null){
			new AlertDialog.Builder(Login.this).setTitle("Connection Error").setMessage(getString(R.string.connection_error)).setNeutralButton("Close", null).show();
		}else{
			String r=result.trim();
			int status=Integer.parseInt(r.replaceAll("[^0-9.]",""));
			if(status==1){
				Log.i(TAG,"success" + _username);
				editor.putString("auth_username", _username);
				editor.commit();
				Intent intent=new Intent(Login.this,Home.class);
				startActivity(intent);
				//save username in the preferences
				//start a new activity
			}else{
				Log.i(TAG,"failure");
				new AlertDialog.Builder(Login.this).setTitle("Invalid").setMessage(getString(R.string.login_error)).setNeutralButton("Close", null).show();
			}
		}
			
		
			
	}
	
	
	
	
	
	
}
