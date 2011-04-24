package com.seekika.android.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Seekika extends Activity {
	private static final String t = "Seekika";
	private static final int SIGNUP=1;
	
	//list components
	private EditText mUsername;
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
    	mBtnSignUp=(Button)findViewById(R.id.btn_signup);
    	mBtnSignIn=(Button)findViewById(R.id.btn_signin);
    	
    	//Sign in 
    	mBtnSignIn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
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