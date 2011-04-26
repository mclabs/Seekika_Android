package com.seekika.android.app;

import com.seekika.android.app.helpers.Encryption;
import com.seekika.android.app.tasks.SignUpTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends Activity {
	private static final String t = "SignupActivity";
	private boolean mError=false;
	private String mErrorMessage="";
	private static final int LOGIN=1;
	
	private String _username;
	private String _name;
	private String _email;
	private String _password;
	//components
	private EditText mUsername;
	private EditText mName;
	private EditText mEmail;
	private EditText mPassword;
	private Button mBtnSignUp;
	private Button mBtnSignIn;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        setTitle(getString(R.string.app_name));
        
        //load components
        initComponents();
    }
    
    private void initComponents(){
    	mName=(EditText)findViewById(R.id.name);
    	mUsername=(EditText)findViewById(R.id.username);
    	mEmail=(EditText)findViewById(R.id.email);
    	mPassword=(EditText)findViewById(R.id.password);
    	mBtnSignUp=(Button)findViewById(R.id.btn_create_profile);
    	mBtnSignIn=(Button)findViewById(R.id.btn_sign_in);
    	
    	mBtnSignUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(t,"Sign up user");
				mError=false;
				//check that name is not empty
				if(TextUtils.isEmpty(mName.getText())){
					mErrorMessage=getString(R.string.empty_profile_name) + "\n";
					mError=true;
				}
				
				if(TextUtils.isEmpty(mUsername.getText())){
					mErrorMessage+=getString(R.string.empty_profile_username) + "\n";
					mError=true;
				}
				
				if(TextUtils.isEmpty(mEmail.getText())){
					mErrorMessage+=getString(R.string.empty_profile_email) + "\n";
					mError=true;
				}
				
				if(TextUtils.isEmpty(mPassword.getText())){
					mErrorMessage+=getString(R.string.empty_profile_password) + "\n";
					mError=true;
				}
				
				if(!mError){
					//save to Seekika Web App
					SignUpTask signUpTask=new SignUpTask();
					signUpTask.applicationContext=Signup.this;
					
					_username=mUsername.getText().toString();
					_name=mName.getText().toString();
					_email=mEmail.getText().toString();
					//password needs to be encrypted
					Encryption enc=new Encryption();
					_password=mPassword.getText().toString();
					signUpTask.execute(_name,_email,_username,enc.md5(_password));
				}else{
					final Toast t = Toast.makeText(Signup.this, "Error!\n\n" + mErrorMessage,
                            Toast.LENGTH_LONG);
                    t.show();
                    mErrorMessage = "";
				}

				
			}
		});
    	
    	mBtnSignIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(t,"Return user to home screen");
				Intent intent=new Intent(Signup.this,Login.class);
				startActivityForResult(intent,LOGIN);
				setResult(RESULT_OK);
				
			}
		});
    		
    	
    	
    	
    }

}
