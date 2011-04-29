package com.seekika.android.app;

import com.seekika.android.app.helpers.Encryption;
import com.seekika.android.app.tasks.AuthenticateTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle(getString(R.string.app_name));
        initComponents();
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
					
					AuthenticateTask authTask=new AuthenticateTask(Login.this);
					authTask.applicationContext=Login.this;
					//pass the username & password
					_username=mUsername.getText().toString();
					_password=mPassword.getText().toString();
					Encryption enc=new Encryption();
					
					authTask.execute(_username,enc.md5(_password));
					mErrorMessage = "";
					
				}else{
					
				}
				
			}
			
		});
		
	}
}
