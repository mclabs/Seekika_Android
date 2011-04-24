package com.seekika.android.app;

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
	private static final String t = "Signup";
	private boolean mError=false;
	private String mErrorMessage="";
	
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
					mErrorMessage=getString(R.string.empty_profile_name);
					mError=true;
				}
				
				if(TextUtils.isEmpty(mUsername.getText())){
					mErrorMessage+=getString(R.string.empty_profile_username);
					mError=true;
				}
				
				if(TextUtils.isEmpty(mEmail.getText())){
					mErrorMessage+=getString(R.string.empty_profile_email);
					mError=true;
				}
				
				if(TextUtils.isEmpty(mPassword.getText())){
					mErrorMessage+=getString(R.string.empty_profile_password);
					mError=true;
				}
				
				if(!mError){
					//save to Seekika Web
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
				Intent intent=new Intent(Signup.this,Seekika.class);
				startActivity(intent);
				
			}
		});
    		
    	
    	
    	
    }

}
