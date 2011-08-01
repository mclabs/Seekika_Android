package com.seekika.android.app;

import java.util.ArrayList;

import com.seekika.android.app.helpers.Encryption;
import com.seekika.android.app.helpers.RestClient;
import com.seekika.android.app.helpers.RestClient.RequestMethod;
import com.seekika.android.app.tasks.AuthenticateTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SeekikaStart extends Activity {
	private static final String TAG = "SeekikaActivity";
	private static final int SIGNUP=1;
	private boolean mError=false;
	private String mErrorMessage="";
	
	private String _username="";
	private String _password="";
	
	//list components
	private EditText mUsername;
	private EditText mPassword;
	private Button mBtnSignUp;
	private Button mBtnSignIn;
	private boolean internetOn=false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle(getString(R.string.app_name));
        //load the components
        initComponents();
        if(isInternetOn()==internetOn){
        	//Log.i(TAG,"No internet connectivity " + isInternetOn());
        }else{
        	//Log.i(TAG,"internet connectivity " + isInternetOn());
        }
    }
    
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	     if (keyCode == KeyEvent.KEYCODE_BACK) {

	     //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR

	     return true;

	     }

	     return super.onKeyDown(keyCode, event);    
	}
    
    public boolean isInternetOn() {

		ConnectivityManager cm =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
    
    
    private void initComponents(){
    	
    	mBtnSignUp=(Button)findViewById(R.id.btn_signup);
    	mBtnSignIn=(Button)findViewById(R.id.btn_signin);
    	
    	
    	
    	//Sign in 
    	mBtnSignIn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Log.i(TAG,"log in activity");
				Intent intent=new Intent(SeekikaStart.this,Login.class);
				startActivity(intent);
			}
		});
    	
    	//Load the sign up activity
    	mBtnSignUp.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				Log.i(TAG,"Sign up activity");
				Intent intent=new Intent(SeekikaStart.this,Signup.class);
				startActivity(intent);
				
			}
		});
    }
}