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

public class Seekika extends Activity {
	private static final String t = "SeekikaActivity";
	private static final int SIGNUP=1;
	private boolean mError=false;
	private String mErrorMessage="";
	
	//list components
	private EditText mUsername;
	private EditText mPassword;
	private Button mBtnSignUp;
	private Button mBtnSignIn;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //load the components
        initComponents();
    }
    
    
    
    private void initComponents(){
    	mUsername=(EditText)findViewById(R.id.username);
    	mPassword=(EditText)findViewById(R.id.password);
    	mBtnSignUp=(Button)findViewById(R.id.btn_signup);
    	mBtnSignIn=(Button)findViewById(R.id.btn_signin);
    	
    	//Sign in 
    	mBtnSignIn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(mUsername.getText())){
					mErrorMessage=getString(R.string.empty_profile_username);
					mError=true;
				}
				if(TextUtils.isEmpty(mPassword.getText())){
					mErrorMessage=getString(R.string.empty_profile_username);
					mError=true;
				}
				
				if(!mError){
					//authenticate on Web app
				}else{
					final Toast t = Toast.makeText(Seekika.this, "Error!\n\n" + mErrorMessage,
                            Toast.LENGTH_LONG);
                    t.show();
                    mErrorMessage = "";
				}
				
			}
		});
    	
    	//Load the sign up activity
    	mBtnSignUp.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				Log.i(t,"Sign up activity");
				Intent intent=new Intent(Seekika.this,Signup.class);
				startActivityForResult(intent,SIGNUP);
				setResult(RESULT_OK);
			}
		});
    }
}