package com.seekika.android.app;

import java.util.ArrayList;

import com.seekika.android.app.helpers.Encryption;
import com.seekika.android.app.helpers.RestClient;
import com.seekika.android.app.helpers.RestClient.RequestMethod;
import com.seekika.android.app.tasks.AuthenticateTask;

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
	private static final String LOGIN_URL="http://192.168.1.102:8080/api/auth/";
	private boolean mError=false;
	private String mErrorMessage="";
	
	private String _username="";
	private String _password="";
	
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
        setTitle(getString(R.string.app_name));
        //load the components
        initComponents();
    }
    
    
    
    private void initComponents(){
    	
    	mBtnSignUp=(Button)findViewById(R.id.btn_signup);
    	mBtnSignIn=(Button)findViewById(R.id.btn_signin);
    	
    	
    	
    	//Sign in 
    	mBtnSignIn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Log.i(t,"log in activity");
				Intent intent=new Intent(Seekika.this,Login.class);
				startActivity(intent);
			}
		});
    	
    	//Load the sign up activity
    	mBtnSignUp.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				Log.i(t,"Sign up activity");
				Intent intent=new Intent(Seekika.this,Signup.class);
				startActivity(intent);
				
			}
		});
    }
}